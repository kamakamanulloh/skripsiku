package com.kamak.skripsweetnewwes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class beritasaya extends AppCompatActivity implements ListView.OnItemClickListener{
    private ListView listView;
    private ImageView imageViewPicasso;
    private String JSON_STRING;
    String iduser;
    public static final String TAG_ID = "id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        Intent intent = getIntent();
        iduser = getIntent().getStringExtra(TAG_ID);

        getJSON();
    }


    private void showEmployee(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(konfigurasi.TAG_id);

                String status = jo.getString(konfigurasi.KEY_status);


                HashMap<String,String> employees = new HashMap<>();
                employees.put(konfigurasi.KEY_status,status);
                employees.put(konfigurasi.TAG_id,id);


                list.add(employees);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                 beritasaya.this, list, R.layout.list_mynews,
                new String[]{konfigurasi.KEY_id,konfigurasi.KEY_status},
                new int[]{R.id.txt_komen, R.id.txtstatus});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(beritasaya.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee();
            }

            @Override
            protected String doInBackground(Void... params) {
                reqhandler rh = new reqhandler();

                String s = rh.sendGetRequestParam(konfigurasi.URL_mynews, TAG_ID);
                return s;

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
        Intent intent = new Intent(this, profil.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String empId = map.get(konfigurasi.TAG_id).toString();
        intent.putExtra(konfigurasi.TAG_id,empId);
        startActivity(intent);
    }
}

