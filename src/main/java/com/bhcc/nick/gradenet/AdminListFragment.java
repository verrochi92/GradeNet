// AdminListFragment.java
// UI Controller for admin list view
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
import com.bhcc.nick.gradenet.database.UserWrapper;

import java.util.ArrayList;
import java.util.List;

public class AdminListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private UserAdapter mAdapter;

    private SQLiteDatabase mDatabase;

    public static AdminListFragment newInstance() {
        AdminListFragment fragment = new AdminListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDatabase = new DatabaseHelper(getActivity()).getWritableDatabase();
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
                User admin = new User();
                Intent intent =
                        AdminDetailActivity.newIntent(getActivity(), admin, true);
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
        List<User> adminList = queryAdmins();

        if (mAdapter == null) {
            mAdapter = new UserAdapter(adminList);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setList(adminList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private List<User> queryAdmins() {
        List<User> adminList = new ArrayList<>();

        UserWrapper cursor = User.queryAdmins(mDatabase);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                adminList.add(cursor.getUser());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return adminList;
    }

    // Inner class: UserHolder
    // ViewHolder implementation for the RecyclerView
    private class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private User mAdmin;

        private TextView mTitleTextView;

        public UserHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_list_item, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView)itemView.findViewById(R.id.title);
        }

        public void bind(User admin) {
            mAdmin = admin;
            mTitleTextView.setText(mAdmin.getLastName() + ", " + mAdmin.getFirstName());
        }

        @Override
        public void onClick(View v) {
            Intent intent = AdminDetailActivity.newIntent(getActivity(), mAdmin, false);
            startActivity(intent);
        }
    }

    // Inner class: UserAdapter
    // Adapter implementation for the RecyclerView
    private class UserAdapter extends RecyclerView.Adapter<UserHolder> {
        private List<User> mAdminList;

        public UserAdapter(List<User> adminList) {
            mAdminList = adminList;
        }

        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new UserHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(UserHolder holder, int position) {
            User admin = mAdminList.get(position);
            holder.bind(admin);
        }

        @Override
        public int getItemCount() {
            return mAdminList.size();
        }

        public void setList(List<User> list) {
            mAdminList = list;
        }
    }

}
