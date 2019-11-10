package com.dezen.riccardo.networkmanager.alternative;

public class StringResourceAlternative implements ResourceAlternative<String, String> {

    private String name;
    private String value;

    public StringResourceAlternative(String name, String value){
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
}
