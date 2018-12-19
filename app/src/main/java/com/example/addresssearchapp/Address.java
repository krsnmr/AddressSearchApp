package com.example.addresssearchapp;

public class Address {

    private int id;
    private String name;

    public Address(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getAddrName() {
        return name;
    }

    public int getId() { return id;  }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
