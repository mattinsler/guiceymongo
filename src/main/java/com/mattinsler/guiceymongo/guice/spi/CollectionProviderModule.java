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

import com.google.inject.*;
import com.mattinsler.guiceymongo.guice.annotation.MongoCollection;
import com.mattinsler.guiceytools.ProviderModule;
import com.mongodb.DB;
import com.mongodb.DBCollection;

class CollectionProviderModule extends ProviderModule<DBCollection> {
	private final String _databaseKey;
	
	private Injector _injector;
	private String _configuration;
	private Provider<DB> _databaseProvider;

	public CollectionProviderModule(String databaseKey, String collectionKey) {
		super(Key.get(DBCollection.class, AnnotationUtil.guiceyMongoCollection(collectionKey)));
		_databaseKey = databaseKey;
	}
	
	private <T> T getInstance(Injector injector, Key<T> key) {
		if (!injector.getBindings().containsKey(key))
			return null;
		return injector.getInstance(key);
	}

	@Inject
	void initialize(Injector injector, @Configuration String configuration) {
		_injector = injector;
		_configuration = configuration;
		_databaseProvider = injector.getProvider(Key.get(DB.class, AnnotationUtil.guiceyMongoDatabase(_databaseKey)));
	}

	public void configure(Binder binder) {
		binder.skipSources(CollectionProviderModule.class).bind(key).toProvider(this);
	}
	
	private void cacheDBCollection() throws Exception {
		String collectionKey = ((MongoCollection) ((Key<?>) key).getAnnotation()).value();

		String clonedConfiguration = getInstance(_injector, Key.get(String.class, AnnotationUtil.clonedConfiguration(_configuration)));
		if (clonedConfiguration == null) {
			String collection = _injector.getInstance(Key.get(String.class, AnnotationUtil.configuredCollection(_configuration, collectionKey)));
			_cachedDBCollection = _databaseProvider.get().getCollection(collection);
		} else {
			String collection = _injector.getInstance(Key.get(String.class, AnnotationUtil.configuredCollection(clonedConfiguration, collectionKey)));
			_cachedDBCollection = _databaseProvider.get().getCollection(collection);
		}
	}
	
	private DBCollection _cachedDBCollection;
	
	public DBCollection get() {
		try {
			if (_cachedDBCollection == null)
				cacheDBCollection();
			return _cachedDBCollection;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
