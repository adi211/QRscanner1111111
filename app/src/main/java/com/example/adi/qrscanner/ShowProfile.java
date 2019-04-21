package com.example.adi.qrscanner;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShowProfile extends AppCompatActivity {

    TextView showName,showId,tv;
    ImageView showImage;
    String email,Id,url,name;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        showId=findViewById(R.id.showId);
        showName=findViewById(R.id.showName);
        showImage=findViewById(R.id.showImage);
        tv=findViewById(R.id.textView16);
        email = getIntent().getStringExtra("email");
        tv.setText("User '"+email+"' page");




        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Users u = ds.getValue(Users.class);
                    if (u.getEmail().equals(email))
                    {
                        url=ds.child("imageUrl").getValue().toString();
                        Picasso.get().load(url).into(showImage);
                        Id=ds.child("userID").getValue().toString();
                        showId.setText("Id: "+Id);
                        name=ds.child("name").getValue().toString();
                        showName.setText("Name: "+name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
