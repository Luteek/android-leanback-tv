package com.fxk.android_tv.hatatv;


import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    // параметр для передачи intent
    public static final String CHANNEL = "channel";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
