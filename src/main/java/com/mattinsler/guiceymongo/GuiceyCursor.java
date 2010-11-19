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

import com.mattinsler.guiceymongo.data.DataWrapper;
import com.mattinsler.guiceymongo.data.IsData;
import com.mongodb.DBCursor;

import java.util.Iterator;

public class GuiceyCursor<Item extends IsData> implements Iterable<Item> {
    private class _Iterator implements Iterator<Item> {
        public boolean hasNext() {
            return _cursor.hasNext();
        }

        public Item next() {
            return _wrapper.wrap(_cursor.next());
        }

        public void remove() {
            throw new UnsupportedOperationException("can't remove from a cursor");
        }
    }

	private final DBCursor _cursor;
	private final DataWrapper<Item> _wrapper;

	public GuiceyCursor(DBCursor cursor, DataWrapper<Item> wrapper) {
		_cursor = cursor;
		_wrapper = wrapper;
	}

	public DBCursor getDBCursor() {
		return _cursor;
	}
	
	public int count() {
		return _cursor.count();
	}

    public GuiceyCursor<Item> snapshot() {
        _cursor.snapshot();
        return this;
    }
	
	public GuiceyCursor<Item> limit(int number) {
        if (number > 0) {
		    _cursor.limit(number);
        }
		return this;
	}
	
	public GuiceyCursor<Item> skip(int number) {
        if (number > 0) {
		    _cursor.skip(number);
        }
		return this;
	}
	
	public GuiceyCursor<Item> sort(Order order) {
		_cursor.sort(order.build());
		return this;
	}
	
	public Iterator<Item> iterator() {
		return new _Iterator();
	}
}
