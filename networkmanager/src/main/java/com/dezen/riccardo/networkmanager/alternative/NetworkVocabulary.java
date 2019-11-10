package com.dezen.riccardo.networkmanager.alternative;

import com.dezen.riccardo.smshandler.SMSPeer;

public class NetworkVocabulary implements Vocabulary<Integer, SMSPeer, Integer, StringResourceAlternative>{

    @Override
    public void addUser(Integer key, SMSPeer value) {

    }

    @Override
    public SMSPeer removeUser(Integer key) {
        return null;
    }

    @Override
    public SMSPeer editUser(Integer key, SMSPeer newValue) {
        return null;
    }

    @Override
    public SMSPeer getUser(Integer key) {
        return null;
    }

    @Override
    public void addResource(Integer key, StringResourceAlternative value) {

    }

    @Override
    public StringResourceAlternative removeResource(Integer key) {
        return null;
    }

    @Override
    public StringResourceAlternative editResource(Integer key, StringResourceAlternative newValue) {
        return null;
    }

    @Override
    public StringResourceAlternative getResource(Integer key) {
        return null;
    }
}
