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

import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mattinsler.guiceymongo.guice.GuiceyMongo;
import com.mongodb.DBObject;

public class StoredProcedureProxyExample {
	public interface PrintQuery {
		void print(String message);
	}
	
	/*
	 * You can create server side methods and then use a javascript proxy to call them:
	 * db.system.js.save({
	 * 		_id: 'getData',
	 * 		value: function(count) {
	 * 			return db.foo.find().limit(count).toArray();
	 * 		}
	 * })
	 * 
	 * db.system.js.save({
	 * 		_id: 'getCount',
	 * 		value: function() {
	 * 			return db.foo.count();
	 * 		}
	 * })
	 * 
	 * WARNING: You cannot return a cursor from a server side method
	 */
	public interface TestQuery {
		// this will execute "db.eval(function(count){return getData(count)}, count)"
		List<DBObject> getData(int count);
		// this will execute "db.eval(function(){return getCount()})"
		int getCount();
	}
	
	@Inject
	StoredProcedureProxyExample(PrintQuery printQuery) {
		// will print Hello world on the server
		printQuery.print("Hello world");
	}
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(
				// create the test configuration
				GuiceyMongo.configure(Configurations.Test)
					.mapDatabase(Databases.Main).to("test_db"),
				
				// generate a proxy class that calls db.eval using the method
				// names and arguments in the proxy interface, using the database
				GuiceyMongo.javascriptProxy(PrintQuery.class, Databases.Main),
				
				// choose the configuration to use
				GuiceyMongo.chooseConfiguration(Configurations.Test)
		);
		
		injector.getInstance(StoredProcedureProxyExample.class);
	}
}
