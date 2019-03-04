package com.example.adi.qrscanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.Placeholder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class SearchPlace extends AppCompatActivity implements AdapterView.OnItemClickListener {

    DatabaseReference ref;
    SearchView searchBar;
    ArrayList<String> list;
    ListView recycler;
    String query;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        query = "";
        searchBar = findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query = searchBar.getQuery().toString();
                refreshList();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                query = searchBar.getQuery().toString();
                refreshList();
                return false;
            }
        });
        ref = FirebaseDatabase.getInstance().getReference().child("Places");
        firebaseAuth=FirebaseAuth.getInstance();
        recycler =  findViewById(R.id.recy);
        recycler.setOnItemClickListener(this);
        refreshList();

    }

    public void refreshList()
    {
        list=new ArrayList <String>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String address = dataSnapshot1.child("address").getValue(String.class);
                    String name = dataSnapshot1.getKey();
                   if (isInSearch(address)||(isInSearch(name))){
                       list.add(name + "\n" + address);
                   }
                }

                ArrayAdapter <String>adp=new ArrayAdapter<String>(SearchPlace.this,R.layout.support_simple_spinner_dropdown_item,list);
                recycler.setAdapter(adp);


                //RequestList adapter = new RequestList(SearchPlace.this,list);
               // recycler.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchPlace.this,"Opss.... something is wrong",Toast.LENGTH_SHORT);
            }
        });
    }
    public boolean isInSearch(String st)
    {
        return st.toLowerCase().contains(query.toLowerCase());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final int x = i;
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean isAdmin = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int p = 0;
                boolean found=false;
                 String v=null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    if ((p == x)&&((found==false))) {
                        DataSnapshot s  = snapshot.child("admins");
                        found=true;
                        if (searchforAdmin(s,x))
                            isAdmin = true;
                    }
                    else p++;
                }
                if (!isAdmin)
                    showDialog(list.get(x).split("\n")[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }private boolean searchforAdmin(DataSnapshot s,final int x) {
        String email = firebaseAuth.getCurrentUser().getEmail();
        for (DataSnapshot adminSnapshot : s.getChildren())
        {
            if (adminSnapshot.child("email").getValue(String.class).equals(email))
            {

                Intent t = new Intent(SearchPlace.this, MainActivity.class);
                t.putExtra("location", list.get(x).split("\n")[0]);
                startActivity(t);
                return true;
            }
        }
        return false;
    }

    private void showDialog(final String loc) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle("Options");
        adb.setMessage("Choose the type of permission you would like to request.");

        final String id = ref.push().getKey();
        final String email = firebaseAuth.getCurrentUser().getEmail();

        adb.setPositiveButton("User", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Add user request
                Request request = new Request(id, email, false);
                ref.child(loc).child("requests").child(id).setValue(request);
                Toast.makeText(SearchPlace.this, "A request to be a user has been sent", Toast.LENGTH_LONG).show();
                dialogInterface.dismiss();
            }
        });

        adb.setNegativeButton("Admin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Request request = new Request(id, email, true);
                ref.child(loc).child("requests").child(id).setValue(request);
                Toast.makeText(SearchPlace.this, "A request to be an admin has been sent", Toast.LENGTH_LONG).show();
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
}
