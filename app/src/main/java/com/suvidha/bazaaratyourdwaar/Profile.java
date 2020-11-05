package com.suvidha.bazaaratyourdwaar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Profile extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mauth;
    EditText edit_name,edit_address,edit_district,edit_state,edit_pincode,edit_email,edit_phonenumber;
    Button profile_button;
    TextView textview_profile;
    ImageView imageview_back;
    Dialog dialog_progress;
    Context context=this;
    LinearLayout layoutId;
    TextView textView_name,textView_address,textView_email,textView_contact;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        mauth = FirebaseAuth.getInstance();
        edit_name = findViewById(R.id.profile_et_fullname);
        edit_address = findViewById(R.id.profile_et_address_value);
        edit_state = findViewById((R.id.profile_et_state));
        edit_district = findViewById(R.id.profile_et_district);
        edit_pincode = findViewById(R.id.profile_et_pincode);
        edit_email = findViewById(R.id.profile_et_email);
        edit_phonenumber = findViewById(R.id.profile_et_phone_number);

        textView_name = findViewById(R.id.textview_profile_username);
        textView_address = findViewById(R.id.textview_profile_useraddress);
        textView_email = findViewById(R.id.textview_profile_useremail);
        textView_contact = findViewById(R.id.textview_profile_usercontact);

        imageview_back = findViewById(R.id.profile_iv_back);
        profile_button = findViewById(R.id.update_profile_button);

        dialog_progress = new Dialog(context);
        dialog_progress.setContentView(R.layout.activity_dialog_loading);


        imageview_back.setOnClickListener(this);
        profile_button.setOnClickListener(this);
    }

    private void UpdateProfile(){
        final String name,address,state,district,pincode,email,phonenumber;

        name = edit_name.getText().toString().trim();
        address = edit_address.getText().toString().trim();
        state = edit_state.getText().toString().trim();
        district = edit_district.getText().toString().trim();
        pincode = edit_pincode.getText().toString().trim();
        email = edit_email.getText().toString().trim();
        phonenumber = edit_phonenumber.getText().toString().trim();

        if(name.isEmpty())
        {
            edit_name.setError("Field required");
            return;
        }
        if(address.isEmpty())
        {
            edit_address.setError("Field required");
            return;
        }
        if(state.isEmpty())
        {
            edit_state.setError("Field required");
            return;
        }
        if(district.isEmpty())
        {
            edit_district.setError("Field required");
            return;
        }
        if(pincode.isEmpty())
        {
            edit_pincode.setError("Field required");
            return;
        }
        if(email.isEmpty())
        {
            edit_email.setError("Field required");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            edit_email.setError("Invalid input");
            return;
        }
        if(phonenumber.isEmpty())
        {
            edit_phonenumber.setError("Field required");
            return;
        }
        if(phonenumber.length()!=10)
        {
            edit_phonenumber.setError("Invalid input");
            return;
        }
        dialog_progress.show();

        SharedPreferences sp;
        sp = getSharedPreferences(Constants.sharedPref, MODE_PRIVATE);
        final String user_id_Key = sp.getString(Constants.sp_key,"");

        final DatabaseReference refDatabase = FirebaseDatabase.getInstance().getReference("UserProfile");
        refDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {

                for(DataSnapshot datasnapshot : snapshot.getChildren())
                {
                    String id = datasnapshot.child("identificationKey").getValue(String.class);

                    if(id.equals(user_id_Key))
                    {
                        dialog_progress.dismiss();
                        String dbKey = datasnapshot.getKey();
                        refDatabase.child(dbKey).child("name").setValue(name);
                        refDatabase.child(dbKey).child("address").setValue(address);
                        refDatabase.child(dbKey).child("district").setValue(district);
                        refDatabase.child(dbKey).child("state").setValue(state);
                        refDatabase.child(dbKey).child("pincode").setValue(pincode);
                        refDatabase.child(dbKey).child("email").setValue(email);
                        refDatabase.child(dbKey).child("phonenumber").setValue(phonenumber);

                        textView_name.setText(name);
                        textView_address.setText("Address:"+address+" "+district+" "+state+" "+pincode);
                        textView_email.setText("Email:"+email);
                        textView_contact.setText("Phone No:"+phonenumber);

                        LinearLayout linearLayout = findViewById(R.id.profile_layout);
                        Snackbar.make(linearLayout,"Profile Updated",Snackbar.LENGTH_LONG).show();
                        break;
                    }
                }
                dialog_progress.dismiss();
                startActivity(new Intent(context,Dashboard.class));
                finish();
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
            case R.id.profile_iv_back:
            {
                finish();
                return;
            }
            case R.id.update_profile_button:
            {
                UpdateProfile();
                return;
            }
        }

    }
}