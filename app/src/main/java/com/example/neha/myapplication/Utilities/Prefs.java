package com.example.neha.myapplication.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public final class Prefs {

    public static SharedPreferences get(Context context){
        return context.getSharedPreferences("geniogames_2018" , Context.MODE_PRIVATE);
    }
}