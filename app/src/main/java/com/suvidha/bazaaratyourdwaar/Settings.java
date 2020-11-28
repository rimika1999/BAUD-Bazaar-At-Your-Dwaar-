package com.suvidha.bazaaratyourdwaar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;

    Context context=this;
    StorageReference storageReference;
    DatabaseReference refDatabase;
    TextView username,address,email,contact;
    CircleImageView profilepic_id;
    ImageView settings_back;
    Button update_button,logout_button;
    LinearLayout layoutId;
    SharedPreferences sp;

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        username= findViewById(R.id.textview_profile_username);
        address = findViewById(R.id.textview_profile_useraddress);
        email = findViewById(R.id.textview_profile_useremail);
        contact = findViewById(R.id.textview_profile_usercontact);
        settings_back = findViewById(R.id.setting_iv_back);
        update_button = findViewById(R.id.settings_profileupdate_button);
        logout_button = findViewById(R.id.settings_logoutbutton_id);

        profilepic_id = findViewById(R.id.settings_profile_pic_id);
        layoutId = findViewById(R.id.settings_layoutId);

        settings_back.setOnClickListener(this);
        update_button.setOnClickListener(this);
        logout_button.setOnClickListener(this);


        if(!checkConnection())
        {
            Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
        }

        SharedPreferences sp;
        sp = getSharedPreferences(Constants.sharedPref,context.MODE_PRIVATE);
        final String userProfileId = sp.getString(Constants.sp_userkey,"");
        refDatabase = FirebaseDatabase.getInstance().getReference("UserProfile").child(userProfileId);


        refDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                String name,add,district,state,pincode,mail,phonenumber;
                name = snapshot.child("name").getValue(String.class);
                add = snapshot.child("address").getValue(String.class);
                district = snapshot.child("district").getValue(String.class);
                state = snapshot.child("state").getValue(String.class);
                pincode = snapshot.child("pincode").getValue(String.class);
                mail = snapshot.child("email").getValue(String.class);
                phonenumber = snapshot.child("phonenumber").getValue(String.class);

                if(name!=null)
                {
                    username.setText(name);
                    address.setText(add+", "+district+", "+state+","+pincode);
                    email.setText(mail);
                    contact.setText(phonenumber);
                }

                String profilepic_url;
                profilepic_url = snapshot.child("profilepic_url").getValue(String.class);
                if(profilepic_url!=null)
                {
                    Picasso.get().load(profilepic_url).into(profilepic_id);
                }
            }

            @Override
            public void onCancelled( @NonNull DatabaseError error ) {

            }
        });
    }


    @Override
    public void onClick( View v ) {
        switch (v.getId())
        {
            case R.id.setting_iv_back:
            {
                if(!checkConnection())
                {
                    Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    finish();
                }
                break;
            }
            case R.id.settings_profileupdate_button:
            {
                if(!checkConnection())
                {
                    Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    startActivity(new Intent(context,Profile.class));
                }
                break;
            }
            case R.id.settings_logoutbutton_id:
            {
                sp=getSharedPreferences(Constants.sharedPref,context.MODE_PRIVATE);
                SharedPreferences.Editor sp_editor = sp.edit();
                sp_editor.putString(Constants.isLoggedin,"false");
                sp_editor.commit();


                refDatabase = FirebaseDatabase.getInstance().getReference("Users");
                String userId = sp.getString(Constants.sp_key,"");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                String currentDateandTime = sdf.format(new Date());
                refDatabase.child(userId).child("logOut_time").setValue(currentDateandTime);

                Intent intent = new Intent(context,Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }

        }

    }
}