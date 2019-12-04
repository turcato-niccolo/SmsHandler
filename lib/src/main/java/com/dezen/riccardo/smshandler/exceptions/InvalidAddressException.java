package com.dezen.riccardo.smshandler.exceptions;

public class InvalidAddressException extends IllegalArgumentException {
    public InvalidAddressException(String message){
        super(message);
    }
}
