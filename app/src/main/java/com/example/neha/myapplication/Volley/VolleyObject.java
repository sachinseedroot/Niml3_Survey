package com.example.neha.myapplication.Volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.neha.myapplication.Utilities.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyObject {

    private static Context mcontext;
    private static MyVolleySingleton myVolleySingleton;
    private static VolleyObject volleyObject;


    public static void initialize(Context context){
        mcontext=context.getApplicationContext();
        myVolleySingleton = new MyVolleySingleton(mcontext);
    }

    public void fetchSureveyData(final String token, final VolleyResponseInterface volleyResponseInterface){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, AppConstants.URL_SURVEY_ARRAY, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                volleyResponseInterface.onResponse(response,null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseInterface.onResponse(null,error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);

                return headers;
            }
        };

        //Retry Policy
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        myVolleySingleton.addToRequestQueue(jsonArrayRequest);
    }

    public void refreshToken(String url, final VolleyObjectResponseInterface volleyResponseInterface){
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("grant_type", "password");
            jsonObject.put("username", "carlos@nimbl3.com");
            jsonObject.put("password", "antikera");
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    volleyResponseInterface.onResponse(response, null);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    volleyResponseInterface.onResponse(null, error);
                }
            });
            //Retry Policy
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Adding request to request queue
            myVolleySingleton.addToRequestQueue(jsonObjReq);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static VolleyObject getSDKconfigInstance() {

        try {
            if (volleyObject == null) {
                volleyObject = new VolleyObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return volleyObject;
    }
}
