package com.example.neha.myapplication.Volley;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

public interface VolleyObjectResponseInterface {

    void onResponse(JSONObject response, VolleyError volleyError);

}
