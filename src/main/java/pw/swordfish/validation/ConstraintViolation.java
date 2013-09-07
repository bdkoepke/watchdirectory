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
package pw.swordfish.validation;

import com.google.common.base.Optional;

/**
 * @author Brandon Koepke
 */
public interface ConstraintViolation<T> {
	/**
	 * The invalid value that caused the violation.
	 */
	T getInvalidValue();

	/**
	 * The message to be displayed with the violation.
	 */
	String getMessage();

	/**
	 * If an exception should be thrown, then get the exception
	 * associated with the error.
	 * @return An optional exception.
	 */
	Optional<Exception> getError();
}
