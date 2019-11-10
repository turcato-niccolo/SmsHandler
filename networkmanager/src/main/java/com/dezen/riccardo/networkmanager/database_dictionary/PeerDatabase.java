package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PeerEntity.class}, version = 1)
public abstract class PeerDatabase extends RoomDatabase {
    public abstract PeerDao access();
}