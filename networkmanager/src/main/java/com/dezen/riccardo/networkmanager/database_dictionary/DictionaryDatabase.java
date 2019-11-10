package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DictionaryEntity.class}, version = 1)
public abstract class DictionaryDatabase extends RoomDatabase {
    public abstract DictionaryDao access();
}