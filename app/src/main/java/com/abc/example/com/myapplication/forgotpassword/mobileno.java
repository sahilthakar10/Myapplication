package com.abc.example.com.myapplication.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class mobileno extends AppCompatActivity implements View.OnClickListener {

    EditText mobileno;
    Button submit;
    RequestQueue queue;
    TextView error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobileno);

        mobileno  = (EditText) findViewById(R.id.Mobileno);
        submit = (Button)findViewById(R.id.next);
        error = (TextView)findViewById(R.id.error);


        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (mobileno.getText().toString().equals(""))
        {
            mobileno.setError("Pls complete field");
        }
        else {
            if (queue == null) {
                queue = Volley.newRequestQueue(getApplicationContext());
            }
            String url = util.URL+"checknumberexist.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            try{
                                if (response.equals("1"))
                                {
                                    Intent otp = new Intent(getApplicationContext() , otpcheck.class);
                                    otp.putExtra("number" , mobileno.getText().toString() );



                                    startActivity(otp);
                                }
                                else if(response.equals("2"))
                                {
                                    error.setText("U ARE NOT REGISTERED");
                                }
                                else if(response.equals("3"))
                                {
                                    error.setText("U ARE NOT CUSTOMER OF THIS COMPANY");
                                }
                                else if(response.equals("4"))
                                {
                                    error.setText("U ARE NOT AUTHORIZED TO USE THIS ANDROID APPLICATION ");
                                }

                            }catch (Exception e)
                            {

                            }

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }){
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("number", mobileno.getText().toString());
                    return params;
                }
            };
            queue.add(stringRequest);

        }

    }
}
