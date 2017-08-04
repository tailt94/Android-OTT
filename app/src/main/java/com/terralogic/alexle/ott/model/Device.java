package com.terralogic.alexle.ott.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by alex.le on 19-Jul-17.
 */

public class Device implements Serializable {
    private String type;
    private String topic;
    private String tokenUser;
    private String token;
    private int status;
    private String port;
    private String name;
    private String chipID;

    public Device() {

    }

    public Device(JSONObject json) {
        type = json.optString("type");
        topic = json.optString("topic");
        tokenUser = json.optString("tokenuser");
        token = json.optString("token");
        status = json.optInt("status");
        port = json.optString("port");
        name = json.optString("name");
        chipID = json.optString("chipID");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTokenUser() {
        return tokenUser;
    }

    public void setTokenUser(String tokenUser) {
        this.tokenUser = tokenUser;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getChipID() {
        return chipID;
    }

    public void setChipID(String chipID) {
        this.chipID = chipID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
