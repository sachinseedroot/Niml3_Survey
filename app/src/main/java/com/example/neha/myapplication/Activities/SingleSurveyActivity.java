package com.example.neha.myapplication.Activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neha.myapplication.Model.SurveyItemModel;
import com.example.neha.myapplication.R;
import com.example.neha.myapplication.Utilities.TypeFaceHelper;
import com.example.neha.myapplication.Utilities.VerticalViewPager;
import com.squareup.picasso.Picasso;

public class SingleSurveyActivity extends AppCompatActivity {

    private Typeface f_light,f_bold;
    private Context mcontext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_survey_activity);
        mcontext = this;

        Bundle data = getIntent().getExtras();
        if(data!=null) {
            SurveyItemModel surveyItemModel = (SurveyItemModel) data.getParcelable("singleobject");
            if(surveyItemModel!=null){
                initialize(surveyItemModel);
            }
        }
    }

    private void initialize(SurveyItemModel surveyItemModel) {
        TextView title = (TextView) findViewById(R.id.single_titleTV);
        TextView descHeaderTV = (TextView) findViewById(R.id.single_descTV);
        ImageView imgV_bg = (ImageView) findViewById(R.id.single_imgv);

        f_light = TypeFaceHelper.getInstance(mcontext).getStyleTypeFace(TypeFaceHelper.FiraSansLight);
        f_bold = TypeFaceHelper.getInstance(mcontext).getStyleTypeFace(TypeFaceHelper.FiraSansBold);


        title.setTypeface(f_bold);
        descHeaderTV.setTypeface(f_light);

        title.setText(surveyItemModel.title);
        descHeaderTV.setText(surveyItemModel.description);
        if(!TextUtils.isEmpty(surveyItemModel.cover_image_url_large)) {
            Picasso.get().load(surveyItemModel.cover_image_url_large)
                    .placeholder(ContextCompat.getDrawable(mcontext,R.drawable.placeholder))
                    .into(imgV_bg);
        }

    }
}
