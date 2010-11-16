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
import com.google.inject.Provider;
import com.mattinsler.guiceymongo.GuiceyBucket;
import com.mattinsler.guiceytools.ProviderModule;
import com.mongodb.gridfs.GridFS;

class BucketProviderModule extends ProviderModule<GuiceyBucket> {
	private final String _bucketKey;
	private Provider<GridFS> _gridFsProvider;

	public BucketProviderModule(String bucketKey) {
		super(Key.get(GuiceyBucket.class, AnnotationUtil.guiceyMongoBucket(bucketKey)));
		_bucketKey = bucketKey;
	}

	@Inject
	void initialize(Injector injector, @Configuration String configuration) {
		_gridFsProvider = injector.getProvider(Key.get(GridFS.class, AnnotationUtil.guiceyMongoBucket(_bucketKey)));
	}

	public void configure(Binder binder) {
		binder.skipSources(BucketProviderModule.class).bind(key).toProvider(this);
	}
	
	private void cacheGuiceyBucket() throws Exception {
		_cachedGuiceyBucket = new GuiceyBucket(_gridFsProvider.get());
	}
	
	private GuiceyBucket _cachedGuiceyBucket;
	
	public GuiceyBucket get() {
		try {
			if (_cachedGuiceyBucket == null)
				cacheGuiceyBucket();
			return _cachedGuiceyBucket;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
