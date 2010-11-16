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

package com.mattinsler.guiceymongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.Arrays;

public class FieldSet {
	private final DBObject _set = new BasicDBObject();

	private FieldSet() {}

	public FieldSet and(String... fields) {
		if (fields != null) {
			for (String field : fields)
				_set.put(field, 1);
		}
		return this;
	}

	public FieldSet and(String field, int limit) {
        if (limit == 0) {
            _set.put(field, 1);
        } else {
		    _set.put(field, new BasicDBObject("$slice", limit));
        }
		return this;
	}

	public FieldSet and(String field, int skip, int limit) {
        if (skip == 0) {
            return and(field, limit);
        } else if (limit != 0) {
		    _set.put(field, new BasicDBObject("$slice", Arrays.<Integer>asList(skip, limit)));
        }
		return this;
	}

	public FieldSet not(String... fields) {
		if (fields != null) {
			for (String field : fields)
				_set.put(field, 0);
		}
		return this;
	}

	public DBObject build() {
		return _set;
	}

	public static FieldSet with(String... fields) {
		return new FieldSet().and(fields);
	}

	public static FieldSet with(String field, int elements) {
		return new FieldSet().and(field, elements);
	}

	public static FieldSet with(String field, int skip, int limit) {
		return new FieldSet().and(field, skip, limit);
	}

	public static FieldSet without(String... fields) {
		return new FieldSet().and(fields);
	}

	public static final FieldSet Empty = FieldSet.with();
}
