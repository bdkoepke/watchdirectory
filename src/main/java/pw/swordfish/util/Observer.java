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

/**
 *
 * @author Brandon Koepke
 */
public interface Observer<T> {
	/**
	 * Invoked when the Observable is done.
	 */
	void onCompleted();
	/**
	 * Invoked when there is an error with the observable.
	 * @param error the exception representing the error.
	 */
	void onError(Exception error);
	/**
	 * The next value to process.
	 * @param value the value to process.
	 */
	void onNext(T value);
}