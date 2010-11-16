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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class GuiceyBucket {
	private final GridFS _grid;
	
	public GuiceyBucket(GridFS gridFs) {
		_grid = gridFs;
	}
	
	public GuiceyBucket(DB database, String bucket) {
		this(new GridFS(database, bucket));
	}
	
	public GridFSInputFile createFile(byte[] data) {
		return _grid.createFile(data);
	}
	
	public GridFSInputFile createFile(File file) throws IOException {
		return _grid.createFile(file);
	}
	
	public GridFSInputFile createFile(InputStream stream) {
		return _grid.createFile(stream);
	}
	
	public GridFSInputFile createFile(InputStream stream, String filename) {
		return _grid.createFile(stream, filename);
	}
	
	public GridFSDBFile findOne(DBObject query) {
		return _grid.findOne(query);
	}
	
	public GridFSDBFile findOne(ObjectId identity) {
		return _grid.findOne(identity);
	}
	
	public GridFSDBFile findOne(String filename) {
		return _grid.findOne(filename);
	}
	
	public List<GridFSDBFile> find(DBObject query) {
		return _grid.find(query);
	}
	
	public GridFSDBFile find(ObjectId identity) {
		return _grid.find(identity);
	}
	
	public List<GridFSDBFile> find(String filename) {
		return _grid.find(filename);
	}
	
	public void remove(DBObject query) {
		_grid.remove(query);
	}
	
	public void remove(ObjectId identity) {
		_grid.remove(identity);
	}
	
	public void remove(String filename) {
		_grid.remove(filename);
	}
	
	public GridFS getGridFS() {
		return _grid;
	}
}
