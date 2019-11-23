package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.room.Database;
import androidx.room.RoomDatabase;
/**
 * @author Giorgia Bortoletti
 */
@Database(entities = {ResourceEntity.class}, version = 1)
public abstract class ResourceDatabase extends RoomDatabase {
    public abstract ResourceDao access();
}