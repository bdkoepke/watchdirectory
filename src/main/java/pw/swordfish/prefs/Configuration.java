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
package pw.swordfish.prefs;

import com.google.common.collect.ImmutableSet;
import java.util.Collections;
import java.util.Set;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import pw.swordfish.main.WatchEventObserver;

/**
 * Container for various configuration items.
 * @author Brandon Koepke
 */
@XmlRootElement(name = "configuration")
public class Configuration {
	@XmlElement(name = "sources")
	private final Sources _sources;
	@XmlElement(name = "watchEventObserver")
	private final WatchEventObserver _watchEventObserver;

	private Configuration() {
		this(null, null);
	}

	private Configuration(Sources sources, WatchEventObserver watchEventObserver) {
		_sources = sources;
		_watchEventObserver = watchEventObserver;
	}

	/**
	 * Creates a new configuration.
	 * @param sources the set of folder sources to watch.
	 * @param visitor the file visitor to invoke when there is a
	 * FileSystem change.
	 */
	public static Configuration of(Sources sources, WatchEventObserver watchEventObserver) {
		return new Configuration(sources, watchEventObserver);
	}

	/**
	 * Creates a new configuration.
	 * @param sources the set of folder sources to watch, this is copied
	 * and a new instance is created.
	 * @param visitor the file visitor to invoke when there is a
	 * FileSystem change.
	 */
	public static Configuration of(Iterable<Source> sources, WatchEventObserver watchEventObserver) {
		return of(ImmutableSet.copyOf(sources), watchEventObserver);
	}

	public Sources getSources() {
		return _sources;
	}

	public WatchEventObserver getWatchEventObserver() {
		return _watchEventObserver;
	}
}
