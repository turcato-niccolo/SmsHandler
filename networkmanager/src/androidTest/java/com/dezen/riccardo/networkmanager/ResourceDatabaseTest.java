package com.dezen.riccardo.networkmanager;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dezen.riccardo.networkmanager.database_dictionary.ResourceDatabase;
import com.dezen.riccardo.networkmanager.database_dictionary.ResourceEntity;

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
public class ResourceDatabaseTest {

    private ResourceDatabase resourceDatabase;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        resourceDatabase = Room.inMemoryDatabaseBuilder(context, ResourceDatabase.class).build();
    }

    @After
    public void closeDb() throws IOException {
        resourceDatabase.close();
    }

    /*@Test //the number of entities depends on the moment, it isn't fixed
    public void getAll() {
        assertEquals(0 , resourceDatabase.access().getAll().length);
    }*/

    @Test
    public void add() {
        resourceDatabase.access().add(new ResourceEntity("1", "111"));
        assertEquals(true , resourceDatabase.access().contains("1"));
    }

    @Test
    public void update() {
        resourceDatabase.access().add(new ResourceEntity("2", "111"));
        resourceDatabase.access().update(new ResourceEntity("2", "222"));
        assertEquals("222", resourceDatabase.access().getAll()[0].value);
    }

    @Test
    public void remove() {
        ResourceEntity entity = new ResourceEntity("3", "333");
        resourceDatabase.access().add(entity);
        resourceDatabase.access().remove(entity);
        assertEquals(false , resourceDatabase.access().contains("3"));
    }

    @Test
    public void getContainsTrue() {
        resourceDatabase.access().add(new ResourceEntity("4", "111"));
        assertEquals(true , resourceDatabase.access().contains("4"));
    }

    @Test
    public void getContainsFalse() {
        assertEquals(false , resourceDatabase.access().contains("55"));
    }


}
