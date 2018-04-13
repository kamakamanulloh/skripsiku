package com.kamak.skripsweetnewwes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class profil extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
Button btnedit;



    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public final static String TAG_pswd = "pswd";

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_ALAMAT = "alamat";
    public static final String TAG_NOHP = "nohp";
    public static final String tgldaftar = "tgldaftar";
    String tag_json_obj = "json_obj_req";
    String iduser, user_name,alamat,nohp,pswd,tanggaldaftar;
    SharedPreferences sharedpreferences;
    TextView txt_alamat,txtnama,txtid,txtnohp,txtpswd,txttgl;

Button btnberitasaya,logout,btn_profil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        sharedpreferences = getSharedPreferences(MainActivity.my_shared_preferences, Context.MODE_PRIVATE);

        iduser = getIntent().getStringExtra(TAG_ID);
        user_name = getIntent().getStringExtra(TAG_USERNAME);
        alamat = getIntent().getStringExtra(TAG_ALAMAT);
        nohp = getIntent().getStringExtra(TAG_NOHP);
        pswd = getIntent().getStringExtra(TAG_pswd);

        tanggaldaftar = getIntent().getStringExtra(tgldaftar);



         txt_alamat = (TextView) findViewById(R.id.txtalamat_);
         txtnama = (TextView) findViewById(R.id.txt_username);
        txtnohp = (TextView) findViewById(R.id.txtnohp_);
        txtpswd = (TextView) findViewById(R.id.txtpswd);
        txtpswd = (TextView) findViewById(R.id.txtpswd);

        txtnama.setText(user_name);
        txt_alamat.setText("alamat "+alamat);
        txtnohp.setText("Nomor Handphone "+nohp);
        txtpswd.setText("Kata Sandi "+pswd);




    }
    public void onBackPressed() {
        Intent intent = new Intent(profil.this, MenuActivity.class);
        intent.putExtra(TAG_ID, iduser);
        intent.putExtra(TAG_USERNAME, user_name);
        intent.putExtra(TAG_ALAMAT, alamat);
        intent.putExtra(TAG_NOHP, nohp);
        intent.putExtra(TAG_pswd, pswd);
        finish();
        startActivity(intent);
    }

    private void DialogForm() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Apakah anda yakin ?")
                .setCancelable(false)//tidak bisa tekan tombol back
                //jika pilih1 yess
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(MenuActivity.session_status, false);
                        editor.putString(TAG_ID, null);
                        editor.putString(TAG_USERNAME, null);
                        editor.commit();

                        Intent intent = new Intent(profil.this, MainActivity.class);
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

}
