package com.abc.example.com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.abc.example.com.myapplication.search.DATABASEHANDLER;

public class update extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        Button b1 = (Button)findViewById(R.id.update);
        b1.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        Intent back = new Intent(getApplicationContext() , afterlogin.class);
        startActivity(back);

    }
}
