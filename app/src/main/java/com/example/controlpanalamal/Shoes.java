package com.example.controlpanalamal;

public class Shoes {
    String type , url , key;
    int price ;
    public Shoes(String type, String url, int price,String Key) {
        this.type = type;
        this.url = url;
        this.price = price;
        this.key = Key;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public int getPrice() {
        return price;
    }

    public String getKey() {
        return key;
    }
}
