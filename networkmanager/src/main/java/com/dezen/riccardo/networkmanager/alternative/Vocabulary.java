package com.dezen.riccardo.networkmanager.alternative;

import com.dezen.riccardo.smshandler.Peer;

/**
 * @param <PK> User key type
 * @param <PV> User value type
 * @param <RK> Resource key type
 * @param <RV> Resource value type
 */
public interface Vocabulary<PK, PV extends Peer, RK, RV extends ResourceAlternative>{
    /**
     * Adds a user
     * @param key key for the user
     * @param value key for
     */
    void addUser(PK key, PV value);

    /**
     * Removes the user with the matching key, if present
     * @param key key of the user to remove
     * @return the removed User, null if not present
     */
    PV removeUser(PK key);

    /**
     * Updates the user with the matching key
     * @param key the key of the user to update
     * @return the old value for the User, null if not present
     */
    PV editUser(PK key, PV newValue);

    /**
     * Returns the User with the matching key, if present
     * @param key the key to look for
     * @return the value of the user, null if not present
     */
    PV getUser(PK key);

    /**
     * Adds a resource
     * @param key the key for the new resource
     * @param value the value for the new resource
     */
    void addResource(RK key, RV value);

    /**
     * Removes the resource with the matching key, if present
     * @param key the key of the resource to remove
     * @return the value of the removed resource, null if not present
     */
    RV removeResource(RK key);

    /**
     * Updates the value of a resource
     * @param key the resource to update
     * @param newValue the new value for the resource
     * @return the old value for the resource, null if not present
     */
    RV editResource(RK key, RV newValue);

    /**
     * Returns the value of a resource
     * @param key the key for the resource
     * @return the value of the resource, null if not present
     */
    RV getResource(RK key);
}
