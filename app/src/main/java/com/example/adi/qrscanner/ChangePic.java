package com.example.adi.qrscanner;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ChangePic extends AppCompatActivity {
    DatabaseReference ref2,ref;
    StorageReference mStorage;
    FirebaseUser user;
    String urlU,ImageUrl;
    ImageView pic;
    FirebaseAuth Auth;
    private static final int GALLERY_INTENT=2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pic);

        pic=findViewById(R.id.pic);
        mStorage= FirebaseStorage.getInstance().getReference();
        Auth = FirebaseAuth.getInstance();
        user=Auth.getCurrentUser();

        ref2 = FirebaseDatabase.getInstance().getReference().child("Users");


        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String em=user.getEmail();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Users u = ds.getValue(Users.class);
                    if (u.getEmail().equals(em))
                    {
                        urlU=ds.child("imageUrl").getValue().toString();
                        Picasso.get().load(urlU).into(pic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Change (View v){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);
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
                        pic.setImageURI(uri);
                        Uri downloadUri = task.getResult();

                        ImageUrl =downloadUri.toString();

                            ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String em=user.getEmail();
                                for (DataSnapshot ds : dataSnapshot.getChildren())
                                {
                                    Users u = ds.getValue(Users.class);
                                    if (u.getEmail().equals(em))
                                    {
                                        ref2.child(u.getId()).child("imageUrl").setValue(ImageUrl);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(ChangePic.this, "upload done", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ChangePic.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void remove (View v){
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String em=user.getEmail();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Users u = ds.getValue(Users.class);
                    if (u.getEmail().equals(em))
                    {
                        ref2.child(u.getId()).child("imageUrl").setValue("https://firebasestorage.googleapis.com/v0/b/qrscanner-c4546.appspot.com/o/Photos%2FnonProfilePhoto.jpg?alt=media&token=f63bd234-8510-44b6-933c-23229cba0007");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/qrscanner-c4546.appspot.com/o/Photos%2FnonProfilePhoto.jpg?alt=media&token=f63bd234-8510-44b6-933c-23229cba0007").into(pic);
        Toast.makeText(this, "The image have been removed", Toast.LENGTH_SHORT).show();

    }

}
