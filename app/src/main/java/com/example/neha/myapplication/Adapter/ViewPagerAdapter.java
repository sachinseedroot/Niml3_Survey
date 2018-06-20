package com.example.neha.myapplication.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.neha.myapplication.Model.SurveyItemModel;
import com.example.neha.myapplication.R;
import com.example.neha.myapplication.Utilities.TypeFaceHelper;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<SurveyItemModel> surveyItemModels;
    private LayoutInflater layoutInflater;
    private Typeface t_light, t_bold;

    public ViewPagerAdapter(Context cnt, ArrayList<SurveyItemModel> sItemModels,  Typeface f_bold, Typeface f_light) {
        this.context = cnt;
        surveyItemModels = sItemModels;
        layoutInflater = LayoutInflater.from(context);
        t_light =f_light;
        t_bold = f_bold;
    }



    @Override
    public int getCount() {
        return surveyItemModels.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View surveyitem = layoutInflater.inflate(R.layout.view_pager_item, container, false);

        TextView title = (TextView) surveyitem.findViewById(R.id.titleHeaderTV);
        TextView descHeaderTV = (TextView) surveyitem.findViewById(R.id.descHeaderTV);
        ImageView imgV_bg = (ImageView) surveyitem.findViewById(R.id.imgV_bg);

        title.setTypeface(t_bold);
        descHeaderTV.setTypeface(t_light);

        title.setText(surveyItemModels.get(position).title);
        descHeaderTV.setText(surveyItemModels.get(position).description);

        if(!TextUtils.isEmpty(surveyItemModels.get(position).cover_image_url_large)){
            Picasso.get().load(surveyItemModels.get(position).cover_image_url_large)
                    .placeholder(ContextCompat.getDrawable(context,R.drawable.placeholder))
                    .into(imgV_bg);
        }


        container.addView(surveyitem);
        return surveyitem;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
