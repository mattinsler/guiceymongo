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

import com.google.inject.Module;
import com.mattinsler.guiceymongo.data.IsData;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

import java.util.List;

public interface Builders {
	public interface FinishableConfiguration extends Module {
		DatabaseConfiguration mapDatabase(String databaseKey);
		CollectionConfiguration mapCollection(String collectionKey);
		BucketConfiguration mapBucket(String bucketKey);
	}
	public interface Configuration extends FinishableConfiguration {
		Module cloneFrom(String configurationName);
	}
	
	public interface DatabaseConfiguration {
		DatabaseOptionConfiguration to(String database);
        DatabaseOptionConfiguration asTestDatabase();
	}
	public interface DatabaseOptionConfiguration extends FinishableConfiguration, Module {
		FinishableConfiguration overConnection(String connectionKey);
	}
	
	public interface CollectionConfigurationOnlyTo {
		CollectionOptionConfiguration to(String collection);
	}
	public interface CollectionConfiguration extends CollectionConfigurationOnlyTo {
		CollectionConfigurationOnlyTo ofType(Class<? extends IsData> dataType);
	}
	public interface CollectionOptionConfiguration {
		FinishableConfiguration inDatabase(String databaseKey);
	}
	
	public interface BucketConfiguration {
		BucketOptionConfiguration to(String bucket);
	}
	public interface BucketOptionConfiguration {
		FinishableConfiguration inDatabase(String databaseKey);
	}
	
	public interface Connection {
		ConnectionWithHost host(String hostname);
		ConnectionWithPort port(int port);
        ConnectionWithSeeds seeds(List<ServerAddress> port);
        ConnectionWithReadPreference readPreference(ReadPreference readPreference);
	}
	public interface ConnectionWithHost extends Module {
		Module port(int port);
	}
	public interface ConnectionWithPort extends Module {
		Module host(String hostname);
	}
    public interface ConnectionWithSeeds extends Module {
        Module readPreference(ReadPreference readPreference);
    }
    public interface ConnectionWithReadPreference extends Module {
        Module seeds(List<ServerAddress> port);
    }
}
