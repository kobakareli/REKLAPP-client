package com.example.koba.reklappclient;

/**
 * Created by Koba on 13/07/2016.
 */
public class Advertisement {

    private String company;
    private String product;
    private String description;
    private double view_gain;
    private String link;
    private int ad_id;
    private int pair_id;
    private String status;

    public Advertisement(String company, String product, double price, String url, int adId, int pairId, String description, String status) {
        this.company = company;
        this.product = product;
        this.view_gain = price;
        this.link = url;
        this.ad_id = adId;
        this.pair_id = pairId;
        this.description = description;
        this.status = status;
    }

    public String getProduct() {
        return product;
    }

    public String getCompany() {
        return company;
    }

    public double price() {
        return view_gain;
    }

    public String getURL() {
        return link;
    }

    public int getAdId() {
        return ad_id;
    }

    public int getPairId() {
        return pair_id;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
}
