package com.dezen.riccardo.networkmanager;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dezen.riccardo.networkmanager.database_dictionary.PeerDatabase;
import com.dezen.riccardo.networkmanager.database_dictionary.PeerEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Giorgia Bortoletti
 */
@RunWith(AndroidJUnit4.class)
public class PeerDatabaseTest {

    private PeerDatabase peerDatabase;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        peerDatabase = Room.inMemoryDatabaseBuilder(context, PeerDatabase.class).build();
    }

    @After
    public void closeDb() throws IOException {
        peerDatabase.close();
    }

    /*@Test //the number of entities depends on the moment, it isn't fixed
    public void getAll() {
        assertEquals(0 , peerDatabase.access().getAll().length);
    }*/

    @Test
    public void add() {
        peerDatabase.access().add(new PeerEntity("0"));
        assertEquals(true , peerDatabase.access().contains("0"));
    }

    @Test
    public void remove() {
        peerDatabase.access().add(new PeerEntity("1"));
        peerDatabase.access().remove(new PeerEntity("1"));
        assertEquals(false, peerDatabase.access().contains("1"));
    }

    @Test
    public void getContainsTrue() {
        peerDatabase.access().add(new PeerEntity("0"));
        assertEquals(true , peerDatabase.access().contains("0"));
    }

    @Test
    public void getContainsFalse() {
        assertEquals(false , peerDatabase.access().contains("333"));
    }

}

