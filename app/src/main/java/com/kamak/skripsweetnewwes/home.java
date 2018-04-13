package com.kamak.skripsweetnewwes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kamak.skripsweetnewwes.slider.SliderIndicator;
import com.kamak.skripsweetnewwes.slider.SliderPagerAdapter;
import com.kamak.skripsweetnewwes.slider.SliderView;

public class home extends AppCompatActivity {
    Button btnlogout,btnberita,btnprofil,btnpost,btndasar,btnpetunjuk;
    Intent intent;
    String id, username,alamat,nohp,pswd;
    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    public static final String TAG_ID = "iduser";
    public static final String TAG_USERNAME = "username";
    public final static String TAG_ALAMAT = "alamat";
    public final static String TAG_NOHP = "nohp";
    public final static String TAG_pswd = "pswd";
    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;

    private SliderView sliderView;
    private LinearLayout mLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        GPSTracker gps = new GPSTracker(home.this);
        btnberita=(Button)findViewById(R.id.button4);
        btnpost=(Button)findViewById(R.id.button5);
        btnprofil=(Button)findViewById(R.id.button6);

        btnlogout=(Button)findViewById(R.id.button9);
        sharedpreferences = getSharedPreferences(login.my_shared_preferences, Context.MODE_PRIVATE);
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setMessage("GPS Off, Buka Setting dan Aktifkan GPS?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final android.support.v7.app.AlertDialog alert = builder.create();
            alert.show();
        } else {
            Toast.makeText(getApplicationContext(), "GPS On", Toast.LENGTH_LONG).show();
        }
        id = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);
        alamat = getIntent().getStringExtra(TAG_ALAMAT);
        nohp= getIntent().getStringExtra(TAG_NOHP);
        pswd= getIntent().getStringExtra(TAG_pswd);

        btnberita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                berita();
            }
        });

        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });
        btnprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, profil_user.class);

                intent.putExtra(TAG_ID, id);
                intent.putExtra(TAG_USERNAME, username);

                finish();
                startActivity(intent);
            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               DialogForm();
            }
        });
    }
    private void post() {

        final CharSequence[] items = {"Kirim Usulan","Kirim Review","Simpan Sebagai Draft","Batal"};

        AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
        builder.setTitle("Pilihan");
        builder.setIcon(R.drawable.kamera);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Kirim Usulan")) {
                    Intent intent = new Intent(home.this, foto.class);

                    intent.putExtra(TAG_ID, id);
                    intent.putExtra(TAG_USERNAME, username);
                    finish();
                    startActivity(intent);

                } else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
                else if (items[item].equals("Kirim Review")) {
                    Intent intent = new Intent(home.this, post_review.class);


                    intent.putExtra(TAG_ID, id);
                    intent.putExtra(TAG_USERNAME, username);
                    finish();
                    startActivity(intent);

                }
                else if (items[item].equals("Simpan Sebagai Draft")) {
                    Intent intent = new Intent(home.this, draft.class);


                    intent.putExtra(TAG_ID, id);
                    intent.putExtra(TAG_USERNAME, username);
                    finish();
                    startActivity(intent);

                }

            }
        });
        builder.show();
    }


    private void berita() {

        final CharSequence[] items = {"Semua Usulan","Proses Perbaikan","Selesai Perbaikan","Review","Draft","Batal"};

        AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
        builder.setTitle("Pilihan");
        builder.setIcon(R.drawable.news);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Semua Usulan")) {
                    Intent intent = new Intent(home.this, all.class);


                    intent.putExtra(TAG_ID, id);
                    intent.putExtra(TAG_USERNAME, username);
                    finish();
                    startActivity(intent);

                } else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
                else if (items[item].equals("Proses Perbaikan")) {
                    Intent intent = new Intent(home.this, proses_perbaikan.class);

                    intent.putExtra(TAG_ID, id);
                    intent.putExtra(TAG_USERNAME, username);

                    finish();
                    startActivity(intent);

                }
                else if (items[item].equals("Selesai Perbaikan")) {
                    Intent intent = new Intent(home.this, selesai_perbaikan.class);


                    intent.putExtra(TAG_ID, id);
                    intent.putExtra(TAG_USERNAME, username);
                    finish();
                    startActivity(intent);

                }
                else if (items[item].equals("Draft")) {
                    Intent intent = new Intent(home.this, draft.class);


                    intent.putExtra(TAG_ID, id);
                    intent.putExtra(TAG_USERNAME, username);
                    finish();
                    startActivity(intent);

                }
                else if (items[item].equals("Review")) {
                    Intent intent = new Intent(home.this, reviewnews.class);


                    intent.putExtra(TAG_ID, id);
                    intent.putExtra(TAG_USERNAME, username);
                    finish();
                    startActivity(intent);

                }
            }
        });
        builder.show();
    }
    private void DialogForm() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Apakah anda yakin ?")
                .setCancelable(false)//tidak bisa tekan tombol back
                //jika pilih1 yess
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(login.session_status, false);
                        editor.putString(TAG_ID, null);
                        editor.putString(TAG_USERNAME, null);
                        editor.commit();

                        Intent intent = new Intent(home.this, login.class);
                        finish();
                        startActivity(intent);
                    }
                })
                //jika pilih no
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }
    boolean doubleBackToExitPressedOnce = false;

    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }//fungsi tekan back 2 kali
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik BACK dua kali untuk exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;

            }
        }, 2000);//fungsi panggil jika tekan 1 kali
    }

}
