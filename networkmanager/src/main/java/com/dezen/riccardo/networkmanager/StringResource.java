package com.dezen.riccardo.networkmanager;

import androidx.annotation.NonNull;

public class StringResource implements Resource<String> {
    private String name;
    private String value;
    public StringResource(String name, String value){
        this.name = name;
        this.value = value;
    }
    @Override
    public String getName() {
        return name;
    }
    public String getValue(){
        return value;
    }

    @NonNull
    @Override
    public String toString() {
        return name+"\n"+value;
    }
}
