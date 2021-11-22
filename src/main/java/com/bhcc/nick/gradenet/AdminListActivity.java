// AdminListActivity.java
// Admin list for GradeNet
// By Nicholas Verrochi
// Last Modified: 5/9/18

package com.bhcc.nick.gradenet;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class AdminListActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AdminListActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return AdminListFragment.newInstance();
    }

}
