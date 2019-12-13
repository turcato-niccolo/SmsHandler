package com.gruppo1.distributednetworkmanager.exceptions;

/**
 * Exception meant to be thrown when a PendingRequest is used after being canceled
 * @author Riccardo De Zen
 */
public class CanceledRequestException extends IllegalStateException {
    public CanceledRequestException(String message){
        super(message);
    }
}
