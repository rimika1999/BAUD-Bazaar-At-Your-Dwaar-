package com.suvidha.bazaaratyourdwaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sp;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Handler handler = new Handler();
        sp=getSharedPreferences(Constants.isLoggedin,context.MODE_PRIVATE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String loggedin = sp.getString(Constants.isLoggedin,"");
                if(loggedin.equals("true"))
                {
                    startActivity(new Intent(context,Dashboard.class));
                    finish();
                }
                else
                {
                    startActivity(new Intent(context,Login.class));
                    finish();
                }
            }
        },1200);
    }
}