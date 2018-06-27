package com.abc.example.com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Webview extends AppCompatActivity {

    WebView webView;
    String name;
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
        else{
            Intent front = new Intent(getApplicationContext() , Mainactivity.class);
            startActivity(front);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_webview);
        Intent i = getIntent();
        name = i.getStringExtra("bodypartname");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        webView = (WebView)findViewById(R.id.webview);
        WebSettings webSetting = webView.getSettings();
        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptEnabled(true);
        String path = getApplicationContext().getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS) + "/.project/"+name+".html";
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file://"+path);


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
        if (id == R.id.Logout) {
            SharedPreferences shared = getSharedPreferences("number", Context.MODE_PRIVATE);
            shared.edit().remove("unumber").commit();
            Intent sigin = new Intent(getApplicationContext() , Signin.class);
            startActivity(sigin);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
