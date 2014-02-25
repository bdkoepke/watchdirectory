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
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Set;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Brandon Koepke
 */
@XmlRootElement(name = "sources")
public class Sources implements Supplier<Set<Source>> {
	@XmlElement(name = "source", type = Source.class)
	private final Set<Source> _sources;

	private Sources() {
		this(Sets.<Source>newHashSet());
	}

	private Sources(Set<Source> sources) {
		_sources = sources;
	}

	public static Sources of(Set<Source> sources) {
		return new Sources(sources);
	}

	public static Sources of(Iterable<Source> sources) {
		return of(Sets.newHashSet(sources));
	}

	@Override
	public Set<Source> get() {
		return Collections.unmodifiableSet(_sources);
	}
}