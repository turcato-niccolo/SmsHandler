package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ResourceEntity {
    public ResourceEntity(String name, String value) {
        this.name = name;
        this.value = value;
    }
    @PrimaryKey
    public String name;
    @ColumnInfo(name = "value")
    public String value;
}
