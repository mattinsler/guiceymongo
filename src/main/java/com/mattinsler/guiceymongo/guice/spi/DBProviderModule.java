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

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.mattinsler.guiceymongo.GuiceyMongoException;
import com.mattinsler.guiceymongo.guice.annotation.MongoDatabase;
import com.mattinsler.guiceytools.ProviderModule;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import java.net.UnknownHostException;
import java.util.UUID;

class DBProviderModule extends ProviderModule<DB> {
	private static class RemoveTemporaryDB extends Thread {
		private final DB _db;
		public RemoveTemporaryDB(DB db) {
			_db = db;
		}
		@Override
		public void run() {
			_db.dropDatabase();
		}
	}
	
	private Injector _injector;
	private String _configuration;
 
	public DBProviderModule(String databaseKey) {
		super(Key.get(DB.class, AnnotationUtil.guiceyMongoDatabase(databaseKey)));
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
	}

	public void configure(Binder binder) {
		binder.skipSources(DBProviderModule.class).bind(key).toProvider(this);
	}
	
	private Mongo getConnection(String configuration, String databaseKey) throws MongoException, UnknownHostException {
		String connectionKey = getInstance(_injector, Key.get(String.class, AnnotationUtil.configuredDatabaseConnection(configuration, databaseKey)));
		if (connectionKey != null) {
			String hostname = getInstance(_injector, Key.get(String.class, AnnotationUtil.configuredConnectionHostname(connectionKey)));
			Integer port = getInstance(_injector, Key.get(int.class, AnnotationUtil.configuredConnectionPort(connectionKey)));

			if (hostname == null)
				hostname = "localhost";
			if (port == null)
				return new Mongo(hostname);
			return new Mongo(hostname, port.intValue());
		}
		return new Mongo();
	}

	private void cacheDB() throws MongoException, UnknownHostException {
		String databaseKey = ((MongoDatabase)key.getAnnotation()).value();
		
		String clonedConfiguration = getInstance(_injector, Key.get(String.class, AnnotationUtil.clonedConfiguration(_configuration)));
		if (clonedConfiguration == null) {
			String database = _injector.getInstance(Key.get(String.class, AnnotationUtil.configuredDatabase(_configuration, databaseKey)));
			Mongo connection = getConnection(_configuration, databaseKey);
			_cachedDB = connection.getDB(database);
            // test connection
            _cachedDB.getCollectionNames();
		} else {
			Mongo connection = getConnection(clonedConfiguration, databaseKey);
			_cachedDB = connection.getDB(UUID.randomUUID().toString());
			String clonedDatabase = _injector.getInstance(Key.get(String.class, AnnotationUtil.configuredDatabase(clonedConfiguration, databaseKey)));
			_cachedDB.eval("var c = connect('" + clonedDatabase + "'); c.system.js.find().forEach(function(x){db.system.js.save(x)}); db.system.js.ensureIndex({_id: 1});");
			
			Runtime.getRuntime().addShutdownHook(new RemoveTemporaryDB(_cachedDB));
		}
	}
 
	private DB _cachedDB;
	
	public DB get() {
		try {
			if (_cachedDB == null)
				cacheDB();
			return _cachedDB;
		} catch (MongoException e) {
			throw new GuiceyMongoException("Could not connect to an instance of MongoDB", e);
		} catch (UnknownHostException e) {
            throw new GuiceyMongoException("Could not connect to an instance of MongoDB", e);
        }
	}
}
