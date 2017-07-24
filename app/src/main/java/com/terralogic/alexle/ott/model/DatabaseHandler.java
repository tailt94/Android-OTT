package com.terralogic.alexle.ott.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alex.le on 24-Jul-17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static DatabaseHandler instance;
    private static final String DATABASE_NAME = "VNC.db";
    private static final int DATABASE_VERSION = 1;

    private static final String USER_TABLE = "user";
    private static final String USER_COLUMN_TOKEN_USER = "tokenuser";
    private static final String USER_COLUMN_TOKEN = "token";
    private static final String USER_COLUMN_EMAIL = "email";
    private static final String USER_COLUMN_PASSWORD = "password";
    private static final String USER_COLUMN_PHONE = "phonenumber";
    private static final String USER_COLUMN_FIRST_NAME = "firstname";
    private static final String USER_COLUMN_LAST_NAME = "lastname";
    private static final String USER_COLUMN_SEX = "sex";
    private static final String USER_COLUMN_BIRTHDAY = "birthday";
    private static final String USER_COLUMN_CITY = "city";
    private static final String USER_COLUMN_COUNTRY = "country";

    public static DatabaseHandler getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHandler(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_USER = "CREATE TABLE " + USER_TABLE + "("
                + USER_COLUMN_TOKEN_USER + " TEXT,"
                + USER_COLUMN_TOKEN + " TEXT,"
                + USER_COLUMN_EMAIL + " TEXT,"
                + USER_COLUMN_PASSWORD + " TEXT,"
                + USER_COLUMN_PHONE + " TEXT,"
                + USER_COLUMN_FIRST_NAME + " TEXT,"
                + USER_COLUMN_LAST_NAME + " TEXT,"
                + USER_COLUMN_SEX + " TEXT,"
                + USER_COLUMN_BIRTHDAY + " TEXT,"
                + USER_COLUMN_CITY + " TEXT,"
                + USER_COLUMN_COUNTRY + " TEXT" + ")";

        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        //Create table again
        onCreate(db);
    }

    public void addUser(User user) {

    }
}
