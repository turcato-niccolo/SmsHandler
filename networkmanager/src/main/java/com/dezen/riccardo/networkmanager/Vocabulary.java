package com.dezen.riccardo.networkmanager;

import java.util.Map;

/**
 * @param <UK> User key type
 * @param <UV> User value type
 * @param <RK> Resource key type
 * @param <RV> Resource value type
 */
public abstract class Vocabulary<UK, UV extends User, RK, RV extends Resource>{
    protected Map<UK, UV> userMap;
    protected Map<RK, RV> resourceMap;
    /**
     * Adds a user
     * @param key
     */
    public abstract void addUser(UK key, UV value);

    /**
     * Removes the user with the corresponding key
     * @param key
     * @return the removed User
     */
    public abstract UV removeUser(UK key);

    /**
     * Edits the user with the matching key
     * @param key
     * @return the old value for the User
     */
    public abstract UV editUser(UK key);

    /**
     *
     * @param key
     * @return
     */
    public abstract UV getUser(UK key);

    /**
     *
     * @param key
     * @param value
     */
    public abstract void addResource(RK key, RV value);

    /**
     *
     * @param key
     * @return
     */
    public abstract RV removeResource(RK key);

    /**
     *
     * @param key
     * @return
     */
    public abstract RV editResource(RK key);

    /**
     *
     * @param key
     * @return
     */
    public abstract RV getResource(RK key);

    /**
     *
     * @param listener
     */
    public abstract void setListener(OnVocabularyEventListener listener);
}
