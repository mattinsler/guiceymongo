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

import com.mattinsler.guiceymongo.guice.annotation.MongoBucket;
import com.mattinsler.guiceymongo.guice.annotation.MongoCollection;
import com.mattinsler.guiceymongo.guice.annotation.MongoDatabase;
import com.mattinsler.guiceytools.annotation.Annotations;

final class AnnotationUtil {
	private AnnotationUtil() {}
	
	public static ClonedConfiguration clonedConfiguration(String configuration) {
		return Annotations.proxy(ClonedConfiguration.class, "configuration", configuration);
	}
	
	public static ConfiguredCollection configuredCollection(String configuration, String collectionKey) {
		return Annotations.proxy(ConfiguredCollection.class, "configuration", configuration, "collection", collectionKey);
	}
	
	public static ConfiguredBucket configuredBucket(String configuration, String bucketKey) {
		return Annotations.proxy(ConfiguredBucket.class, "configuration", configuration, "bucket", bucketKey);
	}
	
	public static ConfiguredCollectionDataType configuredCollectionDataType(String configuration, String collectionKey) {
		return Annotations.proxy(ConfiguredCollectionDataType.class, "configuration", configuration, "collection", collectionKey);
	}
	
	public static ConfiguredConnection configuredConnectionHostname(String connection) {
		return Annotations.proxy(ConfiguredConnection.class, "connection", connection, "key", "hostname");
	}
	
	public static ConfiguredConnection configuredConnectionPort(String connection) {
		return Annotations.proxy(ConfiguredConnection.class, "connection", connection, "key", "port");
	}

    public static ConfiguredConnection configuredConnectionSeeds(String connection) {
        return Annotations.proxy(ConfiguredConnection.class, "connection", connection, "key", "seeds");
    }

    public static ConfiguredConnection configuredConnectionReadPreference(String connection) {
        return Annotations.proxy(ConfiguredConnection.class, "connection", connection, "key", "readPreference");
    }

	public static ConfiguredDatabaseConnection configuredDatabaseConnection(String configuration, String databaseKey) {
		return Annotations.proxy(ConfiguredDatabaseConnection.class, "configuration", configuration, "database", databaseKey);
	}
	
	public static ConfiguredDatabase configuredDatabase(String configuration, String databaseKey) {
		return Annotations.proxy(ConfiguredDatabase.class, "configuration", configuration, "database", databaseKey);
	}
	
	public static MongoCollection guiceyMongoCollection(String collectionKey) {
		return Annotations.proxy(MongoCollection.class, "value", collectionKey);
	}
	
	public static MongoDatabase guiceyMongoDatabase(String databaseKey) {
		return Annotations.proxy(MongoDatabase.class, "value", databaseKey);
	}
	
	public static MongoBucket guiceyMongoBucket(String bucketKey) {
		return Annotations.proxy(MongoBucket.class, "value", bucketKey);
	}
}
