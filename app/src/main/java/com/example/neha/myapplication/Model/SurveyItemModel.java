package com.example.neha.myapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.TextView;

import org.json.JSONObject;

public class SurveyItemModel implements Parcelable {

    public String id;
    public String title;
    public String description;
    public String cover_image_url;
    public String cover_image_url_large;

    public SurveyItemModel(JSONObject jsonObject) {
        id = jsonObject.optString("id");
        title = jsonObject.optString("title");
        description = jsonObject.optString("description");
        cover_image_url = jsonObject.optString("cover_image_url");
        if (!TextUtils.isEmpty(cover_image_url)) {
            cover_image_url_large = cover_image_url + "l";
        }
    }

    public SurveyItemModel(Parcel in) {
        String[] data = new String[5];

        in.readStringArray(data);
        this.id = data[0];
        this.title = data[1];
        this.description = data[2];
        this.cover_image_url = data[3];
        this.cover_image_url_large = data[4];
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.id,
                this.title,
                this.description,
                this.cover_image_url,
                this.cover_image_url_large});
    }

    public static final Creator CREATOR = new Creator() {
        public SurveyItemModel createFromParcel(Parcel in) {
            return new SurveyItemModel(in);
        }

        public SurveyItemModel[] newArray(int size) {
            return new SurveyItemModel[size];
        }
    };

}
