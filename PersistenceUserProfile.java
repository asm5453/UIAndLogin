package com.example.adammoyer.androiduiandlogin_adammoyer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class PersistenceUserProfile implements IPersistence {

    public DatabaseAccess databaseAccess;

    public PersistenceUserProfile(Context context){
        this.databaseAccess = new DatabaseAccess(context);
    }

    @Override
    public void insert(Object o) {
        // Cast the generic object to have access to the movie info.
        UserProfile userProfile = (UserProfile) o;

        SQLiteDatabase sqLiteDatabase = databaseAccess.getWritableDatabase();

        // The ContentValues object create a map of values, where the columns are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserProfileTable.COLUMN_FIRST_NAME, userProfile.getFirstName());
        contentValues.put(UserProfileTable.COLUMN_LAST_NAME, userProfile.getLastName());
        contentValues.put(UserProfileTable.COLUMN_USERNAME, userProfile.getUsername());
        contentValues.put(UserProfileTable.COLUMN_BIRTHDAY, String.valueOf(userProfile.getBirthday()));
        contentValues.put(UserProfileTable.COLUMN_PHONE_NUMBER, userProfile.getPhoneNumber());
        contentValues.put(UserProfileTable.COLUMN_EMAIL, userProfile.getEmail());
        contentValues.put(UserProfileTable.COLUMN_PASSWORD, userProfile.getPassword());

        // Insert the ContentValues into the Movie table.
        sqLiteDatabase.insert(UserProfileTable.TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();
    }

    @Override
    public void delete(Object o) {

    }

    @Override
    public void edit(Object o) {

    }

    @Override
    public ArrayList getDataFromDB() {
        // Create ArrayList of movies
        ArrayList<UserProfile> userProfiles = null;

        // Instatiate the database.
        SQLiteDatabase sqLiteDatabase = databaseAccess.getWritableDatabase();

        // Gather all the records found for the MOVIE table.
        Cursor cursor = sqLiteDatabase.rawQuery(UserProfileTable.select(), null);

        // It will iterate since the first record gathered from the database.
        cursor.moveToFirst();

        // Check if there exist other records in the cursor
        userProfiles = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()){

            do {
                String firstName = cursor.getString(cursor.getColumnIndex(UserProfileTable.COLUMN_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndex(UserProfileTable.COLUMN_LAST_NAME));
                String username = cursor.getString(cursor.getColumnIndex(UserProfileTable.COLUMN_USERNAME));
                String birthday = cursor.getString(cursor.getColumnIndex(UserProfileTable.COLUMN_BIRTHDAY));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(UserProfileTable.COLUMN_PHONE_NUMBER));
                String email = cursor.getString(cursor.getColumnIndex(UserProfileTable.COLUMN_EMAIL));
                String password = cursor.getString(cursor.getColumnIndex(UserProfileTable.COLUMN_PASSWORD));

                UserProfile userProfile = new UserProfile(firstName, lastName, username, birthday, phoneNumber, email, password);
                userProfiles.add(userProfile);

            } while (cursor.moveToNext()) ;
        }

        return userProfiles;

    }
}
