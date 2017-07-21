package com.terralogic.alexle.ott.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alex.le on 19-Jul-17.
 */

public class Name {
    private String firstName;
    private String lastName;

    public Name(JSONObject json) {
        firstName = json.optString("firstname");
        lastName = json.optString("lastname");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}