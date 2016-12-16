package com.binsearch.binsearch;

public class BinData {
    private String bin;
    private String description;
    private String key;

    public BinData() {}

    public String getBin() {
        return bin;
    }

    public String getDescription() {
        return description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String toSet) {
        key = toSet;
    }

    public void setBin(String toSet) {
        if(bin == null){
            bin = toSet;
        }
        else{
            bin = toSet;
        }
    }

    public void setDescription(String toSet) {
        if(description == null){
            description = new String(toSet);
        }
        else{
            description = toSet;
        }
    }
}
