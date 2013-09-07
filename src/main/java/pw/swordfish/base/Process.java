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
package pw.swordfish.base;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Brandon Koepke
 */
@XmlRootElement(name = "process")
public class Process {
	@XmlElement(name = "command")
	private final String _command;
	@XmlElement(name = "arguments")
	private final String[] _arguments;

	private Process(String command, String... arguments) {
		_command = command;
		_arguments = arguments;
	}

	private String[] getCommandArray() {
		return Arrays.prepend(_command, _arguments);
	}

	public java.lang.Process run() throws IOException {
		return Runtime.getRuntime().exec(getCommandArray());
	}

	public static class Builder {
		private final ImmutableList.Builder<String> argumentsBuilder = ImmutableList.builder();
		private Builder() {}
		public Process build(String command) {
			return new Process(command, (String[]) argumentsBuilder.build().toArray());
		}

		public void addArgument(String argument) {
			argumentsBuilder.add(argument);
		}
	}

	public static Builder builder() {
		return new Builder();
	}
}
