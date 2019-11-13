package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DictionaryEntity {
    public DictionaryEntity(String peerAddress, String resourceName) {
        this.peerAddress = peerAddress;
        this.resourceName = resourceName;
    }
    @PrimaryKey
    public String peerAddress;
    @PrimaryKey
    public String resourceName;
}
