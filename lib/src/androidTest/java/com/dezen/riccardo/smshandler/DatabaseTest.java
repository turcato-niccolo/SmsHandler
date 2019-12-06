package com.dezen.riccardo.smshandler;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.runner.RunWith;

/**
 * @author Giorgia Bortoletti
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class DatabaseTest {
    /*

    private SmsDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        db = Room.inMemoryDatabaseBuilder(context, SmsDatabase.class).build();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
    
    @Test
    public void getCount() {
        SmsEntity entity = new SmsEntity(1, "3334455666", "text");
        db.access().insert(entity);
        assertEquals(1, db.access().getCount());
    }

    @Test
    public void loadAllSms() {
        SmsEntity entity = new SmsEntity(1, "3334455666", "text");
        db.access().insert(entity);
        assertEquals(entity.id, db.access().loadAllSms()[0].id);
    }

    @Test
    public void deleteSms() {
        SmsEntity entity = new SmsEntity(1, "3334455666", "text");
        db.access().insert(entity);
        db.access().deleteSms(entity);
        assertEquals(0, db.access().getCount());
    }

    */

}
