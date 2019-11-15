package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ResourceEntity {
    public ResourceEntity(String keyName, String value) {
        this.keyName = keyName;
        this.value = value;
    }
    @PrimaryKey @NonNull
    public String keyName;
    @ColumnInfo(name = "value")
    public String value;
}
