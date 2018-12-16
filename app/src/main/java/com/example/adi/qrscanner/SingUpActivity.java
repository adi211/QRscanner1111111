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

public class SingUpActivity extends AppCompatActivity {
    private EditText TextEmailReg,TextPassReg;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        TextEmailReg=(EditText) findViewById(R.id.TextEmailReg);
        TextPassReg=(EditText) findViewById(R.id.TextPassReg);
        firebaseAuth=FirebaseAuth.getInstance();


    }
    public void buttonSingUp_click(View v){
        final ProgressDialog progressDialog=ProgressDialog.show(SingUpActivity.this,"Please wait...","Procassing...",true);
        (firebaseAuth.createUserWithEmailAndPassword(TextEmailReg.getText().toString(),TextPassReg.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(SingUpActivity.this, "SingUp Successful", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(SingUpActivity.this, loginActivity.class);
                            startActivity(i);
                        }
                        else {
                            Log.e("Error",task.getException().toString());
                            Toast.makeText(SingUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}

