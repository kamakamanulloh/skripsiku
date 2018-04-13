package com.kamak.skripsweetnewwes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class profil_user extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    Button btnedit;



    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public final static String TAG_pswd = "pswd";

    public static final String TAG_ID = "iduser";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_ALAMAT = "alamat";
    public static final String TAG_NOHP = "nohp";
    public static final String tgldaftar = "tgldaftar";
    String tag_json_obj = "json_obj_req";
    String id, user_name,alamat,nohp,pswd,tanggaldaftar,name;
    SharedPreferences sharedpreferences;
    TextView txt_alamat,txtnama,txtid,txtnohp,txtpswd,txttgl;

    Button btnberitasaya,logout,btn_profil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        sharedpreferences = getSharedPreferences(MainActivity.my_shared_preferences, Context.MODE_PRIVATE);
        btnedit=(Button)findViewById(R.id.btnedit);
        id = getIntent().getStringExtra(TAG_ID);
        user_name = getIntent().getStringExtra(TAG_USERNAME);
        alamat = getIntent().getStringExtra(TAG_ALAMAT);
        nohp = getIntent().getStringExtra(TAG_NOHP);
        pswd = getIntent().getStringExtra(TAG_pswd);
        btnberitasaya=(Button)findViewById(R.id.btnmynews);
        btnberitasaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=(new Intent(profil_user.this,lvmynews.class));
                intent.putExtra(TAG_ID,id);
                intent.putExtra(TAG_USERNAME,user_name);
                finish();
                startActivity(intent);
            }
        });

        tanggaldaftar = getIntent().getStringExtra(tgldaftar);

        txt_alamat = (TextView) findViewById(R.id.txtalamat_);
        txtnama = (TextView) findViewById(R.id.txt_username);
        txtnohp = (TextView) findViewById(R.id.txtnohp_);
        txtpswd = (TextView) findViewById(R.id.txtpswd);
        txtpswd = (TextView) findViewById(R.id.txtpswd);
        getEmployee();
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(profil_user.this,editprofil.class);
                intent.putExtra(TAG_ID, id);
                intent.putExtra(TAG_USERNAME, name);
                intent.putExtra(konfigurasi.KEY_alamat, alamat);
                intent.putExtra(konfigurasi.TAG_pswd, pswd);
                intent.putExtra(konfigurasi.TAG_nohp, nohp);

                finish();
                startActivity(intent);

            }
        });
    }
    private void getEmployee(){
        class GetEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(profil_user.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showEmployee(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                reqhandler rh = new reqhandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_profil,id);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();
    }

    private void showEmployee(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
             name = c.getString(konfigurasi.KEY_NAMA);
            id = c.getString(konfigurasi.TAG_id);
alamat = c.getString(konfigurasi.KEY_alamat);
            pswd = c.getString(konfigurasi.TAG_pswd);
           nohp = c.getString(konfigurasi.TAG_nohp);


            txtnama.setText(name);
            txt_alamat.setText("alamat "+alamat);
            txtnohp.setText("Nomor Handphone "+nohp);
            txtpswd.setText("Kata Sandi "+pswd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void onBackPressed() {
        Intent intent = new Intent(profil_user.this, home.class);
        intent.putExtra(TAG_ID, id);
        intent.putExtra(TAG_USERNAME, name);
        intent.putExtra(konfigurasi.KEY_alamat, alamat);
        intent.putExtra(konfigurasi.TAG_pswd, pswd);
        intent.putExtra(konfigurasi.TAG_nohp, nohp);

        finish();
        startActivity(intent);
    }

}
