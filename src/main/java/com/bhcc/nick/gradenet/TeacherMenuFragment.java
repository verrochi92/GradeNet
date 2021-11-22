// TeacherMenuFragment.java
// UI controller for teacher menu
// By Nicholas Verrochi
// Last Modified: 5/9/18

package com.bhcc.nick.gradenet;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TeacherMenuFragment extends Fragment {

    private static final String ARG_USER = "user";

    private User mUser;

    public static TeacherMenuFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);

        TeacherMenuFragment fragment = new TeacherMenuFragment();
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
        View view = inflater.inflate(R.layout.fragment_teacher_menu, container, false);

        TextView welcomeTextView = (TextView)view.findViewById(R.id.welcome_text_view);
        welcomeTextView.setText(getString(R.string.welcome,
                mUser.getFirstName() + " " + mUser.getLastName()));

        Button viewProfileButton = (Button)view.findViewById(R.id.view_profile_button);

        Button viewGradesButton = (Button)view.findViewById(R.id.view_grades_button);

        return view;
    }

}
