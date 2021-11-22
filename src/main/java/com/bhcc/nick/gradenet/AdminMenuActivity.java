// AdminMenuActivity.java
// UI Controller for the admin menu
// By Nicholas Verrochi
// Last Modified: 5/3/18

package com.bhcc.nick.gradenet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class AdminMenuActivity extends SingleFragmentActivity {

    private static final String EXTRA_USER = "user";

    private User mUser;

    @Override
    protected Fragment createFragment() {
        mUser = (User)getIntent().getSerializableExtra(EXTRA_USER);
        return AdminMenuFragment.newInstance(mUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Intent newIntent(Context context, User user) {
        Intent intent = new Intent(context, AdminMenuActivity.class);
        intent.putExtra(EXTRA_USER, user);
        return intent;
    }

}
