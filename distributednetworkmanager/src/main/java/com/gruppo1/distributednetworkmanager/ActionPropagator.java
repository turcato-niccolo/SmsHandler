package com.gruppo1.distributednetworkmanager;

import java.util.List;

/**
 * Interface defining the standard behaviour for a class able to propagate actions through a Network
 * @author Riccardo De Zen
 */
public interface ActionPropagator {
    /**
     * Method to propagate an Action that has been appropriately built by someone
     * @param action
     */
    void propagateAction(KadAction action);
    //TODO
    void propagateActions(List<KadAction> actions);
}
