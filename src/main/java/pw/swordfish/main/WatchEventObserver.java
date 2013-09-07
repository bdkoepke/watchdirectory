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
import pw.swordfish.util.Observer;

/**
 * @author Brandon Koepke
 */
public abstract class WatchEventObserver implements Observer<WatchEvent<Path>> {
	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Exception error) {
	}
}
