/*
 * Copyright (C) 2013 Brandon Koepke
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pw.swordfish.io;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Map;
import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.attribute.BasicFileAttributes;
import pw.swordfish.util.AbstractObservable;
import pw.swordfish.util.Observer;
import pw.swordfish.validation.ConstraintViolation;
import pw.swordfish.validation.Validator;
import pw.swordfish.validation.Validators;
import pw.swordfish.validation.Violations;

/**
 * Observable that watches a directory and returns FileSystem events when they
 * occur.
 *
 * @author Brandon Koepke
 */
public class WatchDirectory extends AbstractObservable<WatchEvent<Path>> implements Runnable {

	private static final Validator<File> DIRECTORY_VALIDATOR = Validators.getDirectoryValidator();
	//private static final WatchEvent.Kind<Path>[] WATCH_EVENTS;
	private final WatchService _watchService;
	private final Map<WatchKey, Path> _keys;
	private final Map<Path, WatchKey> _values;
	private final boolean _recursive;

	private WatchDirectory(WatchService watchService, boolean recursive) {
		_watchService = watchService;
		_recursive = recursive;
		BiMap<WatchKey, Path> biMap = HashBiMap.create();
		_keys = biMap;
		_values = biMap.inverse();
	}

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	public static WatchDirectory of(boolean recursive) throws IOException {
		return new WatchDirectory(
				FileSystems.getDefault().newWatchService(),
				recursive);
	}

	public void register(Path directory) throws IOException {
		WatchKey key = directory.register(_watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

		if (!_values.containsKey(directory)) {
			_keys.put(key, directory);

			if (_recursive) {
				registerAll(directory);
			}
		}
	}

	private void onError(Exception error) {
		for (Observer<WatchEvent<Path>> observer : getObservers()) {
			observer.onError(error);
		}
	}

	@Override
	public void run() {
		for (;;) {
			WatchKey key;
			try {
				key = _watchService.take();
			} catch (InterruptedException ex) {
				onError(ex);
				continue;
			}

			Path directory = _keys.get(key);
			if (directory == null) {
				onError(new NullPointerException("WatchKey not recognized!"));
				continue;
			}

			pollEvents(key, directory);

			if (resetKey(key)) {
				break;
			}
		}
	}

	private void registerAll(Path start) throws IOException {
		Violations<File> violations = DIRECTORY_VALIDATOR.validate(start.toFile());
		if (violations.hasViolations()) {
			for (ConstraintViolation<File> violation : violations) {
				Exception error = violation.getError().isPresent() ?
						violation.getError().get() : 
						new Exception(violation.getMessage());
				onError(error);
			}
		} else {
			Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attributes) throws IOException {
					register(directory);
					return FileVisitResult.CONTINUE;
				}
			});
		}
	}

	private void pollEvents(WatchKey key, Path directory) {
		for (WatchEvent<?> event_ : key.pollEvents()) {
			@SuppressWarnings("rawtypes")
			WatchEvent.Kind kind_ = event_.kind();
			if (kind_.equals(OVERFLOW)) {
				continue;
			}

			WatchEvent<Path> event = cast(event_);
			WatchEvent.Kind<Path> kind = event.kind();
			Path name = event.context();
			Path child = directory.resolve(name);

			for (Observer<WatchEvent<Path>> observer : getObservers()) {
				observer.onNext(event);
				if (kind.equals(ENTRY_CREATE)) {
					handleCreate(child);
				}
			}
		}
	}

	private boolean resetKey(WatchKey key) {
		if (!key.reset()) {
			_keys.remove(key);
			if (_keys.isEmpty()) {
				for (Observer<WatchEvent<Path>> observer : getObservers()) {
					observer.onCompleted();
				}
				return true;
			}
		}
		return false;
	}

	private void handleCreate(Path child) {
		if (_recursive) {
			try {
				registerAll(child);
			} catch (IOException ex) {
				onError(ex);
			}
		}
	}
}
