package com.dezen.riccardo.networkmanager.deprecated;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DictionaryEntity.class}, version = 1, exportSchema = false)
public abstract class DictionaryDatabase extends RoomDatabase {
    public abstract DictionaryDao access();
}