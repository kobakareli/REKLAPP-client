package com.example.koba.reklappclient;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koba.reklappclient.RequestBodies.AddUserBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Koba on 13/07/2016.
 */
public class SignUpActivity extends AppCompatActivity{

    private TextView signin;
    private TextInputEditText birthDate;
    private Spinner cities;
    private Spinner genders;
    private Spinner relationships;
    private TextInputEditText name;
    private TextInputEditText surname;
    private TextInputEditText number;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText id;
    private TextInputEditText address;
    private TextInputEditText numberOfChildren;
    private TextInputEditText income;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("რეგისტრაცია");

        setUpSpinners();
        setUpDatePicker();
        setUpEditTexts();

        Button register = (Button) findViewById(R.id.btn_signup);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog loading = ProgressDialog.show(SignUpActivity.this, "მიმდინარეობს ჩატვირთვა", "გთხოვთ დაიცადოთ...",false,false);
                String nameString = name.getText().toString();
                String surnameString = surname.getText().toString();
                String numberString = number.getText().toString();
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                String addressString = address.getText().toString();
                String numberOfChildrenString = numberOfChildren.getText().toString();
                if (numberOfChildrenString.compareTo("") == 0) {
                    numberOfChildrenString = "-1";
                }
                String incomeString = income.getText().toString();
                if (incomeString.compareTo("") == 0) {
                    incomeString = "-1";
                }
                String birthdateString = birthDate.getText().toString();
                String city = cities.getSelectedItem().toString();
                String gender = genders.getSelectedItem().toString();
                String relationship = relationships.getSelectedItem().toString();
                String idString = id.getText().toString();
                if (isInputValid(nameString, surnameString, numberString, emailString, passwordString, addressString,
                        birthdateString, numberOfChildrenString, incomeString, city, gender, relationship, idString)) {
                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint(RetroFitServer.URI)
                            .build();
                    RetroFitServer api = adapter.create(RetroFitServer.class);
                    final User user = new User(nameString, surnameString, MainActivity.hashPassword(passwordString), idString, "საქართველო", city, addressString,
                            numberString, gender, birthdateString, relationship, emailString, "", Integer.parseInt(numberOfChildrenString), Integer.parseInt(incomeString), 0.0);
                    api.addUser(user, new Callback<AddUserBody>() {
                        @Override
                        public void success(AddUserBody response, Response response2) {
                            if (response != null && response.getProblem().compareTo(getResources().getString(R.string.success_status)) == 0) {
                                loading.dismiss();
                                Intent intent = new Intent(SignUpActivity.this, AppActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                loading.dismiss();
                                Toast.makeText(SignUpActivity.this, response.getProblem(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            loading.dismiss();
                            error.printStackTrace();
                        }
                    });
                }
                else {
                    loading.dismiss();
                }
            }
        });


        signin = (TextView) findViewById(R.id.sign_in);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpSpinners() {
        cities = (Spinner) findViewById(R.id.city_spinner);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.cities_array, android.R.layout.simple_spinner_item);
        cities.setAdapter(cityAdapter);

        genders = (Spinner) findViewById(R.id.sex_spinner);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.genders_array, android.R.layout.simple_spinner_item);
        genders.setAdapter(genderAdapter);


        relationships = (Spinner) findViewById(R.id.relationship_spinner);
        ArrayAdapter<CharSequence> relationshipAdapter = ArrayAdapter.createFromResource(this,
                R.array.relationships_array, android.R.layout.simple_spinner_item);
        relationships.setAdapter(relationshipAdapter);
    }

    private void setUpDatePicker() {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog birthDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                birthDate.setText(formatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        birthDate = (TextInputEditText) findViewById(R.id.birthdate);
        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthDatePicker.show();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(birthDate.getWindowToken(), 0);
            }
        });
        birthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    birthDatePicker.show();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(birthDate.getWindowToken(), 0);
                }
                v.clearFocus();
            }
        });
    }

    private void setUpEditTexts() {
        name = (TextInputEditText) findViewById(R.id.name);
        surname = (TextInputEditText) findViewById(R.id.surname);
        number = (TextInputEditText) findViewById(R.id.number);
        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        id = (TextInputEditText) findViewById(R.id.pid);
        address = (TextInputEditText) findViewById(R.id.address);
        numberOfChildren = (TextInputEditText) findViewById(R.id.numChildren);
        income = (TextInputEditText) findViewById(R.id.income);
    }

    private boolean isInputValid(String name, String surname, String number, String email, String password, String address, String birthdate, String numChildren,
                                 String income, String city, String gender, String relationship, String id) {
        boolean status = true;
        if(password.length() < 6 || password.length() > 20) {
            this.password.setError(getResources().getString(R.string.password_length_error));
            status = false;
        }
        if(id.length() != 0 && id.length() != GlobalVariables.ID_LENGTH) {
            this.id.setError(getResources().getString(R.string.length_error) + GlobalVariables.ID_LENGTH);
            status = false;
        }
        if(id.length() != 0) {
            for(int i = 0; i < id.length(); i++) {
                char c = id.charAt(i);
                if (!Character.isDigit(c)) {
                    this.id.setError(getResources().getString(R.string.number_error));
                    status = false;
                }
            }
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
        if (email.length() != 0 && !isEmailValid(email)) {
            this.email.setError(getResources().getString(R.string.format_error));
            status = false;
        }
        return status;
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
