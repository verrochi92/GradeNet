// LogInFragment.java
// This fragment uses the database to confirm user login
// By Nicholas Verrochi
// Last Modified: 4/5/18

package com.bhcc.nick.gradenet;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bhcc.nick.gradenet.database.DatabaseHelper;
import com.bhcc.nick.gradenet.database.UserWrapper;

public class LogInFragment extends Fragment {

    private SQLiteDatabase mDatabase;

    // UI Elements
    private EditText mUsernameField;
    private EditText mPasswordField;
    private Button mLogInButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_log_in, container, false);

        mDatabase = new DatabaseHelper(getActivity()).getWritableDatabase();

        mUsernameField = (EditText)v.findViewById(R.id.username_field);

        mPasswordField = (EditText)v.findViewById(R.id.password_field);

        mLogInButton = (Button)v.findViewById(R.id.log_in_button);
        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        return v;
    }

    public void logIn() {
        User user = null;
        UserWrapper cursor = User.query(mDatabase, mUsernameField.getText().toString(),
                mPasswordField.getText().toString());

        try {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                user = cursor.getUser();
            }
        } finally {
            cursor.close();
        }

        if (user == null) {
            makeFailToast();
        }
        else {
            launchMenu(user);
        }
    }

    public void makeFailToast() {
        Toast.makeText(getActivity().getApplicationContext(), "Failed to log in!\n" +
                "Please try again...", Toast.LENGTH_SHORT).show();
        mPasswordField.setText("");
    }

    public void launchMenu(User user) {
        if (user.getUserLevel() == user.LEVEL_ADMIN) {
            // launch admin menu
            startActivity(AdminMenuActivity.newIntent(getActivity(), user));
        }
        else if (user.getUserLevel() == user.LEVEL_TEACHER) {
            // launch teacher menu
            Intent intent = TeacherMenuActivity.newIntent(getActivity(), user);
        }
        else {
            // launch student menu
        }
    }

}
