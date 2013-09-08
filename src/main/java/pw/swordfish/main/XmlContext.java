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

import com.google.common.collect.Maps;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import pw.swordfish.lang.Arrays;

/**
 * @author Brandon Koepke
 */
public class XmlContext<T> {
	private final Marshaller _marshaller;
	private final Unmarshaller _unmarshaller;

	private XmlContext(Marshaller marshaller, Unmarshaller unmarshaller) {
		_marshaller = marshaller;
		_unmarshaller = unmarshaller;
	}

	public void marshall(T object, OutputStream o) throws JAXBException {
		_marshaller.marshal(object, o);
	}

	@SuppressWarnings("unchecked")
	public T unmarshall(InputStream is) throws JAXBException {
		return (T)_unmarshaller.unmarshal(is);
	}

	@SuppressWarnings("PublicInnerClass")
	public static class Builder<T> {

		private Map<String, Object> _marshallerProperties = Maps.newHashMap();
		private Map<String, Object> _unmarshallerProperties = Maps.newHashMap();

		private Builder() {}

		public Builder<T> setMarshallerProperty(String property, Object value) {
			_marshallerProperties.put(property, value);
			return this;
		}

		public Builder<T> setUnmarshallerProperty(String property, Object value) {
			_unmarshallerProperties.put(property, value);
			return this;
		}

		public XmlContext<T> build(Class<T> type, Class... types) throws JAXBException {
			JAXBContext context = JAXBContext.newInstance(Arrays.prepend(Class.class, type, types));
			Marshaller marshaller = context.createMarshaller();
			for (Map.Entry<String, Object> property : _marshallerProperties.entrySet()) {
				marshaller.setProperty(property.getKey(), property.getValue());
			}
			Unmarshaller unmarshaller = context.createUnmarshaller();
			for (Map.Entry<String, Object> property : _unmarshallerProperties.entrySet()) {
				unmarshaller.setProperty(property.getKey(), property.getValue());
			}
			return new XmlContext<>(marshaller, unmarshaller);
		}
	}

	public static <T> Builder<T> builder() {
		return new Builder<>();
	}
}
