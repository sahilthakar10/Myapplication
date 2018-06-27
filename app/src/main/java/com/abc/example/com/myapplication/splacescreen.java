package com.abc.example.com.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class splacescreen extends Activity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splacescreen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                try{
                    SharedPreferences shared = getSharedPreferences("number", Context.MODE_PRIVATE);
                    String sender = shared.getString("unumber", null);

                    SharedPreferences share = getSharedPreferences("check", Context.MODE_PRIVATE);
                    String checked = share.getString("checked", null);

                    if (sender!=null && checked!=null)
                    {
                        Intent i = new Intent(getApplicationContext(), Mainactivity.class);
                        startActivity(i);
                    }

                    else if (sender==null){
                        Intent i = new Intent(getApplicationContext(), Signup.class);
                        startActivity(i);
                    }
                    else if (checked==null){
                        Intent i = new Intent(getApplicationContext(), afterlogin.class);
                        startActivity(i);
                    }


                }catch (Exception e){

                }


                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
