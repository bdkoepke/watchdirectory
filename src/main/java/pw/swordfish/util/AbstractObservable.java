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
package pw.swordfish.util;

import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Set;

public abstract class AbstractObservable<T> implements Observable<T> {
	private final Set<Observer<T>> _observers = Sets.newHashSet();

	@Override
	public AutoCloseable subscribe(final Observer<T> observer) {
		_observers.add(observer);
		return new AutoCloseable() {
			@Override
			public void close() throws Exception {
				_observers.remove(observer);
			}
		};
	}

	protected Iterable<Observer<T>> getObservers() {
		return Collections.unmodifiableSet(_observers);
	}
}
