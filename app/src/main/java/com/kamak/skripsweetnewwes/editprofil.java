package com.kamak.skripsweetnewwes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class editprofil extends AppCompatActivity {
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public final static String TAG_pswd = "pswd";
    private String URL_EDITPROFIL = Server.URL+"editprofil.php";
    public static final String TAG_ID = "iduser";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_ALAMAT = "alamat";
    public static final String TAG_NOHP = "nohp";
    public static final String tgldaftar = "tgldaftar";
    String tag_json_obj = "json_obj_req";
    String iduser, user_name,alamat,nohp,pswd,tanggaldaftar;
    SharedPreferences sharedpreferences;
    EditText txt_alamat,txtnama,txtid,txtnohp,txtpswd,txttgl;
    Button btnsimpan,btnbatal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofil);
        iduser = getIntent().getStringExtra(TAG_ID);
        user_name = getIntent().getStringExtra(TAG_USERNAME);
        alamat = getIntent().getStringExtra(TAG_ALAMAT);
        nohp = getIntent().getStringExtra(TAG_NOHP);
        pswd = getIntent().getStringExtra(TAG_pswd);

        tanggaldaftar = getIntent().getStringExtra(tgldaftar);

        btnsimpan=(Button)findViewById(R.id.btnsave);
        btnbatal=(Button)findViewById(R.id.btnbatal);

        txt_alamat = (EditText) findViewById(R.id.txt_komen);
        txtnama = (EditText) findViewById(R.id.txt_nama);
        txtnohp = (EditText) findViewById(R.id.txt_nohp);

        txtpswd = (EditText) findViewById(R.id.txt_password);

        txtnama.setText(user_name);
        txt_alamat.setText(alamat);
        txtnohp.setText(nohp);
        txtpswd.setText(pswd);
        btnbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(editprofil.this, profil_user.class);
                intent.putExtra(TAG_ID, iduser);
                intent.putExtra(TAG_USERNAME, user_name);
                intent.putExtra(TAG_ALAMAT, alamat);
                intent.putExtra(TAG_NOHP, nohp);
                finish();
                startActivity(intent);
            }
        });
        btnsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEmployee();
            }
        });


    }
    private void updateEmployee(){
        final String alamat = txt_alamat.getText().toString().trim();
        final String nama = txtnama.getText().toString().trim();
        final String password = txtpswd.getText().toString().trim();

        final String nohp = txtnohp.getText().toString().trim();



        class UpdateEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(editprofil.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(editprofil.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(TAG_ID,iduser);
                hashMap.put(TAG_ALAMAT,alamat);
                hashMap.put(TAG_USERNAME,nama);
                hashMap.put(TAG_NOHP,nohp);
                hashMap.put(TAG_pswd,password);


                reqhandler rh = new reqhandler();

                String s = rh.sendPostRequest(URL_EDITPROFIL,hashMap);

                return s;
            }
        }

        UpdateEmployee ue = new UpdateEmployee();
        ue.execute();
    }
    public void onBackPressed() {
        Intent intent = new Intent(editprofil.this, profil_user.class);
        intent.putExtra(TAG_ID, iduser);
        intent.putExtra(TAG_USERNAME, user_name);
        intent.putExtra(TAG_ALAMAT, alamat);
        intent.putExtra(TAG_NOHP, nohp);
        finish();
        startActivity(intent);
    }
}
