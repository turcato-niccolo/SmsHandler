package com.dezen.riccardo.smshandler.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * @author Riccardo De Zen
 * Abstract class to allow Room library to instantiate the database.
 */
@Database(entities = {SMSEntity.class}, version = 1)
abstract class SMSDatabase extends RoomDatabase {
    public abstract SMSDao access();
}