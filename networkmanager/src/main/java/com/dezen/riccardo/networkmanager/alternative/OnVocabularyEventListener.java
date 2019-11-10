package com.dezen.riccardo.networkmanager.alternative;

/**
 * Interface for a listener that allows external classes to react to events regarding the vocabulary
 */
public interface OnVocabularyEventListener{
    void onUserAdded(User newUser);
    void onUserRemoved(User remUser);
    void onUserModified(User oldUser, User modUser);
    void onResourceAdded(Resource newResource);
    void onResourceRemoved(Resource remResource);
    void onResourceModified(Resource oldResource, Resource modResource);
}
