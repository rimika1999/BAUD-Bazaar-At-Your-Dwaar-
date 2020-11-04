
package com.suvidha.bazaaratyourdwaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;



public class Login extends AppCompatActivity implements View.OnClickListener{

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    FirebaseAuth mAuth;
    TextInputEditText edit_username,edit_password;
    TextInputLayout layout_username,layout_password;
    Button login_button;
    TextView textView_signup;
    Context context = this;
    ImageView imageView_back;
    Dialog dialog_progress;
    LinearLayout layoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dialog_progress = new Dialog(context);
        dialog_progress.setContentView(R.layout.activity_dialog_loading);

        mAuth=FirebaseAuth.getInstance();
        edit_username=findViewById(R.id.login_et_username);
        edit_password=findViewById(R.id.login_et_password);
        layout_username=findViewById(R.id.login_etl_username);
        layout_password=findViewById(R.id.login_etl_password);
        login_button=findViewById(R.id.login_button);
        layoutId = findViewById(R.id.login_layoutID);

        textView_signup = findViewById(R.id.login_tv_signup);
        imageView_back = findViewById(R.id.login_iv_back);

        textView_signup.setOnClickListener(this);
        imageView_back.setOnClickListener(this);
        login_button.setOnClickListener(this);


    }

    public String hash256(String data) throws NoSuchAlgorithmException {
        MessageDigest md=MessageDigest.getInstance("SHA-256");
        md.update(data.getBytes());
        return bytesToHex(md.digest());
    }

    public String bytesToHex(byte[] bytes)
    {
        StringBuffer result = new StringBuffer();
        for(byte byt : bytes)
        {
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    String userId;
    String login_password;
    String hash_password;


    private void UserLogin()
    {

        userId = edit_username.getText().toString().trim();
        login_password = edit_password.getText().toString().trim();

        if(userId.isEmpty())
        {
            layout_username.setError("Username required");
            return;
        }
        if(login_password.isEmpty())
        {
            layout_password.setError("Password required");
            return;
        }
        dialog_progress.show();

        hash_password = null;
        try {
            hash_password = hash256(login_password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        layout_username.setError(null);
        layout_password.setError(null);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");


        //if user exists
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {

                boolean userFound=false;
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    String dbusername = dataSnapshot.child("email_phone").getValue(String.class);
                    String dbpassword = dataSnapshot.child("password").getValue(String.class);

                    if(dbusername.equals(userId) && dbpassword.equals(hash_password))
                    {
                        userFound=true;
                        break;
                    }
                }

                dialog_progress.dismiss();
                if(userFound)
                {
                    LinearLayout linearLayout = findViewById(R.id.login_layoutID);
                    startActivity(new Intent(context,Dashboard.class));
                    finish();
                }
                else
                {
                    LinearLayout linearLayout = findViewById(R.id.login_layoutID);
                    Snackbar.make(linearLayout,"User does not exist",Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled( @NonNull DatabaseError error ) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.login_tv_signup:
            {
                startActivity(new Intent(context,SignUp.class));
                break;
            }
            case R.id.login_iv_back:
            {
                finish();
                break;
            }
            case R.id.login_button:
            {
                UserLogin();
                break;
            }

        }
    }



}