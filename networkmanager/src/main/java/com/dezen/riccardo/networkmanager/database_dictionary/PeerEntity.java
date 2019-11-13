package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PeerEntity {
    public PeerEntity(String address) {
        this.address = address;
    }
    @PrimaryKey
    public String address;
}
