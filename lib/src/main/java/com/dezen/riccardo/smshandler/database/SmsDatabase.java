package com.dezen.riccardo.smshandler.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

//TODO create a wrapper class with singleton design pattern to improve safety of database operations
@Database(entities = {SmsEntity.class}, version = 1)
public abstract class SmsDatabase extends RoomDatabase {
    public abstract SmsDao access();
}