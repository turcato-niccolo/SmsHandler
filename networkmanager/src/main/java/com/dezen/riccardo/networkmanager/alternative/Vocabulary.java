package com.dezen.riccardo.networkmanager.alternative;

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
     * @param key key for the user
     * @param value key for
     */
    public abstract void addUser(UK key, UV value);

    /**
     * Removes the user with the matching key, if present
     * @param key key of the user to remove
     * @return the removed User, null if not present
     */
    public abstract UV removeUser(UK key);

    /**
     * Updates the user with the matching key
     * @param key the key of the user to update
     * @return the old value for the User, null if not present
     */
    public abstract UV editUser(UK key, UV newValue);

    /**
     * Returns the User with the matching key, if present
     * @param key the key to look for
     * @return the value of the user, null if not present
     */
    public abstract UV getUser(UK key);

    /**
     * Adds a resource
     * @param key the key for the new resource
     * @param value the value for the new resource
     */
    public abstract void addResource(RK key, RV value);

    /**
     * Removes the resource with the matching key, if present
     * @param key the key of the resource to remove
     * @return the value of the removed resource, null if not present
     */
    public abstract RV removeResource(RK key);

    /**
     * Updates the value of a resource
     * @param key the resource to update
     * @param newValue the new value for the resource
     * @return the old value for the resource, null if not present
     */
    public abstract RV editResource(RK key, RV newValue);

    /**
     * Returns the value of a resource
     * @param key the key for the resource
     * @return the value of the resource, null if not present
     */
    public abstract RV getResource(RK key);

    /**
     * Assigns a listener that can answer to events related to various events on the Vocabulary
     * @param listener
     */
    public abstract void setListener(OnVocabularyEventListener listener);
}
