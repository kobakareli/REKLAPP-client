package com.example.koba.reklappclient;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Koba on 21/07/2016.
 */
public interface RetroFitServer {

    public final static String URI = "http://reklapp-server.herokuapp.com";

    @GET("/webapi/users/{mobile_number}/money")
    double getMoney(@Path("mobile_number") String number, Callback<Response> response);

    @Headers("Content-Type: application/json")
    @PUT("/webapi/users")
    void addUser(@Body User user, Callback<Response> response);

    @POST("/webapi/users/{mobile_number}/transfer/{address}")
    void transferMoney(@Path("mobile_number") String number, @Path("address") String address, Callback<Response> response);

    @GET("/webapi/users/{mobile_number}/advertisments/random")
    void getRandomAdvertisement(@Path("mobile_number") String number, Callback<Advertisement> response);

    @GET("/webapi/users/{mobile_number}/{password}")
    void getUserByLogin(@Path("mobile_number") String number, @Path("password") String password, Callback<User> response);

    @POST("/webapi/ads/{ad_id}/view")
    void increaseViewsLeft(@Path("ad_id") String adId);

    @POST("/webapi/pairs/{pair_id}/date")
    void updatePairDate(@Path("pair_id") String pairId);

}
