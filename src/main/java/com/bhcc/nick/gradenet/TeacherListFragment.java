// TeacherListFragment.java
// UI controller for teacher list
// By Nicholas Verrochi
// Last Modified: 5/8/18

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
import com.bhcc.nick.gradenet.database.TeacherWrapper;

import java.util.ArrayList;
import java.util.List;

public class TeacherListFragment extends Fragment {

    private static final String ARG_USER = "user";
    private static final String ARG_CHOOSING = "choosing";

    private static final String EXTRA_TEACHER = "teacher";

    private User mUser;
    private boolean mChoosing;

    private RecyclerView mRecyclerView;
    private TeacherAdapter mAdapter;

    private SQLiteDatabase mDatabase;

    public static Teacher getTeacher(Intent data) {
        return (Teacher)data.getSerializableExtra(EXTRA_TEACHER);
    }

    public static TeacherListFragment newInstance(User user, boolean choosing) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putSerializable(ARG_CHOOSING, choosing);

        TeacherListFragment fragment = new TeacherListFragment();
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
                Teacher teacher = new Teacher();
                Intent intent =
                        TeacherDetailActivity.newIntent(getActivity(), mUser, teacher, true);
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
        List<Teacher> teacherList = queryTeachers();

        if (mAdapter == null) {
            mAdapter = new TeacherAdapter(teacherList);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setList(teacherList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private List<Teacher> queryTeachers() {
        List<Teacher> teacherList = new ArrayList<>();

        SQLiteDatabase db = new DatabaseHelper(getActivity()).getWritableDatabase();
        TeacherWrapper cursor = Teacher.queryAll(db);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                teacherList.add(cursor.getTeacher());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return teacherList;
    }

    // Inner class: TeacherHolder
    // ViewHolder implementation for the RecyclerView
    private class TeacherHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Teacher mTeacher;

        private TextView mTitleTextView;
        private TextView mSubtitleTextView;

        public TeacherHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_list_item, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView)itemView.findViewById(R.id.title);
            mSubtitleTextView = (TextView)itemView.findViewById(R.id.subtitle);
        }

        public void bind(Teacher teacher) {
            mTeacher = teacher;
            User userInfo = teacher.getUser(mDatabase);
            mTitleTextView.setText(userInfo.getLastName() + ", " + userInfo.getFirstName());
            mSubtitleTextView.setText(teacher.getDepartment());
        }

        @Override
        public void onClick(View v) {
            if (mChoosing) {
                Intent data = new Intent();
                data.putExtra(EXTRA_TEACHER, mTeacher);
                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().finish();
            }
            else {
                Intent intent =
                        TeacherDetailActivity.newIntent(getActivity(), mUser, mTeacher, false);
                startActivity(intent);
            }
        }
    }

    // Inner class: TeacherAdapter
    // Adapter implementation for the RecyclerView
    private class TeacherAdapter extends RecyclerView.Adapter<TeacherHolder> {
        private List<Teacher> mTeacherList;

        public TeacherAdapter(List<Teacher> teacherList) {
            mTeacherList = teacherList;
        }

        @Override
        public TeacherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new TeacherHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(TeacherHolder holder, int position) {
            Teacher teacher = mTeacherList.get(position);
            holder.bind(teacher);
        }

        @Override
        public int getItemCount() {
            return mTeacherList.size();
        }

        public void setList(List<Teacher> list) {
            mTeacherList = list;
        }
    }
}
