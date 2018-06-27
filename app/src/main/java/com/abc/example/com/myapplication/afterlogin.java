package com.abc.example.com.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.abc.example.com.myapplication.search.DATABASEHANDLER;
import com.abc.example.com.myapplication.search.Registration;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class afterlogin extends AppCompatActivity implements View.OnClickListener {

    RequestQueue queue;
    String image[] , name[] , description[] , bodyname[]  , httpurl[] ;
    Button next;
    int a=0;
    StringBuilder head , neck ,shoulder , chest , abdomen , arms , pelvis , hips , legs , palms , feet , backhead,backcervical , backshoulder, upperback , lowerback , backarms , backpalms , backbuttocks , backhips,backlegs , backfeet;
    String des;
    File apkStorage;
   // ProgressDialog loading;

   ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    Button b1;
    File outputFile;
    String upperpart = "<html>\n" +
            "<head>\n" +
            "<title>Parts</title>\n" +
            "<style>\n"+"a{\n" +
            "text-decoration:none;\n" +
            "color: inherit;}" +
            "\n" +
            ".list-group-item\n" +
            "{position:relative;display:block;padding:10px 15px;margin-bottom:-1px;background-color:#fff;border:1px solid #ddd}\n" +
            "\n" +
            ".list-group-item.active,.list-group-item.active:focus,.list-group-item.active:hover\n" +
            "{z-index:2;color:#fff;background-color:#337ab7;border-color:#337ab7}\n" +
            "\n" +
            ".list-group-item.active .list-group-item-heading,.list-group-item.active .list-group-item-heading>.small,.list-group-item.active .list-group-item-heading>small,.list-group-item.active:focus .list-group-item-heading,.list-group-item.active:focus .list-group-item-heading>.small,.list-group-item.active:focus .list-group-item-heading>small,.list-group-item.active:hover .list-group-item-heading,.list-group-item.active:hover .list-group-item-heading>.small,.list-group-item.active:hover .list-group-item-heading>small{color:inherit}\n" +
            "\n" +
            ".list-group-item.active .list-group-item-text,.list-group-item.active:focus .list-group-item-text,.list-group-item.active:hover .list-group-item-text\n" +
            "{color:#c7ddef}\n" +
            "\n" +
            "</style>\n" +
            "</head>\n" +
            "<body>\n";

    String lowerpart = "\n" +
            "</body>\n" +
            "</html>";

        DownloadTask i ;
    String version;
    static int count;
    public void onBackPressed() {
     //   Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterlogin);

        try{
            DATABASEHANDLER db = new DATABASEHANDLER(getApplicationContext());
            db.truncate();

        }catch (Exception e){
            Toast.makeText(getApplicationContext() , e.getMessage() , Toast.LENGTH_LONG).show();
        }

        next = (Button)findViewById(R.id.next);
        next.setOnClickListener(this);
        stringbuilder();
        i = new DownloadTask();
        upperpart();
        h1();
        getfiles();
    }

    private void getfiles(){
        class GetUrls extends AsyncTask<Void,Void,Void> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
        //        loading = ProgressDialog.show(afterlogin.this,"Downloading files...","Please wait...",false,false);
       //         Log.e("loading" , "loading");
            }
            @Override
            protected Void doInBackground(Void... voids) {
                fetchalldata();
                return null;
            }
            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
            }
        }
        GetUrls gu = new GetUrls();
        gu.execute();
    }

//////////////////////////////////click listner
    @Override
    public void onClick(View view) {
        if (isConnectingToInternet()) {


           imagedownload();
            lowerpart();
            passdatatocreatefiles();
            //loading = ProgressDialog.show(getApplicationContext(),"Downloading files...","Please wait...",false,false);
        }
        else
            Toast.makeText(afterlogin.this, "Oops!! There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();
    }
///////////////////////////check internet
    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
   //////////////////////////////imagedownload method
    private void imagedownload()
    {
        progressBar = new ProgressDialog(this);

        progressBar.setCancelable(false);
        progressBar.setMessage("File downloading ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);


        progressBarStatus = 0;

        try {
        for (a = 0; a < httpurl.length; a++) {
            //   Log.e("a", String.valueOf(a));


            try {
                new DownloadTask(afterlogin.this, next, httpurl[a], image[a].substring(7), a,progressBar,progressBarHandler);

            } catch (Exception e) {
         //       Log.e("error" , e.getMessage());

            }



        }
            progressBar.show();
    }catch (Exception e)
    {
    }


    }
    private void fetchalldata()
    {
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }
        String url = util.URL+"jsonurl.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray ar = new JSONArray(response);
                            image = new String[ar.length()];
                            name = new String[ar.length()];
                            description = new String[ar.length()];
                            httpurl = new String[ar.length()];
                            bodyname = new String[ar.length()];
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject out = ar.getJSONObject(i);
                                image[i] = out.getString("image");
                                name[i] = out.getString("name");
                                bodyname[i] = out.getString("bodyname");
                                description[i] = out.getString("description");
                                SharedPreferences share = getSharedPreferences("version", Context.MODE_PRIVATE);
                                version = share.getString("checkversion", "");

                                DATABASEHANDLER db = new DATABASEHANDLER(afterlogin.this);
                                    Registration obj = new Registration(bodyname[i], description[i]);
                                    db.addDATA(obj);
                                ////////////////////////Make body parts html file
                                seperatedata(bodyname[i] , name[i] , bodyname[i]);
                                ////////////////////////make url to download image
                                httpurl[i] =util.imageurl+"/"+ out.getString("image");
                                //////////////////////////////////////////////////////
                                /////////////////make 123 html file
                                des = "<html>\n" +
                                        "<head>\n" +
                                        "<style>\n" +
                                        ".image{\n" +
                                        "padding:7px;\n" +
                                        "}\n" +
                                        "\n" +
                                        "</style>\n" +
                                        "\n" +
                                        "</head>\n" +
                                        " \n" +
                                        "<body>\n" +
                                        "\n" +
                                        "<div class='image'>\n" +
                                        "\n" +
                                        "<img class='image' src='"+getApplicationContext().getExternalFilesDir(
                                        Environment.DIRECTORY_DOCUMENTS)+"/.images/"+image[i].substring(7)+"'>\n" +
                                        "<p>"+description[i]+"</p>\n" +
                                        "</div>\n" +
                                        "\n" +
                                        "</body>\n" +
                                        "\n" +
                                        "</html>\n" +
                                        "\n";
                                file(des , bodyname[i]);
                                //////////////////////////////
                            }
                        } catch (JSONException ex) {
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);

    }

    private void seperatedata(String bodyparts , String name , String image)
    {
        try{
            if(name.contains("01"))
            {
                head.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");

            }else if (name.contains("02"))
            {
                neck.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "></a>" + "\n");
            }else if (name.contains("03"))
            {
                shoulder.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("04"))
            {
                chest.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("05"))
            {
                abdomen.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("06"))
            {
                arms.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("07"))
            {
                palms.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("08"))
            {
                pelvis.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("09"))
            {
                hips.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("10"))
            {
                legs.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("11"))
            {
                feet.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("12"))
            {
                backhead.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("13"))
            {
                backcervical.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("14"))
            {
                backshoulder.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("15"))
            {
                upperback.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("16"))
            {
                lowerback.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("17"))
            {
                backarms.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("18"))
            {
                backpalms.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("19"))
            {
                backbuttocks.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("20"))
            {
                backhips.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("21"))
            {
                backlegs.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }else if (name.contains("22"))
            {
                backfeet.append("<a class='list-group-item' href='" + getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+ "/.test/"+ image + ".html'>" + bodyparts + "</a>" + "\n");
            }
        }catch (Exception e)
        {
        }

    }
    private void createfiles(String data , String name)
    {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                apkStorage = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), ".project" );
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    //Log.e(TAG, "Directory Created.");
                }
                else
                {
              //      Toast.makeText(getApplicationContext(), "Oops!! File not created.", Toast.LENGTH_SHORT).show();
                }
                outputFile = new File(apkStorage, name+".html");//Create Output file in Main File
                //Create New File if not present
                try {
                    outputFile.createNewFile();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
                // Log.e(TAG, "File Created");
                try {
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    fos.write(data.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "catch false", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Oops!! .", Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(getApplicationContext(), "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

        }catch (Exception e)
        {
            Log.e("errortocreate" , e.getMessage());
        }

    }
    private void stringbuilder()
    {
        try {
            head = new StringBuilder();
            neck= new StringBuilder();
            shoulder= new StringBuilder();
            chest = new StringBuilder();
            abdomen = new StringBuilder();
            arms = new StringBuilder();
            pelvis = new StringBuilder();
            hips = new StringBuilder();
            legs = new StringBuilder();
            palms = new StringBuilder();
            feet = new StringBuilder();
            backhead= new StringBuilder();
            backcervical  = new StringBuilder();
            backshoulder= new StringBuilder();
            upperback = new StringBuilder();
            lowerback = new StringBuilder();
            backarms = new StringBuilder();
            backpalms = new StringBuilder();
            backbuttocks = new StringBuilder();
            backhips = new StringBuilder();
            backlegs = new StringBuilder();
            backfeet= new StringBuilder();

        }catch (Exception e)
        {
        }
    }

    private void upperpart()
    {
        try {
            head.append(upperpart);
            neck.append(upperpart);
            shoulder.append(upperpart);
            chest.append(upperpart);
            abdomen.append(upperpart);
            arms.append(upperpart);
            pelvis.append(upperpart);
            hips.append(upperpart);
            legs.append(upperpart);
            palms.append(upperpart);
            feet.append(upperpart);
            backhead.append(upperpart);
            backcervical.append(upperpart);
            backshoulder.append(upperpart);
            upperback.append(upperpart);
            lowerback.append(upperpart);
            backarms.append(upperpart);
            backpalms.append(upperpart);
            backbuttocks.append(upperpart);
            backhips.append(upperpart);
            backlegs.append(upperpart);
            backfeet.append(upperpart);

        }catch (Exception e)
        {
        }

    }
    private void lowerpart()
    {
        try {
            head.append(lowerpart);
            neck.append(lowerpart);
            shoulder.append(lowerpart);
            chest.append(lowerpart);
            abdomen.append(lowerpart);
            arms.append(lowerpart);
            pelvis.append(lowerpart);
            hips.append(lowerpart);
            legs.append(lowerpart);
            palms.append(lowerpart);
            feet.append(lowerpart);
            backhead.append(lowerpart);
            backcervical.append(lowerpart);
            backshoulder.append(lowerpart);
            upperback.append(lowerpart);
            lowerback.append(lowerpart);
            backarms.append(lowerpart);
            backpalms.append(lowerpart);
            backhips.append(lowerpart);
            backbuttocks.append(lowerpart);
            backlegs.append(lowerpart);
            backfeet.append(lowerpart);

        }catch (Exception e)
        {
        }

    }
    private void h1()
    {
        try
        {
            head.append("<ul class='list-group-item active'>All Symptoms of the head</ul>");
            neck.append("<ul class='list-group-item active'>All Symptoms of the neck</ul>");
            shoulder.append("<ul class='list-group-item active'>All Symptoms of the shoulder</ul>");
            chest.append("<ul class='list-group-item active'>All Symptoms of the chest</ul>");
            abdomen.append("<ul class='list-group-item active'>All Symptoms of the addomen</ul>");
            arms.append("<ul class='list-group-item active'>All Symptoms of the arms</ul>");
            pelvis.append("<ul class='list-group-item active'>All Symptoms of the pelvis</ul>");
            hips.append("<ul class='list-group-item active'>All Symptoms of the hips</ul>");
            legs.append("<ul class='list-group-item active'>All Symptoms of the legs</ul>");
            palms.append("<ul class='list-group-item active'>All Symptoms of the palms</ul>");
            feet.append("<ul class='list-group-item active'>All Symptoms of the feet</ul>");
            backhead.append("<ul class='list-group-item active'>All Symptoms of the head</ul>");
            backcervical.append("<ul class='list-group-item active'>All Symptoms of the backcervical</ul>");
            backshoulder.append("<ul class='list-group-item active'>All Symptoms of the backshoulder</ul>");
            upperback.append("<ul class='list-group-item active'>All Symptoms of the upperback</ul>");
            lowerback.append("<ul class='list-group-item active'>All Symptoms of the lowerback</ul>");
            backarms.append("<ul class='list-group-item active'>All Symptoms of the backarms</ul>");
            backpalms.append("<ul class='list-group-item active'>All Symptoms of the backpalms</ul>");
            backhips.append("<ul class='list-group-item active'>All Symptoms of the backhips</ul>");
            backbuttocks.append("<ul class='list-group-item active'>All Symptoms of the backbuttocks</ul>");
            backlegs.append("<ul class='list-group-item active'>All Symptoms of the backlegs</ul>");
            backfeet.append("<ul class='list-group-item active'>All Symptoms of the backfeet</ul>");

        }catch (Exception e)
        {
        }

    }

    private void passdatatocreatefiles()
    {
        try {
            createfiles(String.valueOf(head) , "head");
            createfiles(String.valueOf(neck) , "neck");
            createfiles(String.valueOf(shoulder) , "shoulder");
            createfiles(String.valueOf(chest) , "chest");
            createfiles(String.valueOf(abdomen) , "abdomen");
            createfiles(String.valueOf(arms) , "arms");
            createfiles(String.valueOf(backarms)  , "backarms");
            createfiles(String.valueOf(pelvis) , "pelvis");
            createfiles(String.valueOf(hips) , "hips");
            createfiles(String.valueOf(legs) , "legs");
            createfiles(String.valueOf(palms) , "palms");
            createfiles(String.valueOf(backpalms) , "backpalms");
            createfiles(String.valueOf(feet) , "feet");
            createfiles(String.valueOf(backhead) , "backhead");
            createfiles(String.valueOf(backcervical) , "backcervical");
            createfiles(String.valueOf(backshoulder) , "backshoulder");
            createfiles(String.valueOf(upperback) , "upperback");
            createfiles(String.valueOf(lowerback) , "lowerback");
            createfiles(String.valueOf(backbuttocks) , "backbuttocks");
            createfiles(String.valueOf(backhips) , "backhips");
            createfiles(String.valueOf(backlegs) , "backlegs");
            createfiles(String.valueOf(backfeet) , "backfeet");

        }catch (Exception e)
        {
        }
    }


    //////////////////////////////////passing data to create 123 html files

    private void file(String des, String image)
    {
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                apkStorage = new File(getApplicationContext().getExternalFilesDir(
                        Environment.DIRECTORY_DOCUMENTS), ".test" );
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    //          Log.e(TAG, "Directory Created.");
                }
                else
                {
                    //         Toast.makeText(getApplicationContext(), "Oops!! File not created.", Toast.LENGTH_SHORT).show();
                }
                outputFile = new File(apkStorage, image+".html");//Create Output file in Main File
                //Create New File if not present
                ///if (!outputFile.exists()) {
                try {
                    outputFile.createNewFile();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
                // Log.e(TAG, "File Created");
                // }
                try {
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    fos.write(des.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "catch false", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Oops!! .", Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(getApplicationContext(), "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

        }catch (Exception e)
        {
        }
    }
}
