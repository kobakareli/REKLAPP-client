package com.example.koba.reklappclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Koba on 13/07/2016.
 */
public class User implements Parcelable {

    public String name, surname, pin, country, city, street_address, mobile_number, sex, birthdate;
    public String relationship, email, old_mobile_number, password;
    public int number_of_children;
    public int average_monthly_income;
    public double money;

    public User(String name, String surname, String password, String pin, String country, String city, String streetAddress,
                String mobileNumber, String sex, String birthdate, String relationship, String email,
                String oldMobileNumber, int numberOfChildren, int averageMonthlyIncome, double money) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.pin = pin;
        this.country = country;
        this.city = city;
        this.street_address = streetAddress;
        this.mobile_number = mobileNumber;
        this.sex = sex;
        this.birthdate = birthdate;
        this.relationship = relationship;
        this.email = email;
        this.old_mobile_number = oldMobileNumber;
        this.number_of_children = numberOfChildren;
        this.average_monthly_income = averageMonthlyIncome;
        this.money = money;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] { name, surname, password, pin, country, city, street_address, mobile_number,
        sex, birthdate, relationship, email, old_mobile_number});
        dest.writeInt(number_of_children);
        dest.writeInt(average_monthly_income);
        dest.writeDouble(money);
    }

    public User(Parcel in){
        String[] data = new String[13];

        in.readStringArray(data);

        this.name = data[0];
        this.surname = data[1];
        this.password = data[2];
        this.pin = data[3];
        this.country = data[4];
        this.city = data[5];
        this.street_address = data[6];
        this.mobile_number = data[7];
        this.sex = data[8];
        this.birthdate = data[9];
        this.relationship = data[10];
        this.email = data[11];
        this.old_mobile_number = data[12];
        this.number_of_children = in.readInt();
        this.average_monthly_income = in.readInt();
        this.money = in.readDouble();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
