package com.abc.example.com.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Signin extends AppCompatActivity implements View.OnClickListener {

    EditText password,number;
    Button signin;
    RequestQueue queue;
    TextView error;
    TextView forgotpass;
   String IMEI_Number_Holder;
    TelephonyManager telephonyManager;
    ProgressDialog progressBar;
    public void onBackPressed() {
    Intent sigup = new Intent(getApplicationContext() , Signup.class);
        startActivity(sigup);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        IMEI_Number_Holder = telephonyManager.getDeviceId();


        forgotpass = (TextView)findViewById(R.id.forgotpass);

        number = (EditText)findViewById(R.id.numner);
        password = (EditText)findViewById(R.id.password);
        signin =(Button)findViewById(R.id.signin);
        error = (TextView)findViewById(R.id.error);

        signin.setOnClickListener(this);
        forgotpass.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.signin){
            progressBar = new ProgressDialog(this);
            error.setText("");

            progressBar.setCancelable(false);
            progressBar.setMessage("Loading Please Wait ...");
            progressBar.show();
            if (number.getText().toString().equals("") || password.getText().toString().equals("")){
                number.setError("Please complete the field");
                password.setError("Please complete the field");
                progressBar.dismiss();

            }
            else {
                if (number.getText().toString().length() == 10) {

                    if (password.getText().toString().length() >= 6) {
                        if (queue == null) {
                            queue = Volley.newRequestQueue(getApplicationContext());
                        }
                        String url = util.URL + "login.php";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(final String response) {
                                        try {
                                            if (response.equals("1")) {

                                                SharedPreferences share = getSharedPreferences("check", Context.MODE_PRIVATE);
                                                String checked = share.getString("checked", "");
                                                SharedPreferences shared = getSharedPreferences("number", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = shared.edit();
                                                editor.putString("unumber", number.getText().toString());
                                                editor.commit();


                                                if (checked.equals("1")) {
                                                    progressBar.dismiss();
                                                    Intent i = new Intent(getApplicationContext(), Mainactivity.class);
                                                    startActivity(i);
                                                } else if (checked.equals("")) {
                                                    progressBar.dismiss();

                                                    Intent i = new Intent(getApplicationContext(), afterlogin.class);
                                                    startActivity(i);

                                                }
                                                else {
                                                    progressBar.dismiss();

                                                    Intent i = new Intent(getApplicationContext(), afterlogin.class);
                                                    startActivity(i);

                                                }



                                            }else if (response.equals("5")){
                                                progressBar.dismiss();

                                                error.setText("Invalid imei number please kindly contact to customer care");
                                            }
                                            else if(response.equals("6"))
                                            {
                                                progressBar.dismiss();

                                                error.setText("U ARE NOT AUTHORIZED TO USE THIS ANDROID APPLICATION ");
                                            }
                                            else if(response.equals("7"))
                                            {
                                                progressBar.dismiss();

                                                error.setText("U ARE NOT CUSTOMER OF THIS COMPANY..");
                                            }

                                            else {
                                                signin.setEnabled(true);
                                                error.setText("Invailid number or password");
                                                progressBar.dismiss();
                                            }

                                        } catch (Exception e) {

                                        }

                                    }

                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("number", number.getText().toString());
                                params.put("password", password.getText().toString());
                                params.put("imei", IMEI_Number_Holder);
                                return params;
                            }
                        };
                        queue.add(stringRequest);

                    }
                    else {
                        progressBar.dismiss();
                        password.setError("Password length must be more than 6 digit");
                    }
                }else
                {
                    progressBar.dismiss();
                    number.setError("Mobile no. must be 10 digit");
                }
            }
        }else if (view.getId() == R.id.forgotpass){
            Intent forgotpass = new Intent(getApplicationContext() , com.abc.example.com.myapplication.forgotpassword.mobileno.class);
            startActivity(forgotpass);
        }


    }
}
