package com.example.adi.qrscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.webkit.WebSettings.PluginState;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserHome extends AppCompatActivity {


    WebView wb;
    FirebaseAuth firebaseAuth;
    String email, url, str;
    TextView tv;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        wb = (WebView) findViewById(R.id.wb);
        email = firebaseAuth.getCurrentUser().getEmail();

        url = "http://chart.apis.google.com/chart?cht=qr&chs=400x300&chl=" + email + "&chld=H|0";
        wb.loadUrl(url);
    }



    public void addLocation(View view) {
        Intent t = new Intent(this, AddLocation.class);
        startActivity(t);
    }


    public class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url){
            view.loadUrl(url);
            return true;
        }
    }
    public void aaa(View v){
        Intent t = new Intent(this, SearchPlace.class);
        startActivity(t);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        menu.add("LogOut");
        menu.add("Credits");
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        String st=item.getTitle().toString();
        if (st.equals("LogOut")){
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(UserHome.this, loginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        if (st.equals("Credits")){
            Intent i = new Intent(UserHome.this, Credits.class);
            startActivity(i);
        }
         return true;
    }
}
