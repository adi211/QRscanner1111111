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

public class loginActivity extends AppCompatActivity {

    private EditText TextEmailLog,TextPassLog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        TextEmailLog=(EditText)findViewById(R.id.TextEmailLog);
        TextPassLog=(EditText)findViewById(R.id.TextPassLog);

        firebaseAuth=FirebaseAuth.getInstance();
    }

    public void buttonLog_click (View v){
        final ProgressDialog progressDialog=ProgressDialog.show(loginActivity.this,"Please wait...","Procassing...",true);
        (firebaseAuth.signInWithEmailAndPassword(TextEmailLog.getText().toString(),TextPassLog.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()){
                    Toast.makeText(loginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(loginActivity.this, MainActivity.class);
                    startActivity(i);
                }
                else {
                    Log.e("Error",task.getException().toString());
                    Toast.makeText(loginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
