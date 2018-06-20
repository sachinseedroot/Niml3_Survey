package com.example.neha.myapplication.Volley;

import com.android.volley.VolleyError;

import org.json.JSONArray;

public interface VolleyResponseInterface {

    void onResponse(JSONArray response, VolleyError volleyError);

}
