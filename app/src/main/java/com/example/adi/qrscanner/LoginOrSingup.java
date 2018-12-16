package com.example.adi.qrscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginOrSingup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_singup);
    }
    public void btnRegistration_Click(View v){
        Intent i=new Intent(LoginOrSingup.this,SingUpActivity.class);
        startActivity(i);
    }
    public void btnLogin_Click(View v){
        Intent i=new Intent(LoginOrSingup.this,loginActivity.class);
        startActivity(i);
    }
}
