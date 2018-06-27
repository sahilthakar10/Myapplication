package com.abc.example.com.myapplication.search;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.abc.example.com.myapplication.R;

public class datalist extends ArrayAdapter<String> {

    private String[] bodyname;
    private Activity context;
    public datalist(Activity context ,  String[] bodyname) {
        super(context, R.layout.listtext,bodyname);
        this.context = context;
        this.bodyname = bodyname;
  //      Log.e("notification" , String.valueOf(this.bodyname));
    }
    @Override
    public View getView(int position , View convertView , ViewGroup parent)
    {
            LayoutInflater inflater=context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.listtext,null,true);
            TextView textViewContact = (TextView)listViewItem.findViewById(R.id.text);
            textViewContact.setText(bodyname[position].trim());
            return listViewItem;
    }
}
