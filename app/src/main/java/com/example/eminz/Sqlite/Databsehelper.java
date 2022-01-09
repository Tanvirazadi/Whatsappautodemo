package com.example.eminz.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Databsehelper extends SQLiteOpenHelper {
    private static final String DATABASE = "Profile.db";
    private static final String TABLE_NAME = "profile";
    private static final String NAME = "Name";
    private static final String EMAIL = "Email";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String CONFIRM = "Confirm";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            "(" + NAME + " VARCHAR (25)," + USERNAME + " VARCHAR (55)," + PASSWORD +
            " VARCHAR (23)," + CONFIRM + " VARCHAR (22)," +
            "" + EMAIL + " VARCHAR(24));";

    private final Context context;

    public Databsehelper(@Nullable Context context) {
        super(context, DATABASE, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Toast.makeText(context, "Create is called", Toast.LENGTH_LONG).show();
            db.execSQL(CREATE_TABLE);
        } catch (Exception e) {

            Toast.makeText(context, "Exception" + e, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Toast.makeText(context, "Upgrade is called", Toast.LENGTH_LONG).show();
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);


        } catch (Exception e) {

            Toast.makeText(context, "Exception" + e, Toast.LENGTH_LONG).show();
        }


    }

    public long insert(User user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, user.getName());
        contentValues.put(EMAIL, user.getEmail());
        contentValues.put(USERNAME, user.getUsername());
        contentValues.put(PASSWORD, user.getPassword());
        contentValues.put(CONFIRM, user.getConfirmpass());
        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public Boolean findpassword(String uname, String pass) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        boolean result = false;


        if (cursor.getCount() == 0) {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();

        } else {

            while (cursor.moveToNext()) {
                String username = cursor.getString(1);
                String password = cursor.getString(2);

                if (username.equals(uname) && password.equals(pass)) {
                    result = true;
                    break;
                }


            }
        }

        return result;
    }
}
