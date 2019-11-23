package com.dezen.riccardo.networkmanager.deprecated;

import androidx.room.Dao;
@Dao
public interface DictionaryDao {
    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    public void add(DictionaryEntity... entities);

    @Query("DELETE FROM DictionaryEntity WHERE peerAddress =:peerAddress")
    public void remove(String peerAddress);

    @Query("SELECT peerAddress  FROM DictionaryEntity WHERE peerAddress NOT IN (SELECT peerAddress FROM DictionaryEntity)")
    public PeerEntity[] getAvailablePeers();

    @Query("SELECT resourceName FROM DictionaryEntity WHERE resourceName NOT IN (SELECT resourceName FROM DictionaryEntity)")
    public ResourceEntity[] getAvailableResource();

    @Query("SELECT peerAddress FROM DictionaryEntity WHERE resourceName=:resourceName")
    public PeerEntity findPeerWithResource(String resourceName);

    @Query("SELECT resourceName FROM DictionaryEntity WHERE peerAddress=:peerAddress")
    public ResourceEntity[] findResourcesForPeer(String peerAddress);*/


}

