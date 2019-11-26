package com.dezen.riccardo.smshandler;

import android.content.Context;

public class PreferenceManager {

    /**
     * Method to return the Instance of this class for the default preference file.
     */
    public static PreferenceManager getDefault(Context context){
        return null;
    }

    /**
     * Method to return the instance of this class for the specified file name.
     * @param fileName the name of the preference file.
     * @return the Preference Manager for param fileName.
     */
    static PreferenceManager getManagerForFile(String fileName){
        return null;
    }

    /**
     * Method to save a key-value pair into the preferences.
     */
    public boolean putString(String key, String value){
        return false;
    }

    public void putClass(){

    }
}
