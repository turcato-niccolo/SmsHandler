package com.dezen.riccardo.smshandler.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author Riccardo De Zen
 * Class defining the unread SmsMessage entity.
 */
//TODO evaluate advantages of fusion of this class with SMSMessage
@Entity
public class SmsEntity{
    public SmsEntity(int id, String address, String body) {
        this.id = id;
        this.address = address;
        this.body = body;
    }
    @PrimaryKey
    public int id;
    @ColumnInfo(name = "address")
    public String address;
    @ColumnInfo(name = "body")
    public String body;
}
