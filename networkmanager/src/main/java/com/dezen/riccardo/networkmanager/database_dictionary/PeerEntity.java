package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
    public String address;
}
