package com.example.neha.myapplication.Controller;

import android.app.Application;
import android.content.Context;

import com.example.neha.myapplication.Volley.VolleyObject;

public class MainApplication extends Application {


    public static Context mcontext;

    @Override
    public void onCreate() {
        super.onCreate();
        mcontext = this;
        VolleyObject.initialize(mcontext);
    }



}
