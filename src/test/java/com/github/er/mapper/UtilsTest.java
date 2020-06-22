package com.github.er.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilsTest {

    @Test
    public void preconditionOK() {
        boolean caught = false;
        try {
            Utils.precondition("zdub");
        } catch (Exception e) {
            caught = true;
        }

        Assertions.assertFalse(caught);
    }

    @Test
    public void preconditionFailure() {
        boolean caught = false;
        String error = null;
        try {
            Utils.precondition("   ");
        } catch (Error e) {
            caught = true;
            error = e.getLocalizedMessage();
        }

        Assertions.assertTrue(caught);
        Assertions.assertNotNull(error);
        Assertions.assertEquals("field name must be present", error);
    }

    @Test
    public void setterCamelCaseTest() {
        String result = Utils.setterCamelCase("name");
        Assertions.assertEquals("setName", result);
    }

    @Test
    public void getterCamelCaseTest() {
        String result = Utils.getterCamelCase("name");
        Assertions.assertEquals("getName", result);
    }

    @Test
    public void mapTest() {
        String result = Utils.map(x -> "abc" + x, "def");
        Assertions.assertEquals("abcdef", result);
    }

    @Test
    public void testTest() {
        boolean result = Utils.test("abc"::equalsIgnoreCase, "abc");
        Assertions.assertTrue(result);
    }

}
