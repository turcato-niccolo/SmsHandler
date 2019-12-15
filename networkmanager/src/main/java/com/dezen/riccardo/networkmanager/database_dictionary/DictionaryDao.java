package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.room.Dao;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dezen.riccardo.networkmanager.NetworkDictionary;

/**
 * @author Giorgia Bortoletti
 * Class extending the BaseDao class for PeerEntity, ResourceEntity
 */
@Dao
public abstract class DictionaryDao extends BaseDao<PeerEntity, ResourceEntity>{
    /**
     * @return the name of the table containing the peer entities.
     */
    @Override
    public String getPeerTableName(){
        return NetworkDictionary.NETWORK_DICTIONARY_PEER_TABLE_NAME;
    }

    /**
     * @return the name of the table containing the resource entities.
     */
    @Override
    public String getResourceTableName(){
        return NetworkDictionary.NETWORK_DICTIONARY_RESOURCE_TABLE_NAME;
    }

    /**
     * @param address of peer
     * @return peer with that address
     */
    public PeerEntity getPeer(String address){
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                GET_ALL_QUERY + getResourceTableName() +" WHERE "+NetworkDictionary.PEER_TABLE_ADDRESS_COLUMN_NAME+"='"+address+"'"
        );
        return performGetPeer(query);
    }

    /**
     * @param key of resource
     * @return resource with that key
     */
    public ResourceEntity getResource(String key){
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                GET_ALL_QUERY + getResourceTableName() +" WHERE "+ NetworkDictionary.RESOURCE_TABLE_KEY_COLUMN_NAME+" ='"+key+"'"
        );
        return performGetResource(query);
    }
}

