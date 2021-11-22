// CourseListFragment.java
// UI controller for course list
// By Nicholas Verrochi
// Last Modfied: 5/8/18

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

import com.bhcc.nick.gradenet.database.CourseWrapper;
import com.bhcc.nick.gradenet.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CourseListFragment extends Fragment {

    private static final String ARG_USER = "user";

    private User mUser;

    private RecyclerView mRecyclerView;
    private CourseAdapter mAdapter;

    private SQLiteDatabase mDatabase;

    public static CourseListFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);

        CourseListFragment fragment = new CourseListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDatabase = new DatabaseHelper(getActivity()).getWritableDatabase();

        mUser = (User)getArguments().getSerializable(ARG_USER);
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
                Course course = new Course();
                Intent intent =
                        CourseDetailActivity.newIntent(getActivity(), mUser, course, true);
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
        List<Course> courseList = queryCourses();

        if (mAdapter == null) {
            mAdapter = new CourseAdapter(courseList);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setList(courseList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private List<Course> queryCourses() {
        List<Course> courseList = new ArrayList<>();

        SQLiteDatabase db = new DatabaseHelper(getActivity()).getWritableDatabase();
        CourseWrapper cursor = Course.queryAll(db);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                courseList.add(cursor.getCourse());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return courseList;
    }

    // Inner class: CourseHolder
    // ViewHolder implementation for the RecyclerView
    private class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Course mCourse;

        private TextView mTitleTextView;
        private TextView mSubtitleTextView;

        public CourseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_list_item, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView)itemView.findViewById(R.id.title);
            mSubtitleTextView = (TextView)itemView.findViewById(R.id.subtitle);
        }

        public void bind(Course course) {
            mCourse = course;
            mTitleTextView.setText(course.getCourseName());
            mSubtitleTextView.setText(course.getSectionNumber());
        }

        @Override
        public void onClick(View v) {
            Intent intent = CourseDetailActivity.newIntent(getActivity(), mUser, mCourse, false);
            startActivity(intent);
        }
    }

    // Inner class: StudentAdapter
    // Adapter implementation for the RecyclerView
    private class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {
        private List<Course> mCourseList;

        public CourseAdapter(List<Course> courseList) {
            mCourseList = courseList;
        }

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new CourseHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(CourseHolder holder, int position) {
            Course course = mCourseList.get(position);
            holder.bind(course);
        }

        @Override
        public int getItemCount() {
            return mCourseList.size();
        }

        public void setList(List<Course> list) {
            mCourseList = list;
        }
    }

}
