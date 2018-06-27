package com.abc.example.com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.example.com.myapplication.search.DATABASEHANDLER;
import com.abc.example.com.myapplication.search.Registration;
import com.abc.example.com.myapplication.search.datalist;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.lukle.clickableareasimage.ClickableArea;
import at.lukle.clickableareasimage.ClickableAreasImage;
import at.lukle.clickableareasimage.OnClickableAreaClickedListener;
import uk.co.senab.photoview.PhotoViewAttacher;

public class Mainactivity extends AppCompatActivity {

    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    /**
     *
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    ListView l1;
    String version;
    RequestQueue queue;
    String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //////////////////////////////////////////////////////////////////////
        SharedPreferences shared =getSharedPreferences("number", Context.MODE_PRIVATE);
        number = shared.getString("unumber", null);


        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }
        String url1 = util.URL+ "sessioncheck.php";
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try{
                            if (response.equals("1"))
                            {
                                Intent i = new Intent(getApplicationContext() , Signin.class);
                                startActivity(i);
                            }
                        }catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("number", number);
                return params;
            }
        };
        queue.add(stringRequest1);

//////////////////////////////////check for update
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }
        String url = util.URL+"checkfordownload.php?number="+number;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try{
                            if (response.equals("1"))
                            {
                                Intent i = new Intent(getApplicationContext() , update.class);
                                startActivity(i);
                            }

                        }catch (Exception e)
                        {

                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements OnClickableAreaClickedListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        ListView l1;EditText e1;String version;
        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            if (getArguments().getInt(ARG_SECTION_NUMBER)==1){
                try{
                    rootView = inflater.inflate(R.layout.fragment_main2, container, false);
                    ImageView front = (ImageView) rootView.findViewById(R.id.imageView);
                    front.setImageResource(R.drawable.male);
                    // Create your image
                    ClickableAreasImage clickableAreasImage = new ClickableAreasImage(new PhotoViewAttacher(front), this);

                    // Define your clickable area (pixel values: x coordinate, y coordinate, width, height) and assign an object to it
                    List<ClickableArea> clickableAreas = getClickableAreas();

                    clickableAreasImage.setClickableAreas(clickableAreas);

                }catch (Exception e){

                }
            }
            else if (getArguments().getInt(ARG_SECTION_NUMBER)==2){
                try {
                    rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    ImageView back = (ImageView) rootView.findViewById(R.id.imageView);
                    back.setImageResource(R.drawable.maleback);
                    // Create your image
                    ClickableAreasImage clickableAreasImage = new ClickableAreasImage(new PhotoViewAttacher(back), this);

                    // Define your clickable area (pixel values: x coordinate, y coordinate, width, height) and assign an object to it
                    List<ClickableArea> clickableAreas = getClickableAreas();

                    clickableAreasImage.setClickableAreas(clickableAreas);

                }catch (Exception e){

                }
            }
            else if (getArguments().getInt(ARG_SECTION_NUMBER)==3){
                    rootView = inflater.inflate(R.layout.fragment_main3 , container , false);
                l1 = (ListView)rootView.findViewById(R.id.listview);
                e1 = (EditText)rootView.findViewById(R.id.search);
                SharedPreferences share = getActivity().getSharedPreferences("version", Context.MODE_PRIVATE);
                version = share.getString("checkversion", "");
                try {
                    String bn = e1.getText().toString();
                    DATABASEHANDLER db = new DATABASEHANDLER(getActivity());
                    List<Registration> contacts = db.getAllData(bn, version);
                    String[] bodyname = new String[contacts.size()];
                    int i = 0;
                    for (Registration cn : contacts) {
                        bodyname[i] = cn.getBodyname();
                        i++;
                        if(i==contacts.size())break;
                    }
                    datalist datalist = new datalist(getActivity() ,bodyname);
                    //l1.setItemsCanFocus(true);
                    l1.setAdapter(datalist);

                }catch (Exception e){

                }
                try{
                    e1.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {}

                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {




                        }

                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            try {
                                String bn = e1.getText().toString();
                                DATABASEHANDLER db = new DATABASEHANDLER(getActivity());
                                List<Registration> contacts = db.getAllData(bn, version);
                                String[] bodyname = new String[contacts.size()];
                                int i = 0;
                                for (Registration cn : contacts) {
                                    bodyname[i] = cn.getBodyname();
                                    i++;
                                    if(i==contacts.size())break;
                                }
                                datalist datalist = new datalist(getActivity() ,bodyname);
                                //l1.setItemsCanFocus(true);
                                l1.setAdapter(datalist);

                            }catch (Exception e){

                            }

                        }
                    });
                }catch (Exception e){

                }

                try {
                    l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String name = parent.getItemAtPosition(position).toString();
                            Intent body = new Intent(getActivity() , webview2.class);
                            body.putExtra("bodypartname" , name );
                            startActivity(body);
                            Toast.makeText(getActivity(), name, Toast.LENGTH_LONG).show();
                        }
                    });
                }catch (Exception e){

                }
            }
            return rootView;
        }


        @Override
        public void onClickableAreaTouched(Object item) {
            if (item instanceof State) {
                String text = ((State) item).getName();
                Intent body = new Intent(getActivity() , Webview.class);
                body.putExtra("bodypartname" , text );
                startActivity(body);
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        }

        @NonNull
        private List<ClickableArea> getClickableAreas() {
            List<ClickableArea> clickableAreas = new ArrayList<>();

            if (getArguments().getInt(ARG_SECTION_NUMBER)==1){
                try {
                    clickableAreas.add(new ClickableArea(198,0,106,129, new State("head")));
                    clickableAreas.add(new ClickableArea(180,130,147,47, new State("neck")));
                    clickableAreas.add(new ClickableArea(341,165,47,87, new State("shoulder")));
                    clickableAreas.add(new ClickableArea(110,165,47,87, new State("shoulder")));
                    clickableAreas.add(new ClickableArea(158,178,182,94, new State("chest")));
                    clickableAreas.add(new ClickableArea(162,273,176,147 , new State("abdomen")));
                    clickableAreas.add(new ClickableArea(202,444,96,330 , new State("pelvis")));
                    clickableAreas.add(new ClickableArea(299,421,47,111 , new State("hips")));
                    clickableAreas.add(new ClickableArea(152,421,47,111 , new State("hips")));
                    clickableAreas.add(new ClickableArea(171,533,157,396 , new State("legs")));
                    clickableAreas.add(new ClickableArea(0,457,75,76 , new State("palms")));
                    clickableAreas.add(new ClickableArea(424,457,75,76 , new State("palms")));
                    clickableAreas.add(new ClickableArea(97,254,56,72 , new State("arms")));
                    clickableAreas.add(new ClickableArea(347,254,56,72 , new State("arms")));
                    clickableAreas.add(new ClickableArea(64,327,72,82 , new State("arms")));
                    clickableAreas.add(new ClickableArea(365,327,72,82 , new State("arms")));
                    clickableAreas.add(new ClickableArea(44,410,66,44 , new State("arms")));
                    clickableAreas.add(new ClickableArea(390,410,66,44 , new State("arms")));
                    clickableAreas.add(new ClickableArea(178,930,146,47 , new State("feet")));
                }catch (Exception e){
                }
            }
            else if (getArguments().getInt(ARG_SECTION_NUMBER)==2){
                try {
                    clickableAreas.add(new ClickableArea(198,0,106,129, new State("backhead")));
                    clickableAreas.add(new ClickableArea(180,130,147,47, new State("backcervical")));
                    clickableAreas.add(new ClickableArea(341,165,47,87, new State("backshoulder")));
                    clickableAreas.add(new ClickableArea(110,165,47,87, new State("backshoulder")));
                    clickableAreas.add(new ClickableArea(158,178,182,94, new State("upperback")));
                    clickableAreas.add(new ClickableArea(162,273,176,147 , new State("lowerback")));
                    clickableAreas.add(new ClickableArea(202,444,96,330 , new State("backbuttocks")));
                    clickableAreas.add(new ClickableArea(299,421,47,111 , new State("backhips")));
                    clickableAreas.add(new ClickableArea(152,421,47,111 , new State("backhips")));
                    clickableAreas.add(new ClickableArea(171,533,157,444 , new State("backlegs")));
                    clickableAreas.add(new ClickableArea(0,457,75,76 , new State("backpalms")));
                    clickableAreas.add(new ClickableArea(424,457,75,76 , new State("backpalms")));
                    clickableAreas.add(new ClickableArea(97,254,56,72 , new State("backarms")));
                    clickableAreas.add(new ClickableArea(347,254,56,72 , new State("backarms")));
                    clickableAreas.add(new ClickableArea(64,327,72,82 , new State("backarms")));
                    clickableAreas.add(new ClickableArea(365,327,72,82 , new State("backarms")));
                    clickableAreas.add(new ClickableArea(44,410,66,44 , new State("backarms")));
                    clickableAreas.add(new ClickableArea(390,410,66,44 , new State("backarms")));
                    clickableAreas.add(new ClickableArea(178,930,146,47 , new State("backfeet")));

                }catch (Exception e){

                }

            }
            return clickableAreas;

        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            return super.onOptionsItemSelected(item);

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "FRONT BODY";
                case 1:
                    return "BACK BODY";
                case 2:
                    return "SEARCH";
            }
            return null;
        }
    }
}
