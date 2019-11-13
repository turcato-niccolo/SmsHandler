package com.dezen.riccardo.networkmanager;

import android.content.Context;

import androidx.room.Room;

import com.dezen.riccardo.networkmanager.database_dictionary.DictionaryDatabase;
import com.dezen.riccardo.networkmanager.database_dictionary.DictionaryEntity;
import com.dezen.riccardo.networkmanager.database_dictionary.PeerEntity;
import com.dezen.riccardo.networkmanager.database_dictionary.ResourceEntity;

import static com.dezen.riccardo.smshandler.SmsHandler.SMS_HANDLER_LOCAL_DATABASE;

public class NetworkDictionaryDatabase implements DictionaryDb<PeerEntity, ResourceEntity> {

    private DictionaryDatabase db;

    public NetworkDictionaryDatabase(Context context){
        this.db = Room.databaseBuilder(context, DictionaryDatabase.class, SMS_HANDLER_LOCAL_DATABASE)
                .enableMultiInstanceInvalidation()
                .build();
    }

    @Override
    public PeerEntity findPeerWithResource(ResourceEntity resource) {
        PeerEntity peer = db.access().findPeerWithResource(resource.resourceName);
        return peer;
    }

    @Override
    public ResourceEntity[] findResourcesForPeer(PeerEntity peer) {
        ResourceEntity[] resourcesDb = db.access().findResourcesForPeer(peer.peerAddress);
        return resourcesDb;
    }

    @Override
    public PeerEntity[] getAvailablePeers() {
        return db.access().getAvailablePeers();
    }

    @Override
    public ResourceEntity[] getAvailableResource() {
        return db.access().getAvailableResource();
    }

    @Override
    public void add(PeerEntity peer, ResourceEntity[] resources) {
        for(ResourceEntity resource : resources)
            db.access().add(new DictionaryEntity(peer.peerAddress, resource.resourceName));
    }

    @Override
    public void remove(PeerEntity peer) {
        db.access().remove(peer.peerAddress);
    }
}
