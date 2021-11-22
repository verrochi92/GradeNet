// CourseStudentListFragment.java
// UI controller for course - student list
// By Nicholas Verrochi
// Last Modified: 5/9/18

package com.bhcc.nick.gradenet;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bhcc.nick.gradenet.database.DatabaseHelper;
import com.bhcc.nick.gradenet.database.Schema.CourseStudentTable;

import java.util.List;

public class CourseStudentListFragment extends Fragment {

    private static final String ARG_COURSE = "course";
    private static final String ARG_USER = "user";

    private static int REQUEST_CODE_CHOOSE = 0;

    private User mUser;
    private Course mCourse;

    private RecyclerView mRecyclerView;
    private StudentAdapter mAdapter;

    private SQLiteDatabase mDatabase;

    public static CourseStudentListFragment newInstance(User user, Course course) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_COURSE, course);
        args.putSerializable(ARG_USER, user);

        CourseStudentListFragment fragment = new CourseStudentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDatabase = new DatabaseHelper(getActivity()).getWritableDatabase();
        mUser = (User)getArguments().getSerializable(ARG_USER);
        mCourse = (Course)getArguments().getSerializable(ARG_COURSE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_list, parent, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_entry:
                Intent intent = StudentListActivity.newIntent(getActivity(), mUser, true);
                startActivityForResult(intent, REQUEST_CODE_CHOOSE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void onResult(Student student) {
        CourseStudent courseStudent = new CourseStudent();
        courseStudent.setCourseID(mCourse.getCourseID());
        courseStudent.setStudentID(student.getUUID());
        courseStudent.setGrade("none");

        mDatabase.insert(CourseStudentTable.NAME, null, courseStudent.getContentValues());
    }

    private void updateUI() {
        List<Student> studentList = queryStudents();

        if (mAdapter == null) {
            mAdapter = new StudentAdapter(studentList);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setList(studentList);
            mAdapter.notifyDataSetChanged();
        }
    }

    protected List<Student> queryStudents() {
        List<Student> studentList = Student.queryAllFromCourse(mDatabase, mCourse);
        return studentList;
    }

    // Inner class: StudentHolder
    // ViewHolder implementation for the RecyclerView
    private class StudentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Student mStudent;

        private TextView mNameTextView;

        public StudentHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_list_item, parent, false));
            itemView.setOnClickListener(this);

            mNameTextView = (TextView)itemView.findViewById(R.id.title);
        }

        public void bind(Student student) {
            mStudent = student;
            User userInfo = mStudent.getUser(mDatabase);
            String name = userInfo.getFirstName() + " " + userInfo.getLastName();
            mNameTextView.setText(name);
        }

        @Override
        public void onClick(View v) {
            Intent intent = CourseStudentDetailActivity.newIntent(getActivity(), mCourse, mStudent);
            startActivity(intent);
        }
    }

    // Inner class: StudentAdapter
    // Adapter implementation for the recycler view
    private class StudentAdapter extends RecyclerView.Adapter<StudentHolder> {
        private List<Student> mStudentList;

        public StudentAdapter(List<Student> studentList) {
            mStudentList = studentList;
        }

        @Override
        public StudentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new StudentHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(StudentHolder holder, int position) {
            Student student = mStudentList.get(position);
            holder.bind(student);
        }

        @Override
        public int getItemCount() {
            return mStudentList.size();
        }

        public void setList(List<Student> studentList) {
            mStudentList = studentList;
        }
    }

}
