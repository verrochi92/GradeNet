// CourseDetailFragment.java
// UI controller for course detail view
// By Nicholas Verrochi
// Last Modified: 5/8/18

package com.bhcc.nick.gradenet;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bhcc.nick.gradenet.database.DatabaseHelper;
import com.bhcc.nick.gradenet.database.Schema;
import com.bhcc.nick.gradenet.database.Schema.CourseTable;

public class CourseDetailFragment extends Fragment {

    private static final String ARG_USER = "user";
    private static final String ARG_COURSE = "course";
    private static final String ARG_NEW_ENTRY = "newEntry";

    private static int REQUEST_CODE_CHOOSE = 0;

    private User mUser;
    private Course mCourse;
    private boolean mNewEntry;

    private SQLiteDatabase mDatabase;

    private EditText mCourseNameField;
    private EditText mSectionNumberField;
    private TextView mTeacherTextView;
    private Button mDoneButton;

    public static CourseDetailFragment newInstance(User user, Course course, boolean newEntry) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putSerializable(ARG_COURSE, course);
        args.putBoolean(ARG_NEW_ENTRY, newEntry);

        CourseDetailFragment fragment = new CourseDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = new DatabaseHelper(getActivity()).getWritableDatabase();
        mUser = (User)getArguments().getSerializable(ARG_USER);
        mCourse = (Course)getArguments().getSerializable(ARG_COURSE);
        mNewEntry = getArguments().getBoolean(ARG_NEW_ENTRY, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_course_detail, container, false);

        mCourseNameField = (EditText)view.findViewById(R.id.course_name_field);
        mSectionNumberField = (EditText)view.findViewById(R.id.section_number_field);
        mTeacherTextView = (TextView)view.findViewById(R.id.teacher_label);

        Button addTeacherButton = (Button)view.findViewById(R.id.add_teacher_button);
        addTeacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFields();
                Intent intent = TeacherListActivity.newIntent(getActivity(), mUser, true);
                startActivityForResult(intent, REQUEST_CODE_CHOOSE);
            }
        });

        Button studentsButton = (Button)view.findViewById(R.id.course_students_button);
        studentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFields();
                Intent intent = CourseStudentListActivity.newIntent(getActivity(), mUser, mCourse);
                startActivity(intent);
            }
        });

        mDoneButton = (Button)view.findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
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
        else {
            mDoneButton.setEnabled(false);
        }

        return view;
    }

    public void getFields() {
        mCourse.setCourseName(mCourseNameField.getText().toString());
        mCourse.setSectionNumber(mSectionNumberField.getText().toString());
    }

    public void makeNewEntry() {
        mDatabase.insert(CourseTable.NAME, null, mCourse.getContentValues());
    }

    public void update() {
        mDatabase.update(CourseTable.NAME, mCourse.getContentValues(),
                CourseTable.Cols.CLASS_ID + " = ?",
                new String [] { mCourse.getCourseID().toString() }
        );
    }

    public void onResult(Teacher teacher) {
        mCourse.setTeacherID(teacher.getUUID());
        setFields();
        mDoneButton.setEnabled(true);
    }

    private void setFields() {
        User userInfo = mCourse.getTeacher(mDatabase).getUser(mDatabase);
        String teacherName = (userInfo.getFirstName() + " " + userInfo.getLastName());

        mCourseNameField.setText(mCourse.getCourseName());
        mSectionNumberField.setText(mCourse.getSectionNumber());
        mTeacherTextView.setText("teacher: " + teacherName);
    }

}
