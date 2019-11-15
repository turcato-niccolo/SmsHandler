package com.dezen.riccardo.smshandler.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * @author Riccardo De Zen
 * Abstract class to allow Room library to instantiate the database.
 */
//TODO create a wrapper class with singleton design pattern and synchronized to improve database performance
@Database(entities = {SmsEntity.class}, version = 1)
public abstract class SmsDatabase extends RoomDatabase {
    public abstract SmsDao access();
}