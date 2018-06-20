package com.example.neha.myapplication.MVP;

import android.content.Context;

import org.json.JSONArray;


public interface MainMVP {
    interface view{
        void displayDialog(Context context, String title, String msg);
        void getdata(JSONArray jsonArray);
        void authFailure();
        void refreshedToken(String token);
    }

    interface presenter{
        void clickDisplayDialog(Context context, String title, String msg);
        void getdata(Context mcontext, String url);
        void authFailure(final Context mcontext);
    }
}
