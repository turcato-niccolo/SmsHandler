package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ResourceEntity {
    public ResourceEntity(String resourceName, String value) {
        this.resourceName = resourceName;
        this.value = value;
    }
    @PrimaryKey @NonNull
    public String resourceName;
    @ColumnInfo(name = "value")
    public String value;
}
