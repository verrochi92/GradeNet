// Teacher.java
// Class to represent a teacher
// By Nicholas Verrochi
// Last Modified: 5/7/18

package com.bhcc.nick.gradenet;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bhcc.nick.gradenet.database.Schema.TeacherTable;
import com.bhcc.nick.gradenet.database.TeacherWrapper;
import com.bhcc.nick.gradenet.database.UserWrapper;

import java.io.Serializable;
import java.util.UUID;

public class Teacher implements Serializable {

    private UUID mUUID;
    private String mDepartment;
    private String mPublicEmail;

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(TeacherTable.Cols.UUID, mUUID.toString());
        values.put(TeacherTable.Cols.DEPARTMENT, mDepartment);
        values.put(TeacherTable.Cols.PUBLIC_EMAIL, mPublicEmail);
        return values;
    }

    public static TeacherWrapper queryAll(SQLiteDatabase db) {
        Cursor cursor = db.query(
                TeacherTable.NAME, null,
                null, null,
                null, null, null
        );

        return new TeacherWrapper(cursor);
    }

    public static TeacherWrapper query(SQLiteDatabase db, UUID uuid) {
        Cursor cursor = db.query(
                TeacherTable.NAME, null,
                TeacherTable.Cols.UUID + " = ?",
                new String[] {uuid.toString()},
                null, null, null
        );

        return new TeacherWrapper(cursor);
    }

    public User getUser(SQLiteDatabase db) {
        User user = null;
        UserWrapper cursor = null;

        try {
            cursor = User.query(db, mUUID);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                user = cursor.getUser();
            }
        } catch (NullPointerException e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }

    public void setUUID(UUID uuid) {
        mUUID = uuid;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setDepartment(String department) {
        mDepartment = department;
    }

    public String getDepartment() {
        return mDepartment;
    }

    public void setPublicEmail(String publicEmail) {
        mPublicEmail = publicEmail;
    }

    public String getPublicEmail() {
        return mPublicEmail;
    }

}
