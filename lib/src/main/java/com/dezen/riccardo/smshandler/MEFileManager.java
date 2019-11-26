package com.dezen.riccardo.smshandler;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Class to handle mutually exclusive reading/writing to a file in the internal storage.
 */
public class MEFileManager {

    private static File rootDir;

    /**
     * Public constructor. Uses the given context to find the current file directory.
     * @param filePath the path for the file. If the file does not exist it will be created after the
     *                 first write operation.
     */
    private MEFileManager(String filePath){

    }

    /**
     *
     * @param context
     * @param filePath
     * @return
     */
    public static MEFileManager getInstanceForFile(Context context, String filePath){
        rootDir = context.getFilesDir();
        //TODO object pool
        return null;
    }

    /**
     * Method to write the toString() method output of an object to a given file.
     * @param data the data to be written to the file.
     */
    public synchronized void write(CharSequence data){

    }

    /**
     * Overload for write(String data)
     */
    public synchronized void write(byte[] data){

    }

    /**
     * Overload for write(String data)
     */
    public synchronized void write(char[] data){

    }

    /**
     * Method to read from an existing file.
     * @throws FileNotFoundException if the file could not be found.
     */
    public synchronized String read() throws FileNotFoundException {
        return null;
    }
}
