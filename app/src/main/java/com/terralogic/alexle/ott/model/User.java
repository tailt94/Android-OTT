package com.terralogic.alexle.ott.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex.le on 18-Jul-17.
 */

public class User implements Serializable {
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

    public User() {

    }

    public User(JSONObject json) {
        tokenUser = json.optString("tokenuser");
        sex = json.optString("sex");
        phoneNumber = json.optString("phonenumber");
        email = json.optString("email");
        country = json.optString("country");
        city = json.optString("city");
        birthday = json.optString("birthday");
        token = json.optString("token");
        password = json.optString("password");
        name = new Name(json.optJSONObject("name"));

        JSONArray deviceArray = json.optJSONArray("devices");
        if (deviceArray != null) {
            for (int i = 0; i < deviceArray.length(); i++) {
                devices.add(new Device(deviceArray.optJSONObject(i)));
            }
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
