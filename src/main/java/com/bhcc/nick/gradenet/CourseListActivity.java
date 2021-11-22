// CourseListActivity.java
// Displays a list of classes
// By Nicholas Verrochi
// Last Modified: 5/8/18

package com.bhcc.nick.gradenet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class CourseListActivity extends SingleFragmentActivity {

    private static final String EXTRA_USER = "com.bhcc.nick.gradenet.user";

    private User mUser;

    public static Intent newIntent(Context context, User user) {
        Intent intent = new Intent(context, CourseListActivity.class);
        intent.putExtra(EXTRA_USER, user);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = (User)getIntent().getSerializableExtra(EXTRA_USER);
    }

    @Override
    protected Fragment createFragment() {
        return CourseListFragment.newInstance(mUser);
    }

}
