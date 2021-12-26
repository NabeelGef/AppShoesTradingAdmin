package com.example.controlpanalamal;

public class Profile {
    String Uri , name , numberTel;
    Profile(){}
    public Profile(String uri, String name, String numberTel) {
        this.Uri = uri;
        this.name = name;
        this.numberTel = numberTel;
    }
    public String getUri() {
        return Uri;
    }

    public String getName() {
        return name;
    }

    public String getNumberTel() {
        return numberTel;
    }
}
