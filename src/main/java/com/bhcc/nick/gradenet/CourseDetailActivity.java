// CourseDetailActivity.java
// Course detail view for GradeNet
// By Nicholas Verrochi
// Last Modified: 5/8/18

package com.bhcc.nick.gradenet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class CourseDetailActivity extends SingleFragmentActivity {

    private static final String EXTRA_USER = "user";
    private static final String EXTRA_COURSE = "course";
    private static final String EXTRA_NEW_ENTRY = "newEntry";

    public static Intent newIntent(Context context, User user, Course course, boolean newEntry) {
        Intent intent = new Intent(context, CourseDetailActivity.class);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_COURSE, course);
        intent.putExtra(EXTRA_NEW_ENTRY, newEntry);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        User user = (User)getIntent().getSerializableExtra(EXTRA_USER);
        Course course = (Course)getIntent().getSerializableExtra(EXTRA_COURSE);
        boolean newEntry = getIntent().getBooleanExtra(EXTRA_NEW_ENTRY, true);

        return CourseDetailFragment.newInstance(user, course, newEntry);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CourseDetailFragment fragment = (CourseDetailFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        else {
            if (data == null) {
                return;
            }
            fragment.onResult(TeacherListFragment.getTeacher(data));
        }
    }

}
