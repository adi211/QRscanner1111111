package com.example.adi.qrscanner;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WhoHaveP extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView ListW;
    ArrayList<String> list;
    DatabaseReference ref,ref1;
    SearchView searchBar;
    String query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_have_p);
        final String loc = getIntent().getStringExtra("location");
        ListW =  findViewById(R.id.li);
        ref = FirebaseDatabase.getInstance().getReference().child("Places").child(loc).child("permission");
        ListW.setOnItemClickListener(this);
        ref1 = FirebaseDatabase.getInstance().getReference().child("Places").child(loc);



        query = "";
        searchBar = findViewById(R.id.ser);
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
                ArrayAdapter<String> adp=new ArrayAdapter<String>(WhoHaveP.this,R.layout.support_simple_spinner_dropdown_item,list);
                ListW.setAdapter(adp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final int position=i;
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setMessage("Do you want to cancel permission for this user?");
        adb.setNeutralButton("Cancel permission", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               remove(position);
               dialogInterface.dismiss();
            }
        });
        adb.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        adb.show();

    }


    public void remove(final int position){

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String selectedFromList = (String) ListW.getItemAtPosition(position);
                    Users u = ds.getValue(Users.class);
                    if (selectedFromList.equals(u.getEmail()))
                    {
                        //Found
                        ref.child(u.getId()).removeValue();
                        ref1.child("WIIMB").child(u.getId()).removeValue();
                    }
                }
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

}

