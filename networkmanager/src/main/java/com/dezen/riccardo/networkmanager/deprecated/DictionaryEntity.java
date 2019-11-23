package com.dezen.riccardo.networkmanager.deprecated;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"peerAddress","resourceName"})
public class DictionaryEntity {
    public DictionaryEntity(String peerAddress, String resourceName) {
        this.peerAddress = peerAddress;
        this.resourceName = resourceName;
    }
    @NonNull
    public String peerAddress;
    @NonNull
    public String resourceName;
}
