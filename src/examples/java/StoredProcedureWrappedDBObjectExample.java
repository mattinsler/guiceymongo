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

//import org.junit.Test;
//
//import com.google.inject.Guice;
//import com.google.inject.Injector;
//import com.google.inject.Key;
//import com.mattinsler.guiceymongo.IsReadable;
//import com.mattinsler.guiceymongo.guice.GuiceyMongo;
//import com.mattinsler.guiceymongo.guice.spi.MongoDatabases;
//import com.mongodb.DB;
//
//public class StoredProcedureWrappedDBObjectExample {
//	public interface FooQuery {
////		Foo get();
//	}
//	
//	private void printReadableDBObject(IsReadable object) {
//		for (String key : object.keySet()) {
//			if (object.hasField(key))
//				System.out.println(key + ": " + object.getField(key));
//		}
//	}
//	
//	@Test
//	public void test() {
//		Injector injector = Guice.createInjector(
//				GuiceyMongo.configure("Test")
//					.mapDatabase("Main").to("foo_db"),
//				GuiceyMongo.javascriptProxy(FooQuery.class, "Main"),
//				GuiceyMongo.chooseConfiguration("Test")
//		);
//		
//		DB db = injector.getInstance(Key.get(DB.class, MongoDatabases.database("Main")));
//		db.eval("db.system.js.save({_id: 'get', value: function() {return {foo: 'baz', bar: 4, baz: ['foo', 'bar']}}})");
//		
//		FooQuery query = injector.getInstance(FooQuery.class);
//		
////		Foo foo = query.get();
////		printReadableDBObject(foo);
//	}
//	
//	@Test
//	public void generateFoo() {
////		GuiceyDBObjectGenerator.generate("example", "example.mongo", "foo.json");
////		GuiceyDBObjectGenerator.generate("example", "example.mongo", "user.json");
////		GuiceyDBObjectGenerator.generate("example", "example.mongo", "session.json");
//	}
//}
