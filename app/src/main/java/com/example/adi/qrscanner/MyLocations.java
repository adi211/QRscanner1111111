package com.example.adi.qrscanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyLocations extends AppCompatActivity {
  ListView list;
  List locations;
  DatabaseReference ref;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_locations);

    list = findViewById(R.id.list);
    ref = FirebaseDatabase.getInstance().getReference("Places");
  }
}
