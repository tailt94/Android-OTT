package com.terralogic.alexle.ott.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alex.le on 19-Jul-17.
 */

public class Device {
    private String type;
    private String tokenUser;
    private String port;
    private String chipID;

    public Device(JSONObject json) {
        try {
            type = json.getString("type");
            tokenUser = json.getString("tokenuser");
            port = json.getString("port");
            chipID = json.getString("chipID");
        } catch (JSONException e) {
            Log.e(this.getClass().getSimpleName(), "JSON mapping error!");
        }
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
