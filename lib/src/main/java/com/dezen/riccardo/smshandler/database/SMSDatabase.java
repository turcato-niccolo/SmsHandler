package com.dezen.riccardo.smshandler.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.dezen.riccardo.smshandler.SMSMessage;

/**
 * @author Riccardo De Zen
 * Abstract class to allow Room library to instantiate the database.
 */
@Database(entities = {SMSMessage.class}, version = 1)
@TypeConverters({SMSConverters.class})
abstract class SMSDatabase extends RoomDatabase {
    public abstract SMSDao access();
}