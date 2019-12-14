package com.gruppo1.distributednetworkmanager.exceptions;

/**
 * Exception meant to be thrown when the user attempts to create a PendingRequest through an invalid
 * Action type.
 * @author Riccardo De Zen
 */
public class InvalidActionException extends Throwable {
    /**
     * Default constructor
     */
    public InvalidActionException(){
        super();
    }

    /**
     * @param message Error message for this Exception
     */
    public InvalidActionException(String message){
        super(message);
    }
}
