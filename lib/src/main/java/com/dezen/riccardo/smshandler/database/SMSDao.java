package com.dezen.riccardo.smshandler.database;

import androidx.room.Dao;

import com.dezen.riccardo.smshandler.SMSMessage;

/**
 * @author Riccardo De Zen
 * Interface extending the BaseDao class for SMSMessage
 */
@Dao
abstract class SMSDao extends BaseDao<SMSMessage>{
    /**
     * @return the name of the table containing the SMSMessage entities.
     */
    @Override
    public String getTableName(){
        return SMSMessage.SMS_TABLE_NAME;
    }
}