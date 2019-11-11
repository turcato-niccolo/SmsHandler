package com.dezen.riccardo.networkmanager;

public class StringResource implements Resource<String, String> {

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

    @Override
    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isValid(){
        return this.name != null && !this.name.isEmpty();
    }
}
