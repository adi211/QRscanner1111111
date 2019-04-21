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
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WhoInB extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView ListW;
    ArrayList<String> list;
    DatabaseReference ref;
    SearchView searchBar;
    String query,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_in_b);

        final String loc = getIntent().getStringExtra("location");
        ListW =  findViewById(R.id.ListW);
        ref = FirebaseDatabase.getInstance().getReference().child("Places").child(loc).child("WIIMB");

        query = "";
        searchBar = findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query = searchBar.getQuery().toString();
                refreshList11();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                query = searchBar.getQuery().toString();
                refreshList11();
                return false;
            }
        });

        ListW.setOnItemClickListener(this);



        refreshList11();
    }

    private void refreshList11() {
        list=new ArrayList <String>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Users u = dataSnapshot1.getValue(Users.class);
                    String name = u.getEmail();
                    if (isInSearch(name))
                     list.add(name);
                }
                ArrayAdapter<String> adp=new ArrayAdapter<String>(WhoInB.this,R.layout.support_simple_spinner_dropdown_item,list);
                ListW.setAdapter(adp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean isInSearch(String st)
    {
        return st.toLowerCase().contains(query.toLowerCase());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        email=list.get(i);


        Intent j = new Intent(WhoInB.this, ShowProfile.class);
        j.putExtra("email",email);
        startActivity(j);

    }




}
