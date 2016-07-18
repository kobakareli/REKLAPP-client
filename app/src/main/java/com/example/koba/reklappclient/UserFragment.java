package com.example.koba.reklappclient;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Koba on 18/07/2016.
 */
public class UserFragment extends Fragment {

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
        return rootView;
    }
}
