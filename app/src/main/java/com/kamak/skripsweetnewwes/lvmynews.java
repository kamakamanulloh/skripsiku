package com.kamak.skripsweetnewwes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class lvmynews extends AppCompatActivity implements ListView.OnItemClickListener{
    TextView tes;
    private ListView listView;
    private ImageView imageViewPicasso;
    private String JSON_STRING;


    public static final String TAG_NO       = "no";
    public static final String TAG_IDnews      = "id";
    public static final String TAG_ALAMAT    = "alamat";
    public static final String TAG_TGL      = "tgl";
    public static final String TAG_ISI      = "isi";
    public static final String TAG_GAMBAR   = "gambar";
    public static final String TAG_NAMA   = "nama";
    public static final String TAG_STATUS   = "status";
    public static final String TAG_ID = "iduser";
    public static final String TAG_USERNAME = "username";

    SharedPreferences sharedpreferences;


    Handler handler;
    Runnable runnable;
    String iduser, username,alamat,nohp,idnews;

    public final static String TAG_alamat = "alamat";
    public final static String TAG_NOHP = "nohp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        Intent intent = getIntent();
        iduser=getIntent().getStringExtra(TAG_ID);

        username = getIntent().getStringExtra(TAG_USERNAME);

        getJSON();
    }

    private void showEmployee(String json) {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(TAG_IDnews);
                String tgl = jo.getString(TAG_TGL);
                String statuss = jo.getString(TAG_STATUS);
                String alamat = jo.getString(TAG_ALAMAT);


                HashMap<String, String> employees = new HashMap<>();
                employees.put(TAG_IDnews, id);
                employees.put(konfigurasi.TAG_TGLl, tgl);
                employees.put(konfigurasi.TAG_STATUS, statuss);
                employees.put(konfigurasi.TAG_ALAMATt, alamat);

                list.add(employees);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                lvmynews.this, list, R.layout.list_item,
                new String[]{TAG_ALAMAT,TAG_STATUS,TAG_TGL},
                new int[]{R.id.name, R.id.komentar,R.id.txttgl});

        listView.setAdapter(adapter);

    }
    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(lvmynews.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                reqhandler rh = new reqhandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_mynews,iduser);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void onBackPressed() {
        Intent intent = new Intent(lvmynews.this, profil_user.class);
        intent.putExtra(TAG_ID, iduser);
        intent.putExtra(TAG_USERNAME, username);


        finish();
        startActivity(intent);
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DetailNews.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String empId = map.get(konfigurasi.TAG_IDnews).toString();
        intent.putExtra(konfigurasi.TAG_IDnews,empId);
        intent.putExtra(TAG_ID,iduser);
        startActivity(intent);
    }


}
