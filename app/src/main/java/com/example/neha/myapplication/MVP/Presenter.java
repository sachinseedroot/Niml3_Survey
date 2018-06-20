package com.example.neha.myapplication.MVP;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.neha.myapplication.Controller.MainApplication;
import com.example.neha.myapplication.Model.RefreshTokenModel;
import com.example.neha.myapplication.Utilities.AppConstants;
import com.example.neha.myapplication.Utilities.AppUtilities;
import com.example.neha.myapplication.Volley.VolleyObject;
import com.example.neha.myapplication.Volley.VolleyObjectResponseInterface;
import com.example.neha.myapplication.Volley.VolleyResponseInterface;

import org.json.JSONArray;
import org.json.JSONObject;



public class Presenter implements MainMVP.presenter {

    private final MainMVP.view mainview;
    private ProgressDialog mProgressDialog;
    private Context mcontext;

    public Presenter(MainMVP.view view, Context context) {
        mainview = view;
        mcontext = context;
        initProgressbar(mcontext);
    }


    @Override
    public void clickDisplayDialog(Context context, String title, String msg) {
        mainview.displayDialog(context, title, msg);
    }

    @Override
    public void getdata(final Context mcontext, String token) {
        if(!TextUtils.isEmpty(token)) {
            if (AppUtilities.isNetworkAvailable(mcontext)) {
                showProgress("Fetching survey details, please wait...");
                VolleyObject.getSDKconfigInstance().fetchSureveyData(token, new VolleyResponseInterface() {
                    @Override
                    public void onResponse(JSONArray response, VolleyError volleyError) {
                        stopProgress();
                        if (volleyError == null) {
                            mainview.getdata(response);
                        } else {
                            if (volleyError instanceof AuthFailureError) {
                                mainview.authFailure();
                            } else {
                                mainview.displayDialog(mcontext, findError(volleyError).toUpperCase(), "Something went wrong...");
                            }

                        }
                    }
                });
            } else {
                mainview.displayDialog(mcontext, "No internet connection!", "Please check your internet & click refresh");
            }
        }else{
            mainview.displayDialog(mcontext, "Oops!", "Something went wrong...");
        }
    }

    @Override
    public void authFailure(final Context context) {
        if (AppUtilities.isNetworkAvailable(mcontext)) {
            showProgress("Refreshing auth token, please wait...");
            VolleyObject.getSDKconfigInstance().refreshToken(AppConstants.URL_SURVEY_TOKEN_SURVEY, new VolleyObjectResponseInterface() {
                @Override
                public void onResponse(JSONObject response, VolleyError volleyError) {
                    stopProgress();
                    if (volleyError == null) {
                        if (response != null && response.length() > 0) {
                            RefreshTokenModel refreshTokenModel = new RefreshTokenModel(response);
                            if (refreshTokenModel != null && !TextUtils.isEmpty(refreshTokenModel.access_token)) {
                                String token =  refreshTokenModel.access_token;
                                mainview.refreshedToken(token);
                            } else {
                                mainview.displayDialog(context, "Auth failure", "Auth token refresh failed...");
                            }
                        } else {
                            mainview.displayDialog(context, "Auth failure", "Auth token refresh failed...");
                        }
                    } else {
                        mainview.displayDialog(context, "Auth failure", "Auth token refresh failed...");
                    }
                }
            });
        } else {
            mainview.displayDialog(mcontext, "No internet connection!", "Please check your internet & click refresh");
        }
    }


    public void showProgress(String msg) {
        try {
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.setMessage(msg);
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopProgress() {

        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initProgressbar(Context mcontext) {
        mProgressDialog = new ProgressDialog(mcontext);
        //  mProgressDialog.setMessage("Loading, please wait...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    private static String findError(VolleyError error) {
        if (error instanceof TimeoutError) {
            return "Timeout Error";
        } else if (error instanceof AuthFailureError) {
            return "Auth Failure Error";
        } else if (error instanceof ServerError) {
            return "Server Error";
        } else if (error instanceof NetworkError) {
            return "Network Error";
        } else if (error instanceof ParseError) {
            return "Parse Error";
        } else if (error instanceof NoConnectionError) {
            return "No Connection Error";
        } else {
            return "Other Error - " + error.getMessage();
        }
    }


}
