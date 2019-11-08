package com.dezen.riccardo.networkmanager;

import java.util.List;
/**
 * Class to manage network access
 */
public abstract class NetworkManager<I extends Invitation, V extends Vocabulary>{
    protected V vocabulary;
    public abstract void join(I invitation, User newUser);
    public abstract void leave();
    public abstract boolean isReady();
    public abstract List<User> getUsers();
    public abstract List<Resource> getResources();
}
