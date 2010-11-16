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

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.internal.Lists;
import com.google.inject.internal.Maps;
import com.google.inject.internal.Sets;
import com.mattinsler.guiceymongo.data.IsData;

public class GuiceyMongoBindingCollector {
	private final List<String> _errors = Lists.newArrayList();
	private final Set<Module> _modules = Sets.newHashSet();
	private final Map<Key<?>, Object> _instanceBindings = Maps.newHashMap();
	
	public GuiceyMongoBindingCollector() {
	}
	
	public void addError(String message) {
		_errors.add(message);
	}
	
	public void bindClonedConfiguration(String configurationName, String clonedConfigurationName) {
		_instanceBindings.put(Key.get(String.class, AnnotationUtil.clonedConfiguration(configurationName)), clonedConfigurationName);
	}
	
	public void bindConnectionHostname(String connectionKey, String hostname) {
		_instanceBindings.put(Key.get(String.class, AnnotationUtil.configuredConnectionHostname(connectionKey)), hostname);
	}
	
	public void bindConnectionPort(String connectionKey, int port) {
		_instanceBindings.put(Key.get(int.class, AnnotationUtil.configuredConnectionPort(connectionKey)), port);
	}
	
	public void bindConfiguredDatabase(String configurationName, String databaseKey, String database) {
		_modules.add(new DBProviderModule(databaseKey));
		_instanceBindings.put(Key.get(String.class, AnnotationUtil.configuredDatabase(configurationName, databaseKey)), database);
	}
	
	public void bindConfiguredDatabaseConnection(String configurationName, String databaseKey, String connectionKey) {
		_instanceBindings.put(Key.get(String.class, AnnotationUtil.configuredDatabaseConnection(configurationName, databaseKey)), connectionKey);
	}
	
	public void bindConfiguredCollection(String configurationName, String databaseKey, String collectionKey, String collection) {
		_modules.add(new CollectionProviderModule(databaseKey, collectionKey));
		_instanceBindings.put(Key.get(String.class, AnnotationUtil.configuredCollection(configurationName, collectionKey)), collection);
	}

	public <T extends IsData> void bindConfiguredCollectionDataType(String configurationName, String collectionKey, Class<T> dataType) {
		_modules.add(new GuiceyCollectionProviderModule<T>(collectionKey, dataType));
	}
	
	public void bindConfiguredBucket(String configurationName, String databaseKey, String bucketKey, String bucket) {
		_modules.add(new GridFSProviderModule(databaseKey, bucketKey));
		_modules.add(new BucketProviderModule(bucketKey));
		_instanceBindings.put(Key.get(String.class, AnnotationUtil.configuredBucket(configurationName, bucketKey)), bucket);
	}
	
	List<String> getErrors() {
		return _errors;
	}
	
	Set<Module> getModules() {
		return _modules;
	}
	
	Map<Key<?>, Object> getInstanceBindings() {
		return _instanceBindings;
	}
}
