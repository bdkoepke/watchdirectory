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

import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Brandon Koepke
 */
public class Violations<T> implements Iterable<ConstraintViolation<T>>{
	private final Set<ConstraintViolation<T>> _violations;

	private Violations(Set<ConstraintViolation<T>> violations) {
		_violations = violations;
	}

	public static <T> Violations<T> of(Iterable<ConstraintViolation<T>> violations) {
		return of(ImmutableSet.copyOf(violations));
	}

	public static <T> Violations<T> of(ImmutableSet<ConstraintViolation<T>> violations) {
		return new Violations<>(violations);
	}
		
	@Override
	public Iterator<ConstraintViolation<T>> iterator() {
		return _violations.iterator();
	}

	public boolean hasViolations() {
		return ! _violations.isEmpty();
	}
}
