package org.openape.api.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.openape.api.usercontext.Context;
import org.openape.api.usercontext.Preference;
import org.openape.api.usercontext.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserContextTest {
	private static Logger logger = LoggerFactory.getLogger(UserContextTest.class);

	@Test
	public void emptyJsonParsingTest() {
		UserContext.getObjectFromJson("{}");
	}

	@Test
	public void emptyDefaultContextJsonParsingTest() {
		UserContext.getObjectFromJson("{\"default\":{\"preferences\":{}}}");
	}

	@Test
	public void stringPreferenceJsonParsingTest() throws IllegalArgumentException, IOException {
		String preferenceTerm = "http://openape.gpii.eu/terms/test";
		UserContext uc = UserContext
				.getObjectFromJson("{\"default\":{\"preferences\":  { \"" + preferenceTerm + "\":  \"test\"} } }");
		assertEquals(uc.getContext("default").getPreference(preferenceTerm).getValue(), "test");
	}

	@Test
	public void booleanPreferenceJsonParsingTest() {
		String preferenceTerm = "http://openape.gpii.eu/terms/booleanTest";
		UserContext uc = UserContext
				.getObjectFromJson("{\"default\":{\"preferences\":  { \"" + preferenceTerm + "\":  true} } }");
		assertEquals(uc.getContext("default").getPreference(preferenceTerm).getValue(), true);
	}

	@Test
	public void integerPreferenceJsonParsingTest() {
		String preferenceTerm = "http://openape.gpii.eu/terms/integerTest";
		System.out.println("integertest:");
		UserContext uc = UserContext
				.getObjectFromJson("{\"default\":{\"preferences\":  { \"" + preferenceTerm + "\":  42} } }");
		assertEquals(42, uc.getContext("default").getPreference(preferenceTerm).getValue());
	}

	@Test
	public void doublePreferenceJsonParsingTest() throws IllegalArgumentException, IOException {
		String preferenceTerm = "http://openape.gpii.eu/terms/doubleTest";
		UserContext uc = UserContext
				.getObjectFromJson("{\"default\":{\"preferences\":  { \"" + preferenceTerm + "\":  3.141} } }");

		assertEquals((Double) uc.getContext("default").getPreference(preferenceTerm).getValue(), new Double(3.141));
	}

	@Test public void testSerilization() throws IOException {
		String baseUri = "http://openape.gpii.eu/terms/test/";
		String preferenceTermText = baseUri + "test";
		String preferenceTermBoolean = baseUri + "boolean";
		String preferenceTermDouble = baseUri + "double" ;
		String preferenceTermInt = baseUri + "int";
		
		UserContext uc = new UserContext();
		Context c = new Context("default", "Default Context");
		c.addPreference( new Preference(preferenceTermText, "text" )  );
		c.addPreference( new Preference(preferenceTermBoolean, true)  );
		c.addPreference( new Preference(preferenceTermDouble, 3.141)  );
		c.addPreference( new Preference(preferenceTermInt, 42)  );
				
		uc.addContext(c);
		
		String jsonString = uc.getFrontendJson();
		logger.info(jsonString);
		assertTrue(jsonString.contains(buildJsonString(preferenceTermText)+ "\"text\"" ) );
		assertTrue(jsonString.contains(buildJsonString(preferenceTermBoolean)+ true ) );
		assertTrue(jsonString.contains(buildJsonString(preferenceTermDouble)+ 3.141 ) );
		assertTrue(jsonString.contains(buildJsonString(preferenceTermDouble)+ 3.141 ) );
	}

	private String buildJsonString(String preferenceTerm) {
		
		return "\"" + preferenceTerm	+ "\":";
	}


}
