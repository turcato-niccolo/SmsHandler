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
public class ResourceEntity {

    /**
     * Constructor of ResourceEntity
     * @param key
     * @param value
     */
    public ResourceEntity(String key, String value) {
        this.key = key;
        this.value = value;
    }
    @PrimaryKey @NonNull
    @ColumnInfo(name = NetworkDictionary.RESOURCE_TABLE_KEY_COLUMN_NAME)
    public String key;
    @ColumnInfo(name = NetworkDictionary.RESOURCE_TABLE_VALUE_COLUMN_NAME)
    public String value;
}
