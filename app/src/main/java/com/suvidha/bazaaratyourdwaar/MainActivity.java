package com.suvidha.bazaaratyourdwaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    SharedPreferences sp;
    Context context = this;
    ConstraintLayout layoutid;

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        layoutid = findViewById(R.id.layout_main_activity);

        if(!checkConnection())
        {
            Snackbar.make(layoutid,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
        }

        Handler handler = new Handler();
        sp=getSharedPreferences(Constants.sharedPref,context.MODE_PRIVATE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String loggedin = sp.getString(Constants.isLoggedin,"");
                if(!checkConnection())
                {
                    Snackbar.make(layoutid,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                }
                else
                {
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

            }
        },1200);


    }


    private boolean checkPermission()
    {
        return ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
    }

    @Override
    public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 101: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Permission Not Granted", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }


}
