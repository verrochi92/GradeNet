// LogInActivity.java
// Hosts a LogInFragment
// By Nicholas Verrochi
// Last Modified: 4/5/18

package com.bhcc.nick.gradenet;

import android.support.v4.app.Fragment;

public class LogInActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new LogInFragment();
    }

}
