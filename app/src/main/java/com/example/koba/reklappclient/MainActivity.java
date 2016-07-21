package com.example.koba.reklappclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private final int NUMBER_LENGTH = 9;

    private TextView signup;
    private TextInputEditText number;
    private TextInputEditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        signup = (TextView) findViewById(R.id.sign_up);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        number = (TextInputEditText) findViewById(R.id.input_number);
        password = (TextInputEditText) findViewById(R.id.input_password);

        Button b = (Button) findViewById(R.id.btn_login);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberString = number.getText().toString();
                String passwordString = password.getText().toString();
                if (isInputValid(numberString, passwordString)) {
                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint(RetroFitServer.URI)
                            .build();
                    RetroFitServer api = adapter.create(RetroFitServer.class);
                    api.getUserByLogin(number.getText().toString(), hashPassword(passwordString), new Callback<User>() {
                        @Override
                        public void success(User user, Response response) {
                            if (user == null || user.mobile_number == "") {
                                Toast.makeText(MainActivity.this, "მომხმარებელი აღნიშნული ნომრით და პაროლით ვერ მოიძებნა", Toast.LENGTH_SHORT);
                                return;
                            }
                            Intent intent = new Intent(MainActivity.this, AppActivity.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private boolean isInputValid(String numberString, String passwordString) {
        boolean status = true;
        if (numberString.length() != NUMBER_LENGTH) {
            this.number.setError("სიგრძე უნდა იყოს " + NUMBER_LENGTH);
            status = false;
        }
        for(int i = 0; i < numberString.length(); i++) {
            char c = numberString.charAt(i);
            if (!Character.isDigit(c)) {
                this.number.setError("შეიყვანეთ მხოლოდ ციფრები");
                status = false;
            }
        }
        if (passwordString.length() == 0) {
            this.password.setError("პაროლი არაა შეყვანილი");
            status = false;
        }
        return status;
    }

    private String hashPassword(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new String(md.digest(password.getBytes()));
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
