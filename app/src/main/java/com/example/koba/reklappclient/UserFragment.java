package com.example.koba.reklappclient;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Koba on 18/07/2016.
 */
public class UserFragment extends Fragment {

    private TextView balance;
    private TextView name;
    private TextView surname;
    private TextView email;
    private TextView id;
    private TextView city;
    private TextView address;
    private TextView sex;
    private TextView birthdate;
    private TextView relationship;
    private TextView numberOfChildren;
    private TextView averageIncome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_layout, container, false);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        Drawable editIcon = getResources().getDrawable(R.drawable.ic_edit);
        fab.setImageDrawable(editIcon);
        fab.animate().translationX(0).alpha(1.0f).setDuration(0);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.currentFragmentId = 4;
                Fragment newFragment = ((AppActivity) getActivity()).getFragmentById(AppActivity.currentFragmentId);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.flContent, newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        Bundle args = getArguments();
        User user = args.getParcelable("user");
        initTextViews(rootView, user);

        return rootView;
    }

    private void initTextViews(View rootView, User user) {
        balance = (TextView) rootView.findViewById(R.id.balance);
        balance.setText("ანგარიში: " + user.money);
        name = (TextView) rootView.findViewById(R.id.name);
        name.setText("სახელი: " + user.name);
        surname = (TextView) rootView.findViewById(R.id.surname);
        surname.setText("გვარი: " + user.surname);
        email = (TextView) rootView.findViewById(R.id.email);
        email.setText("ელ. ფოსტა: " + user.email);
        id = (TextView) rootView.findViewById(R.id.pid);
        id.setText("პირადი ნომერი: " + user.pin);
        city = (TextView) rootView.findViewById(R.id.city);
        city.setText("ქალაქი: " + user.city);
        address = (TextView) rootView.findViewById(R.id.address);
        address.setText("მისამართი: " + user.street_address);
        sex = (TextView) rootView.findViewById(R.id.sex);
        sex.setText("სქესი: " + user.sex);
        birthdate = (TextView) rootView.findViewById(R.id.birthdate);
        birthdate.setText("დაბადების თარიღი: " + user.birthdate);
        relationship = (TextView) rootView.findViewById(R.id.relationship);
        relationship.setText("ოჯახური მდგომარეობა: " + user.relationship);
        numberOfChildren = (TextView) rootView.findViewById(R.id.numChildren);
        numberOfChildren.setText("შვილების რაოდენობა: " + Integer.toString(user.number_of_children));
        averageIncome = (TextView) rootView.findViewById(R.id.income);
        averageIncome.setText("საშუალო თვიური შემოსავალი: " + Integer.toString(user.average_monthly_income));
    }
}
