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

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mattinsler.guiceymongo.GuiceyBucket;
import com.mattinsler.guiceymongo.guice.GuiceyMongo;
import com.mattinsler.guiceymongo.guice.annotation.MongoBucket;
import com.mongodb.gridfs.GridFS;

public class BucketConfigurationExample {
	@Inject
	BucketConfigurationExample(@MongoBucket(Buckets.Main) GridFS grid, @MongoBucket(Buckets.Main) GuiceyBucket bucket) {
		System.out.println(grid + " : " + bucket.getGridFS());
	}
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(
				// create the test configuration
				GuiceyMongo.configure(Configurations.Test)
					.mapDatabase(Databases.Main).to("test_db")
					// bucket names are global, you just need to assign the bucket to a database
					.mapBucket(Buckets.Main).to("bucket").inDatabase(Databases.Main),
				
				// choose the configuration to use
				GuiceyMongo.chooseConfiguration(Configurations.Test)
		);
		
		injector.getInstance(BucketConfigurationExample.class);
	}
}
