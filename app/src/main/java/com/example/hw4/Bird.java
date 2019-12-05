package com.example.hw4;

public class Bird {

    public String birdname;
    public int zipcode;
    public String personname;
    public int points;

    public Bird() {
    }

    public Bird(String birdname, int zipcode, String personname) {
        this.birdname = birdname;
        this.zipcode = zipcode;
        this.personname = personname;
        points = 0;
    }

    public void addImportance() {
        points = points + 1;
    }
}
