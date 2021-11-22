// CourseWrapper.java
// CursorWrapper for course objects
// By Nicholas Verrochi
// Last Modified: 5/8/18

package com.bhcc.nick.gradenet.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bhcc.nick.gradenet.Course;
import com.bhcc.nick.gradenet.database.Schema.CourseTable;

import java.util.UUID;

public class CourseWrapper extends CursorWrapper {

    public CourseWrapper(Cursor cursor) {
        super(cursor);
    }

    public Course getCourse() {
        UUID courseID = UUID.fromString(getString(getColumnIndex(CourseTable.Cols.CLASS_ID)));
        UUID teacherID = UUID.fromString(getString(getColumnIndex(CourseTable.Cols.TEACHER_ID)));
        String courseName = getString(getColumnIndex(CourseTable.Cols.COURSE_NAME));
        String sectionNumber = getString(getColumnIndex(CourseTable.Cols.SECTION_NUMBER));

        Course course = new Course();
        course.setCourseID(courseID);
        course.setTeacherID(teacherID);
        course.setCourseName(courseName);
        course.setSectionNumber(sectionNumber);
        return course;
    }

}
