package org.openape.api.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openape.api.usercontext.Context;
import org.openape.api.usercontext.Preference;

public class ContextTest {
@Test public void testGetPreference() {
	String preferenceTerm = "http://openape.gpii.eu/terms/test";
	Preference preference = new Preference(preferenceTerm, "test");
	Context c = new Context();
	c.addPreference(preference);;
	assertEquals(c.getPreference(preferenceTerm), preference);
}
}
