package com.suvidha.bazaaratyourdwaar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Temp_SignUp extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mauth;
    TextView textView_login;
    Context context = this;
    ImageView imageView_back;
    Button signUp_button;
    EditText email, username, password, confirmPassword, phone;
    LinearLayout layoutId;
    Dialog dialog_progress;
    boolean otp_verification=false;

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dialog_progress = new Dialog(context);
        dialog_progress.setContentView(R.layout.activity_dialog_loading);

        textView_login = findViewById(R.id.signup_tv_login);
        imageView_back = findViewById(R.id.signup_iv_back);
        signUp_button = findViewById(R.id.signUp_button);
        email = findViewById(R.id.signup_et_email);
        phone = findViewById(R.id.signup_et_phone);
        username = findViewById(R.id.signup_et_username);
        password = findViewById(R.id.signup_et_password);
        confirmPassword = findViewById(R.id.signup_et_confirmPassword);
        layoutId = findViewById(R.id.signUp_layoutId);

        textView_login.setOnClickListener(this);
        imageView_back.setOnClickListener(this);
        signUp_button.setOnClickListener(this);
    }

    @Override
    public void onClick( View v ) {

        switch (v.getId()) {
            case R.id.signup_iv_back: {
                finish();
                break;
            }
            case R.id.signup_tv_login: {
                if (!checkConnection()) {
                    Snackbar.make(layoutId, "No Internet Connection, please try again later", Snackbar.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(context, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                break;
            }
            case R.id.signUp_button: {
                if (!checkConnection()) {
                    Snackbar.make(layoutId, "No Internet Connection, please try again later", Snackbar.LENGTH_SHORT).show();
                }
                else {

                    dialog_progress.setContentView(R.layout.activity_dialog_loading);
                    //check if user already exists
                    final String phoneNo = phone.getText().toString();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange( @NonNull DataSnapshot snapshot ) {

                            Boolean userExists = false;
                            for(DataSnapshot dataSnapshot:snapshot.getChildren())
                            {
                                String dbUserphone = dataSnapshot.child("phone").getValue(String.class);
                                if(dbUserphone.equals(phoneNo))
                                {
                                    LinearLayout linearLayout = findViewById(R.id.signUp_layoutId);
                                    Snackbar.make(linearLayout, "User already in use", Snackbar.LENGTH_LONG).show();
                                    userExists=true;
                                    break;
                                }
                            }

                            if(userExists)
                            {
                                return;
                            }
                            else
                            {
                                if(registerUser())
                                {

                                }
                            }
                        }

                        @Override
                        public void onCancelled( @NonNull DatabaseError error ) {

                        }
                    });

                }


            }
        }
    }

    private Boolean registerUser()
    {
        String UserName = username.getText().toString();
        String Email=email.getText().toString();
        String phoneNo = phone.getText().toString();
        String Password = password.getText().toString();
        String ConfirmPassword = confirmPassword.getText().toString();

        if(UserName.isEmpty())
        {
            username.setError("Username Required");
            username.requestFocus();
            return false;
        }
        else if(Email.isEmpty())
        {
            email.setError("Email required");
            email.requestFocus();
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
        {
            email.setError("Invalid Email");
            email.requestFocus();
            return false;
        }
        else if(phoneNo.isEmpty())
        {
            phone.setError("Mobile Number required");
            phone.requestFocus();
            return false;
        }
        else if(phoneNo.length()!=10)
        {
            phone.setError("Invalid Mobile Number");
            phone.requestFocus();
            return false;
        }
        else if(Password.isEmpty() || Password.length()<6)
        {
            password.setError("Invalid password(minimum 6 characters required)");
            password.requestFocus();
            return false;
        }
        else if(ConfirmPassword.isEmpty())
        {
            confirmPassword.setError("Password confirmation required");
            confirmPassword.requestFocus();
            return false;
        }
        else if(!ConfirmPassword.equals(Password))
        {
            confirmPassword.setError("Password entered is incorrect");
            return false;
        }
        else
        {
            dialog_progress.show();

            //Mobile number verification
            requestOTP(phoneNo);
        }
        return true;
    }

    String verificationId;
    PhoneAuthProvider.ForceResendingToken token;
    private void requestOTP(String phoneNo)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNo, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent( @NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken ) {
                super.onCodeSent(s, forceResendingToken);

                dialog_progress.dismiss();
                verificationId = s;
                token = forceResendingToken;

                final Dialog dialog_otp = new Dialog(context);
                dialog_otp.setContentView(R.layout.activity_otp_dialog);

                final EditText OTP_code;
                Button verifyCode_button;

                verifyCode_button = dialog_otp.findViewById(R.id.OTP_verification_button);
                OTP_code=dialog_otp.findViewById(R.id.enter_OTP);

                dialog_otp.show();
                verifyCode_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick( View v ) {

                        String userOTP = OTP_code.getText().toString();
                        if(!userOTP.isEmpty() && userOTP.length()==6)
                        {
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,userOTP);
                            signInWithPhoneAuthCredential(credential);
                        }
                        else
                        {
                            dialog_otp.dismiss();
                            return;
                        }

                        dialog_otp.dismiss();
                    }
                });



            }

            @Override
            public void onCodeAutoRetrievalTimeOut( @NonNull String s ) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted( @NonNull PhoneAuthCredential phoneAuthCredential ) {

            }

            @Override
            public void onVerificationFailed( @NonNull FirebaseException e ) {

                dialog_progress.dismiss();
                Log.d("TAG","Authentication failed");
                Snackbar.make(layoutId,"Authentication failed",Snackbar.LENGTH_SHORT).show();
            }

        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mauth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( @NonNull Task<AuthResult> task ) {

                if(task.isSuccessful())
                {
                    Log.d("TAG","Sign in with credential:Success");
                    otp_verification=true;

                }
                else
                {
                    //sign in failed
                    Snackbar.make(layoutId,"Sign in failed",Snackbar.LENGTH_SHORT).show();
                    Log.w("TAG","Sign in with credential:Failure",task.getException());
                }
            }
        });
    }
}
