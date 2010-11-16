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

package com.mattinsler.guiceymongo.guice.spi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.internal.Lists;
import com.google.inject.internal.Maps;
import com.mattinsler.guiceymongo.GuiceyMongoEvalException;
import com.mattinsler.guiceymongo.annotation.ItemType;
import com.mattinsler.guiceymongo.data.DataWrapper;
import com.mattinsler.guiceymongo.data.IsData;
import com.mattinsler.guiceymongo.data.IsWrapper;
import com.mattinsler.guiceymongo.guice.GuiceyMongo;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class JavascriptProxy<T> implements Module, Provider<T> {
	private final String _databaseKey;
	private final Class<T> _proxyInterface;
	
	private Provider<DB> _databaseProvider;
	
	public JavascriptProxy(Class<T> proxyInterface, String databaseKey) {
		if (!proxyInterface.isInterface())
			throw new RuntimeException("Proxy class must be an interface");
		_proxyInterface = proxyInterface;
		_databaseKey = databaseKey;
	}

	@Inject
	void initialize(Injector injector) {
		_databaseProvider = injector.getProvider(Key.get(DB.class, AnnotationUtil.guiceyMongoDatabase(_databaseKey)));
	}
	
	public void configure(Binder binder) {
		binder.skipSources(JavascriptProxy.class, GuiceyMongo.class).bind(_proxyInterface).toProvider(this);
	}
	
	@SuppressWarnings("unchecked")
	public T get() {
		return (T)Proxy.newProxyInstance(_proxyInterface.getClassLoader(), new Class<?>[] { _proxyInterface }, new Handler<T>(_proxyInterface, _databaseProvider.get()));
	}
	
	private static String createArgumentString(int numArguments) {
		StringBuilder builder = new StringBuilder("(");
		for (int x = 0; x < numArguments; ++x) {
			if (x > 0)
				builder.append(",");
			builder.append((char)('a' + x));
		}
		builder.append(")");
		return builder.toString();
	}
	
	private interface Invocation {
		Object invoke(DB database, Object[] args);
	}
	
	private static class VoidNoArgInvocation implements Invocation {
		private final String _code;
		public VoidNoArgInvocation(String methodName) {
			_code = methodName + "()";
		}
		public Object invoke(DB database, Object[] args) {
			return database.eval(_code);
		}
	}
	
	private static class VoidInvocation implements Invocation {
		private final String _code;
		public VoidInvocation(String methodName, Class<?>[] argumentTypes) {
			String argumentString = createArgumentString(argumentTypes.length);
			_code = "function" + argumentString + "{" + methodName + argumentString + "}";
		}
		public Object invoke(DB database, Object[] args) {
			return database.eval(_code, args);
		}
	}
	
	private interface Converter<From, To> {
		To convert(From value);
	}
	private static final Converter<Double, Integer> DoubleToIntConverter = new Converter<Double, Integer>() {
		public Integer convert(Double value) {
			return value.intValue();
		}
	};
	private static final Converter<Object, Object> NoOpConverter = new Converter<Object, Object>() {
		public Object convert(Object value) {
			return value;
		}
	};
	
	private static class DBObjectToWrapperConverter<T extends IsData> implements Converter<DBObject, T> {
		private final DataWrapper<T> _wrapper;
		public DBObjectToWrapperConverter(Class<T> dataClass) {
			try {
				_wrapper = (DataWrapper<T>)dataClass.getField("DataWrapper").get(null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		public T convert(DBObject value) {
			try {
				return (T)_wrapper.wrap(value);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	private static class ListDBObjectToWrapperConverter<T extends IsData> implements Converter<List<DBObject>, List<T>> {
		private final DBObjectToWrapperConverter<T> _itemWrapper;
		public ListDBObjectToWrapperConverter(Class<T> dataClass) {
			_itemWrapper = new DBObjectToWrapperConverter<T>(dataClass);
		}
		public List<T> convert(List<DBObject> value) {
			try {
				List<T> list = Lists.newArrayList();
				for (DBObject o : value)
					list.add(_itemWrapper.convert(o));
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	private static class ReturningInvocation implements Invocation {
		private final String _code;
		
		private Converter<?, ?> _converter = NoOpConverter;
		
		public ReturningInvocation(Class<?> returnType, Method method, Class<?>[] argumentTypes) {
			String argumentString = createArgumentString(argumentTypes.length);
			_code = "function" + argumentString + "{return " + method.getName() + argumentString + "}";

			if (IsData.class.isAssignableFrom(returnType) || IsWrapper.class.isAssignableFrom(returnType)) {
				_converter = new DBObjectToWrapperConverter(returnType);
			} else if (List.class.isAssignableFrom(returnType)) {
				ItemType itemType = method.getAnnotation(ItemType.class);
				if (itemType != null && (IsData.class.isAssignableFrom(itemType.value()) || IsWrapper.class.isAssignableFrom(itemType.value())))
					_converter = new ListDBObjectToWrapperConverter(itemType.value());
			} else if (int.class.equals(returnType) || Integer.class.equals(returnType)) {
				_converter = DoubleToIntConverter;
			}
		}
		@SuppressWarnings("unchecked")
		public Object invoke(DB database, Object[] args) {
			return ((Converter<Object, Object>)_converter).convert(database.eval(_code, args));
		}
	}
	
	private static class Handler<T> implements InvocationHandler {
		private final DB _database;
		private final Class<T> _proxyInterface;
		private final Map<Method, Invocation> _invocations = Maps.newHashMap();
		
		public Handler(Class<T> proxyInterface, DB database) {
			_proxyInterface = proxyInterface;
			_database = database;
			
			for (Method method : proxyInterface.getDeclaredMethods()) {
				if (Modifier.isPublic(method.getModifiers()) && Modifier.isAbstract(method.getModifiers())) {
					Invocation invocation;
					
					String methodName = method.getName();
					Class<?> methodReturnType = method.getReturnType();
					Class<?>[] argumentTypes = method.getParameterTypes();
					
					if (Void.class.equals(methodReturnType) || void.class.equals(methodReturnType)) {
						if (argumentTypes.length == 0) {
							invocation = new VoidNoArgInvocation(methodName);
						} else {
							invocation = new VoidInvocation(methodName, argumentTypes);
						}
					} else {
						invocation = new ReturningInvocation(methodReturnType, method, argumentTypes);
					}
					
					_invocations.put(method, invocation);
				}
			}
		}
		
		public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) throws Throwable {
			if (Object.class.equals(method.getDeclaringClass())) {
				return method.invoke(this, args);
			}
			try {
				Invocation invocation = _invocations.get(method);
				if (invocation == null)
					throw new RuntimeException();
				return invocation.invoke(_database, args);
			} catch (MongoException e) {
				throw GuiceyMongoEvalException.create(e);
			}
		}
		
		@Override
		public String toString() {
			return _proxyInterface.getName();
		}
	}
}
