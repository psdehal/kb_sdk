package us.kbase.mobu.initializer.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;
import us.kbase.mobu.initializer.ModuleInitializer;

public class InitializerTest {

	private static final String[] LANGS = { "python", "perl", "java" };
	private static final String SIMPLE_MODULE_NAME = "a_simple_module_for_unit_testing";
	private static final String EXAMPLE_METHOD_NAME = "count_contigs_in_set"; 
	private static final String USER_NAME = "kbasedev";
	private static final String[] EXPECTED_PATHS = {
	   "docs", 
	   "scripts",
	   "test", 
	   "ui", 
	   "lib",
	   "ui/narrative", 
	   "ui/narrative/methods/",
	   "ui/widgets",
	   "lib/README.md",
	   "docs/README.md",
	   "test/README.md",
	   "data/README.md",
	   "scripts/entrypoint.sh",
	   "LICENSE",
	   "README.md",
	   ".travis.yml",
	   "Dockerfile",
	   "Makefile"
	};
	private static final String[] EXPECTED_DEFAULT_PATHS = {
	   "ui/narrative/methods/example_method/img",
	   "ui/narrative/methods/example_method/spec.json",
	   "ui/narrative/methods/example_method/display.yaml"
	};
	private static List<String> allExpectedDefaultPaths;
	private static List<String> allExpectedExamplePaths;
	private static List<String> perlPaths;
	private static List<String> pythonPaths;
	private static List<String> javaPaths;
	
	@After
	public void tearDownModule() throws IOException {
		File module = Paths.get(SIMPLE_MODULE_NAME).toFile();
		if (module.exists() && module.isDirectory()) {
			FileUtils.deleteDirectory(module);
		}
	}
	
	public boolean checkPaths(List<String> pathList, String moduleName) {
		for (String p : pathList) {
			File f = Paths.get(moduleName, p).toFile();
			if (!f.exists()) {
				System.out.println("Unable to find path: " + f.toString());
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks that all directories and files are present as expected.
	 * @param moduleName
	 * @return
	 */
	public boolean examineModule(String moduleName, boolean useExample, String language) {
		List<String> expectedPaths = allExpectedDefaultPaths;
		if (useExample)
			expectedPaths = allExpectedExamplePaths;

		if (!checkPaths(expectedPaths, moduleName))
			return false;
		
		if (useExample) {
			List<String> langPaths;
			switch(language) {
				case "python":
					langPaths = pythonPaths;
					break;
				case "perl":
					langPaths = perlPaths;
					break;
				case "java":
					langPaths = javaPaths;
					break;
				default:
					langPaths = pythonPaths;
					break;
			}
			if (!checkPaths(langPaths, moduleName))
				return false;
		}
		return true;
	}
	
	@BeforeClass
	public static void prepPathsToCheck() {
		allExpectedDefaultPaths = new ArrayList<String>(Arrays.asList(EXPECTED_PATHS));
		allExpectedDefaultPaths.addAll(Arrays.asList(EXPECTED_DEFAULT_PATHS));
		allExpectedDefaultPaths.add(SIMPLE_MODULE_NAME + ".spec");
		
		allExpectedExamplePaths = new ArrayList<String>(Arrays.asList(EXPECTED_PATHS));
		allExpectedExamplePaths.add(SIMPLE_MODULE_NAME + ".spec");
		allExpectedExamplePaths.add("scripts/start_server.sh");
		allExpectedExamplePaths.add("ui/narrative/methods/" + EXAMPLE_METHOD_NAME);
		allExpectedExamplePaths.add("ui/narrative/methods/" + EXAMPLE_METHOD_NAME + "/img");
		allExpectedExamplePaths.add("ui/narrative/methods/" + EXAMPLE_METHOD_NAME + "/spec.json");
		allExpectedExamplePaths.add("ui/narrative/methods/" + EXAMPLE_METHOD_NAME + "/display.yaml");
		
		perlPaths = new ArrayList<String>();
		perlPaths.add("lib/Bio/KBase/" + SIMPLE_MODULE_NAME + "/Impl.pm");

		pythonPaths = new ArrayList<String>();
		pythonPaths.add("lib/biokbase/" + SIMPLE_MODULE_NAME + "/Impl.py");

		javaPaths = new ArrayList<String>();
		javaPaths.add("lib/src/us/kbase/" + SIMPLE_MODULE_NAME + "/" + SIMPLE_MODULE_NAME + "_impl.java");
		
	}
	
	@Test
	public void testSimpleModule() throws Exception {
		boolean useExample = false;
		ModuleInitializer initer = new ModuleInitializer(SIMPLE_MODULE_NAME, USER_NAME, false);
		initer.initialize(useExample);
		Assert.assertTrue(examineModule(SIMPLE_MODULE_NAME, useExample, ModuleInitializer.DEFAULT_LANGUAGE));
	}
	
	@Test
	public void testModuleWithUser() throws Exception {
		boolean useExample = false;
		String language = "python";
		ModuleInitializer initer = new ModuleInitializer(SIMPLE_MODULE_NAME, USER_NAME, language, false);
		initer.initialize(false);
		Assert.assertTrue(examineModule(SIMPLE_MODULE_NAME, useExample, language));
	}
	
	@Test(expected=IOException.class)
	public void testModuleAlreadyExists() throws Exception {
		File f = Paths.get(SIMPLE_MODULE_NAME).toFile();
		if (!f.exists())
			f.mkdir();
		ModuleInitializer initer = new ModuleInitializer(SIMPLE_MODULE_NAME, USER_NAME, false);
		initer.initialize(false);
	}
	
	@Test(expected=Exception.class)
	public void testNoNameModule() throws Exception {
		ModuleInitializer initer = new ModuleInitializer(null, null, false);
		initer.initialize(false);
	}
	
	@Test
	public void testPerlModuleExample() throws Exception {
		boolean useExample = true;
		String lang = "perl";
		ModuleInitializer initer = new ModuleInitializer(SIMPLE_MODULE_NAME, USER_NAME, lang, false);
		initer.initialize(useExample);
		Assert.assertTrue(examineModule(SIMPLE_MODULE_NAME, useExample, lang));
	}

	@Test
	public void testPythonModuleExample() throws Exception {
		boolean useExample = true;
		String lang = "python";
		ModuleInitializer initer = new ModuleInitializer(SIMPLE_MODULE_NAME, USER_NAME, lang, false);
		initer.initialize(useExample);
		Assert.assertTrue(examineModule(SIMPLE_MODULE_NAME, useExample, lang));
	}

	@Test
	public void testJavaModuleExample() throws Exception {
		boolean useExample = true;
		String lang = "java";
		ModuleInitializer initer = new ModuleInitializer(SIMPLE_MODULE_NAME, USER_NAME, lang, false);
		initer.initialize(useExample);
		Assert.assertTrue(examineModule(SIMPLE_MODULE_NAME, useExample, lang));
	}
}