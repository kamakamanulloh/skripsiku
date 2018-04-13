package com.kamak.skripsweetnewwes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class lvkomentar extends AppCompatActivity implements ListView.OnItemClickListener{
    TextView tes;
    private ListView listView;
    private ImageView imageViewPicasso;
    private String JSON_STRING;
    public static final String TAG_IDuser       = "iduser";
    public static final String TAG_username       = "username";
    String id,iduser,username;
    public static final String TAG_ID       = "id";
    EditText txtt;
    Button btnkomen;
    TextView txtkomen;
    String idnews;
    Button btnfoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listkomen);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        iduser = getIntent().getStringExtra(TAG_IDuser);
        username = getIntent().getStringExtra(TAG_username);
        Intent intent = getIntent();
        id=getIntent().getStringExtra(TAG_ID);
        btnkomen=(Button)findViewById(R.id.btnkomen);
        txtkomen=(EditText)findViewById(R.id.editText);
        btnfoto=(Button)findViewById(R.id.btnfoto);
        btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(lvkomentar.this, komentargambar.class);
                intent.putExtra(TAG_IDuser, iduser);
                intent.putExtra(TAG_username, username);
                intent.putExtra(TAG_ID, id);

                finish();
                startActivity(intent);

            }
        });

        btnkomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendkomen();

            }
        });




        getJSON();
    }
    private void selectImage() {

        final CharSequence[] items = {"Ambil Foto","Tulis Komentar",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(lvkomentar.this);
        builder.setTitle("Pilihan");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Ambil Foto")) {




                }  else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
                else if (items[item].equals("Tulis Komentar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(lvkomentar.this,"Mengambil Data","Mohon Tunggu...",false,false);
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
                String s = rh.sendGetRequestParam(konfigurasi.URL_KOMENT,id);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    private void showEmployee(String json){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(konfigurasi.KEY_idkomen);
                String name = jo.getString(konfigurasi.KEY_NAMAkomen);
                String tglkomen = jo.getString(konfigurasi.KEY_tglkomen);
                String isikoment = jo.getString(konfigurasi.KEY_KOMENTAR);
                String gbr = jo.getString(konfigurasi.TAG_gbr);



                HashMap<String,String> employees = new HashMap<>();
                employees.put(konfigurasi.KEY_idkomen,id);
                employees.put(konfigurasi.KEY_NAMAkomen,name);
                employees.put(konfigurasi.KEY_tglkomen,tglkomen);
                employees.put(konfigurasi.KEY_KOMENTAR,isikoment);
                employees.put(konfigurasi.TAG_gbr,gbr);

                list.add(employees);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                lvkomentar.this, list, R.layout.list_item,
                new String[]{konfigurasi.KEY_NAMAkomen,konfigurasi.KEY_KOMENTAR,konfigurasi.KEY_tglkomen,konfigurasi.TAG_gbr},
                new int[]{R.id.name, R.id.komentar,R.id.txttgl,R.id.networkImageView});

        listView.setAdapter(adapter);
    }

    public void onBackPressed() {
        Intent intent = new Intent(lvkomentar.this, DetailNews.class);
        intent.putExtra(TAG_IDuser, iduser);
        intent.putExtra(TAG_username, username);
        intent.putExtra(TAG_ID, id);

        finish();
        startActivity(intent);
    }

    private void sendkomen(){

        final String koment = txtkomen.getText().toString().trim();



        class AddEmployee extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(lvkomentar.this,"Mengirim...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(lvkomentar.this,s, Toast.LENGTH_LONG).show();
                getJSON();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(konfigurasi.KEY_KOMENTAR,koment);
                params.put(konfigurasi.KEY_NAMAkomen,username);

                params.put(konfigurasi.KEY_IDNEWS,id);



                reqhandler rh = new reqhandler();
                String res = rh.sendPostRequest(konfigurasi.URL_POSTKOMENT, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
