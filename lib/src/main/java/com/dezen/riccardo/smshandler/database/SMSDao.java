package com.dezen.riccardo.smshandler.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * @author Riccardo De Zen
 * Interface defining methods to access the unread sms database.
 */
@Dao
interface SMSDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SMSEntity... entities);
    @Update
    void updateSms(SMSEntity... entities);
    @Delete
    void deleteSms(SMSEntity... entities);
    @Query("SELECT * FROM SMSEntity")
    SMSEntity[] loadAllSms();
    @Query("SELECT COUNT(id) FROM SMSEntity")
    int getCount();
}
