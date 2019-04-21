package com.example.adi.qrscanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AddLocation extends AppCompatActivity {

    EditText name, address;
    DatabaseReference ref,ref1,ref2;
    FirebaseAuth firebaseAuth ,Auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        name = findViewById(R.id.name);
        address = findViewById(R.id.address);


        ref = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();


        Auth = FirebaseAuth.getInstance();
        user=Auth.getCurrentUser();
        ref2 = FirebaseDatabase.getInstance().getReference().child("Users");


        ref1 = FirebaseDatabase.getInstance().getReference().child("Places");
    }




    public void create(View view) {
        final String n = name.getText().toString();
        final String a = address.getText().toString();

        if ((!n.isEmpty() && !a.isEmpty())) {

            ref1.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean exist=false;
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        if (ds.getKey().equals(n)){
                            Toast.makeText(AddLocation.this, "this place have been created. Try another name", Toast.LENGTH_LONG).show();
                            exist=true;
                        }
                    }
                    if (!exist)
                        addVal(n,a);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }



    public void addVal(final String n,final String a) {

        ref.child("Places").child(n).child("address").setValue(a);
        final String email = firebaseAuth.getCurrentUser().getEmail();
        ref.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Users u = ds.getValue(Users.class);
                    if (u.getEmail().equals(email)) {
                        ref.child("Places").child(n).child("admins").child(u.getId()).setValue(u);
                        Toast.makeText(AddLocation.this, "This place was created successfully. Now you can manage your place", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Intent t = new Intent(this, UserHome.class);
        startActivity(t);
    }
}
