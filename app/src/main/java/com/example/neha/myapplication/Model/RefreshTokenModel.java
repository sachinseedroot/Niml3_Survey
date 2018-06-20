package com.example.neha.myapplication.Model;

import org.json.JSONObject;

public class RefreshTokenModel {

    public String access_token;
    public String token_type;
    public int expires_in;
    public int created_at;

    public RefreshTokenModel(JSONObject jsonObject){
        access_token = jsonObject.optString("access_token");
        token_type = jsonObject.optString("token_type");
        expires_in = jsonObject.optInt("expires_in");
        created_at = jsonObject.optInt("created_at");
    }
}
