// AdminDetailFragment.java
// UI controller for admin detail view

package com.bhcc.nick.gradenet;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bhcc.nick.gradenet.database.DatabaseHelper;
import com.bhcc.nick.gradenet.database.Schema;
import com.bhcc.nick.gradenet.database.Schema.UserTable;

public class AdminDetailFragment extends Fragment {

    private static final String ARG_ADMIN = "admin";
    private static final String ARG_NEW_ENTRY = "newEntry";

    private User mAdmin;
    private boolean mNewEntry;

    private SQLiteDatabase mDatabase;

    private TextView mNameTextView;
    private TextView mUsernameTextView;
    private TextView mPasswordTextView;

    public static AdminDetailFragment newInstance(User admin, boolean newEntry) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ADMIN, admin);
        args.putBoolean(ARG_NEW_ENTRY, newEntry);

        AdminDetailFragment fragment = new AdminDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = new DatabaseHelper(getActivity()).getWritableDatabase();
        mAdmin = (User)getArguments().getSerializable(ARG_ADMIN);
        mNewEntry = getArguments().getBoolean(ARG_NEW_ENTRY, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_admin_detail, container, false);

        mNameTextView = (TextView)view.findViewById(R.id.name_field);
        mUsernameTextView = (TextView)view.findViewById(R.id.username_field);
        mPasswordTextView = (TextView)view.findViewById(R.id.password_field);

        Button doneButton = (Button)view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFields();

                if (mNewEntry) {
                    makeNewEntry();
                }
                else {
                    update();
                }

                getActivity().finish();
            }
        });

        if (!mNewEntry) {
            setFields();
        }

        return view;
    }

    private void setFields() {
        String nameString = mAdmin.getFirstName() + " " + mAdmin.getLastName();

        mNameTextView.setText(nameString);
        mUsernameTextView.setText(mAdmin.getUsername());
        mPasswordTextView.setText(mAdmin.getPassword());
    }

    private void getFields() {
        mAdmin.setFirstName(getFirstName());
        mAdmin.setLastName(getLastName());
        mAdmin.setUsername(mUsernameTextView.getText().toString());
        mAdmin.setPassword(mPasswordTextView.getText().toString());
        mAdmin.setUserLevel(User.LEVEL_ADMIN);
    }

    private void makeNewEntry() {
        mDatabase.insert(UserTable.NAME, null, mAdmin.getContentValues());
    }

    private void update() {
        String uuidString = mAdmin.getUUID().toString();
        mDatabase.update(UserTable.NAME, mAdmin.getContentValues(),
                UserTable.Cols.UUID + " = ?", new String [] {uuidString});
    }

    private String getFirstName() {
        String name = mNameTextView.getText().toString();
        if (name.contains(" ")) {
            int i = name.indexOf(' ');
            return name.substring(0, i);
        }
        else {
            return name;
        }
    }

    private String getLastName() {
        String name = mNameTextView.getText().toString();
        if (name.contains(" ")) {
            int i = name.indexOf(' ');
            return name.substring(i + 1);
        }
        else {
            return "";
        }
    }

}
