package com.mattinsler.guiceymongo.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.mattinsler.guiceymongo.data.generator.GuiceyDataGenerator;

/**
 * Unit tests for the com.mattinsler.guiceymongo.data.generator.
 */
public class GeneratorTest {
	private static final GuiceyDataGenerator GENERATOR = new GuiceyDataGenerator();
	private static final String SCHEMA = "src/test/data";
	private static final String OUTPUT = "src/test/generated";
	private static final String PACKAGE = "com.example";
	private static final String PACKAGE_FOLDER = PACKAGE.replace('.', '/');
	private static final String FILE_EXTENSION = ".data";
	private static final String SCHEMA_PERSON = String.format("%s/person.data", SCHEMA);
	private static final String SCHEMA_VEHICLE = String.format("%s/subfolder/vehicle.data", SCHEMA);
	private static final String OUTPUT_PERSON = String.format("%s/%s/Person.java", OUTPUT, PACKAGE_FOLDER);
	private static final String OUTPUT_VEHICLE = String.format("%s/%s/Vehicle.java", OUTPUT, PACKAGE_FOLDER);
	private static final Pattern REGEX_JAVADOC_PROPERTY = Pattern.compile("/\\*\\*\\s+ \\* Full name\\s+ \\*/\\s+@Override\\s+public String getName()");
	private static final Pattern REGEX_JAVADOC_DATA = Pattern.compile("/\\*\\*\\s+ \\* Represents a single person\\s+ \\*/\\s+public abstract class Person");
	private static final Pattern REGEX_JAVADOC_ENUM = Pattern.compile("/\\*\\*\\s+ \\* Color of eye\\s+ \\*/\\s+public static enum EyeColor");
	
	
	/**
	 * Perform necessary set up tasks before each test runs.
	 */
	@Before
	public void setUp() {
    	//Configure com.mattinsler.guiceymongo.data.generator
        GENERATOR.setSourceDirectory(OUTPUT);
        GENERATOR.setOutputPackage(PACKAGE);
        GENERATOR.setFileExtensions(FILE_EXTENSION);
        
        try {
        	//Delete anything in the output directory
			FileUtils.deleteDirectory(new File(OUTPUT));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("Could not clear output directory for test.");
		}
	}
    
    /**
     * Test file generation properly recurses into subdirectories to look for
     * schema definition files.
     */
    @Test
    public void directoryRecursion() {
        //Generate source files
        GENERATOR.generate(SCHEMA);
        
        //Test for class in root of directory
    	final File person = new File(OUTPUT_PERSON);
        Assert.assertTrue(person.exists());
        
        //Test for class stored in subdirectory
    	final File vehicle = new File(OUTPUT_VEHICLE);
        Assert.assertTrue(vehicle.exists());
    }
    
    /**
     * Ensure every path to generate is being generated.
     */
    @Test
    public void multiplePaths() {
        //Generate source files
        GENERATOR.generate(SCHEMA_PERSON, SCHEMA_VEHICLE);
        
        //Test for class in root of directory
    	final File person = new File(OUTPUT_PERSON);
        Assert.assertTrue(person.exists());
        
        //Test for class stored in subdirectory
    	final File vehicle = new File(OUTPUT_VEHICLE);
        Assert.assertTrue(vehicle.exists());
    }
    
    /**
     * Test that nothing is written to System.out when the isQuiet option is
     * specified.
     */
    @Test
    public void isQuiet() {
    	//Keep the original System.out
    	final PrintStream oldSystemOut = System.out;
    	
    	//Configure com.mattinsler.guiceymongo.data.generator
        GENERATOR.setIsQuiet(true);
        
        //Replace System.out with a stream we can measure
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final PrintStream systemOut = new PrintStream(outStream);
        System.setOut(systemOut);
        
        //Generate source files
        GENERATOR.generate(SCHEMA_PERSON);
        
        //Flush any writes and close to prevent further ones
        systemOut.flush();
        systemOut.close();
        
        //Check to make sure nothing was written during execution
        Assert.assertEquals(outStream.size(), 0);
        
        //Restore System.out
        System.setOut(oldSystemOut);
        GENERATOR.setIsQuiet(false);
        
    }
    
    /**
     * Make sure that the property JavaDocs are being properly generated.
     */
    @Test
    public void propertyJavaDoc() {
        //Generate source files
        GENERATOR.generate(SCHEMA_PERSON);
        
        //Test for class in root of directory
    	final File person = new File(OUTPUT_PERSON);
        Assert.assertTrue(person.exists());
        
        try {
			final String contents = FileUtils.readFileToString(person);
			Assert.assertTrue(REGEX_JAVADOC_PROPERTY.matcher(contents).find());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("Could not read generated file.");
		}
    }
    
    /**
     * Make sure that the data JavaDocs are being properly generated.
     */
    @Test
    public void dataJavaDoc() {
        //Generate source files
        GENERATOR.generate(SCHEMA_PERSON);
        
        //Test for class in root of directory
    	final File person = new File(OUTPUT_PERSON);
        Assert.assertTrue(person.exists());
        
        try {
			final String contents = FileUtils.readFileToString(person);
			Assert.assertTrue(REGEX_JAVADOC_DATA.matcher(contents).find());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("Could not read generated file.");
		}
    }
    
    /**
     * Make sure that the enum JavaDocs are being properly generated.
     */
    @Test
    public void enumJavaDoc() {
        //Generate source files
        GENERATOR.generate(SCHEMA_PERSON);
        
        //Test for class in root of directory
    	final File person = new File(OUTPUT_PERSON);
        Assert.assertTrue(person.exists());
        
        try {
			final String contents = FileUtils.readFileToString(person);
			Assert.assertTrue(REGEX_JAVADOC_ENUM.matcher(contents).find());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("Could not read generated file.");
		}
    }
}