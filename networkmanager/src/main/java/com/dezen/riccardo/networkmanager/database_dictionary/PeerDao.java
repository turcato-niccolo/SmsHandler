package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * @author Giorgia Bortoletti
 */
@Dao
public interface PeerDao {
    public static final String nameDb="peerentity";

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void add(PeerEntity... peerEntities);
    @Update
    public void update(PeerEntity... peerEntities);
    @Delete
    public void remove(PeerEntity... peerEntities);

    @Query("SELECT * FROM "+nameDb)
    public PeerEntity[] getAll();

    @Query("SELECT 1 FROM " +nameDb+" WHERE address=:address")
    public boolean contains(String address);

}
