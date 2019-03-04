package com.example.adi.qrscanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {

    private EditText TextEmailLog,TextPassLog;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser != null && firebaseUser.isEmailVerified()){
            Intent i = new Intent(loginActivity.this, UserHome.class);
            startActivity(i);
        }



        TextEmailLog=(EditText)findViewById(R.id.TextEmailLog);
        TextPassLog=(EditText)findViewById(R.id.TextPassLog);


    }

    public void buttonLog_click (View v){
        final ProgressDialog progressDialog=ProgressDialog.show(loginActivity.this,"Please wait...","Processing...",true);
        (firebaseAuth.signInWithEmailAndPassword(TextEmailLog.getText().toString(),TextPassLog.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()){
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(loginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(loginActivity.this, UserHome.class);
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(loginActivity.this, "Please verify your email address", Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Log.e("Error",task.getException().toString());
                    Toast.makeText(loginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void forgot(View v){
        Intent i = new Intent(loginActivity.this, ResetPasswordActivity.class);
        startActivity(i);
    }
    public void aaaaa(View v){
        Intent i = new Intent(loginActivity.this, SingUpActivity.class);
        startActivity(i);
    }
}
