package com.example.adi.qrscanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static TextView resultTV,tv;
    Button scan_btn,add;
    private FirebaseAuth firebaseAuth;
    String a, location;
    DatabaseReference ref,refU;
    ListView list;
    List<Request> requestList;
    ArrayList<String> list1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTV=(TextView)findViewById(R.id.result_text);
        scan_btn=(Button) findViewById(R.id.btn_scan);
        tv=(TextView)findViewById(R.id.textView5);
        add=(Button) findViewById(R.id.add);
        list = (ListView) findViewById(R.id.list);
        firebaseAuth=FirebaseAuth.getInstance();

        list.setOnItemClickListener(this);

        requestList = new ArrayList<>();
        location = getIntent().getStringExtra("location");
        //a=firebaseAuth.getCurrentUser().getEmail();
        tv.setText("Welcome to "+location.toUpperCase()+" menager page");

        ref = FirebaseDatabase.getInstance().getReference().child("Places");
        refU=FirebaseDatabase.getInstance().getReference().child("Users");

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ScanCodeActivity.class));
            }
        });


    }

    @Override
    protected void onStart() {
        String loc = getIntent().getStringExtra("location");
        ref.child(loc).child("requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    requestList.add(ds.getValue(Request.class));
                RequestList adapter = new RequestList(MainActivity.this, requestList);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onStart();
    }


    public void addto(View v){
        final String email = resultTV.getText().toString();
        ref.child(getIntent().getStringExtra("location")).child("permission").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot adiHagever : dataSnapshot.getChildren())
                {
                    Users u = adiHagever.getValue(Users.class);
                    if (email.equals(u.getEmail()))
                    {
                        found = true;
                        imb("WIIMB");
                    }
                }
                if (!found)
                    Toast.makeText(MainActivity.this, "No permission", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void imb(String t){
        final String email = resultTV.getText().toString();
        final String loc = getIntent().getStringExtra("location");
        final String type = t;
        ref.child(loc).child(type).addListenerForSingleValueEvent(new ValueEventListener() {
            boolean found = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Users u = ds.getValue(Users.class);
                    if (email.equals(u.getEmail())){
                        Toast.makeText(MainActivity.this, "This user has been removed", Toast.LENGTH_LONG).show();
                        ref.child(loc).child(type).child(u.getId()).removeValue();
                        found = true;
                    }
                }
                if (!found)
                    checkAndAdd(email, type);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkAndAdd(final String email, final String type) {
        refU.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Users u = ds.getValue(Users.class);
                    if (email.equals(u.getEmail()))
                    {
                        //Found
                        if (type.equals("admins")) {
                            ref.child(getIntent().getStringExtra("location")).child("permission").child(u.getId()).removeValue();
                            ref.child(getIntent().getStringExtra("location")).child("WIIMB").child(u.getId()).removeValue();
                        }
                        ref.child(getIntent().getStringExtra("location")).child(type).child(u.getId()).setValue(u);
                        Toast.makeText(MainActivity.this, "This user has been added", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void qwe(View view){
        Intent i = new Intent(MainActivity.this, WhoInB.class);
        final String loc = getIntent().getStringExtra("location");
        i.putExtra("location",loc);
        startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        final Request request = requestList.get(i);

        adb.setTitle("Options");
        adb.setMessage("Grant permission?");

        final String id = ref.push().getKey();
        final String email = firebaseAuth.getCurrentUser().getEmail();

        adb.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkAndAdd(request.getEmail(),request.isType()? "admins" : "permission");
                ref.child(getIntent().getStringExtra("location")).child("requests").child(request.getId()).removeValue();
                dialogInterface.dismiss();
            }
        });

        adb.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ref.child(getIntent().getStringExtra("location")).child("requests").child(request.getId()).removeValue();
                dialogInterface.dismiss();
            }
        });

        adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog ad = adb.create();
        ad.show();
    }

    public void zxc(View view){
        Intent i = new Intent(MainActivity.this, WhoHaveP.class);
        final String loc = getIntent().getStringExtra("location");
        i.putExtra("location",loc);
        startActivity(i);
    }
}
