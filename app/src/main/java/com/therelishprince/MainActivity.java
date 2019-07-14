package com.therelishprince;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.therelishprince.Common.Common;


import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity{

    ProgressBar mProgress;
    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;
    private TextView txtPhone;
    private TextView txtOtp;
    private Button btnSend;
    public static final String TAG="MainActivity";
    private int btnType=0;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    FirebaseAuth.AuthStateListener mAuthListener;

    private final static int RC_SIGN_IN=2;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtPhone=(TextView)findViewById(R.id.txtPhone);
        txtOtp=(TextView)findViewById(R.id.txtOtp);
        btnSend=(Button)findViewById(R.id.btnSend);
        btnSend.setEnabled(false);


        mAuth=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance().getReference();


        txtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                enableSubmitIfReady();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Common.isConnectedToInternet(getBaseContext()))
                {
                    if (!txtPhone.getText().toString().equals("7471115203") && !txtPhone.getText().toString().equals("8518913250")
                            && !txtPhone.getText().toString().equals("9644508764")
                            && !txtPhone.getText().toString().equals("7415100418") && !txtPhone.getText().toString().equals("7697210545"))
                    {
                        if (btnType == 0)
                        {
                            /*mProgress=new ProgressDialog(MainActivity.this);
                            mProgress.setMessage("Sending Verification code");

                            mProgress.show();*/
                            mProgress = (ProgressBar) findViewById(R.id.spin_kit);
                            FoldingCube foldingCube = new FoldingCube();
                            mProgress.setIndeterminateDrawable(foldingCube);
                            mProgress.setVisibility(View.VISIBLE);
                            txtPhone.setEnabled(false);
                            txtOtp.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void afterTextChanged(Editable arg0) {
                                    enableSubmitIfReady1();
                                }

                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                }
                            });
                            String phoneNumber = "+91" + txtPhone.getText().toString();
                            mProgress.setVisibility(View.VISIBLE);
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    phoneNumber,
                                    60,
                                    TimeUnit.SECONDS,
                                    MainActivity.this,
                                    mCallbacks
                            );
                        } else {
                            /*mProgress.setMessage("Welcome to The Relish Prince");
                            mProgress.show();*/
                            mProgress.setVisibility(View.VISIBLE);
                            String verificationCode = txtOtp.getText().toString();
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                            signInWithPhoneAuthCredential(credential);
                        }
                    }
                    else
                    {
                        Snackbar.make(view, "You can not Login!!", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                }
                else
                {
                    Snackbar.make(view, "Please check your Internet Connection!!", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
            }
        });

        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                mProgress.setVisibility(View.VISIBLE);
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                    mProgress.setVisibility(View.INVISIBLE);
                    txtPhone.setEnabled(true);
                    btnSend.setEnabled(true);
                    Toast.makeText(MainActivity.this,"Invalid Phone Number",Toast.LENGTH_LONG).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {

                    mProgress.setVisibility(View.INVISIBLE);
                    txtPhone.setEnabled(true);
                    btnSend.setEnabled(true);
                    Toast.makeText(MainActivity.this,"You have exceeded the verification limit of day!!!",Toast.LENGTH_SHORT).show();

                }else
                {
                    /*mProgress.dismiss();*/
                    mProgress.setVisibility(View.INVISIBLE);
                    txtPhone.setEnabled(true);
                    btnSend.setEnabled(true);
                    btnSend.setText("Verify code");
                    Toast.makeText(MainActivity.this,"Something went wrong,please try again!!!",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                txtOtp.setEnabled(true);
                mProgress.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this,"Verification code sent",Toast.LENGTH_SHORT).show();


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                btnType=1;

                /*mProgress.dismiss();*/
                mProgress.setVisibility(View.INVISIBLE);
                txtOtp.setVisibility(View.VISIBLE);
                btnSend.setText("Verify code");
                btnSend.setEnabled(false);

                // ...
            }
        };


        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser()!=null)
                {
                    startActivity(new Intent(MainActivity.this,Home.class));
                }
            }
        };

    }

    private void enableSubmitIfReady1() {
        boolean isReady1 = txtOtp.getText().toString().length() > 0;
        btnSend.setEnabled(isReady1);
    }

    private void enableSubmitIfReady() {

        boolean isReady = txtPhone.getText().toString().length() > 0;
        btnSend.setEnabled(isReady);


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            mProgress.setVisibility(View.VISIBLE);
                            FirebaseUser user = task.getResult().getUser();
                            Intent homeIntent= new Intent(MainActivity.this,Home.class);
                            startActivity(homeIntent);
                            finish();

                        } else {

                            mProgress.setVisibility(View.INVISIBLE);
                            btnSend.setText("Verify code");
                            Toast.makeText(MainActivity.this,"Sign in failed!!!",Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                /*mProgress.dismiss();*/
                                mProgress.setVisibility(View.INVISIBLE);
                                btnSend.setText("Verify code");
                                Toast.makeText(MainActivity.this,"The verification code entered was invalid!!!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    private Boolean exit = false;
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}
