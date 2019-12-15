package com.dezen.riccardo.networkmanager;

import android.os.Bundle;

/**
 * String key and value implementation for Resource interface.
 * @author Riccardo De Zen.
 */
public class StringResource extends Resource<String, String> {

    private static final String PARSE_ERR = "The provided separator does not divide into a valid key-value pair.";

    private String name;
    private String value;

    public StringResource(String name, String value){
        this.name = name;
        this.value = value;
    }

    /**
     * @return the name of this Resource
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return the value of this Resource
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * Setter for value
     * @param value the new value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Method to check whether this Resource is valid
     * @return true if this Resource is valid, false if not
     */
    @Override
    public boolean isValid(){
        return this.name != null && !this.name.isEmpty();
    }

    /**
     * Method to return extras for this Resource type, no extras are implemented for this type.
     * @return extras attached to this Resource, if any, an empty Bundle otherwise
     */
    @Override
    public Bundle getExtras() {
        return new Bundle();
    }

    /**
     * @return a default invalid Resource
     */
    public static StringResource getDefaultInvalid(){
        return new StringResource("","");
    }

    /**
     * Method to parse a String Resource from a String
     * @param string the String to parse
     * @param separator the separator which divides key and value, must not be a character of the
     *                  key nor of the value.
     * @return The StringResource
     * @throws IllegalArgumentException if the separator splits the String in more than two parts
     */
    public static StringResource parseString(String string, String separator) throws IllegalArgumentException{
        String[] parts = string.split(separator);
        if(parts.length > 2)
            throw new IllegalArgumentException(PARSE_ERR);
        return new StringResource(parts[0], parts[1]);
    }
}
