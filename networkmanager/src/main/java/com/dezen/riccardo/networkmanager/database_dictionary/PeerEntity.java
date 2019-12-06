package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.dezen.riccardo.networkmanager.NetworkDictionary;

/**
 * @author Giorgia Bortoletti
 */
@Entity
public class PeerEntity {
    /**
     * Constructor of PeerEntity
     * @param address
     */
    public PeerEntity(String address) {
        this.address = address;
    }
    @PrimaryKey @NonNull
    @ColumnInfo (name = NetworkDictionary.PEER_TABLE_ADDRESS_COLUMN_NAME)
    public String address;
}
