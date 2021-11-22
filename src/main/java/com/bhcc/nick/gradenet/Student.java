// Student.java
// Represents a student-level user
// By Nicholas Verrochi
// Last Modified: 5/3/18

package com.bhcc.nick.gradenet;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bhcc.nick.gradenet.database.CourseStudentWrapper;
import com.bhcc.nick.gradenet.database.Schema.StudentTable;
import com.bhcc.nick.gradenet.database.StudentWrapper;
import com.bhcc.nick.gradenet.database.UserWrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Student implements Serializable {

    private UUID mUUID;
    private String mProgram;
    private String mGPA;

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(StudentTable.Cols.UUID, mUUID.toString());
        values.put(StudentTable.Cols.PROGRAM, mProgram);
        values.put(StudentTable.Cols.GPA, mGPA);
        return values;
    }

    public static StudentWrapper queryAll(SQLiteDatabase db) {
        Cursor cursor = db.query(
                StudentTable.NAME, null,
                null, null,
                null, null, null
        );

        return new StudentWrapper(cursor);
    }

    public static StudentWrapper query(SQLiteDatabase db, UUID uuid) {
        Cursor cursor = db.query(
                StudentTable.NAME, null,
                StudentTable.Cols.UUID + " = ?",
                new String []  { uuid.toString() },
                null, null, null
        );
        return new StudentWrapper(cursor);
    }

    public static List<Student> queryAllFromCourse(SQLiteDatabase db, Course course) {
        List<CourseStudent> courseStudentList = new ArrayList<>();
        List<Student> studentList = new ArrayList<>();
        CourseStudentWrapper cursor = CourseStudent.query(db, course);

        try {
            if (!(cursor.getCount() == 0)) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    courseStudentList.add(cursor.getCourseStudent());
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }

        for (CourseStudent courseStudent : courseStudentList) {
            studentList.add(courseStudent.getStudent(db));
        }

        return studentList;
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

    public void setProgram(String program) {
        mProgram = program;
    }

    public String getProgram() {
        return mProgram;
    }

    public void setGPA(String gpa) {
        mGPA = gpa;
    }

    public String getGPA() {
        return mGPA;
    }

}
