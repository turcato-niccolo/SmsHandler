package com.dezen.riccardo.smshandler;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dezen.riccardo.smshandler.database.SMSDao;
import com.dezen.riccardo.smshandler.database.SMSDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Giorgia Bortoletti
 */
@RunWith(AndroidJUnit4.class)
public class SMSDatabaseTest {

    private SMSDatabase smsDatabase;
    private String bodyMessage = "text";
    private String addressPeer = "+39892424";
    private SMSPeer peer;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        smsDatabase = Room.inMemoryDatabaseBuilder(context, SMSDatabase.class).build();
        peer = new SMSPeer(addressPeer);
    }

    @After
    public void closeDb() throws IOException {
        smsDatabase.close();
    }

    @Test
    public void insert() {
        SMSMessage message = new SMSMessage(peer, bodyMessage);
        SMSDao dbAccess = smsDatabase.access();
        int countPrec = dbAccess.count();
        dbAccess.insert(message);
        assertEquals(countPrec+1 , dbAccess.count());
    }

    @Test
    public void delete() {
        SMSMessage message = new SMSMessage(5, peer, bodyMessage);
        SMSDao dbAccess = smsDatabase.access();
        dbAccess.insert(message);
        int countPrec = dbAccess.count();
        dbAccess.delete(message);
        assertEquals(countPrec-1, dbAccess.count());
    }
}

