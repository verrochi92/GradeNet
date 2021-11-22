// StudentListFragment.java
// UI Controller for the student list
// By Nicholas Verrochi
// Last Modified: 5/3/18

package com.bhcc.nick.gradenet;

import android.app.Activity;
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
import com.bhcc.nick.gradenet.database.StudentWrapper;

import java.util.ArrayList;
import java.util.List;

public class StudentListFragment extends Fragment {

    private static final String ARG_USER = "user";
    private static final String ARG_CHOOSING = "choosing";

    private static final String EXTRA_STUDENT = "student";

    private User mUser;
    private boolean mChoosing;

    private RecyclerView mRecyclerView;
    private StudentAdapter mAdapter;

    protected SQLiteDatabase mDatabase;

    public static Student getStudent(Intent data) {
        return (Student)data.getSerializableExtra(EXTRA_STUDENT);
    }

    public static StudentListFragment newInstance(User user, boolean choosing) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putSerializable(ARG_CHOOSING, choosing);

        StudentListFragment fragment = new StudentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = new DatabaseHelper(getActivity()).getWritableDatabase();

        mUser = (User)getArguments().getSerializable(ARG_USER);
        mChoosing = getArguments().getBoolean(ARG_CHOOSING, false);
        setHasOptionsMenu(!mChoosing);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

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
                Student student = new Student();
                Intent intent =
                        StudentDetailActivity.newIntent(getActivity(), mUser, student, true);
                startActivity(intent);
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
        List<Student> studentList = new ArrayList<>();

        SQLiteDatabase db = new DatabaseHelper(getActivity()).getWritableDatabase();
        StudentWrapper cursor = Student.queryAll(db);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                studentList.add(cursor.getStudent());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return studentList;
    }

    // Inner class: StudentHolder
    // ViewHolder implementation for the RecyclerView
    private class StudentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Student mStudent;

        private TextView mTitleTextView;
        private TextView mSubtitleTextView;

        public StudentHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_list_item, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView)itemView.findViewById(R.id.title);
            mSubtitleTextView = (TextView)itemView.findViewById(R.id.subtitle);
        }

        public void bind(Student student) {
            mStudent = student;
            User userInfo = student.getUser(mDatabase);
            mTitleTextView.setText(userInfo.getLastName() + ", " + userInfo.getFirstName());
            mSubtitleTextView.setText(student.getProgram());
        }

        @Override
        public void onClick(View v) {
            if (mChoosing) {
                Intent data = new Intent();
                data.putExtra(EXTRA_STUDENT, mStudent);
                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().finish();
            }
            else {
                Intent intent = StudentDetailActivity.newIntent(getActivity(), mUser, mStudent, false);
                startActivity(intent);
            }
        }
    }

    // Inner class: StudentAdapter
    // Adapter implementation for the RecyclerView
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

        public void setList(List<Student> list) {
            mStudentList = list;
        }
    }

}
