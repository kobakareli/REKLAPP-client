package com.example.koba.reklappclient;

import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Koba on 13/07/2016.
 */
public class ServerData {

    private final static String URI = "http://reklapp-server.herokuapp.com/webapi/";

    public static double getMoney(String number) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(URI + "users/" + number + "/money")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (response.getStatus() != 200) {
            return -1;
        }
        try {
            JSONObject json = response.readEntity(JSONObject.class);
            if (json.has("money")) {
                return json.getDouble("money");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String addUser(String name, String surname, String pin, String country, String city, String password, String streetAddress, String mobileNumber,
                               String sex, String birthDate, String relationship, String numberOfChildren, String income, String email, String oldNumber, String money) {
        Client client = ClientBuilder.newClient();
        Entity body = Entity.json("{ \"name\": \"" + name + "\", \"surname\": \"" + surname + "\", \"pin\": \"" + pin + "\", \"country\": \"" + country + "\", " +
                "\"city\": \"" + city + "\", \"password\": \"" + password + "\", \"street_address\": \"" + streetAddress + "\", \"mobile_number\": \"" + mobileNumber + "\", \"sex\": \"" + sex + "\"," +
                "\"birthdate\": \"" + birthDate + "\", \"relationship\": \"" + relationship + "\", \"number_of_children\": " + numberOfChildren + ", \"average_monthly_income\": " +
                 income + ", \"email\": \"" + email + "\", \"old_mobile_number\": \"" + oldNumber + "\", \"money\": " + money + "}");
        Response response = client.target(URI + "users")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(body);
        if (response.getStatus() != 200) {
            return "Error occurred. Try again";
        }
        try {
            JSONObject json = response.readEntity(JSONObject.class);
            if (json.has("problem")) {
                return json.getString("problem");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return "Error occurred. Try again";
    }

    public static String transferMoney(String mobileNumber, String address, double amount) {
        Client client = ClientBuilder.newClient();
        Entity body = Entity.json("{ \"amount\": " + amount + " }");
        Response response = client.target(URI + "users/" + mobileNumber + "/transfer/" + address)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(body);
        if (response.getStatus() != 200) {
            return "Error occurred. Try again";
        }
        try {
            JSONObject json = response.readEntity(JSONObject.class);
            if (json.has("problem")) {
                return json.getString("problem");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return "Error occurred. Try again";
    }

    public static Advertisement getRandomAdvertisement(String mobileNumber) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(URI + "users/" + mobileNumber + "/advertisments/random")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (response.getStatus() != 200) {
            return null;
        }
        try {
            JSONObject json = response.readEntity(JSONObject.class);
            return new Advertisement(json.getString("company"), json.getString("product"), json.getDouble("price"), json.getString("link"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getUserByLogin(String mobileNumber, String password) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(URI + "users/" + mobileNumber + "/" + password)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (response.getStatus() != 200) {
            return null;
        }
        try {
            JSONObject json = response.readEntity(JSONObject.class);
            if (json.getString("mobile_number").equals(""))
                return null;
            return new User(json.getString("name"), json.getString("surname"), json.getString("password"),
                    json.getString("pin"), json.getString("country"), json.getString("city"), json.getString("street_address"),
                    json.getString("mobile_number"), json.getString("sex"), json.getString("birthdate"), json.getString("relationship"),
                    json.getString("email"), json.getString("old_mobile_number"), json.getInt("number_of_children"),
                    json.getInt("average_monthly_income"), json.getDouble("money"));
        }

        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
