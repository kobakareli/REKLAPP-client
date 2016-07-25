package com.example.koba.reklappclient;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koba.reklappclient.RequestBodies.AddUserBody;
import com.example.koba.reklappclient.RequestBodies.TransferRequestBody;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Koba on 18/07/2016.
 */
public class UserFragment extends Fragment {

    private TextView balance;
    private Button transferMoney;
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
        AppCompatButton fab = (AppCompatButton) rootView.findViewById(R.id.fab);

        Bundle args = getArguments();
        final User user = args.getParcelable("user");
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.currentFragmentId = GlobalVariables.USER_EDIT_FRAGMENT_ID;
                Fragment newFragment = ((AppActivity) getActivity()).getFragmentById(AppActivity.currentFragmentId, user);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.flContent, newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        initTextViews(rootView, user);

        transferMoney = (Button) rootView.findViewById(R.id.btn_transfer);
        transferMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                final EditText coinbaseEmail = new EditText(getActivity());
                coinbaseEmail.setHint("Enter coinbase email");
                alert.setView(coinbaseEmail);
                alert.setPositiveButton("გადარიცხვა", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String email = coinbaseEmail.getText().toString().trim();
                        if (SignUpActivity.isEmailValid(email)) {
                            RestAdapter adapter = new RestAdapter.Builder()
                                    .setEndpoint(RetroFitServer.URI)
                                    .build();
                            RetroFitServer api = adapter.create(RetroFitServer.class);
                            TransferRequestBody trb = new TransferRequestBody(user.money);
                            api.transferMoney(user.mobile_number, email, trb, new Callback<AddUserBody>() {
                                @Override
                                public void success(AddUserBody addUserBody, Response response) {
                                    String problem = addUserBody.getProblem();
                                    if (problem.compareTo(getResources().getString(R.string.transfer_success_status)) == 0) {
                                        Toast.makeText(getActivity(), "გადარიცხვა დასრულებულია", Toast.LENGTH_SHORT).show();
                                        user.money = 0;
                                        balance.setText(Html.fromHtml("<b>ანგარიში:<b> " + user.money));
                                    }
                                    else {
                                        Toast.makeText(getActivity(), problem, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    error.printStackTrace();
                                }
                            });
                        }
                        Toast.makeText(getActivity(), getResources().getString(R.string.format_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton("გაუქმება", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });


        return rootView;
    }

    private void initTextViews(View rootView, User user) {
        balance = (TextView) rootView.findViewById(R.id.balance);
        balance.setText(Html.fromHtml("<b>ანგარიში:</b >" + user.money));
        name = (TextView) rootView.findViewById(R.id.name);
        name.setText(Html.fromHtml("<b>სახელი:</b> " + user.name));
        surname = (TextView) rootView.findViewById(R.id.surname);
        surname.setText(Html.fromHtml("<b>გვარი:</b> " + user.surname));
        email = (TextView) rootView.findViewById(R.id.email);
        email.setText(Html.fromHtml("<b>ელ. ფოსტა:</b> " + user.email));
        id = (TextView) rootView.findViewById(R.id.pid);
        id.setText(Html.fromHtml("<b>პირადი ნომერი:</b> " + user.pin));
        city = (TextView) rootView.findViewById(R.id.city);
        city.setText(Html.fromHtml("<b>ქალაქი:</b> " + user.city));
        address = (TextView) rootView.findViewById(R.id.address);
        address.setText(Html.fromHtml("<b>მისამართი:</b> " + user.street_address));
        sex = (TextView) rootView.findViewById(R.id.sex);
        sex.setText(Html.fromHtml("<b>სქესი:</b> " + user.sex));
        birthdate = (TextView) rootView.findViewById(R.id.birthdate);
        birthdate.setText(Html.fromHtml("<b>დაბადების თარიღი:</b> " + user.birthdate));
        relationship = (TextView) rootView.findViewById(R.id.relationship);
        relationship.setText(Html.fromHtml("<b>ოჯახური მდგომარეობა:</b> " + user.relationship));
        numberOfChildren = (TextView) rootView.findViewById(R.id.numChildren);
        numberOfChildren.setText(Html.fromHtml("<b>შვილების რაოდენობა:</b> " + Integer.toString(user.number_of_children)));
        averageIncome = (TextView) rootView.findViewById(R.id.income);
        averageIncome.setText(Html.fromHtml("<b>საშუალო თვიური შემოსავალი:</b> " + Integer.toString(user.average_monthly_income)));
    }
}
