package com.example.neha.myapplication.Volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

public class VolleyObject {

    private static Context mcontext;
    private static MyVolleySingleton myVolleySingleton;

    private String urlString="http://wap.globocom.co.in/mglobopay/getHlrLookUpByMdn/919922622444";
    public static void initSDK(Context context){
        mcontext=context.getApplicationContext();
        myVolleySingleton = new MyVolleySingleton(mcontext);
    }

    public void fetchDatabyUrl(JSONObject jsonObject, final VolleyResponseInterface volleyResponseInterface){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlString, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                volleyResponseInterface.onResponse(response.toString(),null);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseInterface.onResponse(null,error);
            }
        });

        // Adding request to request queue
        myVolleySingleton.addToRequestQueue(jsonObjReq);
    }

    public void fetchDatabyUrlInterface(JSONObject jsonObject, final VolleyResponseInterface volleyResponseInterface){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlString, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                volleyResponseInterface.onResponse(response.toString(),null);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseInterface.onResponse(null,error);
            }
        });

        // Adding request to request queue
        myVolleySingleton.addToRequestQueue(jsonObjReq);
    }

    public void fetchDatabyUrlString(String url, final VolleyResponseInterface volleyResponseInterface){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        volleyResponseInterface.onResponse(response,null);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseInterface.onResponse(null,error);
            }
        });

        // Adding request to request queue
        myVolleySingleton.addToRequestQueue(stringRequest);
    }
}
