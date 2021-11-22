// User.java
// This class stores common user data for the GradeNet app
// By Nicholas Verrochi
// Last Modified: 5/9/18

package com.bhcc.nick.gradenet;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.UUID;

import com.bhcc.nick.gradenet.database.Schema.UserTable;
import com.bhcc.nick.gradenet.database.UserWrapper;

public class User implements Serializable {

    private UUID mUUID;
    private String mFirstName;
    private String mLastName;
    private String mUsername;
    private String mPassword;
    private int mUserLevel;

    // User levels
    public static final int LEVEL_STUDENT = 0;
    public static final int LEVEL_TEACHER = 1;
    public static final int LEVEL_ADMIN = 2;

    public User() {
        mUUID = UUID.randomUUID();
    }

    public static User generateAdmin() {
        User user = new User();
        user.setFirstName("Nicholas");
        user.setLastName("Verrochi");
        user.setUsername("admin");
        user.setPassword("admin");
        user.setUserLevel(LEVEL_ADMIN);
        return user;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(UserTable.Cols.UUID, mUUID.toString());
        values.put(UserTable.Cols.FIRST_NAME, mFirstName);
        values.put(UserTable.Cols.LAST_NAME, mLastName);
        values.put(UserTable.Cols.USERNAME, mUsername);
        values.put(UserTable.Cols.PASSWORD, mPassword);
        values.put(UserTable.Cols.USER_LEVEL, String.valueOf(mUserLevel));
        return values;
    }

    public static UserWrapper query(SQLiteDatabase db, String username, String password) {
        Cursor cursor = db.query(
                UserTable.NAME,
                null,
                UserTable.Cols.USERNAME + " = ? and " + UserTable.Cols.PASSWORD + " = ?",
                new String[] {username, password},
                null, null, null
        );

        return new UserWrapper(cursor);
    }

    public static UserWrapper query(SQLiteDatabase db, UUID uuid) {
        Cursor cursor = db.query(
                UserTable.NAME, null,
                UserTable.Cols.UUID + " = ?",
                new String[] {uuid.toString()},
                null, null, null
        );

        return new UserWrapper(cursor);
    }

    public static UserWrapper queryAdmins(SQLiteDatabase db) {
        Cursor cursor = db.query(
                UserTable.NAME, null,
                UserTable.Cols.USER_LEVEL + " = ?",
                new String [] { String.valueOf(LEVEL_ADMIN) },
                null, null, null
        );

        return new UserWrapper(cursor);
    }

    // Getters and setters

    public void setUUID(UUID uuid) {
        mUUID = uuid;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setUserLevel(int level) {
        mUserLevel = level;
    }

    public int getUserLevel() {
        return mUserLevel;
    }

}
