/**
 *      Copyright (C) 2010 Lowereast Software
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.mattinsler.guiceymongo.util;

import java.util.Arrays;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public final class DBObjectUtil {
	private DBObjectUtil() {}
	
    private static <T> T checkArgNull(T obj, String name) throws IllegalArgumentException
    {
        if (obj == null)
            throw new IllegalArgumentException("The argument '" + name + "' was null.");
        return obj;
    }
    
	public static <E extends Enum<E>> E valueOfEnum(Class<E> enumClass, String value, E defaultValue) {
		try {
			if (value != null) {
				return Enum.valueOf(enumClass, value);
			}
		} catch (Exception e) {
			// log?
		}
		return defaultValue;
	}
	
	private static Object navigateNext(List<?> current, String key, boolean create) {
		Object next = null;
		try {
			int index = Integer.parseInt(key);
			next = current.get(index);
		} catch (NumberFormatException e) {
			throw new IllegalStateException("Navigation error: When the current object is a list, the key must be a number", e);
		}
		
		return next;
	}
	
	private static Object navigateNext(DBObject current, String key, boolean create) {
		Object next = current.get(key);
		if (next == null && create) {
			next = new BasicDBObject();
			current.put(key, next);
		}
		return next;
	}
	
	private static Object navigateToSubObject(DBObject dbObject, String[] property) {
		return navigateToSubObject(dbObject, property, false);
	}
	
	private static Object navigateToSubObject(DBObject dbObject, String[] property, boolean create) {
		Object current = dbObject;
		for (String key : property) {
			Object next = null;
			
			if (current instanceof List<?>) {
				next = navigateNext((List<?>)current, key, create);
			} else if (current instanceof DBObject) {
				next = navigateNext((DBObject)current, key, create);
			} else {
				throw new IllegalStateException("This shouldn't happen...  find me and fix me now!");
			}
			
			if (next == null)
				return null;
			current = next;
		}
		return current;
	}
	
	private static Object setPropertyValue(Object object, String key, Object value) {
		if (value instanceof Enum<?>)
			value = ((Enum<?>)value).name();

		if (object instanceof List<?>) {
			// not sure yet
		} else if (object instanceof DBObject) {
			return ((DBObject)object).put(key, value);
		}
		throw new IllegalStateException();
	}
	
	private static Object removePropertyValue(Object object, String key) {
		if (object instanceof List<?>) {
			// not sure yet
		} else if (object instanceof DBObject) {
			return ((DBObject)object).removeField(key);
		}
		throw new IllegalStateException();
	}
	
	
	
	public static boolean containsProperty(DBObject dbObject, String property) {
		checkArgNull(dbObject, "dbObject");
		String[] pieces = checkArgNull(property, "property").split("[.]");
		return navigateToSubObject(dbObject, pieces) != null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getProperty(DBObject dbObject, String property) {
		checkArgNull(dbObject, "dbObject");
		String[] pieces = checkArgNull(property, "property").split("[.]");
		return (T)navigateToSubObject(dbObject, pieces);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getProperty(DBObject dbObject, String property, T defaultValue) {
		checkArgNull(dbObject, "dbObject");
		String[] pieces = checkArgNull(property, "property").split("[.]");
		Object value = navigateToSubObject(dbObject, pieces);
		return value == null ? defaultValue : (T)value;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getProperty(DBObject dbObject, String property, T defaultValue, boolean saveIfDefault) {
		checkArgNull(dbObject, "dbObject");
		String[] pieces = checkArgNull(property, "property").split("[.]");
		Object value = navigateToSubObject(dbObject, pieces);
		if (value != null)
			return (T)value;
		if (saveIfDefault)
			setProperty(dbObject, property, defaultValue);
		return defaultValue;
	}
	
	public static <T extends Enum<T>> T getEnumProperty(DBObject dbObject, Class<T> enumClass, String property, T defaultValue) {
		checkArgNull(dbObject, "dbObject");
		checkArgNull(enumClass, "enumClass");
		String[] pieces = checkArgNull(property, "property").split("[.]");
		Object value = navigateToSubObject(dbObject, pieces);
		return valueOfEnum(enumClass, (String)value, defaultValue);
	}
	
	public static <T> T setProperty(DBObject dbObject, String property, T value) {
		checkArgNull(dbObject, "dbObject");
		String[] pieces = checkArgNull(property, "property").split("[.]");
		checkArgNull(value, "value");
		
		if (pieces.length == 0)
			throw new IllegalArgumentException("");
		for (String key : pieces) {
			if (key == null)
				throw new IllegalArgumentException("");
		}
		
		Object object = dbObject;
		if (pieces.length > 1)
			object = navigateToSubObject(dbObject, Arrays.copyOf(pieces, pieces.length - 1), true);
		return (T)setPropertyValue(object, pieces[pieces.length - 1], value);
	}
	
	public static <T> T removeProperty(DBObject dbObject, String property) {
		checkArgNull(dbObject, "dbObject");
		String[] pieces = checkArgNull(property, "property").split("[.]");
		
		if (pieces.length == 0)
			throw new IllegalArgumentException("");
		for (String key : pieces) {
			if (key == null)
				throw new IllegalArgumentException("");
		}
		
		Object object = dbObject;
		if (pieces.length > 1)
			object = navigateToSubObject(dbObject, Arrays.copyOf(pieces, pieces.length - 1), true);
		return (T)removePropertyValue(object, pieces[pieces.length - 1]);
	}

	public static String encodeKey(String key) {
		return key.replace("_", "_" + (int)'_').replace(".", "_" + (int)'.');
	}
	
	public static String decodeKey(String key) {
		return key.replace("_" + (int)'.', ".").replace("_" + (int)'_', "_");
	}
}
