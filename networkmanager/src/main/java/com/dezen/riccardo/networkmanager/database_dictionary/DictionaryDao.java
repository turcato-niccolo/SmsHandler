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
public interface DictionaryDao {
    public static final String databaseResource="resourceentity";
    public static final String databasePeer="peerentity";

    /**
     * Add a new resource
     * @param resourceEntities to add
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addResource(ResourceEntity... resourceEntities);

    /**
     * Update a resource
     * @param resourceEntities to update
     */
    @Update
    public void updateResource(ResourceEntity... resourceEntities);

    /**
     * Delete a resource
     * @param resourceEntities to remove
     */
    @Delete
    public void removeResource(ResourceEntity... resourceEntities);

    /**
     * Find and return only one resource with this key
     * @param key of resource
     * @return ResourceEntity
     */
    @Query("SELECT * FROM "+databaseResource+" WHERE keyName=:key")
    public ResourceEntity getResource(String key);

    /**
     * Get all resources
     * @return array of ResourceEntity
     */
    @Query("SELECT * FROM "+databaseResource)
    public ResourceEntity[] getAllResources();

    /**
     * Verify if a Resource is in the database
     * @param key of Resource to find
     * @return true if database contains the resource
     */
    @Query("SELECT COUNT(*) FROM "+databaseResource+" WHERE keyName=:key")
    public boolean containsResource(String key);

    /**
     * Add a new peer
     * @param peerEntities to add
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addPeer(PeerEntity... peerEntities);

    /**
     * Update a peer
     * @param peerEntities to update
     */
    @Update
    public void updatePeer(PeerEntity... peerEntities);

    /**
     * Delete a peer
     * @param peerEntities to remove
     */
    @Delete
    public void removePeer(PeerEntity... peerEntities);

    /**
     * Get all peers
     * @return array of PeerEntity
     */
    @Query("SELECT * FROM "+databasePeer)
    public PeerEntity[] getAllPeers();

    /**
     * Verify if a Resource is in the database
     * @param address of Peer to find
     * @return true if database contains the peer
     */
    @Query("SELECT COUNT(*) FROM " +databasePeer+" WHERE address=:address")
    public boolean containsPeer(String address);

}
