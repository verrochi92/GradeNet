// CourseStudentListActivity.java
// Assign students to a course
// By Nicholas Verrochi
// Last Modified: 5/9/18

package com.bhcc.nick.gradenet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class CourseStudentListActivity extends SingleFragmentActivity {

    private static final String EXTRA_USER = "user";
    private static final String EXTRA_COURSE = "course";

    private User mUser;
    private Course mCourse;

    public static Intent newIntent(Context context, User user, Course course) {
        Intent intent = new Intent(context, CourseStudentListActivity.class);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_COURSE, course);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        mUser = (User)getIntent().getSerializableExtra(EXTRA_USER);
        mCourse = (Course)getIntent().getSerializableExtra(EXTRA_COURSE);
        return CourseStudentListFragment.newInstance(mUser, mCourse);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CourseStudentListFragment fragment = (CourseStudentListFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        else {
            if (data == null) {
                return;
            }
            fragment.onResult(StudentListFragment.getStudent(data));
        }
    }

}
