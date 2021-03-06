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
package pw.swordfish.main;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * @author Brandon Koepke
 */
public class PrintlnWatchEventObserver extends WatchEventObserver {
	@Override
	public void onCompleted() {
		System.out.println("onCompleted");
	}

	@Override
	public void onError(Exception error) {
		System.out.println(error);
	}

	@Override
	public void onNext(WatchEvent<Path> value) {
		System.out.println(value.kind().name());
	}
}
