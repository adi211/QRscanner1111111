package com.example.adi.qrscanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

public class loginActivity extends AppCompatActivity {

    private EditText TextEmailLog,TextPassLog;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        if(!isConnected(loginActivity.this)) buildDialog(loginActivity.this).show();
        else {
            setContentView(R.layout.activity_login);
            check();
        }


        TextEmailLog=(EditText)findViewById(R.id.TextEmailLog);
        TextPassLog=(EditText)findViewById(R.id.TextPassLog);





        TextView tv = (TextView)findViewById(R.id.textView12);

        Spannable wordtoSpan = new SpannableString("LogIn to WIIMB");
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new ForegroundColorSpan(0xFFC51010), 9, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(wordtoSpan);


    }



    public void check(){

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser != null && firebaseUser.isEmailVerified()){
            Intent i = new Intent(loginActivity.this, UserHome.class);
            startActivity(i);
        }


    }

    public void buttonLog_click (View v) {

        if (!(TextEmailLog.getText().toString().isEmpty() || TextPassLog.getText().toString().isEmpty())) {
            final ProgressDialog progressDialog = ProgressDialog.show(loginActivity.this, "Please wait...", "Processing...", true);
            (firebaseAuth.signInWithEmailAndPassword(TextEmailLog.getText().toString(), TextPassLog.getText().toString()))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                    Toast.makeText(loginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(loginActivity.this, UserHome.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(loginActivity.this, "Please verify your email address", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Log.e("Error", task.getException().toString());
                                Toast.makeText(loginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        else Toast.makeText(this, "Please enter all the information", Toast.LENGTH_SHORT).show();
    }
    public void forgot(View v){
        Intent i = new Intent(loginActivity.this, ResetPasswordActivity.class);
        startActivity(i);
    }
    public void aaaaa(View v){
        Intent i = new Intent(loginActivity.this, SingUpActivity.class);
        startActivity(i);
    }



    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if ((netinfo != null && netinfo.isConnectedOrConnecting())) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }



    public AlertDialog.Builder buildDialog(Context c) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to use this app. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }


}
