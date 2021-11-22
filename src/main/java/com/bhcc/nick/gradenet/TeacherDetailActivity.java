// TeacherDetailActivity.java
// Teacher detail view
// By Nicholas Verrochi
// Last Modified: 5/7/18

package com.bhcc.nick.gradenet;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class TeacherDetailActivity extends SingleFragmentActivity {

    private static final String EXTRA_USER = "user";
    private static final String EXTRA_TEACHER = "teacher";
    private static final String EXTRA_NEW_ENTRY = "newEntry";

    public static Intent newIntent(Context context, User user, Teacher teacher, boolean newEntry) {
        Intent intent = new Intent(context, TeacherDetailActivity.class);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_TEACHER, teacher);
        intent.putExtra(EXTRA_NEW_ENTRY, newEntry);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        User user = (User)getIntent().getSerializableExtra(EXTRA_USER);
        Teacher teacher = (Teacher)getIntent().getSerializableExtra(EXTRA_TEACHER);
        boolean newEntry = getIntent().getBooleanExtra(EXTRA_NEW_ENTRY, true);

        return TeacherDetailFragment.newInstance(user, teacher, newEntry);
    }

}
