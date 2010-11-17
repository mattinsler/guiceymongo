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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.mattinsler.guiceymongo.guice.GuiceyMongo;
import com.mattinsler.guiceymongo.guice.annotation.MongoDatabase;
import com.mongodb.DB;

public class DatabaseConfigurationExample {
	@Inject
	DatabaseConfigurationExample(@MongoDatabase(Databases.Main) DB mainDatabase, @MongoDatabase(Databases.Search) DB searchDatabase) {
		System.out.println(mainDatabase);
		System.out.println(searchDatabase);
	}
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(
				// create the production configuration
				GuiceyMongo.configure(Configurations.Production)
					.mapDatabase(Databases.Main).to("prod_db")
					.mapDatabase(Databases.Search).to("prod_search_db"),
					
				// create the test configuration
				GuiceyMongo.configure(Configurations.Test)
					.mapDatabase(Databases.Main).to("test_db"),
				
				// configurations can be augmented in separate modules
				GuiceyMongo.configure(Configurations.Test)
					.mapDatabase(Databases.Search).to("test_search_db"),
				
				// choose the configuration to use
				GuiceyMongo.chooseConfiguration(Configurations.Test),
				
				new AbstractModule() {
					@Override
					protected void configure() {
						bindInterceptor(Matchers.any(), Matchers.any(), new MethodInterceptor() {
							public Object invoke(MethodInvocation invocation) throws Throwable {
								System.out.println("Before: " + invocation.getMethod());
								Object result = invocation.proceed();
								System.out.println("After: " + invocation.getMethod());
								return result;
							}
						});
					}
				}
		);
		
		injector.getInstance(DatabaseConfigurationExample.class);
	}
}
