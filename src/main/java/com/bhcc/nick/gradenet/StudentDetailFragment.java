// StudentDetailFragment.java
// UI controller for student detail view
// By Nicholas Verrochi
// Last Modified: 5/5/18

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
import com.bhcc.nick.gradenet.database.Schema.StudentTable;
import com.bhcc.nick.gradenet.database.Schema.UserTable;

public class StudentDetailFragment extends Fragment {

    private static final String ARG_USER = "user";
    private static final String ARG_STUDENT = "student";
    private static final String ARG_NEW_ENTRY = "newEntry";

    private User mUser;
    private Student mStudent;
    private User mUserInfo;
    private boolean mNewEntry;

    private SQLiteDatabase mDatabase;

    private TextView mNameTextView;
    private TextView mProgramTextView;
    private TextView mGPATextView;
    private TextView mUsernameTextView;
    private TextView mPasswordTextView;

    public static StudentDetailFragment newInstance(User user, Student student, boolean newEntry) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putSerializable(ARG_STUDENT, student);
        args.putBoolean(ARG_NEW_ENTRY, newEntry);

        StudentDetailFragment fragment = new StudentDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = new DatabaseHelper(getActivity()).getWritableDatabase();
        mUser = (User)getArguments().getSerializable(ARG_USER);
        mStudent = (Student)getArguments().getSerializable(ARG_STUDENT);
        mNewEntry = getArguments().getBoolean(ARG_NEW_ENTRY, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_student_detail, container, false);

        mNameTextView = (TextView)view.findViewById(R.id.name_field);
        mProgramTextView = (TextView)view.findViewById(R.id.program_field);
        mGPATextView = (TextView)view.findViewById(R.id.gpa_field);
        mUsernameTextView = (TextView)view.findViewById(R.id.username_field);
        mPasswordTextView = (TextView)view.findViewById(R.id.password_field);

        Button doneButton = (Button)view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User userInfo = mStudent.getUser(mDatabase);

                if (userInfo == null) {
                    userInfo = new User();
                }

                makeNewUser(userInfo);
                getStudentFields();

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
        User userInfo = mStudent.getUser(mDatabase);
        String nameString = userInfo.getFirstName() + " " + userInfo.getLastName();

        mNameTextView.setText(nameString);
        mProgramTextView.setText(mStudent.getProgram());
        mGPATextView.setText(mStudent.getGPA());
        mUsernameTextView.setText(userInfo.getUsername());
        mPasswordTextView.setText(userInfo.getPassword());
    }

    private void makeNewUser(User user) {
        user.setFirstName(getFirstName());
        user.setLastName(getLastName());
        user.setUsername(mUsernameTextView.getText().toString());
        user.setPassword(mPasswordTextView.getText().toString());
        user.setUserLevel(User.LEVEL_STUDENT);
    }

    private void makeNewEntry(User userInfo) {
        mDatabase.insert(UserTable.NAME, null, userInfo.getContentValues());
        mStudent.setUUID(userInfo.getUUID());
        mDatabase.insert(StudentTable.NAME, null, mStudent.getContentValues());
    }

    private void update(User userInfo) {
        String uuidString = String.valueOf(userInfo.getUUID());
        mDatabase.update(UserTable.NAME, userInfo.getContentValues(),
                UserTable.Cols.UUID + " = ?", new String [] {uuidString});
        mDatabase.update(StudentTable.NAME, mStudent.getContentValues(),
                StudentTable.Cols.UUID + " = ?", new String [] {uuidString});
    }

    private void getStudentFields() {
        mStudent.setProgram(mProgramTextView.getText().toString());
        mStudent.setGPA(mGPATextView.getText().toString());
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
