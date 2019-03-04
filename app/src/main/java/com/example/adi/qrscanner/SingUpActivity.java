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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SingUpActivity extends AppCompatActivity {
    private EditText TextEmailReg,TextPassReg,NameReg,IdReg;
    private FirebaseAuth firebaseAuth;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        TextEmailReg=(EditText) findViewById(R.id.TextEmailReg);
        TextPassReg=(EditText) findViewById(R.id.TextPassReg);
        firebaseAuth=FirebaseAuth.getInstance();

        NameReg=(EditText) findViewById(R.id.NameReg);
        IdReg=(EditText) findViewById(R.id.IdReg);
        ref=FirebaseDatabase.getInstance().getReference("Users");




    }

    public void buttonSingUp_click(View v) {
        if (!(IdReg.getText().toString().isEmpty() || NameReg.getText().toString().isEmpty())) {

           //if (inTerface(IdReg.getText().toString())) {
               final ProgressDialog progressDialog = ProgressDialog.show(SingUpActivity.this, "Please wait...", "Processing...", true);
               (firebaseAuth.createUserWithEmailAndPassword(TextEmailReg.getText().toString(), TextPassReg.getText().toString()))
                       .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               progressDialog.dismiss();

                               if (task.isSuccessful()) {
                                   firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()) {
                                               String id = ref.push().getKey();
                                               Users user = new Users(id, TextEmailReg.getText().toString(), TextPassReg.getText().toString(), IdReg.getText().toString(), NameReg.getText().toString());
                                               ref.child(id).setValue(user);

                                               Toast.makeText(SingUpActivity.this, "SingUp successfully. Please check your email for verification", Toast.LENGTH_LONG).show();
                                               Intent i = new Intent(SingUpActivity.this, loginActivity.class);
                                               startActivity(i);
                                           } else {
                                               Toast.makeText(SingUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                           }
                                       }
                                   });


                               } else {
                                   Log.e("Error", task.getException().toString());
                                   Toast.makeText(SingUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                               }
                           }
                       });
          // }
          // else Toast.makeText(this, "Please enter real id", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(SingUpActivity.this, "Please enter all the information", Toast.LENGTH_LONG).show();

    }
    public static boolean inTerface(String id) {
        int sum, bikoret;


        if(id.length()==9)
        {
            sum = idVerification(id);
            bikoret = Character.getNumericValue(id.charAt(8));

            if((sum+bikoret)%10==0)
                return true;

            else
                return false;
        }
        else
        {
           return false;

        }

    }

    public static int idVerification(String id) {
        String a;
        int sum=0, i, j, num;

        for(i=0; i<8; i++)
        {
            if(i==0 || i%2==0)
            {
                num = Character.getNumericValue(id.charAt(i));
                sum = sum+num;
            }
            else
            {
                num = Character.getNumericValue(id.charAt(i));
                if((num*2)>10)
                {
                    a = Integer.toString(num*2);
                    for(j=0; j<2; j++)
                    {
                        num = Character.getNumericValue(a.charAt(j));
                        sum = sum+num;
                    }
                }
                else
                    sum = sum+(num*2);
            }
        }

        return sum;
    }


}







