package com.suvidha.bazaaratyourdwaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Payments extends AppCompatActivity implements View.OnClickListener {

    ImageView back_icon;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        back_icon = findViewById(R.id.payments_iv_back);

        back_icon.setOnClickListener(this);
    }

    @Override
    public void onClick( View v ) {

        switch(v.getId())
        {
            case R.id.payments_iv_back:
            {
                finish();
                break;
            }
        }
    }
}