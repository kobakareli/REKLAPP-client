package com.example.koba.reklappclient;

/**
 * Created by Koba on 13/07/2016.
 */
public class Advertisement {

    private String company;
    private String product;
    private double price;
    private String url;

    public Advertisement(String company, String product, double price, String url) {
        this.company = company;
        this.product = product;
        this.price = price;
        this.url = url;
    }

    public String getProduct() {
        return product;
    }

    public String getCompany() {
        return company;
    }

    public double price() {
        return price;
    }

    public String getURL() {
        return url;
    }

}
