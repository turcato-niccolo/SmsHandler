package com.dezen.riccardo.smshandler;

public interface PeerProviderStub<P extends Peer>{
    /**
     * @return a randomly generated valid Peer
     */
    P getRandomPeer();
}
