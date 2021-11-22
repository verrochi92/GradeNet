// TeacherListActivity.java
// List of teachers for gradenet
// By Nicholas Verrochi
// Last Modified: 5/7/18

package com.bhcc.nick.gradenet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class TeacherListActivity extends SingleFragmentActivity {

    private static final String EXTRA_USER = "user";
    private static final String EXTRA_CHOOSING = "choosing";

    private User mUser;
    private boolean mChoosing;

    public static Intent newIntent(Context context, User user, boolean choosing) {
        Intent intent = new Intent(context, TeacherListActivity.class);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_CHOOSING, choosing);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        mUser = (User)getIntent().getSerializableExtra(EXTRA_USER);
        mChoosing = getIntent().getBooleanExtra(EXTRA_CHOOSING, false);
        return TeacherListFragment.newInstance(mUser, mChoosing);
    }

}
