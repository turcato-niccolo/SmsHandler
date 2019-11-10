package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ResourceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void add(ResourceEntity... entities);
    @Update
    public void update(ResourceEntity... entities);
    @Delete
    public void remove(ResourceEntity... peer);
}
