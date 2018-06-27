package com.abc.example.com.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    EditText ephone , epassword , ecpassword;
    Button signup;
    RequestQueue queue;
    TextView error , login;
    ProgressDialog progressBar;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;


    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        login = (TextView)findViewById(R.id.login);
        ephone = (EditText)findViewById(R.id.ephonenumber);
        epassword = (EditText)findViewById(R.id.epassword);
        ecpassword = (EditText)findViewById(R.id.ecpassword);
        signup = (Button)findViewById(R.id.bsignup);
        error = (TextView)findViewById(R.id.error);
        SharedPreferences shared = getSharedPreferences("number", Context.MODE_PRIVATE);
        String sender = shared.getString("unumber", null);
        error.setText(sender);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);

        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);


        if (ActivityCompat.checkSelfPermission(Signup.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Signup.this, Manifest.permission.READ_PHONE_STATE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Signup.this, new String[]{Manifest.permission.READ_PHONE_STATE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant PHONE STATE", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(Signup.this, new String[]{Manifest.permission.READ_PHONE_STATE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }


            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.READ_PHONE_STATE,true);
            editor.commit();




        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bsignup)
        {

            final String phonenumber = ephone.getText().toString();
            final String password = epassword.getText().toString();
            String cpassword = ecpassword.getText().toString();
            progressBar = new ProgressDialog(this);
            progressBar.setCancelable(false);
            progressBar.setMessage("Loading Please Wait ...");
            progressBar.show();

            if (phonenumber.equals("") || password.equals("") || cpassword.equals(""))
            {
                ephone.setError("Please complete field");
                epassword.setError("Please complete field");
                ecpassword.setError("Please complete field");
                progressBar.dismiss();
            }
            else {
                if (phonenumber.length() == 10)
                {

                    if (password.length()>=6)
                    {

                        if (password.equals(cpassword) )
                        {
                            if (queue == null) {
                                queue = Volley.newRequestQueue(getApplicationContext());
                            }
                            String url = util.URL+"customer.php";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try{
                                                if (response.equals("1"))
                                                {
                                                    progressBar.dismiss();

                                                    Intent i = new Intent(getApplicationContext(), Otp.class);
                                                    i.putExtra("number" , ephone.getText().toString());
                                                    i.putExtra("password" , epassword.getText().toString());
                                                    startActivity(i);
                                                }
                                                else if (response.equals("2"))
                                                {
                                                    progressBar.dismiss();

                                                    error.setText("U R ALREADY REGISTERED");
                                                }
                                                else if (response.equals("3"))
                                                {

                                                    progressBar.dismiss();
                                                    error.setText("U ARE NOT CUSTOMER OF THIS COMPANY");
                                                }
                                                else if(response.equals("5"))
                                                {
                                                    progressBar.dismiss();
                                                    error.setText("U ARE NOT AUTHORIZED TO USE THIS ANDROID APPLICATION");

                                                }
                                                else
                                                {
                                                    progressBar.dismiss();
                                                    error.setText("SOMETHING WENT WRONG");
                                                }
                                            }catch (Exception e)
                                            {

                                            }

                                        }

                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            } ) {
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
                        else{
                            progressBar.dismiss();
                            epassword.setError("password does not match ");
                            ecpassword.setError("password does not match ");

                        }

                    }
                    else {
                        progressBar.dismiss();
                        epassword.setError("LENGTH SHOULD BE GREATER THAN 6");
                        ecpassword.setError("LENGTH SHOULD BE GREATER THAN 6");

                    }

                }
                else
                {
                    progressBar.dismiss();
                    ephone.setError("Please enter 10 digit number");
                }
            }

        }else  if (view.getId() == R.id.login)
        {
            Intent signin = new Intent(getApplicationContext() , Signin.class);
            startActivity(signin);
        }


    }

    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        Toast.makeText(getBaseContext(), "We got the Storage Permission", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(Signup.this, Manifest.permission.READ_PHONE_STATE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(Signup.this, new String[]{Manifest.permission.READ_PHONE_STATE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(Signup.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(Signup.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }


}

