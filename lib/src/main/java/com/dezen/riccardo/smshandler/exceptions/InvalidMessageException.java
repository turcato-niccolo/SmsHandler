package com.dezen.riccardo.smshandler.exceptions;

public class InvalidMessageException extends IllegalArgumentException {
    public InvalidMessageException(String message){
        super(message);
    }
}
