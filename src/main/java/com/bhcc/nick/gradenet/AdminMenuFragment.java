// AdminMenuFragment.java
// UI Controller for the Admin menu
// By Nicholas Verrochi
// Last Modified: 5/8/18

package com.bhcc.nick.gradenet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AdminMenuFragment extends Fragment {

    private static final String ARG_USER = "user";

    private User mUser;

    public static AdminMenuFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);

        AdminMenuFragment fragment = new AdminMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = (User)getArguments().getSerializable(ARG_USER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_admin_menu, container, false);

        TextView welcomeTextView = (TextView)v.findViewById(R.id.welcome_text_view);
        welcomeTextView.setText(getString(R.string.welcome,
                mUser.getFirstName() + " " + mUser.getLastName()));

        Button studentsButton = (Button)v.findViewById(R.id.students_button);
        studentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = StudentListActivity.newIntent(getActivity(), mUser, false);
                startActivity(intent);
            }
        });

        Button teachersButton = (Button)v.findViewById(R.id.teachers_button);
        teachersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TeacherListActivity.newIntent(getActivity(), mUser, false);
                startActivity(intent);
            }
        });

        Button classesButton = (Button)v.findViewById(R.id.classes_button);
        classesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CourseListActivity.newIntent(getActivity(), mUser);
                startActivity(intent);
            }
        });

        Button adminsButton = (Button)v.findViewById(R.id.admins_button);
        adminsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminListActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });

        return v;
    }

}
