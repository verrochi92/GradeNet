// TeacherMenuActivity.java
// Teacher menu for GradeNet
// By Nicholas Verrochi
// Last Modified: 5/9/18

package com.bhcc.nick.gradenet;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class TeacherMenuActivity extends SingleFragmentActivity {

    private static final String EXTRA_USER = "user";

    private User mUser;

    @Override
    protected Fragment createFragment() {
        mUser = (User)getIntent().getSerializableExtra(EXTRA_USER);
        return TeacherMenuFragment.newInstance(mUser);
    }

    public static Intent newIntent(Context context, User user) {
        Intent intent = new Intent(context, TeacherMenuActivity.class);
        intent.putExtra(EXTRA_USER, user);
        return intent;
    }

}
