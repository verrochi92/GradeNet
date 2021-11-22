// CourseStudentDetailActivity.java
// Course-student detail view
// By Nicholas Verrochi
// Last Modified: 5/9/18

package com.bhcc.nick.gradenet;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;

import com.bhcc.nick.gradenet.database.DatabaseHelper;

public class CourseStudentDetailActivity extends SingleFragmentActivity {

    private static final String EXTRA_COURSE = "course";
    private static final String EXTRA_STUDENT = "student";

    public static Intent newIntent(Context context, Course course, Student student) {
        Intent intent = new Intent(context, CourseStudentDetailActivity.class);
        intent.putExtra(EXTRA_COURSE, course);
        intent.putExtra(EXTRA_STUDENT, student);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();

        Course course = (Course)getIntent().getSerializableExtra(EXTRA_COURSE);
        Student student = (Student)getIntent().getSerializableExtra(EXTRA_STUDENT);
        CourseStudent courseStudent = CourseStudent.get(db, course, student);

        return CourseStudentDetailFragment.newInstance(course, student, courseStudent);
    }

}
