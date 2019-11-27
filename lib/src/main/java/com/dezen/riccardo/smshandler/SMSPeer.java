package com.dezen.riccardo.smshandler;

import android.telephony.PhoneNumberUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dezen.riccardo.smshandler.exceptions.InvalidAddressException;

/**
 * @author Riccardo De Zen. Based on decisions of whole class.
 */
public class SMSPeer extends Peer<String> {

    private static final String CON_ERROR =
            "The given address is invalid, refer to SMSPeer.checkAddressValidity(String address)";
    //Max length includes + sign.
    public static final int MAX_ADDRESS_LENGTH = 16;
    public static final int MIN_ADDRESS_LENGTH = 3;

    private String address;

    /**
     * @param address the address for the peer
     */
    public SMSPeer(String address){
        if(isAddressValid(address) != PhoneNumberValidity.ADDRESS_VALID)
            throw new InvalidAddressException(CON_ERROR);
        this.address = address;
    }

    /**
     * @return the address of the peer
     */
    @Override
    public String getAddress() {
        return address;
    }

    /**
     * @return true if address fulfills international phone address standards
     */
    @Override
    public boolean isValid(){
        return isAddressValid(address) == PhoneNumberValidity.ADDRESS_VALID;
    }

    /**
     * @param address the address whose validity should be checked
     * @return An enum value to indicate what is wrong with the phone number or that nothing is wrong
     */
    public static PhoneNumberValidity isAddressValid(String address){

        if(!address.matches("\\+?\\d+"))
            return PhoneNumberValidity.ADDRESS_NOT_PHONE_NUMBER;

        if(address.charAt(0) != '+')
            return PhoneNumberValidity.ADDRESS_NO_COUNTRY_CODE;

        if(address.length() > MAX_ADDRESS_LENGTH)
            return PhoneNumberValidity.ADDRESS_TOO_LONG;

        if(address.length() < MIN_ADDRESS_LENGTH)
            return PhoneNumberValidity.ADDRESS_TOO_SHORT;
        //Failure of some other criteria in isGlobalPhoneNumber, the country code is probably invalid.
        if(!PhoneNumberUtils.isGlobalPhoneNumber(address))
            return PhoneNumberValidity.ADDRESS_GENERIC_INVALID;

        return PhoneNumberValidity.ADDRESS_VALID;
    }

    /**
     * @return String type representation of the Object
     */
    @NonNull
    public String toString(){
        return address;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof SMSPeer) return ((SMSPeer) obj).address.equals(this.address);
        else return false;
    }

    /**
     * Enum to indicate the validity or invalidity of an address.
     */
    public enum PhoneNumberValidity {
        ADDRESS_NOT_PHONE_NUMBER,
        ADDRESS_NO_COUNTRY_CODE,
        ADDRESS_TOO_LONG,
        ADDRESS_TOO_SHORT,
        ADDRESS_GENERIC_INVALID,
        ADDRESS_VALID
    }
}
