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

package com.mattinsler.guiceymongo.guice;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.util.Types;
import com.mattinsler.guiceymongo.GuiceyBucket;
import com.mattinsler.guiceymongo.GuiceyCollection;
import com.mattinsler.guiceymongo.data.IsData;
import com.mattinsler.guiceymongo.guice.spi.Configuration;
import com.mattinsler.guiceymongo.guice.spi.MongoBuckets;
import com.mattinsler.guiceymongo.guice.spi.MongoCollections;
import com.mattinsler.guiceymongo.guice.spi.MongoDatabases;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public final class GuiceyMongoUtil {
	@Inject
	static Injector _injector;
	
	private GuiceyMongoUtil() {}
	
	public static String getCurrentConfiguration() {
		return _injector.getInstance(Key.get(String.class, Configuration.class));
	}
	
	public static void setCurrentConfiguration(Binder binder, String configuration) {
		binder.skipSources(GuiceyMongoUtil.class).bindConstant().annotatedWith(Configuration.class).to(configuration);
	}
	
	public static DB getDatabase(String databaseKey) {
		return _injector.getInstance(Key.get(DB.class, MongoDatabases.database(databaseKey)));
	}
	
	public static DBCollection getCollection(String collectionKey) {
		return _injector.getInstance(Key.get(DBCollection.class, MongoCollections.collection(collectionKey)));
	}
	
	public static <T extends IsData> GuiceyCollection<T> getGuiceyCollection(String collectionKey, Class<T> dataClass) {
		return (GuiceyCollection<T>)_injector.getInstance(Key.get(Types.newParameterizedType(GuiceyCollection.class, dataClass), MongoCollections.collection(collectionKey)));
	}
	
	public static GuiceyBucket getGuiceyBucket(String bucketKey) {
		return _injector.getInstance(Key.get(GuiceyBucket.class, MongoBuckets.bucket(bucketKey)));
	}
}
