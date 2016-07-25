package com.example.koba.reklappclient;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
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
import android.widget.Toast;

import com.example.koba.reklappclient.RequestBodies.AddUserBody;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Koba on 18/07/2016.
 */
public class UserEditFragment extends Fragment {

    private TextInputEditText birthDate;
    private Spinner cities;
    private Spinner genders;
    private Spinner relationships;
    private TextInputEditText name;
    private TextInputEditText surname;
    private TextInputEditText email;
    private TextInputEditText number;
    private TextInputEditText id;
    private TextInputEditText address;
    private TextInputEditText numberOfChildren;
    private TextInputEditText income;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_edit_layout, container, false);
        setUpSpinners(rootView);
        setUpDatePicker(rootView);
        setUpEditTexts(rootView);
        final User user = getArguments().getParcelable("user");
        fillData(user);

        Button cancel = (Button) rootView.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        Button save = (Button) rootView.findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveChanges(user);
            }
        });
        return rootView;
    }

    private void setUpSpinners(View rootView) {
        cities = (Spinner) rootView.findViewById(R.id.city_spinner);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.cities_array, android.R.layout.simple_spinner_item);
        cities.setAdapter(cityAdapter);

        genders = (Spinner) rootView.findViewById(R.id.sex_spinner);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.genders_array, android.R.layout.simple_spinner_item);
        genders.setAdapter(genderAdapter);


        relationships = (Spinner) rootView.findViewById(R.id.relationship_spinner);
        ArrayAdapter<CharSequence> relationshipAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.relationships_array, android.R.layout.simple_spinner_item);
        relationships.setAdapter(relationshipAdapter);
    }

    private void setUpDatePicker(View rootView) {
        birthDate = (TextInputEditText) rootView.findViewById(R.id.birthdate);
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
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

    private void setUpEditTexts(View rootView) {
        name = (TextInputEditText) rootView.findViewById(R.id.name);
        surname = (TextInputEditText) rootView.findViewById(R.id.surname);
        email = (TextInputEditText) rootView.findViewById(R.id.email);
        number = (TextInputEditText) rootView.findViewById(R.id.number);
        id = (TextInputEditText) rootView.findViewById(R.id.pid);
        address = (TextInputEditText) rootView.findViewById(R.id.address);
        numberOfChildren = (TextInputEditText) rootView.findViewById(R.id.numChildren);
        income = (TextInputEditText) rootView.findViewById(R.id.income);
    }

    private void fillData(User user) {
        name.setText(user.name);
        surname.setText(user.surname);
        email.setText(user.email);
        number.setText(user.mobile_number);
        id.setText(user.pin);
        address.setText(user.street_address);
        numberOfChildren.setText(Integer.toString(user.number_of_children));
        income.setText(Integer.toString(user.average_monthly_income));
        birthDate.setText(user.birthdate);
        String[] cities = getResources().getStringArray(R.array.cities_array);
        for (int i = 1; i < cities.length; i++) {
            if(cities[i].compareTo(user.city) == 0) {
                this.cities.setSelection(i);
                break;
            }
        }
        String[] genders = getResources().getStringArray(R.array.genders_array);
        for (int i = 1; i < genders.length; i++) {
            if(genders[i].compareTo(user.sex) == 0) {
                this.genders.setSelection(i);
                break;
            }
        }
        String[] relationships = getResources().getStringArray(R.array.relationships_array);
        for (int i = 1; i < relationships.length; i++) {
            if(relationships[i].compareTo(user.relationship) == 0) {
                this.relationships.setSelection(i);
                break;
            }
        }
    }

    private boolean isInputValid(String name, String surname, String email, String number, String address, String birthdate, String numChildren,
                                 String income, String city, String gender, String relationship, String id) {
        boolean status = true;
        if(name.length() == 0) {
            this.name.setError(getResources().getString(R.string.empty_field_error));
            status = false;
        }
        if(surname.length() == 0) {
            this.surname.setError(getResources().getString(R.string.empty_field_error));
            status = false;
        }
        if(email.length() == 0) {
            this.email.setError(getResources().getString(R.string.empty_field_error));
            status = false;
        }
        if (address.length() == 0) {
            this.address.setError(getResources().getString(R.string.empty_field_error));
            status = false;
        }
        if (birthdate.length() == 0) {
            this.birthDate.setError(getResources().getString(R.string.empty_field_error));
            status = false;
        }
        if (numChildren.length() == 0) {
            this.numberOfChildren.setError(getResources().getString(R.string.empty_field_error));
            status = false;
        }
        if (income.length() == 0) {
            this.income.setError(getResources().getString(R.string.empty_field_error));
            status = false;
        }
        if(id.length() != GlobalVariables.ID_LENGTH) {
            this.id.setError(getResources().getString(R.string.length_error) + GlobalVariables.ID_LENGTH);
            status = false;
        }
        if(number.length() != GlobalVariables.NUMBER_LENGTH) {
            this.number.setError(getResources().getString(R.string.length_error) + GlobalVariables.NUMBER_LENGTH);
            status = false;
        }
        for(int i = 0; i < number.length(); i++) {
            char c = number.charAt(i);
            if (!Character.isDigit(c)) {
                this.number.setError(getResources().getString(R.string.number_error));
                status = false;
            }
        }
        for(int i = 0; i < id.length(); i++) {
            char c = id.charAt(i);
            if (!Character.isDigit(c)) {
                this.id.setError(getResources().getString(R.string.number_error));
                status = false;
            }
        }
        for(int i = 0; i < numChildren.length(); i++) {
            char c = numChildren.charAt(i);
            if (!Character.isDigit(c)) {
                this.numberOfChildren.setError(getResources().getString(R.string.number_error));
                status = false;
            }
        }
        for(int i = 0; i < income.length(); i++) {
            char c = income.charAt(i);
            if (!Character.isDigit(c)) {
                this.income.setError(getResources().getString(R.string.number_error));
                status = false;
            }
        }
        if (!SignUpActivity.isEmailValid(email)) {
            this.email.setError(getResources().getString(R.string.format_error));
            status = false;
        }
        if (city.compareTo(getResources().getStringArray(R.array.cities_array)[0]) == 0) {
            Toast.makeText(getActivity(), getResources().getString(R.string.city_error), Toast.LENGTH_SHORT).show();
            status = false;
        }
        if (gender.compareTo(getResources().getStringArray(R.array.genders_array)[0]) == 0) {
            Toast.makeText(getActivity(), getResources().getString(R.string.gender_error), Toast.LENGTH_SHORT).show();
            status = false;
        }
        if (relationship.compareTo(getResources().getStringArray(R.array.relationships_array)[0]) == 0) {
            Toast.makeText(getActivity(), getResources().getString(R.string.relationship_error), Toast.LENGTH_SHORT).show();
            status = false;
        }
        return status;
    }

    public void saveChanges(User user) {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "მიმდინარეობს ჩატვირთვა", "გთხოვთ დაიცადოთ...",false,false);
        String nameString = name.getText().toString();
        String surnameString = surname.getText().toString();
        String emailString = email.getText().toString();
        String numberString = number.getText().toString();
        String addressString = address.getText().toString();
        String numberOfChildrenString = numberOfChildren.getText().toString();
        String incomeString = income.getText().toString();
        String birthdateString = birthDate.getText().toString();
        String city = cities.getSelectedItem().toString();
        String gender = genders.getSelectedItem().toString();
        String relationship = relationships.getSelectedItem().toString();
        String idString = id.getText().toString();
        if (isInputValid(nameString, surnameString, emailString, numberString, addressString,
                birthdateString, numberOfChildrenString, incomeString, city, gender, relationship, idString)) {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(RetroFitServer.URI)
                    .build();
            RetroFitServer api = adapter.create(RetroFitServer.class);
            final User newUser = new User(nameString, surnameString, user.password, idString, getResources().getString(R.string.country), city, addressString,
                    numberString, gender, birthdateString, relationship, emailString, user.mobile_number, Integer.parseInt(numberOfChildrenString), Integer.parseInt(incomeString), user.money);
            api.addUser(newUser, new Callback<AddUserBody>() {
                @Override
                public void success(AddUserBody response, Response response2) {
                    loading.dismiss();
                    String problem = response.getProblem();
                    if(problem.compareTo(getResources().getString(R.string.success_status)) == 0) {
                        Intent intent = new Intent(getActivity(), AppActivity.class);
                        intent.putExtra("user", newUser);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else {
                        Toast.makeText(getActivity(), problem, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    loading.dismiss();
                    error.printStackTrace();
                }
            });
        }
    }

}
