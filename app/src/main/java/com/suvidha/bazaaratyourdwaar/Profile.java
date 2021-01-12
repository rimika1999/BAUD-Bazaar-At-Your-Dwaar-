package com.suvidha.bazaaratyourdwaar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends AppCompatActivity implements View.OnClickListener{

    Context context=this;
    FirebaseAuth mauth;
    EditText edit_name,edit_address,edit_district,edit_state,edit_pincode,edit_email,edit_phonenumber;
    Button profile_button;
    TextView textview_profile;
    ImageView imageview_back;
    Dialog dialog_progress,dialog_noConnection;
    LinearLayout layoutId;
    CircleImageView profilepic_profile;
    ImageView profile_pic,edit_profilepic;

    StorageReference storageReference;
    DatabaseReference refDatabase;
    String URL;

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mauth = FirebaseAuth.getInstance();

        layoutId = findViewById(R.id.profile_layout);
        edit_name = findViewById(R.id.profile_et_fullname);
        edit_address = findViewById(R.id.profile_et_address_value);
        edit_state = findViewById((R.id.profile_et_state));
        edit_district = findViewById(R.id.profile_et_district);
        edit_pincode = findViewById(R.id.profile_et_pincode);
        edit_email = findViewById(R.id.profile_et_email);
        edit_phonenumber = findViewById(R.id.profile_et_phone_number);

        profile_pic = findViewById(R.id.profile_pic);
        edit_profilepic = findViewById(R.id.edit_profilepic);

        imageview_back = findViewById(R.id.signup_iv_back);
        profile_button = findViewById(R.id.update_profile_button);
        profilepic_profile = findViewById(R.id.profile_pic);

        dialog_progress = new Dialog(context);
        dialog_progress.setContentView(R.layout.activity_dialog_loading);


        imageview_back.setOnClickListener(this);
        profile_button.setOnClickListener(this);
        edit_profilepic.setOnClickListener(this);

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
                    edit_name.setText(name);
                    edit_address.setText(add);
                    edit_district.setText(district);
                    edit_state.setText(state);
                    edit_pincode.setText(pincode);
                    edit_email.setText(mail);
                    edit_phonenumber.setText(phonenumber);
                }

                String profilepic_url;
                profilepic_url = snapshot.child("profilepic_url").getValue(String.class);
                if(profilepic_url!=null)
                {
                    Picasso.get().load(profilepic_url).into(profilepic_profile);
                }
            }

            @Override
            public void onCancelled( @NonNull DatabaseError error ) {

            }
        });

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
        final String userProfileId = sp.getString(Constants.sp_userkey,"");
        refDatabase = FirebaseDatabase.getInstance().getReference("UserProfile");

        refDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {

                refDatabase.child(userProfileId).child("name").setValue(name);
                refDatabase.child(userProfileId).child("address").setValue(address);
                refDatabase.child(userProfileId).child("district").setValue(district);
                refDatabase.child(userProfileId).child("state").setValue(state);
                refDatabase.child(userProfileId).child("pincode").setValue(pincode);
                refDatabase.child(userProfileId).child("email").setValue(email);
                refDatabase.child(userProfileId).child("phonenumber").setValue(phonenumber);

                dialog_progress.dismiss();
                LinearLayout linearLayout = findViewById(R.id.profile_layout);
                Snackbar.make(linearLayout,"Profile Updated",Snackbar.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled( @NonNull DatabaseError error ) {

            }
        });

    }

    void setProfilepic()
    {
        Intent openGalleryIntent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent,1000);
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, @Nullable Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);//data with uri of the image

        if(requestCode == 1000)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Uri imageUri = data.getData();//getting uri of the image
                //profilepic_profile.setImageURI(imageUri);
                dialog_progress.show();
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase( final Uri imageUri) {

        storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference fileref = storageReference.child("Users/"+mauth.getCurrentUser().getUid()+"/Profilepic.jpg");

        fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess( UploadTask.TaskSnapshot taskSnapshot ) {

                fileref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete( @NonNull Task<Uri> task ) {
                         URL=task.getResult().toString();

                        SharedPreferences sp;
                        sp = getSharedPreferences(Constants.sharedPref, MODE_PRIVATE);
                        final String user_profile_Key = sp.getString(Constants.sp_userkey,"");
                        refDatabase = FirebaseDatabase.getInstance().getReference("UserProfile").child(user_profile_Key);

                        refDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange( @NonNull DataSnapshot snapshot ) {

                                dialog_progress.dismiss();
                                Picasso.get().load(URL).into(profile_pic);
                                refDatabase.child("profilepic_url").setValue(URL);


                            }

                            @Override
                            public void onCancelled( @NonNull DatabaseError error ) {

                            }
                        });

                    }
                });
            }
        });

    }

    @Override
    public void onClick( View v ) {
        switch (v.getId())
        {
            case R.id.signup_iv_back:
            {
                if(!checkConnection())
                {
                    Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    finish();
                    return;
                }
                break;
            }
            case R.id.edit_profilepic:
            {
                if(!checkConnection())
                {
                    Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    setProfilepic();
                    break;
                }
            }
            case R.id.update_profile_button:
            {
                if(!checkConnection())
                {
                    Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    UpdateProfile();
                    break;
                }
            }
        }

    }
}