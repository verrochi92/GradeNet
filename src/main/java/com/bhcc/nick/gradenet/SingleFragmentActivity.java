// SingleFragmentActivity.java
// Activity that hosts a single fragment
// By Nicholas Verrochi
// Last Modified: 4/5/18

package com.bhcc.nick.gradenet;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        TextView copyrightNotice = (TextView)findViewById(R.id.copyright_notice);
        copyrightNotice.append(" API Level: " + Build.VERSION.SDK_INT);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

}
