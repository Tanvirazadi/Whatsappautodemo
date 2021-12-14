package com.example.eminz.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import static java.lang.Boolean.TRUE;
import static java.sql.Types.VARCHAR;

import com.example.eminz.Model.Sms;

public class SmsDatabaseHelper extends SQLiteOpenHelper {

    // Initialise constants
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SMS";
    private static final String SMS_TABLE_NAME = "Messages";
    private static final String[] COLUMN_NAMES = {"SmsID", "Name", "messageDate", "messageTime", "Message", "messageStatus"};

    // Construct CREATE query string
    private static final String SMS_TABLE_CREATE =
            "CREATE TABLE " + SMS_TABLE_NAME + " (" +
                    COLUMN_NAMES[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAMES[1] + " TEXT, " +
                    COLUMN_NAMES[2] + " TEXT, " +
                    COLUMN_NAMES[3] + " TEXT, " +
                    COLUMN_NAMES[4] + " TEXT, " +
                    COLUMN_NAMES[5] + " TEXT, ";


    public SmsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creates the database if it doesn't exist and adds Messages table
        // Execute SQL query.
        db.execSQL(SMS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int addSms(Sms sms) {
        // Put Sms data into ContentValues object to insert into database
        ContentValues row = new ContentValues();
        row.put(COLUMN_NAMES[1], sms.name);
        row.put(COLUMN_NAMES[3], sms.messageDate);
        row.put(COLUMN_NAMES[4], sms.messageTime);
        row.put(COLUMN_NAMES[5], sms.message);
        row.put(COLUMN_NAMES[6], sms.messageStatus);


        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // insert sms details into a new row, return the id of the new row.
        long longid = db.insert(SMS_TABLE_NAME, null, row);
        db.close();

        // convert id from long to int and return
        int SmsID = (int) longid;
        return SmsID;
    }

    public ArrayList<Sms> getPendingSmsList() {
        // Get readable database
        Cursor result;
        try (SQLiteDatabase db = this.getReadableDatabase()) {

            // Query the database for all sms where messageStatus is pending
            result = db.rawQuery("SELECT * FROM Messages WHERE messageStatus ='Pending' ORDER BY SmsID DESC", null);
        }

        // Create list of sms objects
        ArrayList<Sms> sms = new ArrayList<Sms>();

        // For number of sms retrieved create a sms object with name, number, message, message date, message time.
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            sms.add(new Sms(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6)));
        }
        return sms;
    }

    public ArrayList<Sms> getSentSmsList() {
        // Get readable database
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database for all sms where messageStatus is Sent
        Cursor result = db.rawQuery("SELECT * FROM Messages WHERE messageStatus = 'Sent' ORDER BY SmsID DESC", null);

        // Create list of sms objects
        ArrayList<Sms> sms = new ArrayList<Sms>();

        // For number of sms retrieved create a sms object with name, number, message, message date, message time.
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            sms.add(new Sms(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6)));
        }
        return sms;
    }

    public ArrayList<Sms> getFailedSmsList() {
        // Get readable database
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database for all sms where messageStatus is Failed
        Cursor result = db.rawQuery("SELECT * FROM Messages WHERE messageStatus = 'Failed' ORDER BY SmsID DESC", null);

        // Create list of sms objects
        ArrayList<Sms> sms = new ArrayList<Sms>();

        // For number of sms retrieved create a sms object with name, number, message, message date, message time.
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            sms.add(new Sms(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6)));
        }
        return sms;
    }


    public ArrayList<Sms> getSmsByID(int searchID) {
        // Get the readable database.
        SQLiteDatabase db = this.getReadableDatabase();

        // Get sms by ID
        Cursor result = db.rawQuery("SELECT * FROM Messages WHERE SmsID =" + searchID, null);

        // Create list of sms objects
        ArrayList<Sms> sms = new ArrayList<Sms>();

        // For number of sms retrieved create a sms object with name, number, message, message date, message time.
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            sms.add(new Sms(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6)));
        }

        return sms;
    }

    public int retrieveSmsID(Sms sms) {
        // Get the readable database.
        SQLiteDatabase db = this.getReadableDatabase();

        // Create where clause from details of SMS object
        String whereClause = "Name = '" + sms.name + "' AND Number = '"  + "' AND messageDate= '" + sms.messageDate + "' AND messageTime= '" + sms.messageTime + "' AND message = '" + sms.message + "'";

        // Returns the number of affected rows. 0 means no rows were deleted.
        Cursor result = db.rawQuery("SELECT SmsID FROM Messages WHERE " + whereClause, null);
        int retrievedID = 0;

        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            retrievedID = result.getInt(0);
        }
        return retrievedID;
    }

    public void removeSms(Integer SmsID) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Create where clause
        String whereClause = "SmsID = '" + SmsID + "'";

        // Remove row from table with SmsID passed.
        db.delete(SMS_TABLE_NAME, whereClause, null);
    }

    public void updateSmsToSent(Integer SmsID) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Put Sms ID into ContentValues object
        ContentValues cv = new ContentValues();
        cv.put("messageStatus", "Sent");

        // Update messageStatus to Sent where SMSID is equal to SMS ID passed
        db.update(SMS_TABLE_NAME, cv, "SmsID=" + SmsID, null);
    }

    public void updateSmsToFailed(Integer SmsID) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Put Sms ID into ContentValues object update SMS
        ContentValues cv = new ContentValues();
        cv.put("messageStatus", "Failed");

        // Update messageStatus to Failed where SMSID is equal to SMS ID passed
        db.update(SMS_TABLE_NAME, cv, "SmsID=" + SmsID, null);
    }

}


