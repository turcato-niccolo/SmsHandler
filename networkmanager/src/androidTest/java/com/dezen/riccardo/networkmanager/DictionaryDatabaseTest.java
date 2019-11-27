package com.dezen.riccardo.networkmanager;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dezen.riccardo.networkmanager.database_dictionary.DictionaryDatabase;
import com.dezen.riccardo.networkmanager.database_dictionary.PeerEntity;
import com.dezen.riccardo.networkmanager.database_dictionary.ResourceEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * @author Giorgia Bortoletti
 */
@RunWith(AndroidJUnit4.class)
public class DictionaryDatabaseTest {

    private DictionaryDatabase dictionaryDatabase;
    private String valueResource;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        dictionaryDatabase = Room.inMemoryDatabaseBuilder(context, DictionaryDatabase.class).build();
        valueResource = "example";
    }

    @After
    public void closeDb() throws IOException {
        dictionaryDatabase.close();
    }

    @Test
    public void addPeer() {
        String address = "0";
        PeerEntity peer = new PeerEntity(address);
        dictionaryDatabase.access().addPeer(peer);
        assertTrue(dictionaryDatabase.access().containsPeer(address));
    }

    @Test
    public void removePeer() {
        String address = "1";
        PeerEntity peer = new PeerEntity(address);
        dictionaryDatabase.access().addPeer(peer);
        dictionaryDatabase.access().removePeer(peer);
        assertFalse(dictionaryDatabase.access().containsPeer(address));
    }

    @Test
    public void getContainsPeerTrue() {
        String address = "0";
        PeerEntity peer = new PeerEntity(address);
        dictionaryDatabase.access().addPeer(peer);
        dictionaryDatabase.access().addPeer(peer);
        assertTrue(dictionaryDatabase.access().containsPeer(address));
    }

    @Test
    public void getContainsPeerFalse() {
        assertFalse(dictionaryDatabase.access().containsPeer("333"));
    }

    @Test
    public void addResource() {
        String key = "1";
        ResourceEntity entity = new ResourceEntity(key, valueResource);
        dictionaryDatabase.access().addResource(entity);
        assertTrue(dictionaryDatabase.access().containsResource(key));
    }

    @Test
    public void updateResource() {
        String key = "2";
        ResourceEntity entity = new ResourceEntity(key, valueResource);
        dictionaryDatabase.access().addResource(entity);
        String valueToUpdate = valueResource+"11";
        ResourceEntity entityToUpdate = new ResourceEntity(key, valueToUpdate);
        dictionaryDatabase.access().updateResource(entityToUpdate);
        assertEquals(valueToUpdate, dictionaryDatabase.access().getResource(key).value);
    }

    @Test
    public void removeResource() {
        String keyName = "3";
        ResourceEntity entity = new ResourceEntity(keyName, valueResource);
        dictionaryDatabase.access().addResource(entity);
        dictionaryDatabase.access().removeResource(entity);
        assertFalse(dictionaryDatabase.access().containsResource(keyName));
    }

    @Test
    public void getContainsResourceTrue() {
        String keyName = "4";
        ResourceEntity entity = new ResourceEntity(keyName, valueResource);
        dictionaryDatabase.access().addResource(entity);
        assertTrue(dictionaryDatabase.access().containsResource(keyName));
    }

    @Test
    public void getContainsResourceFalse() {
        assertFalse(dictionaryDatabase.access().containsResource("55"));
    }
    
}

