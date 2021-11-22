// StudentDetailActivity.java
// Hosts a StudentDetailFragment
// By Nicholas Verrochi
// Last Modified: 5/3/18

package com.bhcc.nick.gradenet;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class StudentDetailActivity extends SingleFragmentActivity {

    private static final String EXTRA_USER = "user";
    private static final String EXTRA_STUDENT = "student";
    private static final String EXTRA_NEW_ENTRY = "newEntry";

    public static Intent newIntent(Context context, User user, Student student, boolean newEntry) {
        Intent intent = new Intent(context, StudentDetailActivity.class);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_STUDENT, student);
        intent.putExtra(EXTRA_NEW_ENTRY, newEntry);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        User user = (User)getIntent().getSerializableExtra(EXTRA_USER);
        Student student = (Student)getIntent().getSerializableExtra(EXTRA_STUDENT);
        boolean newEntry = getIntent().getBooleanExtra(EXTRA_NEW_ENTRY, true);

        return StudentDetailFragment.newInstance(user, student, newEntry);
    }

}
