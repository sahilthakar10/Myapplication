package com.abc.example.com.myapplication;

/**
 * Created by ABC on 01-02-2018.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SONU on 29/10/15.
 */
public class DownloadTask {

    private static final String TAG = "Download Task";
    private Context context;
    private Button buttonText;
    private String downloadUrl = "" ,downloadFileName;
    private TextView progress;
    ProgressDialog loading;
    RequestQueue queue ;
    static int count ;
    static int j;
ProgressDialog pr;
    Handler ph;
    public DownloadTask(){
        j=1;

    }

    public DownloadTask(Context context , Button buttonText , String downloadUrl , String filename , int i, ProgressDialog pr, Handler ph)
    {
        count = i;
        this.context = context;
        this.buttonText = buttonText;
        this.downloadUrl = downloadUrl;
        downloadFileName = filename;
        new DownloadingTask().execute();
        this.pr=pr;
        this.ph=ph;
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            buttonText.setEnabled(false);
          //  loading = ProgressDialog.show(context,"Downloading files...","Please wait...",false,false);
            buttonText.setText(R.string.downloadStarted);//Set Button Text when download started
            //      buttonText.setText(R.string.downloadStarted);//Set Button Text when download started

        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    buttonText.setEnabled(true);
            //       loading.dismiss();

                    j++;
                    if(count+1 == j/2)
                    {
                        Log.e("12345" ,"12345");
                        if(pr!=null)
                        {

                            pr.dismiss();
                        }
                        buttonText.setText(R.string.downloadCompleted);//If Download completed then change button text
                        if (queue == null)
                        {
                            queue = Volley.newRequestQueue(context);
                        }

                        SharedPreferences shared = context.getSharedPreferences("number", Context.MODE_PRIVATE);
                        String sender = shared.getString("unumber", null);

                        String url = util.URL+"updateflag.php?number="+sender;
                         Log.e("url",url);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                            if (response.contains("1"))
                                            {
                                                Intent selectbody = new Intent(context , Mainactivity.class);
                                                context.startActivity(selectbody);

                                                SharedPreferences check = context.getSharedPreferences("check", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor edit = check.edit();
                                                edit.putString("checked", "1");

                                                edit.commit();

                                            }
                                    }

                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                        queue.add(stringRequest);
                    }
              } else {
                    buttonText.setText(R.string.downloadFailed);//If download failed change button text
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buttonText.setEnabled(true);
                            buttonText.setText(R.string.downloadAgain);//Change button text again after 3sec
                        }
                    }, 3000);


                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs
                buttonText.setText(R.string.downloadFailed);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonText.setEnabled(true);
                        buttonText.setText(R.string.downloadAgain);
                    }
                }, 3000);

            }

            super.onPostExecute(result);
        }



        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection
                // getting file length

                int lenghtOfFile = c.getContentLength();

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }
                //Get File if SD card is present

                if (new CheckForSDCard().isSDCardPresent()) {

                    apkStorage = new File(context.getExternalFilesDir(
                            Environment.DIRECTORY_DOCUMENTS),".images");
                } else
                    Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                    outputFile.createNewFile();

               FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection
                Log.e("filelength" , String.valueOf(outputFile.length()));
                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                long total=0;
                while ((len1 = is.read(buffer)) != -1) {
                    total += len1;
                    //fos.write(buffer, 0, len1);//Write new file
                    fos.write(buffer , 0 , len1);

                }
                fos.close();
                is.close();
                if(ph!=null && pr!=null) {
                    ph.post(new Runnable() {
                        public void run() {
                            pr.setProgress(((j/2)*100)/(count+1));
                        }
                    });
                }
            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
            }

            return null;
        }

    }
}
