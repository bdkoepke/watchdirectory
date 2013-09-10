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

import java.io.File;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import pw.swordfish.validation.Validator;
import pw.swordfish.validation.Validators;
import pw.swordfish.validation.Violations;

/**
 * @author Brandon Koepke
 */
@XmlRootElement(name = "source")
public class Source {
	private static final Validator<File> DIRECTORY_VALIDATOR = Validators.getDirectoryValidator();

	@XmlElement(name = "name")
	private String _name;
	@XmlElement(name = "recursive", required = false)
	private boolean _recursive;
	@XmlElement(name = "path")
	private File _file;

	private Source() {}

	private Source(String name, File file, boolean recursive) {
		_name = name;
		_file = file;
		_recursive = recursive;
	}

	/**
	 * Creates a new source path.
	 * @param name the name of the path.
	 * @param file the file for this source.
	 * @param recursive true if the path should be recursively
	 * enumerated, false otherwise.
	 * @return a new source.
	 */
	public static Source of(String name, File file, boolean recursive) {
		Violations<File> violations = DIRECTORY_VALIDATOR.validate(file);
		return new Source(name, file, recursive);
	}

	/**
	 * Creates a new non-recursive source path.
	 * @param name the name of the path.
	 * @param file the file for this source.
	 * @return a new source.
	 */
	public static Source of(String name, File file) {
		return of(name, file, false);
	}

	/**
	 * The name of the source.
	 */
	public String getName() {
		return _name;
	}

	/**
	 * The file of the source
	 */
	public File getDirectory() {
		return _file;
	}

	/**
	 * Gets a value indicating whether the path should be 
	 * recursively enumerated or not.
	 */
	public boolean isRecursive() {
		return _recursive;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}