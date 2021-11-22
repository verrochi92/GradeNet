// CourseStudentWrapper.java
// CourseStudent CursorWrapper
// By Nicholas Verrochi
// Last Modified: 5/9/18

package com.bhcc.nick.gradenet.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bhcc.nick.gradenet.CourseStudent;
import com.bhcc.nick.gradenet.database.Schema.CourseStudentTable;

import java.util.UUID;

public class CourseStudentWrapper extends CursorWrapper {

    public CourseStudentWrapper(Cursor cursor) {
        super(cursor);
    }

    public CourseStudent getCourseStudent() {
        UUID courseID =
                UUID.fromString(getString(getColumnIndex(CourseStudentTable.Cols.CLASS_ID)));
        UUID studentID =
                UUID.fromString(getString(getColumnIndex(CourseStudentTable.Cols.STUDENT_ID)));
        String grade = getString(getColumnIndex(CourseStudentTable.Cols.GRADE));

        CourseStudent courseStudent = new CourseStudent();
        courseStudent.setCourseID(courseID);
        courseStudent.setStudentID(studentID);
        courseStudent.setGrade(grade);
        return courseStudent;
    }

}
