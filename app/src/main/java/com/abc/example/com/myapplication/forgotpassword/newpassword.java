package com.abc.example.com.myapplication.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.abc.example.com.myapplication.Signin;
import com.abc.example.com.myapplication.R;
import com.abc.example.com.myapplication.util;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class newpassword extends AppCompatActivity implements View.OnClickListener {

    EditText newpass, confirmpass;
    Button submit;
    RequestQueue queue;
    String phonenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpassword);

        Intent i = getIntent();
        phonenumber = i.getStringExtra("number");


        newpass = (EditText) findViewById(R.id.newpassword);
        confirmpass = (EditText) findViewById(R.id.confirmpassword);
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
     final String newpas =   newpass.getText().toString();
        String cpassword = confirmpass.getText().toString();
        if (newpas.equals("") || cpassword.equals("")) {
            newpass.setError("Please complete field");
            confirmpass.setError("Please complete field");

        } else {

                if (newpas.length() >= 6) {

                    if (newpas.equals(cpassword)) {
                        if (queue == null) {
                            queue = Volley.newRequestQueue(getApplicationContext());
                        }
                        String url = util.URL + "upldatepassword.php";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            if (response.equals("1")) {
                                                Intent i = new Intent(getApplicationContext(), Signin.class);
                                                startActivity(i);
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
                                params.put("number", phonenumber);
                                params.put("password" , newpas);
                                return params;
                            }
                        };


                        queue.add(stringRequest);

                    } else {
                        newpass.setError("password does not match ");
                        confirmpass.setError("password does not match ");

                    }

                } else {
                    newpass.setError("LENGTH SHOULD BE GREATER THAN 6");
                    confirmpass.setError("LENGTH SHOULD BE GREATER THAN 6");

                }

            }

    }
}