// TeacherDetailFragment.java
// UI controller for Teacher detail view
// By Nicholas Verrochi
// Last Modified: 5/7/18

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
import com.bhcc.nick.gradenet.database.Schema.TeacherTable;
import com.bhcc.nick.gradenet.database.Schema.UserTable;

public class TeacherDetailFragment extends Fragment {

    private static final String ARG_USER = "user";
    private static final String ARG_TEACHER = "teacher";
    private static final String ARG_NEW_ENTRY = "newEntry";

    private User mUser;
    private Teacher mTeacher;
    private User mUserInfo;
    private boolean mNewEntry;

    private SQLiteDatabase mDatabase;

    private TextView mNameTextView;
    private TextView mDepartmentTextView;
    private TextView mPublicEmailTextView;
    private TextView mUsernameTextView;
    private TextView mPasswordTextView;

    public static TeacherDetailFragment newInstance(User user, Teacher teacher, boolean newEntry) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putSerializable(ARG_TEACHER, teacher);
        args.putBoolean(ARG_NEW_ENTRY, newEntry);

        TeacherDetailFragment fragment = new TeacherDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = new DatabaseHelper(getActivity()).getWritableDatabase();
        mUser = (User)getArguments().getSerializable(ARG_USER);
        mTeacher = (Teacher)getArguments().getSerializable(ARG_TEACHER);
        mNewEntry = getArguments().getBoolean(ARG_NEW_ENTRY, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_teacher_detail, container, false);

        mNameTextView = (TextView)view.findViewById(R.id.name_field);
        mDepartmentTextView = (TextView)view.findViewById(R.id.department_field);
        mPublicEmailTextView = (TextView)view.findViewById(R.id.public_email_field);
        mUsernameTextView = (TextView)view.findViewById(R.id.username_field);
        mPasswordTextView = (TextView)view.findViewById(R.id.password_field);

        Button doneButton = (Button)view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User userInfo = mTeacher.getUser(mDatabase);

                if (userInfo == null) {
                    userInfo = new User();
                }

                makeNewUser(userInfo);
                getTeacherFields();

                if (mNewEntry) {
                    makeNewEntry(userInfo);
                }
                else {
                    update(userInfo);
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
        User userInfo = mTeacher.getUser(mDatabase);
        String nameString = userInfo.getFirstName() + " " + userInfo.getLastName();

        mNameTextView.setText(nameString);
        mDepartmentTextView.setText(mTeacher.getDepartment());
        mPublicEmailTextView.setText(mTeacher.getPublicEmail());
        mUsernameTextView.setText(userInfo.getUsername());
        mPasswordTextView.setText(userInfo.getPassword());
    }

    private void makeNewUser(User user) {
        user.setFirstName(getFirstName());
        user.setLastName(getLastName());
        user.setUsername(mUsernameTextView.getText().toString());
        user.setPassword(mPasswordTextView.getText().toString());
        user.setUserLevel(User.LEVEL_TEACHER);
    }

    private void makeNewEntry(User userInfo) {
        mDatabase.insert(UserTable.NAME, null, userInfo.getContentValues());
        mTeacher.setUUID(userInfo.getUUID());
        mDatabase.insert(TeacherTable.NAME, null, mTeacher.getContentValues());
    }

    private void update(User userInfo) {
        String uuidString = String.valueOf(userInfo.getUUID());
        mDatabase.update(UserTable.NAME, userInfo.getContentValues(),
                UserTable.Cols.UUID + " = ?", new String [] {uuidString});
        mDatabase.update(TeacherTable.NAME, mTeacher.getContentValues(),
                TeacherTable.Cols.UUID + " = ?", new String [] {uuidString});
    }

    private void getTeacherFields() {
        mTeacher.setDepartment(mDepartmentTextView.getText().toString());
        mTeacher.setPublicEmail(mPublicEmailTextView.getText().toString());
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
