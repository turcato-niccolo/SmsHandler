package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

/**
 * Base class for a Dao accessing a table in a Room database.
 * Any class extending this should be abstract and Override {@link #getPeerTableName()}, {@Link #getResourceTableName}
 * Overriding anything else is unnecessary for full functionality.
 * @author Giorgia Bortoletti
 * @param <P> The Type of Peer Entity the Dao provides access to.
 * @param <R> The Type of Resource Entity the Dao provides access to.
 */
@Dao
public abstract class BaseDao<P, R>{

    //Table name has to follow
    protected static final String COUNT_QUERY = "SELECT COUNT(*) FROM ";

    //Table name has to follow
    protected static final String GET_ALL_QUERY = "SELECT * FROM ";

    /**
     * Add a new resource
     * @param resourceEntities to add
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void addResource(R... resourceEntities);

    /**
     * Update a resource
     * @param resourceEntities to update
     */
    @Update
    public abstract void updateResource(R... resourceEntities);

    /**
     * Delete a resource
     * @param resourceEntities to remove
     */
    @Delete
    public abstract void removeResource(R... resourceEntities);

    /**
     * Add a new peer
     * @param peerEntities to add
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void addPeer(P... peerEntities);

    /**
     * Update a peer
     * @param peerEntities to update
     */
    @Update
    public abstract void updatePeer(P... peerEntities);

    /**
     * Delete a peer
     * @param peerEntities to remove
     */
    @Delete
    public abstract void removePeer(P... peerEntities);

    /**
     * @return the number of peers rows in the table
     */
    public int countPeers(){
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                COUNT_QUERY + getPeerTableName()
        );
        return performCount(query);
    }

    /**
     * @return the number of resources rows in the table
     */
    public int countResources(){
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                COUNT_QUERY + getResourceTableName()
        );
        return performCount(query);
    }

    /**
     * @param address of peer to find
     * @return true if database contains the peer
     */
    public boolean containsPeer(String address){
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                COUNT_QUERY + getPeerTableName() +" WHERE address=:"+address
        );
        return performCount(query)!=0;
    }

    /**
     * @param key of resource to find
     * @return true if database contains the resource
     */
    public boolean containsResource(String key){
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                COUNT_QUERY + getResourceTableName() +" WHERE keyName=:"+key
        );
        return performCount(query)!=0;
    }


    /**
     * @param key of resource
     * @return resource with that key
     */
    public R getResource(String key){
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                COUNT_QUERY + getResourceTableName() +" WHERE keyName=:"+key
        );
        return performGetResource(query);
    }

    /**
     * Method to perform the query correctly through Room
     * @param query the query to be performed
     * @return an int value returned by the query. In this case the number of rows in the table.
     */
    @RawQuery
    protected abstract int performCount(SupportSQLiteQuery query);

    /**
     * Method to perform the query correctly through Room
     * @param query the query to be performed
     * @return an int value returned by the query. In this case the resource found.
     */
    @RawQuery
    protected abstract R performGetResource(SupportSQLiteQuery query);

    /**
     * @return all the peers rows in the table
     */
    public List<P> getAllPeers(){
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                GET_ALL_QUERY + getPeerTableName()
        );
        return performGetAllPeers(query);
    }

    /**
     * @return all the resources rows in the table
     */
    public List<R> getAllResources(){
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                GET_ALL_QUERY + getResourceTableName()
        );
        return performGetAllResources(query);
    }

    /**
     * Method to perform the query correctly through Room
     * @param query the query to be performed
     * @return a List returned by the query. In this case all the peers rows in the table.
     */
    @RawQuery
    protected abstract List<P> performGetAllPeers(SupportSQLiteQuery query);

    /**
     * Method to perform the query correctly through Room
     * @param query the query to be performed
     * @return a List returned by the query. In this case all the resources rows in the table.
     */
    @RawQuery
    protected abstract List<R> performGetAllResources(SupportSQLiteQuery query);

    /**
     * Method to be overridden in order to find the name of the peer table.
     * @return a String containing the name for the table this Dao provide access to.
     */
    public abstract String getPeerTableName();

    /**
     * Method to be overridden in order to find the name of the resource table.
     * @return a String containing the name for the table this Dao provide access to.
     */
    public abstract String getResourceTableName();
}