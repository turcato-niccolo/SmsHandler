package com.dezen.riccardo.networkmanager;

import com.dezen.riccardo.smshandler.SMSMessage;

public interface OnNetworkEventListener<R extends Resource>{
    /**
     * Method to be called when a requested Resource has ben fully delivered and made available.
     * @param obtRes the obtained Resource
     */
    void onResourceObtained(R obtRes);
    /**
     * //TODO better onMessageReceived with generic types
     */
    void onMessageReceived(SMSMessage message);
}
