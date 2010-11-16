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

import java.util.Map;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.mattinsler.guiceymongo.guice.GuiceyMongoUtil;

public class GuiceyMongoBinder {
	private final Binder _binder;
	
	public GuiceyMongoBinder(Binder binder) {
		_binder = binder.skipSources(GuiceyMongoBinder.class);
		_binder.install(new SingletonModule<Class<GuiceyMongoUtil>>(GuiceyMongoUtil.class) {
			public void configure(Binder binder) {
				binder.requestStaticInjection(GuiceyMongoUtil.class);
			}
		});
	}
	
	public void addError(String message) {
		_binder.addError(message);
	}
	
	public void bindConnectionHostname(String connectionKey, String hostname) {
		_binder.bind(Key.get(String.class, AnnotationUtil.configuredConnectionHostname(connectionKey))).toInstance(hostname);
	}
	
	public void bindConnectionPort(String connectionKey, int port) {
		_binder.bind(Key.get(int.class, AnnotationUtil.configuredConnectionPort(connectionKey))).toInstance(port);
	}
	
	public void bindConfiguredDatabase(String configurationName, String databaseKey, String database) {
		_binder.install(new DBProviderModule(databaseKey));
		_binder.bind(Key.get(String.class, AnnotationUtil.configuredDatabase(configurationName, databaseKey))).toInstance(database);
	}
	
	public void bindConfiguredDatabaseConnection(String configurationName, String databaseKey, String connectionKey) {
		_binder.bind(Key.get(String.class, AnnotationUtil.configuredDatabaseConnection(configurationName, databaseKey))).toInstance(connectionKey);
	}
	
	public void bindConfiguredCollection(String configurationName, String databaseKey, String collectionKey, String collection) {
		_binder.install(new CollectionProviderModule(databaseKey, collectionKey));
		_binder.bind(Key.get(String.class, AnnotationUtil.configuredCollection(configurationName, collectionKey))).toInstance(collection);
	}
	
	public void bindConfiguredBucket(String configurationName, String databaseKey, String bucketKey, String bucket) {
		_binder.install(new GridFSProviderModule(databaseKey, bucketKey));
		_binder.install(new BucketProviderModule(bucketKey));
		_binder.bind(Key.get(String.class, AnnotationUtil.configuredBucket(configurationName, bucketKey))).toInstance(bucket);
	}
	
	@SuppressWarnings("unchecked")
	public void bind(GuiceyMongoBindingCollector collector) {
		if (collector.getErrors().size() > 0) {
			for (String error : collector.getErrors())
				_binder.addError(error);
			return;
		}
		for (Module module : collector.getModules())
			_binder.install(module);
		for (Map.Entry<Key<?>, Object> binding : collector.getInstanceBindings().entrySet())
			_binder.bind((Key)binding.getKey()).toInstance(binding.getValue());
	}
}
