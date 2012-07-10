package junit;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for junit");
		//$JUnit-BEGIN$

		//$JUnit-END$
		return suite;
	}

}
