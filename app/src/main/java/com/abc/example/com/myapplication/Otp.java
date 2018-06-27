package com.abc.example.com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

public class Otp extends AppCompatActivity implements View.OnClickListener {

    int count=1,counter=90;
    TextView timer,error;
    String phonenumber , password;
    RequestQueue queue;
    EditText otp;
    Button submit , resend;
    String IMEI_Number_Holder;
    TelephonyManager telephonyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        IMEI_Number_Holder = telephonyManager.getDeviceId();

         timer = (TextView)findViewById(R.id.timer);
        otp = (EditText)findViewById(R.id.otp);
        submit = (Button)findViewById(R.id.send);
        resend = (Button)findViewById(R.id.resend);
        error = (TextView)findViewById(R.id.error);

        resend.setEnabled(false);

        submit.setOnClickListener(this);
        resend.setOnClickListener(this);

        Intent i = getIntent();
        phonenumber = i.getStringExtra("number");
        password = i.getStringExtra("password");
        new CountDownTimer(90000, 1000){
            public void onTick(long millisUntilFinished){
                timer.setText("00:"+ String.valueOf(counter));
                counter--;

            }
            public  void onFinish(){
                timer.setText("00:00");
                resend.setEnabled(true);
            }
        }.start();
            if (queue == null) {
                queue = Volley.newRequestQueue(getApplicationContext());
            }
            String url = util.URL+"isotp.php?number="+phonenumber;
            Log.e("url",url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            try{
                                if (response.equals("1"))
                                {
                                    resend.setEnabled(false);
                                }
                                else
                                {
                                   error.setText("OTP SENDED TO YOUR MOBILE PHONE");


                                }
                            }catch (Exception e)
                            {

                            }

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            } ){
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("number", phonenumber);
                    return params;
                }
            };
            queue.add(stringRequest);


    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.send)
        {
            if (otp.getText().toString().equals("0")){
                otp.setError("Pls complete this field");
            }else {
                if (queue == null) {
                    queue = Volley.newRequestQueue(getApplicationContext());
                }
                String url = util.URL+"checkotp.php";
                Log.e("url",url);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String response) {
                                try{
                                    if (response.equals("1"))
                                    {
                                        resend.setEnabled(false);
                                        error.setText("YOU ARE SUCCESSFULLY REGISTERD");
                                        timer.setVisibility(View.GONE);
                                        Intent i = new Intent(getApplicationContext() , Signin.class);
                                        startActivity(i);
                                    }
                                    else
                                    {
                                        error.setText("Pls check your otp");
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
                    {Map<String, String> params = new HashMap<String, String>();
                        params.put("number", phonenumber);
                        params.put("otp",otp.getText().toString());
                        params.put("password" , password);
                        params.put("imei",IMEI_Number_Holder);
                        return params;
                    }
                };
                queue.add(stringRequest);

            }

        }
        if (count>3)
        {
            submit.setEnabled(false);
            resend.setEnabled(false);

            if (queue == null) {
                queue = Volley.newRequestQueue(getApplicationContext());
            }
            String url = util.URL+"block.php?number="+phonenumber;
            Log.e("url",url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            try{
                                if (response.equals("1"))
                                {
                                    resend.setEnabled(false);
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
                    params.put("number", phonenumber);
                    return params;
                }
            };
            queue.add(stringRequest);

        }
        else if (view.getId() == R.id.resend)
        {
            count++;

            if (queue == null) {
                queue = Volley.newRequestQueue(getApplicationContext());
            }
            String url = util.URL+"resend.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            try{
                                if (response.equals("1"))
                                {
                                    resend.setEnabled(false);
                                    submit.setEnabled(false);
                                }
                                else
                                {
                                    resend.setEnabled(false);
                                    error.setText("OTP SENDED TO YOUR MOBILE PHONE");
                                    counter = 90;
                                    new CountDownTimer(90000, 1000){
                                        public void onTick(long millisUntilFinished){
                                            timer.setText("00:"+ String.valueOf(counter));
                                            counter--;

                                        }
                                        public  void onFinish(){
                                            resend.setEnabled(true);
                                            timer.setText("00:00");
                                        }
                                    }.start();

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
                    params.put("number", phonenumber);
                    return params;
                }
            };
            queue.add(stringRequest);

        }
    }
}
