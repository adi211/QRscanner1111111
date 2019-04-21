package com.example.adi.qrscanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SingUpActivity extends AppCompatActivity {
    private EditText TextEmailReg,TextPassReg,NameReg,IdReg;
    private FirebaseAuth firebaseAuth;
    DatabaseReference ref;

    private ImageButton mSelectImage;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT=2;
    String ImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        ImageUrl="https://firebasestorage.googleapis.com/v0/b/qrscanner-c4546.appspot.com/o/Photos%2FnonProfilePhoto.jpg?alt=media&token=f63bd234-8510-44b6-933c-23229cba0007";

        TextEmailReg=(EditText) findViewById(R.id.TextEmailReg);
        TextPassReg=(EditText) findViewById(R.id.TextPassReg);
        firebaseAuth=FirebaseAuth.getInstance();

        mSelectImage=findViewById(R.id.SelIMa);
        mStorage= FirebaseStorage.getInstance().getReference();
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        NameReg=(EditText) findViewById(R.id.NameReg);
        IdReg=(EditText) findViewById(R.id.IdReg);
        ref=FirebaseDatabase.getInstance().getReference("Users");




        TextView tv = (TextView)findViewById(R.id.textView17);

        Spannable wordtoSpan = new SpannableString("SingUp to WIIMB");
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new ForegroundColorSpan(0xFFC51010), 10, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(wordtoSpan);





}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK){
            final Uri uri= data.getData();
            final StorageReference filepath=mStorage.child("Photos").child(uri.getLastPathSegment());

            filepath.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        mSelectImage.setImageURI(uri);
                        Uri downloadUri = task.getResult();
                        Toast.makeText(SingUpActivity.this, "upload done", Toast.LENGTH_SHORT).show();
                        ImageUrl =downloadUri.toString();

                    } else {
                        Toast.makeText(SingUpActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
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
                                               //ImageUrl="adi";
                                               Users user = new Users(id, TextEmailReg.getText().toString(),ImageUrl , IdReg.getText().toString(),NameReg.getText().toString() );//TextPassReg.getText().toString()
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
                                   //Toast.makeText(SingUpActivity.this, ImageUrl, Toast.LENGTH_LONG).show();
                               }
                           }
                       });
          // }
          // else Toast.makeText(this, "Please enter real id", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(SingUpActivity.this, "Please enter all the information", Toast.LENGTH_LONG).show();

    }
    public static boolean inTerface( String id) {
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




