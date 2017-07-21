package com.terralogic.alexle.ott.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex.le on 18-Jul-17.
 */

public class User {
    private String tokenUser;
    private String token;
    private String email;
    private String password;
    private String phoneNumber;
    private Name name;
    private String sex;
    private String birthday;
    private String city;
    private String country;
    private List<Device> devices = new ArrayList<>();

    public User(JSONObject json) {
        try {
            tokenUser = json.getString("tokenuser");
            sex = json.getString("sex");
            phoneNumber = json.getString("phonenumber");
            email = json.getString("email");
            country = json.getString("country");
            city = json.getString("city");
            birthday = json.getString("birthday");
            token = json.getString("token");
            name = new Name(json.getJSONObject("name"));

            JSONArray deviceArray = json.getJSONArray("devices");
            for (int i = 0; i < deviceArray.length(); i++) {
                devices.add(new Device(deviceArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Log.e(this.getClass().getSimpleName(), "JSON mapping error!");
        }
    }

    public String getTokenUser() {
        return tokenUser;
    }

    public void setTokenUser(String tokenUser) {
        this.tokenUser = tokenUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
