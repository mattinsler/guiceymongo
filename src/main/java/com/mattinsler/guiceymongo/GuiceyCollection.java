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
import com.mattinsler.guiceymongo.data.IsBuilder;
import com.mattinsler.guiceymongo.data.IsData;
import com.mongodb.*;

import java.util.List;

public class GuiceyCollection<Item extends IsData> {
	private final DBCollection _collection;
	private final DataWrapper<Item> _wrapper;
	
	public GuiceyCollection(DBCollection collection, DataWrapper<Item> wrapper) {
		_collection = collection;
		_wrapper = wrapper;
	}
	
	public DBCollection getDBCollection() {
		return _collection;
	}
	
	public DB getDB() {
		return _collection.getDB();
	}

    public long count() {
        return _collection.count();
    }

    public long count(DBObject query) {
        return _collection.count(query);
    }
	
	@SuppressWarnings("unchecked")
	public List<String> distinct(String key) {
		return _collection.distinct(key);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> distinct(String key, DBObject query) {
		return _collection.distinct(key, query);
	}

	public Item findOne() {
		return _wrapper.wrap(_collection.findOne());
	}
	
	public Item findOne(DBObject query) {
		return _wrapper.wrap(_collection.findOne(query));
	}
	
	public Item findOne(DBObject query, DBObject fields) {
		return _wrapper.wrap(_collection.findOne(query, fields));
	}
	
	public Item findOne(DBObject query, FieldSet fields) {
		return findOne(query, fields.build());
	}
	
//	public Item findOne(GuiceyQuery.Builder<Item> query) {
//		return null;
//	}
	
//	public GuiceyQuery.ExecutableBuilder<Item> findOne() {
//		return GuiceyQuery.newExecutableBuilder();
//	}
	
	public GuiceyCursor<Item> find() {
		DBCursor cursor = _collection.find();
		if (cursor == null)
			return null;
		return new GuiceyCursor<Item>(cursor, _wrapper);
	}
	
	public GuiceyCursor<Item> find(DBObject query) {
		DBCursor cursor = _collection.find(query);
		if (cursor == null)
			return null;
		return new GuiceyCursor<Item>(cursor, _wrapper);
	}
	
	public GuiceyCursor<Item> find(DBObject query, DBObject fields) {
		DBCursor cursor = _collection.find(query, fields);
		if (cursor == null)
			return null;
		return new GuiceyCursor<Item>(cursor, _wrapper);
	}
	
	public GuiceyCursor<Item> find(DBObject query, FieldSet fields) {
		return find(query, fields.build());
	}
	
	private static final DBObject EMPTY = new BasicDBObject();
	
	public Item findAndModify(DBObject query, DBObject update) {
		return findAndModify(query, update, null, false, null, false);
	}
	
	public Item findAndModify(DBObject query, DBObject update, DBObject sort) {
		return findAndModify(query, update, sort, false, null, false);
	}
	
	public Item findAndModify(DBObject query, DBObject update, boolean returnNewValue) {
		return findAndModify(query, update, null, returnNewValue, null, false);
	}
	
	public Item findAndModify(DBObject query, DBObject update, DBObject sort, boolean returnNewValue, FieldSet fields, boolean upsert) {
		DBObject command = new BasicDBObject("findandmodify", _collection.getName())
			.append("update", update);
		if (query != null && !EMPTY.equals(query))
			command.put("query", query);
		if (sort != null && !EMPTY.equals(sort))
			command.put("sort", sort);
		if (returnNewValue)
			command.put("new", true);
        if (fields != null)
            command.put("fields", fields.build());
        if (upsert)
            command.put("upsert", upsert);
		
		DBObject result = _collection.getDB().command(command);
        if (result.containsField("ok"))
			return _wrapper.wrap((DBObject)result.get("value"));
		throw new MongoException((Integer)result.get("code"), (String)result.get("errmsg"));
	}
	
	public Item findAndRemove(DBObject query, DBObject sort) {
		DBObject command = new BasicDBObject("findandmodify", _collection.getName())
			.append("remove", true);
		if (query != null && !EMPTY.equals(query))
			command.put("query", query);
		if (sort != null && !EMPTY.equals(sort))
			command.put("sort", sort);
		
		DBObject result = _collection.getDB().command(command);
		if (1 == (Integer)result.get("ok"))
			return _wrapper.wrap((DBObject)result.get("value"));
		throw new MongoException((Integer)result.get("code"), (String)result.get("errmsg"));
	}
	
//	public GuiceyQuery.ExecutableCursorBuilder<Item> find() {
//		return GuiceyQuery.newExecutableCursorBuilder();
//	}
	
//	public GuiceyQuery.ExecutableCursorBuilder<Item> select(FieldSet fieldSet) {
//		return GuiceyQuery.newExecutableCursorBuilder(fieldSet);
//	}
	
	public void insert(DBObject item) {
		_collection.insert(item);
	}
	
	public void insert(DBObject[] items) {
		_collection.insert(items);
	}
	
	public void insert(IsBuilder<Item> item) {
		insert(item.build());
	}
	
	public void insert(IsBuilder<Item>[] items) {
		DBObject[] objects = new DBObject[items.length];
		for (int x = 0; x < items.length; ++x)
			objects[x] = items[x].build();
		insert(objects);
	}
	
	public void insert(List<IsBuilder<Item>> items) {
		DBObject[] objects = new DBObject[items.size()];
		for (int x = 0; x < items.size(); ++x)
			objects[x] = items.get(x).build();
		insert(objects);
	}
	
	public void remove(DBObject query) {
		_collection.remove(query);
	}
	
	public void save(DBObject item) {
		_collection.save(item);
	}
	
	public void save(IsBuilder<Item> item) {
		save(item.build());
	}
	
	public void update(DBObject query, DBObject update) {
		_collection.update(query, update);
	}
	
	public void update(DBObject query, DBObject update, boolean upsert, boolean multi) {
		_collection.update(query, update, upsert, multi);
	}
	
	public void updateMulti(DBObject query, DBObject update) {
		_collection.updateMulti(query, update);
	}
}
