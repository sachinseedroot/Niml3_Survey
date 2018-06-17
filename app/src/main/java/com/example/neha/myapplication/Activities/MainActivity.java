package com.example.neha.myapplication.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import com.example.neha.myapplication.R;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayoutContainer;
    private Context mcontext;
    private Stack<Fragment> fragmentStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext = this;
   }
}
