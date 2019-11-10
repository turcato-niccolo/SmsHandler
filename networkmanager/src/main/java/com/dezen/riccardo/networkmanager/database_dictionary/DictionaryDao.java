package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
@Dao
public interface DictionaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void add(DictionaryEntity... entities);
    @Delete
    public void remove(DictionaryEntity... peer);

    @Query("SELECT peerAddress FROM DictionaryEntity WHERE peerAddress NOT IN (SELECT peerAddress FROM DictionaryEntity)")
    public int getAvailablePeers();
    @Query("SELECT resourceName FROM DictionaryEntity WHERE resourceName NOT IN (SELECT resourceName FROM DictionaryEntity)")
    public int getAvailableResource();

    @Query("SELECT peerAddress FROM DictionaryEntity WHERE resourceName=:resourceName")
    public int findPeerWithResource(String resourceName);
    @Query("SELECT resourceName FROM DictionaryEntity WHERE peerAddress=:peerAddress")
    public int findResourcesForPeer(String peerAddress);


}

