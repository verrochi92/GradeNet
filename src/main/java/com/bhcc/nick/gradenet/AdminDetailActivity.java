// AdminDetailActivity.java
// Admin detail view
// By Nicholas Verrochi
// Last Modified: 5/9/18

package com.bhcc.nick.gradenet;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class AdminDetailActivity extends SingleFragmentActivity {

    private static final String EXTRA_ADMIN = "admin";
    private static final String EXTRA_NEW_ENTRY = "newEntry";

    public static Intent newIntent(Context context, User admin, boolean newEntry) {
        Intent intent = new Intent(context, AdminDetailActivity.class);
        intent.putExtra(EXTRA_ADMIN, admin);
        intent.putExtra(EXTRA_NEW_ENTRY, newEntry);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        User admin = (User)getIntent().getSerializableExtra(EXTRA_ADMIN);
        boolean newEntry = getIntent().getBooleanExtra(EXTRA_NEW_ENTRY, true);

        return AdminDetailFragment.newInstance(admin, newEntry);
    }

}
