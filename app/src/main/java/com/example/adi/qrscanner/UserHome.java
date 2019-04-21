package com.example.adi.qrscanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.webkit.WebSettings.PluginState;
import android.widget.Toast;




import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class UserHome extends AppCompatActivity {


    WebView wb;
    FirebaseAuth firebaseAuth;
    String email, url, str;
    TextView tv;
    FirebaseUser firebaseUser;
    FloatingActionButton fab1, fab2, fab3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);




        fab1 = findViewById(R.id.fab11);
        fab2 = findViewById(R.id.fab12);
        fab3 = findViewById(R.id.fab13);



        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(UserHome.this, loginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserHome.this, Credits.class);
                startActivity(i);
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserHome.this, ChangePic.class);
                startActivity(i);
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        wb = (WebView) findViewById(R.id.wb);
        email = firebaseAuth.getCurrentUser().getEmail();

        url = "http://chart.apis.google.com/chart?cht=qr&chs=400x300&chl=" + email + "&chld=H|0";
        wb.loadUrl(url);

    }


    @Override
    public void onBackPressed() {

    }

    public void addLocation(View view) {
        Intent t = new Intent(this, AddLocation.class);
        startActivity(t);
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public void aaa(View v) {
        Intent t = new Intent(this, SearchPlace.class);
        startActivity(t);
    }





}
