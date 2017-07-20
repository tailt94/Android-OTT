package com.terralogic.alexle.ott.model;

/**
 * Created by alex.le on 19-Jul-17.
 */

public class Device {
    private String type;
    private String tokenUser;
    private String port;
    private String chipID;

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
