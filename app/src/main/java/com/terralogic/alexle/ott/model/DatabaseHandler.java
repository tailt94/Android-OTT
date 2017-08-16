package com.terralogic.alexle.ott.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex.le on 24-Jul-17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static DatabaseHandler instance;

    public static final String DATABASE_NAME = "VNC.db";
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

    private static final String DEVICE_TABLE = "device";
    private static final String DEVICE_COLUMN_TYPE = "type";
    private static final String DEVICE_COLUMN_TOPIC = "topic";
    private static final String DEVICE_COLUMN_TOKEN_USER = "tokenuser";
    private static final String DEVICE_COLUMN_TOKEN = "token";
    private static final String DEVICE_COLUMN_STATUS = "status";
    private static final String DEVICE_COLUMN_PORT = "port";
    private static final String DEVICE_COLUMN_NAME = "name";
    private static final String DEVICE_COLUMN_CHIP_ID = "chipID";

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
                + USER_COLUMN_TOKEN_USER + " TEXT PRIMARY KEY,"
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

        String CREATE_TABLE_DEVICE = "CREATE TABLE " + DEVICE_TABLE + "("
                + DEVICE_COLUMN_TYPE + " TEXT,"
                + DEVICE_COLUMN_TOPIC + " TEXT,"
                + DEVICE_COLUMN_TOKEN_USER + " TEXT,"
                + DEVICE_COLUMN_TOKEN + " TEXT,"
                + DEVICE_COLUMN_STATUS + " INTEGER,"
                + DEVICE_COLUMN_PORT + " TEXT,"
                + DEVICE_COLUMN_NAME + " TEXT,"
                + DEVICE_COLUMN_CHIP_ID + " TEXT,"
                + "FOREIGN KEY(" + DEVICE_COLUMN_TOKEN_USER + ") REFERENCES " + USER_TABLE + "(" + USER_COLUMN_TOKEN_USER + ")"
                + ")";

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_DEVICE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + DEVICE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        //Create table again
        onCreate(db);
    }

    public void clearDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + DEVICE_TABLE);
        db.execSQL("DELETE FROM " + USER_TABLE);
    }

    public long addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_COLUMN_TOKEN_USER, user.getTokenUser());
        values.put(USER_COLUMN_TOKEN, user.getToken());
        values.put(USER_COLUMN_EMAIL, user.getEmail());
        values.put(USER_COLUMN_PASSWORD, user.getPassword());
        values.put(USER_COLUMN_PHONE, user.getPhoneNumber());
        values.put(USER_COLUMN_FIRST_NAME, user.getName().getFirstName());
        values.put(USER_COLUMN_LAST_NAME, user.getName().getLastName());
        values.put(USER_COLUMN_SEX, user.getSex());
        values.put(USER_COLUMN_BIRTHDAY, user.getBirthday());
        values.put(USER_COLUMN_CITY, user.getCity());
        values.put(USER_COLUMN_COUNTRY, user.getCountry());
        if (user.getDevices().size() != 0) {
            for (Device device : user.getDevices()) {
                addDevice(device);
            }
        }

        return db.insert(USER_TABLE, null, values);
    }

    public User getUser() {
        User user = null;

        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + USER_TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            user = new User();
            user.setTokenUser(cursor.getString(cursor.getColumnIndex(USER_COLUMN_TOKEN_USER)));
            user.setToken(cursor.getString(cursor.getColumnIndex(USER_COLUMN_TOKEN)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(USER_COLUMN_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(USER_COLUMN_PASSWORD)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(USER_COLUMN_PHONE)));
            user.setName(new Name(cursor.getString(cursor.getColumnIndex(USER_COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(USER_COLUMN_LAST_NAME))));
            user.setSex(cursor.getString(cursor.getColumnIndex(USER_COLUMN_SEX)));
            user.setBirthday(cursor.getString(cursor.getColumnIndex(USER_COLUMN_BIRTHDAY)));
            user.setCity(cursor.getString(cursor.getColumnIndex(USER_COLUMN_CITY)));
            user.setCountry(cursor.getString(cursor.getColumnIndex(USER_COLUMN_COUNTRY)));
            user.setDevices(getListDevice(cursor.getString(cursor.getColumnIndex(USER_COLUMN_TOKEN_USER))));

            cursor.close();
        }
        return user;
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = USER_COLUMN_TOKEN_USER + " = ?";
        String[] args = {user.getTokenUser()};
        db.delete(USER_TABLE, whereClause, args);
    }

    public int updateUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_COLUMN_TOKEN_USER, user.getTokenUser());
        values.put(USER_COLUMN_TOKEN, user.getToken());
        values.put(USER_COLUMN_EMAIL, user.getEmail());
        values.put(USER_COLUMN_PASSWORD, user.getPassword());
        values.put(USER_COLUMN_PHONE, user.getPhoneNumber());
        values.put(USER_COLUMN_FIRST_NAME, user.getName().getFirstName());
        values.put(USER_COLUMN_LAST_NAME, user.getName().getLastName());
        values.put(USER_COLUMN_SEX, user.getSex());
        values.put(USER_COLUMN_BIRTHDAY, user.getBirthday());
        values.put(USER_COLUMN_CITY, user.getCity());
        values.put(USER_COLUMN_COUNTRY, user.getCountry());

        String whereClause = USER_COLUMN_TOKEN_USER + " = ?";
        String[] args = {user.getTokenUser()};
        return db.update(USER_TABLE, values, whereClause, args);
    }

    public List<Device> getListDevice(String tokenUser) {
        List<Device> devices = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DEVICE_TABLE
                + " WHERE " + DEVICE_COLUMN_TOKEN_USER + " = ?" ;
        String[] selectionArgs = {tokenUser};
        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                Device device = new Device();
                device.setType(cursor.getString(cursor.getColumnIndex(DEVICE_COLUMN_TYPE)));
                device.setTopic(cursor.getString(cursor.getColumnIndex(DEVICE_COLUMN_TOPIC)));
                device.setTokenUser(cursor.getString(cursor.getColumnIndex(DEVICE_COLUMN_TOKEN_USER)));
                device.setToken(cursor.getString(cursor.getColumnIndex(DEVICE_COLUMN_TOKEN)));
                device.setStatus(cursor.getInt(cursor.getColumnIndex(DEVICE_COLUMN_STATUS)));
                device.setPort(cursor.getString(cursor.getColumnIndex(DEVICE_COLUMN_PORT)));
                device.setName(cursor.getString(cursor.getColumnIndex(DEVICE_COLUMN_NAME)));
                device.setChipID(cursor.getString(cursor.getColumnIndex(DEVICE_COLUMN_CHIP_ID)));

                devices.add(device);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return devices;
    }

    public long addDevice(Device device) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DEVICE_COLUMN_TYPE, device.getType());
        values.put(DEVICE_COLUMN_TOPIC, device.getTopic());
        values.put(DEVICE_COLUMN_TOKEN_USER, device.getTokenUser());
        values.put(DEVICE_COLUMN_TOKEN, device.getToken());
        values.put(DEVICE_COLUMN_STATUS, device.getStatus());
        values.put(DEVICE_COLUMN_PORT, device.getPort());
        values.put(DEVICE_COLUMN_NAME, device.getName());
        values.put(DEVICE_COLUMN_CHIP_ID, device.getChipID());

        return db.insert(DEVICE_TABLE, null, values);
    }

    public int updateDevice(Device device) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DEVICE_COLUMN_TYPE, device.getType());
        values.put(DEVICE_COLUMN_TOPIC, device.getTopic());
        values.put(DEVICE_COLUMN_TOKEN_USER, device.getTokenUser());
        values.put(DEVICE_COLUMN_TOKEN, device.getToken());
        values.put(DEVICE_COLUMN_STATUS, device.getStatus());
        values.put(DEVICE_COLUMN_PORT, device.getPort());
        values.put(DEVICE_COLUMN_NAME, device.getName());
        values.put(DEVICE_COLUMN_CHIP_ID, device.getChipID());

        String whereClause = DEVICE_COLUMN_TOKEN + " = ?";
        String[] args = {device.getToken()};
        return db.update(DEVICE_TABLE, values, whereClause, args);
    }

    public int updateDevices(List<Device> devices) {
        int affectedRowCount = 0;
        for (Device device : devices) {
            affectedRowCount += updateDevice(device);
        }
        return affectedRowCount;
    }
}
