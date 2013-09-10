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
import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;

class DirectoryValidator implements Validator<File> {
	private class DirectoryConstraintValidation implements ConstraintViolation<File> {
		private final File _invalidValue;
		private final String _message;
		private final Exception _error;
		public DirectoryConstraintValidation(File invalidValue, String message, Exception error) {
			_invalidValue = invalidValue;
			_message = message;
			_error = error;
		}

		@Override
		public File getInvalidValue() {
			return _invalidValue;
		}

		@Override
		public String getMessage() {
			return _message;
		}

		@Override
		public Optional<Exception> getError() {
			return Optional.of(_error);
		}
	}

	private ImmutableSet<ConstraintViolation<File>> getValidationsSet(File file) {
		ImmutableSet.Builder<ConstraintViolation<File>> constraintBuilder = ImmutableSet.builder();
		Path path = file.toPath();
		if (! Files.exists(path)) {
			constraintBuilder.add(new DirectoryConstraintValidation(file, "Directory doesn't exist " + path.toString(), new IOException(path.toString())));
			return constraintBuilder.build();
		}
		if (!Files.isDirectory(path)) {
			constraintBuilder.add(new DirectoryConstraintValidation(file, "Not a directory " + path.toString(), new NotDirectoryException(path.toString())));
		}
		if (!Files.isReadable(path)) {
			constraintBuilder.add(new DirectoryConstraintValidation(file, "Access denied " + path.toString(), new AccessDeniedException(path.toString())));
		}
		return constraintBuilder.build();
	}
		
	@Override
	public Violations<File> validate(File file) {
		return Violations.of(getValidationsSet(file));
	}
}
