// Course.java
// Represents class information
// By Nicholas Verrochi
// Last Modified: 5/8/18

package com.bhcc.nick.gradenet;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bhcc.nick.gradenet.database.CourseWrapper;
import com.bhcc.nick.gradenet.database.Schema.CourseTable;
import com.bhcc.nick.gradenet.database.TeacherWrapper;

import java.io.Serializable;
import java.util.UUID;

public class Course  implements Serializable {

    private UUID mCourseID;
    private UUID mTeacherID;
    private String mCourseName;
    private String mSectionNumber;

    public Course() {
        mCourseID = UUID.randomUUID();
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(CourseTable.Cols.CLASS_ID, mCourseID.toString());
        values.put(CourseTable.Cols.TEACHER_ID, mTeacherID.toString());
        values.put(CourseTable.Cols.COURSE_NAME, mCourseName);
        values.put(CourseTable.Cols.SECTION_NUMBER, mSectionNumber);
        return values;
    }

    public static CourseWrapper queryAll(SQLiteDatabase db) {
        Cursor cursor = db.query(CourseTable.NAME, null, null, null, null, null, null);
        return new CourseWrapper(cursor);
    }

    public Teacher getTeacher(SQLiteDatabase db) {
        Teacher teacher = null;
        TeacherWrapper cursor = null;

        try {
            cursor = Teacher.query(db, mTeacherID);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                teacher = cursor.getTeacher();
            }
        } catch (NullPointerException e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return teacher;
    }

    public void setCourseID(UUID courseID) {
        mCourseID = courseID;
    }

    public UUID getCourseID() {
        return mCourseID;
    }

    public void setTeacherID(UUID teacherID) {
        mTeacherID = teacherID;
    }

    public UUID getTeacherID() {
        return mTeacherID;
    }

    public void setCourseName(String courseName) {
        mCourseName = courseName;
    }

    public String getCourseName() {
        return mCourseName;
    }

    public void setSectionNumber(String sectionNumber) {
        mSectionNumber = sectionNumber;
    }

    public String getSectionNumber() {
        return mSectionNumber;
    }

}
