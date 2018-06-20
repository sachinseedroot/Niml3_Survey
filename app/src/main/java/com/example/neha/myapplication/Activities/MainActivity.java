package com.example.neha.myapplication.Activities;

import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.neha.myapplication.Adapter.ViewPagerAdapter;
import com.example.neha.myapplication.Controller.MainApplication;
import com.example.neha.myapplication.MVP.MainMVP;
import com.example.neha.myapplication.MVP.Presenter;
import com.example.neha.myapplication.Model.RefreshTokenModel;
import com.example.neha.myapplication.Model.SurveyItemModel;
import com.example.neha.myapplication.R;
import com.example.neha.myapplication.Utilities.AppConstants;
import com.example.neha.myapplication.Utilities.AppUtilities;
import com.example.neha.myapplication.Utilities.TypeFaceHelper;
import com.example.neha.myapplication.Utilities.VerticalViewPager;
import com.example.neha.myapplication.Volley.VolleyObject;
import com.example.neha.myapplication.Volley.VolleyObjectResponseInterface;
import com.example.neha.myapplication.Volley.VolleyResponseInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements MainMVP.view, View.OnClickListener {

    private Context mcontext;
    private Presenter presenter;
    private TextView titleTV, refreshTV, hamburgerTV;
    private String accessToken;
    private VerticalViewPager viewpager_survey;
    private ArrayList<SurveyItemModel> surveyItemModels;
    private LinearLayout bullets_lin;
    private Typeface fa_typeface;
    private Button takeSurveyBtn;
    private int positionPage = 0;
    private Typeface f_light;
    private Typeface f_bold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext = this;
        initialize();

    }

    private void initialize() {
        titleTV = (TextView) findViewById(R.id.titleTV);
        refreshTV = (TextView) findViewById(R.id.refreshTV);
        hamburgerTV = (TextView) findViewById(R.id.hamburgerTV);
        viewpager_survey = (VerticalViewPager) findViewById(R.id.viewpager_survey);
        bullets_lin = (LinearLayout) findViewById(R.id.bullets_lin);
        takeSurveyBtn = (Button) findViewById(R.id.takeSurveyBtn);

        fa_typeface = TypeFaceHelper.getInstance(mcontext).getStyleTypeFace(TypeFaceHelper.FONT_AWESOME);
        f_light = TypeFaceHelper.getInstance(mcontext).getStyleTypeFace(TypeFaceHelper.FiraSansLight);
        f_bold = TypeFaceHelper.getInstance(mcontext).getStyleTypeFace(TypeFaceHelper.FiraSansBold);

        titleTV.setTypeface(f_bold);
        refreshTV.setTypeface(fa_typeface);
        hamburgerTV.setTypeface(fa_typeface);
        takeSurveyBtn.setTypeface(f_light);

        refreshTV.setOnClickListener(this);
        takeSurveyBtn.setOnClickListener(this);

        //  fetchURL = AppConstants.URL_SURVEY_ARRAY + AppConstants.URL_SURVEY_ARRAY_ACCESS_TOKEN;
        presenter = new Presenter(this, mcontext);
        presenter.authFailure(mcontext);

    }


    @Override
    public void displayDialog(Context context, String title, String msg) {
        AppUtilities.showAlertDialog(context, title, msg);
    }

    @Override
    public void getdata(JSONArray jsonArray) {
        if (jsonArray != null && jsonArray.length() > 0) {
            surveyItemModels = new ArrayList<>();
            bullets_lin.removeAllViews();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject sureveyObject = jsonArray.optJSONObject(i);
                if (sureveyObject != null && sureveyObject.length() > 0) {
                    SurveyItemModel surveyItemModel = new SurveyItemModel(sureveyObject);
                    surveyItemModels.add(surveyItemModel);

                    TextView textView = new TextView(mcontext);
                    textView.setTag(i);
                    textView.setLayoutParams(new RelativeLayout.LayoutParams(50, 50));
                    if (i == 0) {
                        textView.setText(mcontext.getResources().getString(R.string.fa_solid_circle));
                    } else {
                        textView.setText(mcontext.getResources().getString(R.string.fa_hollow_circle));
                    }
                    textView.setTextColor(ContextCompat.getColor(mcontext, R.color.white));
                    textView.setTypeface(fa_typeface);
                    bullets_lin.addView(textView);
                }
            }

            loadSurvey();
        }
    }

    @Override
    public void authFailure() {
        presenter.authFailure(mcontext);
    }

    @Override
    public void refreshedToken(String token) {
        accessToken = token;
        presenter.getdata(mcontext, token);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refreshTV:
                presenter.getdata(mcontext, accessToken);
                break;
            case R.id.takeSurveyBtn:
                singleSurveyActivity();
                break;
        }
    }


    private void loadSurvey() {
        if (surveyItemModels != null && surveyItemModels.size() > 0) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(mcontext, surveyItemModels, f_bold, f_light);
            viewpager_survey.setAdapter(adapter);
            viewpager_survey.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    positionPage = position;
                    changeCirclIndicators(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            viewpager_survey.setCurrentItem(0);
        } else {
            presenter.clickDisplayDialog(mcontext, "Oops!", "Failed to load survey list");
        }

    }

    private void changeCirclIndicators(int position) {
        if (bullets_lin != null && bullets_lin.getChildCount() > 0) {
            for (int i = 0; i < bullets_lin.getChildCount(); i++) {
                View view = bullets_lin.getChildAt(i);
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    if (Integer.parseInt(textView.getTag().toString()) == position) {
                        textView.setText(mcontext.getResources().getString(R.string.fa_solid_circle));
                    } else {
                        textView.setText(mcontext.getResources().getString(R.string.fa_hollow_circle));
                    }
                }
            }
        }
    }

    private void singleSurveyActivity() {
        if (surveyItemModels != null && surveyItemModels.size() > 0) {
            Intent intent = new Intent(mcontext, SingleSurveyActivity.class);
            intent.putExtra("singleobject", surveyItemModels.get(positionPage));
            startActivity(intent);
        }
    }
}
