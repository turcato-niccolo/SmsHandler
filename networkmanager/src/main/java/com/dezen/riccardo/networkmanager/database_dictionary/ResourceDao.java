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
public interface ResourceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void add(ResourceEntity... resourceEntities);
    @Update
    public void update(ResourceEntity... resourceEntities);
    @Delete
    public void remove(ResourceEntity... resourceEntities);

    @Query("SELECT * FROM resourceentity")
    public ResourceEntity[] getAll();

    @Query("SELECT 1 FROM resourceentity WHERE keyName=:key")
    public boolean contains(String key);
}
