// database.DatabaseHelper.java
// SQLite helper class for GradeNet app
// By Nicholas Verrochi
// Last Modified: 5/3/18

package com.bhcc.nick.gradenet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bhcc.nick.gradenet.User;
import com.bhcc.nick.gradenet.database.Schema.CourseStudentTable;
import com.bhcc.nick.gradenet.database.Schema.CourseTable;
import com.bhcc.nick.gradenet.database.Schema.StudentTable;
import com.bhcc.nick.gradenet.database.Schema.TeacherTable;
import com.bhcc.nick.gradenet.database.Schema.UserTable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "gradenet.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    // create new database
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("pragma foreign_keys = 1");

        db.execSQL("create table " + UserTable.NAME + "(" +
                UserTable.Cols.UUID + " primary key, " +
                UserTable.Cols.FIRST_NAME + ", " +
                UserTable.Cols.LAST_NAME + ", " +
                UserTable.Cols.USERNAME + ", " +
                UserTable.Cols.PASSWORD + ", " +
                UserTable.Cols.USER_LEVEL + ")"
        );

        db.execSQL("create table " + StudentTable.NAME + "(" +
                StudentTable.Cols.UUID + ", " +
                StudentTable.Cols.PROGRAM + ", " +
                StudentTable.Cols.GPA + ", " +
                "foreign key(" + StudentTable.Cols.UUID + ") references " +
                UserTable.NAME + "(" + UserTable.Cols.UUID + "))"
        );

        db.execSQL("create table " + TeacherTable.NAME + "(" +
                TeacherTable.Cols.UUID + ", " +
                TeacherTable.Cols.DEPARTMENT + ", " +
                TeacherTable.Cols.PUBLIC_EMAIL + ", " +
                "foreign key(" + TeacherTable.Cols.UUID + ") references " +
                UserTable.NAME + "(" + UserTable.Cols.UUID + "))"
        );

        db.execSQL("create table " + CourseTable.NAME + "(" +
                CourseTable.Cols.CLASS_ID + " primary key, " +
                CourseTable.Cols.TEACHER_ID + ", " +
                CourseTable.Cols.COURSE_NAME + ", " +
                CourseTable.Cols.SECTION_NUMBER + ", " +
                "foreign key(" + CourseTable.Cols.TEACHER_ID + ") references " +
                UserTable.NAME + "(" + UserTable.Cols.UUID + "))"
        );

        db.execSQL("create table " + CourseStudentTable.NAME + "(" +
                CourseStudentTable.Cols.CLASS_ID + ", " +
                CourseStudentTable.Cols.STUDENT_ID + ", " +
                CourseStudentTable.Cols.GRADE + ", " +

                "foreign key(" + CourseStudentTable.Cols.CLASS_ID + ") references " +
                CourseTable.NAME + "(" + CourseTable.Cols.CLASS_ID +"), " +
                "foreign key(" + CourseStudentTable.Cols.STUDENT_ID + ") references " +
                UserTable.NAME + "(" + UserTable.Cols.UUID + "))"
        );

        // create admin account
        db.insert(UserTable.NAME, null, User.generateAdmin().getContentValues());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}
