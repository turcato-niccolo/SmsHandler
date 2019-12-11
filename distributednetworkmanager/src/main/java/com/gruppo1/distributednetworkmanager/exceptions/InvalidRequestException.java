package com.gruppo1.distributednetworkmanager.exceptions;

/**
 * Exception meant to be thrown when the user attempts to create a PendingRequest through an invalid
 * Action type.
 * @author Riccardo De Zen
 */
public class InvalidRequestException extends Throwable {
    /**
     * Default constructor
     */
    public InvalidRequestException(){
        super();
    }

    /**
     * @param message Error message for this Exception
     */
    public InvalidRequestException(String message){
        super(message);
    }
}
