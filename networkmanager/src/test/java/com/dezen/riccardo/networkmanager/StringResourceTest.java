package com.dezen.riccardo.networkmanager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringResourceTest {

    private static final String SEPARATOR = "\r";

    @Test
    public void getName() {
        String name = "Harry";
        String value = "Potter";
        StringResource stringResource = new StringResource(name, value);
        assertEquals(name, stringResource.getName());
    }

    @Test
    public void getValue() {
        String name = "Severus";
        String value = "Snape";
        StringResource stringResource = new StringResource(name, value);
        assertEquals(value, stringResource.getValue());
    }

    @Test
    public void setValue() {
        String originalName = "OriginalName";
        String originalValue = "OriginalValue";
        StringResource stringResource = new StringResource(originalName, originalValue);
        String newValue = "newValue";
        stringResource.setValue(newValue);
        assertEquals(newValue, stringResource.getValue());
    }

    @Test
    public void isValid_nullName(){
        String name = null;
        String value = "exampleValue";
        StringResource stringResource = new StringResource(name, value);
        assertFalse(stringResource.isValid());
    }

    @Test
    public void isValid_emptyName(){
        String name = "";
        String value = "exampleValue";
        StringResource stringResource = new StringResource(name, value);
        assertFalse(stringResource.isValid());
    }

    @Test
    public void isValid(){
        String name = "exampleName";
        String value = "exampleValue";
        StringResource stringResource = new StringResource(name, value);
        assertTrue(stringResource.isValid());
    }

    @Test
    public void getDefaultInvalid(){
        assertFalse(StringResource.getDefaultInvalid().isValid());
    }

    @Test
    public void parseString_justFine(){
        String key = "I'm";
        String value = "fine";
        String stringToParse = key+SEPARATOR+value;
        StringResource parseResult = StringResource.parseString(stringToParse, SEPARATOR);
        assertEquals(key, parseResult.getName());
        assertEquals(value, parseResult.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseString_tooLittleParts(){
        String key = "I'm";
        String stringToParse = key+SEPARATOR;
        StringResource parseResult = StringResource.parseString(stringToParse, SEPARATOR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseString_tooManyParts(){
        String key = "I'm";
        String value = "fine";
        String extra = "I'm an extra";
        String stringToParse = key+SEPARATOR+value+SEPARATOR+extra;
        StringResource parseResult = StringResource.parseString(stringToParse, SEPARATOR);
    }
}