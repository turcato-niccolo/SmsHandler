package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PeerEntity {
    public PeerEntity(String peerAddress) {
        this.peerAddress = peerAddress;
    }
    @PrimaryKey @NonNull
    public String peerAddress;
}
