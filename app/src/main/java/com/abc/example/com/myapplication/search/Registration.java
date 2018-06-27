package com.abc.example.com.myapplication.search;

/**
 * Created by ABC on 29-09-2017.
 */

public class Registration {
    private int id;
    private String bodyname;
    private String description;
    public  Registration()
    {

    }
    public Registration(int id , String bodyname , String description ) {
        this.id = id;
        this.bodyname = bodyname;
        this.description = description;
    }

    public Registration(String bodyname , String description)
    {
        this.bodyname = bodyname;
        this.description = description;
    }
    public int getId() {return id;}
    public void setId(int id){this.id = id;}

    public String getBodyname(){ return bodyname;}
    public void setBodyname(String bodyname) {this.bodyname = bodyname;}

    public String getDescription(){ return description;}
    public void setDescription(String description) {this.description = description;}


}
