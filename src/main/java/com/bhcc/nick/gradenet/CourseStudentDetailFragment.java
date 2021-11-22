// CourseStudentDetailFragment.java
// UI controller for course-student detail view
// By Nicholas Verrochi
// Last Modified: 5/9/18

package com.bhcc.nick.gradenet;

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
import com.bhcc.nick.gradenet.database.Schema.CourseStudentTable;

public class CourseStudentDetailFragment extends Fragment {

    private static final String ARG_COURSE = "course";
    private static final String ARG_STUDENT = "student";
    private static final String ARG_COURSE_STUDENT = "courseStudent";

    private Course mCourse;
    private Student mStudent;
    private CourseStudent mCourseStudent;

    private SQLiteDatabase mDatabase;

    private TextView mCourseNameTextView;
    private TextView mStudentNameTextView;
    private EditText mGradeField;

    public static CourseStudentDetailFragment newInstance(Course course, Student student,
                                                          CourseStudent courseStudent)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_COURSE, course);
        args.putSerializable(ARG_STUDENT, student);
        args.putSerializable(ARG_COURSE_STUDENT, courseStudent);

        CourseStudentDetailFragment fragment = new CourseStudentDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = new DatabaseHelper(getActivity()).getWritableDatabase();
        mCourse = (Course)getArguments().getSerializable(ARG_COURSE);
        mStudent = (Student)getArguments().getSerializable(ARG_STUDENT);
        mCourseStudent = (CourseStudent)getArguments().getSerializable(ARG_COURSE_STUDENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInsanceState)
    {
        View view = inflater.inflate(R.layout.fragment_course_student_detail, container, false);

        mStudentNameTextView = (TextView)view.findViewById(R.id.student_name_label);
        mCourseNameTextView = (TextView)view.findViewById(R.id.course_name_label);
        mGradeField = (EditText)view.findViewById(R.id.grade_field);

        Button doneButton = (Button)view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFields();
                update();
                getActivity().finish();
            }
        });

        setFields();

        return view;
    }

    public void setFields() {
        User userInfo = mStudent.getUser(mDatabase);
        String studentName = userInfo.getFirstName() + " " + userInfo.getLastName();

        mCourseNameTextView.setText("Course: " + mCourse.getCourseName());
        mStudentNameTextView.setText("Student: " + studentName);
        mGradeField.setText(mCourseStudent.getGrade());
    }

    public void getFields() {
        mCourseStudent.setGrade(mGradeField.getText().toString());
    }

    public void update() {
        mDatabase.update(CourseStudentTable.NAME, mCourseStudent.getContentValues(),
                CourseStudentTable.Cols.CLASS_ID + " = ? and " +
                CourseStudentTable.Cols.STUDENT_ID + " = ?",
                new String [] { mCourse.getCourseID().toString(), mStudent.getUUID().toString() }
        );
    }

}
