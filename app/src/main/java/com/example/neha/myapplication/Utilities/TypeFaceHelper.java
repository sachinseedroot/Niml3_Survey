package com.example.neha.myapplication.Utilities;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

public class TypeFaceHelper {

    private static TypeFaceHelper typeFaceHelper;
    private final Context context;




    private TypeFaceHelper(Context context) {
        this.context = context;
    }

    public static TypeFaceHelper getInstance(Context context){
        if (typeFaceHelper == null){
            typeFaceHelper = new TypeFaceHelper(context);
        }
        return typeFaceHelper;
    }


    private static HashMap<String, Typeface> sTypeFaces = new HashMap<>(1);

    public Typeface getStyleTypeFace(String fileName) {
        Typeface typeface = sTypeFaces.get(fileName);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getAssets(), fileName);
            sTypeFaces.put(fileName, typeface);
        }
        return typeface;
    }
}
