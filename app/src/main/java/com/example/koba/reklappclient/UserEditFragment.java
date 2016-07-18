package com.example.koba.reklappclient;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Koba on 18/07/2016.
 */
public class UserEditFragment extends Fragment {

    private EditText birthDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_edit_layout, container, false);
        setUpSpinners(rootView);
        setUpDatePicker(rootView);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        Button cancel = (Button) rootView.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return rootView;
    }

    private void setUpSpinners(View rootView) {
        Spinner cities = (Spinner) rootView.findViewById(R.id.city_spinner);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.cities_array, android.R.layout.simple_spinner_item);
        cities.setAdapter(cityAdapter);

        Spinner genders = (Spinner) rootView.findViewById(R.id.sex_spinner);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.genders_array, android.R.layout.simple_spinner_item);
        genders.setAdapter(genderAdapter);


        Spinner relationships = (Spinner) rootView.findViewById(R.id.relationship_spinner);
        ArrayAdapter<CharSequence> relationshipAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.relationships_array, android.R.layout.simple_spinner_item);
        relationships.setAdapter(relationshipAdapter);
    }

    private void setUpDatePicker(View rootView) {
        birthDate = (EditText) rootView.findViewById(R.id.birthdate);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog birthDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                birthDate.setText(formatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthDatePicker.show();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(birthDate.getWindowToken(), 0);
            }
        });
        birthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    birthDatePicker.show();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(birthDate.getWindowToken(), 0);
                }
                v.clearFocus();
            }
        });
    }

}
