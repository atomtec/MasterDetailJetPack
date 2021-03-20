package com.example.masterdetail;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.tablet)){
            getSupportFragmentManager().setFragmentFactory(new FragmentFactoryImpl(0l,false));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public boolean isTablet(){
        return getResources().getBoolean(R.bool.tablet) == true;
    }



}