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

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import java.io.File;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Brandon Koepke
 */
@XmlRootElement(name = "source")
public class Source {
	@XmlElement(name = "name", required = true)
	private final String _name;
	@XmlElement(name = "path", required = true)
	private final String _path;
	@XmlElement(name = "recursive", required = false)
	private final boolean _recursive;
	/**
	 * We have to lazily initialize the file from the path due to
	 * the way that serialization works. Pretty sure there is something
	 * in Effective Java about this, but this is just for personal use
	 * anyway...
	 */
	private final Supplier<File> _file = Suppliers.memoize(new Supplier<File>() {
		@Override
		public File get() {
			return new File(_path);
		}
	});

	private Source() {
		this(null, null, false);
	}

	private Source(String name, String path, boolean recursive) {
		_name = name;
		_path = path;
		_recursive = recursive;
	}

	/**
	 * Creates a new source path.
	 * @param name the name of the path.
	 * @param path the actual path.
	 * @param recursive true if the path should be recursively
	 * enumerated, false otherwise.
	 * @return a new source.
	 */
	public static Source of(String name, String path, boolean recursive) {
		return new Source(name, path, recursive);
	}

	/**
	 * Creates a new non-recursive source path.
	 * @param name the name of the path.
	 * @param path the actual path.
	 * @return a new source.
	 */
	public static Source of(String name, String path) {
		return new Source(name, path, false);
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
	public File getFile() {
		return _file.get();
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