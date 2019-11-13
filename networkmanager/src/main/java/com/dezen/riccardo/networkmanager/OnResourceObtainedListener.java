package com.dezen.riccardo.networkmanager;

public interface OnResourceObtainedListener<R extends Resource>{
    /**
     * Method to be called when a requested Resource has ben fully delivered and made available.
     * @param obtRes the obtained Resource
     */
    void onResourceObtained(R obtRes);
}
