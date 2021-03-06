package com.suvidha.bazaaratyourdwaar;

import androidx.annotation.NonNull;
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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.security.MessageDigest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    SharedPreferences sp;

    TextView textView_login;
    Context context = this;
    ImageView imageView_back;
    Button signUp_button;
    EditText email,username,password,confirmPassword,phone;
    Dialog dialog_progress,dialog_noConnection;
    String verificationID;
    Boolean otp_verification = false;
    LinearLayout layoutId;


    private FirebaseAuth mAuth;

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialog_progress = new Dialog(context);
        dialog_progress.setContentView(R.layout.activity_dialog_loading);

        mAuth = FirebaseAuth.getInstance();

        textView_login = findViewById(R.id.signup_tv_login);
        imageView_back = findViewById(R.id.signup_iv_back);
        signUp_button = findViewById(R.id.signUp_button);
        email = findViewById(R.id.signup_et_email);
        phone = findViewById(R.id.signup_et_phone);
        username = findViewById(R.id.signup_et_username);
        password = findViewById(R.id.signup_et_password);
        confirmPassword = findViewById(R.id.signup_et_confirmPassword);
        layoutId = findViewById(R.id.signUp_layoutId);

        sp = getSharedPreferences(Constants.sharedPref,context.MODE_PRIVATE);

        textView_login.setOnClickListener(this);
        imageView_back.setOnClickListener(this);
        signUp_button.setOnClickListener(this);

        if(!checkConnection())
        {
            Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
        }

        mCallBacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            FirebaseUser User = mAuth.getCurrentUser();
            @Override
            public void onCodeSent( @NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken ) {
                super.onCodeSent(s, forceResendingToken);
                dialog_progress.dismiss();
                verificationID=s;
                Log.d("TAG","On Code Sent:"+s);
                //pop-up
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
                        String verificationCode = OTP_code.getText().toString();
                        if(!verificationCode.isEmpty())
                        {
                            PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationID,verificationCode);

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
                dialog_progress.dismiss();
                Log.d("TAG","Time out");
                Snackbar.make(layoutId,"Authentication failed",Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationCompleted( @NonNull PhoneAuthCredential phoneAuthCredential ) {

                dialog_progress.dismiss();
                Log.d("TAG","On Verification Completion:"+phoneAuthCredential);
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed( @NonNull FirebaseException e ) {

                dialog_progress.dismiss();
                Log.d("TAG","Authentication failed");
                Snackbar.make(layoutId,"Authentication failed",Snackbar.LENGTH_SHORT).show();
            }
        };

    }

    private void emailAuthentication(String email,String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( @NonNull Task<AuthResult> task ) {
                if(task.isSuccessful()){
                    dialog_progress.dismiss();
                    FirebaseUser User=mAuth.getCurrentUser();

                    User.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete( @NonNull Task<Void> task ) {

                            if(task.isSuccessful())
                            {
                                Snackbar.make(layoutId,"Please verify your Email address",Snackbar.LENGTH_SHORT).show();
                                //startActivity(new Intent(context,Login.class));
                                //finish();
                            }
                            else
                            {
                                Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    dialog_progress.dismiss();
                }
            }
        });
    }



    //method for OTP verification
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( @NonNull Task<AuthResult> task ) {

                if(task.isSuccessful())
                {
                    Log.d("TAG","Sign in with credential:Success");
                    otp_verification=true;
                    //startActivity(new Intent(context,Login.class));
                    //finish();
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
    //method for OTP input
    private void requestOTP(String phoneNumber)
    {
        Log.d("TAG","Request sent for otp");
        //firebase class -phone authentication
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,60L, TimeUnit.MINUTES,this,mCallBacks);
    }

    private boolean registerUser()
    {
        String UserName = username.getText().toString();
        String Email=email.getText().toString();
        String phoneNo = phone.getText().toString();
        String Password = password.getText().toString();
        String ConfirmPassword = confirmPassword.getText().toString();

        if(UserName.isEmpty())
        {
            username.setError("Username required");
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
            /*if(Patterns.EMAIL_ADDRESS.matcher(Email).matches())
            {
                //Email authentication
                emailAuthentication(Email,Password);
                return true;
            }
            else if(Email.length()==10)
            {
                String phoneNum=Email;
                Log.d("TAG", "onClick: phone number value="+phoneNum);
                requestOTP("+91"+phoneNum);
                return true;
            }*/

            //mobile number authentication
            if(phoneNo.length()==10)
            {
                Log.d("TAG", "onClick: phone number value="+phoneNo);
                requestOTP("+91"+phoneNo);
                return true;
            }
            return false;
        }

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


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.signup_iv_back:
            {
                finish();
                break;
            }
            case R.id.signup_tv_login:
            {
                if(!checkConnection())
                {
                    Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(context,Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                break;
            }
            case R.id.signUp_button:
            {
                if(!checkConnection())
                {
                    Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                }
                else
                {

                    dialog_progress.setContentView(R.layout.activity_dialog_loading);
                    //check if user already exists
                    final String phoneNo = phone.getText().toString();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange( @NonNull DataSnapshot snapshot ) {
                            boolean userExists=false;

                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String dbusername = dataSnapshot.child("phone").getValue(String.class);
                                if (dbusername.equals(phoneNo)) {
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

                            DatabaseReference UserDatabase;

                            String userIdentification_key;
                            String Username = username.getText().toString();
                            String Password = password.getText().toString();
                            String Email = email.getText().toString();
                            String Phone = phone.getText().toString();
                            String encodedPass = null;
                            try {
                                encodedPass = hash256(Password);
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }


                            UserDatabase = FirebaseDatabase.getInstance().getReference();


                            userIdentification_key=UserDatabase.child("Users").push().getKey();
                            HelperClass_User user_helperclass = new HelperClass_User(Username, Email,Phone, encodedPass,userIdentification_key,null,null);
                            UserDatabase.child("Users").child(userIdentification_key).setValue(user_helperclass);

                            SharedPreferences.Editor sp_editor = sp.edit();
                            sp_editor.putString(Constants.sp_key,userIdentification_key);
                            sp_editor.commit();

                            String user_profileKey,user_cartKey;
                            user_profileKey = UserDatabase.child("UserProfile").push().getKey();
                            user_cartKey = UserDatabase.child("UserItems").push().getKey();

                            HelperClass_cart userCart = new HelperClass_cart(null,0);
                            UserDatabase.child("UserItems").child(user_cartKey).setValue(userCart);

                            HelperClass_Profile user_profileDB = new HelperClass_Profile(user_profileKey,userIdentification_key,null,null,null,null,null,null,null,null,user_cartKey);
                            UserDatabase.child("UserProfile").child(user_profileKey).setValue(user_profileDB);

                            sp_editor.putString(Constants.sp_userkey,user_profileKey);
                            sp_editor.putString(Constants.sp_usercart_key,user_cartKey);
                            sp_editor.commit();


                            startActivity(new Intent(context,Login.class));
                            finish();

                            /*if(registerUser()) {

                                //if email verified
                                FirebaseUser User=mAuth.getCurrentUser();

                                if(otp_verification)
                                {
                                    //save data in firebase database
                                    DatabaseReference UserDatabase;

                                    String userIdentification_key;
                                    String Username = username.getText().toString();
                                    String Password = password.getText().toString();
                                    String Email = email.getText().toString();
                                    String Phone = phone.getText().toString();
                                    String encodedPass = null;
                                    try {
                                        encodedPass = hash256(Password);
                                    } catch (NoSuchAlgorithmException e) {
                                        e.printStackTrace();
                                    }


                                    UserDatabase = FirebaseDatabase.getInstance().getReference();


                                    userIdentification_key=UserDatabase.child("Users").push().getKey();
                                    HelperClass_User user_helperclass = new HelperClass_User(Username, Email,Phone, encodedPass,userIdentification_key,null,null);
                                    UserDatabase.child("Users").child(userIdentification_key).setValue(user_helperclass);

                                    SharedPreferences.Editor sp_editor = sp.edit();
                                    sp_editor.putString(Constants.sp_key,userIdentification_key);
                                    sp_editor.commit();

                                    String user_profileKey,user_cartKey;
                                    user_profileKey = UserDatabase.child("UserProfile").push().getKey();
                                    user_cartKey = UserDatabase.child("UserItems").push().getKey();

                                    HelperClass_cart userCart = new HelperClass_cart(null,0);
                                    UserDatabase.child("UserItems").child(user_cartKey).setValue(userCart);

                                    HelperClass_Profile user_profileDB = new HelperClass_Profile(user_profileKey,userIdentification_key,null,null,null,null,null,null,null,null,user_cartKey);
                                    UserDatabase.child("UserProfile").child(user_profileKey).setValue(user_profileDB);

                                    sp_editor.putString(Constants.sp_userkey,user_profileKey);
                                    sp_editor.putString(Constants.sp_usercart_key,user_cartKey);
                                    sp_editor.commit();


                                    startActivity(new Intent(context,Login.class));
                                    finish();

                                }

                            }*/
                        }

                        @Override
                        public void onCancelled( @NonNull DatabaseError error ) {

                        }
                    });


                }
                break;

            }

        }
    }


}