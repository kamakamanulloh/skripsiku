package com.kamak.skripsweetnewwes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class berita extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    Button all,proses,selesai;
    String id, username,alamat,nohp;
    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_ALAMAT    = "alamat";
    public final static String TAG_NOHP = "nohp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita2);
        sharedpreferences = getSharedPreferences(MainActivity.my_shared_preferences, Context.MODE_PRIVATE);

        id = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);
        all = (Button) findViewById(R.id.all);
        all.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

                Intent intent = new Intent(berita.this, all.class);

                intent.putExtra(TAG_ID, id);
                intent.putExtra(TAG_USERNAME, username);
                intent.putExtra(TAG_ALAMAT, alamat);
                intent.putExtra(TAG_NOHP, nohp);
                finish();
                startActivity(intent);
            }

        });
        proses = (Button) findViewById(R.id.proses);
        proses.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

                Intent intent = new Intent(berita.this, proses_perbaikan.class);

                intent.putExtra(TAG_ID, id);
                intent.putExtra(TAG_USERNAME, username);
                intent.putExtra(TAG_ALAMAT, alamat);
                intent.putExtra(TAG_NOHP, nohp);


                finish();
                startActivity(intent);
            }

        });
        selesai = (Button) findViewById(R.id.selesai);
        selesai.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

                Intent intent = new Intent(berita.this, selesai_perbaikan.class);

                intent.putExtra(TAG_ID, id);
                intent.putExtra(TAG_USERNAME, username);

                intent.putExtra(TAG_ALAMAT, alamat);
                intent.putExtra(TAG_NOHP, nohp);
                finish();
                startActivity(intent);
            }

        });

    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik BACK dua kali untuk exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;

            }
        }, 2000);
    }
}
