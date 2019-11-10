package com.dezen.riccardo.networkmanager;

import com.dezen.riccardo.smshandler.SMSPeer;

import java.util.List;

public class NetworkDictionary implements Dictionary<SMSPeer, StringResource>{
    private List<SMSPeer> peers;
    private List<StringResource[]> resources;
    @Override
    public SMSPeer findPeerWithResource(StringResource resource) {
        return null;
    }

    @Override
    public StringResource[] findResourcesForPeer(SMSPeer peer) {
        return new StringResource[0];
    }

    @Override
    public SMSPeer[] getAvailablePeers() {
        return new SMSPeer[0];
    }

    @Override
    public StringResource[] getAvailableResource() {
        return new StringResource[0];
    }

    @Override
    public void add(SMSPeer peer, StringResource[] resources) {

    }

    @Override
    public void remove(SMSPeer peer) {

    }
}
