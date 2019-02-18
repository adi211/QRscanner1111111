package com.example.adi.qrscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public static TextView resultTV,tv;
    Button scan_btn;
    private FirebaseAuth firebaseAuth;
    String a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTV=(TextView)findViewById(R.id.result_text);
        scan_btn=(Button) findViewById(R.id.btn_scan);
        tv=(TextView)findViewById(R.id.textView5);
        firebaseAuth=FirebaseAuth.getInstance();

        a=firebaseAuth.getCurrentUser().getEmail();
        tv.setText(a);

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ScanCodeActivity.class));
            }
        });
    }
    public void cl (View v){
        Intent i = new Intent(MainActivity.this, LoginOrSingup.class);
        startActivity(i);
    }
}
