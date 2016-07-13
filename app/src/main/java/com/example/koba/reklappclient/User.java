package com.example.koba.reklappclient;

/**
 * Created by Koba on 13/07/2016.
 */
public class User {

    public String name, surname, pin, country, city, streetAddress, mobileNumber, sex, birthdate;
    public String relationship, email, oldMobileNumber, password;
    public int numberOfChildren;
    public double averageMonthlyIncome, money;

    public User(String name, String surname, String password, String pin, String country, String city, String streetAddress,
                String mobileNumber, String sex, String birthdate, String relationship, String email,
                String oldMobileNumber, int numberOfChildren, double averageMonthlyIncome, double money) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.pin = pin;
        this.country = country;
        this.city = city;
        this.streetAddress = streetAddress;
        this.mobileNumber = mobileNumber;
        this.sex = sex;
        this.birthdate = birthdate;
        this.relationship = relationship;
        this.email = email;
        this.oldMobileNumber = oldMobileNumber;
        this.numberOfChildren = numberOfChildren;
        this.averageMonthlyIncome = averageMonthlyIncome;
        this.money = money;
    }

}
