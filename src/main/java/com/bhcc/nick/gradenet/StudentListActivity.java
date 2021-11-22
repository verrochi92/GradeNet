// StudentListActivity.java
// Student list view
// By Nicholas Verrochi
// Last Modfied: 5/9/18

package com.bhcc.nick.gradenet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class StudentListActivity extends SingleFragmentActivity {

    private static final String EXTRA_USER = "com.bhcc.nick.gradenet.user";
    private static final String EXTRA_CHOOSING = "choosing";

    private User mUser;
    private boolean mChoosing;

    public static Intent newIntent(Context context, User user, boolean choosing) {
        Intent intent = new Intent(context, StudentListActivity.class);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_CHOOSING, choosing);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        mUser = (User)getIntent().getSerializableExtra(EXTRA_USER);
        mChoosing = getIntent().getBooleanExtra(EXTRA_CHOOSING, false);
        return StudentListFragment.newInstance(mUser, mChoosing);
    }

}
