package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * @author Giorgia Bortoletti
 */
@Database(entities = {PeerEntity.class, ResourceEntity.class}, version = 1, exportSchema = false)
public abstract class DictionaryDatabase extends RoomDatabase {
    public abstract DictionaryDao access();
}