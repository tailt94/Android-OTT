package com.terralogic.alexle.ott.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by alex.le on 19-Jul-17.
 */

public class Device implements Serializable {
    private String type;
    private String tokenUser;
    private String port;
    private String chipID;

    public Device() {

    }

    public Device(JSONObject json) {
        type = json.optString("type");
        tokenUser = json.optString("tokenuser");
        port = json.optString("port");
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
}
