// CourseStudent.java
// Contains information shared between a class and student
// By Nicholas Verrochi
// Last Modified: 5/9/18

package com.bhcc.nick.gradenet;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bhcc.nick.gradenet.database.CourseStudentWrapper;
import com.bhcc.nick.gradenet.database.Schema.CourseStudentTable;
import com.bhcc.nick.gradenet.database.StudentWrapper;

import java.io.Serializable;
import java.util.UUID;

public class CourseStudent implements Serializable {

    private UUID mCourseID;
    private UUID mStudentID;
    private String mGrade;

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(CourseStudentTable.Cols.CLASS_ID, mCourseID.toString());
        values.put(CourseStudentTable.Cols.STUDENT_ID, mStudentID.toString());
        values.put(CourseStudentTable.Cols.GRADE, mGrade);
        return values;
    }

    public static CourseStudent get(SQLiteDatabase db, Course course, Student student) {
        CourseStudent courseStudent = null;
        CourseStudentWrapper cursor = query(db, course, student);

        try {
            if (!cursor.isAfterLast()) {
                cursor.moveToFirst();
                courseStudent = cursor.getCourseStudent();
            }
        } finally {
            cursor.close();
        }

        return courseStudent;
    }

    public static CourseStudentWrapper query(SQLiteDatabase db, Course course, Student student) {
        Cursor cursor = db.query(CourseStudentTable.NAME, null,
                CourseStudentTable.Cols.CLASS_ID + " = ? and " +
                CourseStudentTable.Cols.STUDENT_ID + " = ?",
                new String [] { course.getCourseID().toString(), student.getUUID().toString() },
                null, null, null
        );
        return new CourseStudentWrapper(cursor);
    }

    public static CourseStudentWrapper query(SQLiteDatabase db, Course course) {
        Cursor cursor = db.query(CourseStudentTable.NAME, null,
                CourseStudentTable.Cols.CLASS_ID + " = ?",
                new String [] {course.getCourseID().toString()},
                null, null, null
        );
        return new CourseStudentWrapper(cursor);
    }

    public Student getStudent(SQLiteDatabase db) {
        Student student = null;
        StudentWrapper cursor = null;

        try {
            cursor = Student.query(db, mStudentID);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                student = cursor.getStudent();
            }
        } catch (NullPointerException e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return student;
    }

    public void setCourseID(UUID courseID) {
        mCourseID = courseID;
    }

    public UUID getCourseID() {
        return mCourseID;
    }

    public void setStudentID(UUID studentID) {
        mStudentID = studentID;
    }

    public UUID getStudentID() {
        return mStudentID;
    }

    public void setGrade(String grade) {
        mGrade = grade;
    }

    public String getGrade() {
        return mGrade;
    }

}
