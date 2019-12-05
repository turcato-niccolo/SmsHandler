package com.dezen.riccardo.networkmanager.database_dictionary;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
}

