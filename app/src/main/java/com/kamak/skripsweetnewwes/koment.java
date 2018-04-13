package com.kamak.skripsweetnewwes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.kamak.skripsweetnewwes.app.AppController;
import com.kamak.skripsweetnewwes.data.DataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chukamak on 27/11/2017.
 */
public class koment extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ProgressDialog pDialog;
    List<DataModel> listData = new ArrayList<DataModel>();
    com.kamak.skripsweetnewwes.adapter.Adapter adapter;
    SwipeRefreshLayout swipe;
    ListView list_view;
    RecyclerView.Adapter Adapter;
    /* 10.0.2.2 adalah IP Address localhost EMULATOR ANDROID STUDIO,
    Ganti IP Address tersebut dengan IP Laptop Apabila di RUN di HP. HP dan Laptop harus 1 jaringan */
    public static final String url_data = "http://10.0.2.2/android/kuncoro_search_view/data.php";
    public static final String url_cari = Server.URL+"listkomentar.php";

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String TAG_IDnews = "id";
    public static final String TAG_ID = "iduser";

    public static final String TAG_IDkoment = "ID_KOMENTAR";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_ISI = "isi";
    public static final String TAG_TGL = "tgl";
    public static final String TAG_RESULTS = "results";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_VALUE = "value";
    String iduser,username,idnews,id;
    public static final String TAG_IDuser = "iduser";
    public static final String TAG_USERNAME = "username";


    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detail");
        setContentView(R.layout.activity_koment);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        list_view = (ListView) findViewById(R.id.list_view);
        iduser = getIntent().getStringExtra(TAG_IDuser);
        idnews = getIntent().getStringExtra(TAG_IDnews);
        username = getIntent().getStringExtra(TAG_USERNAME);
        adapter = new com.kamak.skripsweetnewwes.adapter.Adapter(koment.this, listData);
        list_view.setAdapter(adapter);

        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           cariData(idnews);
                       }
                   }
        );

    }


    private void cariData(final String idnews) {
        swipe.setRefreshing(true);

        StringRequest strReq = new StringRequest(Request.Method.POST, url_cari, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);

                    int value = jObj.getInt(TAG_VALUE);

                    if (value == 1) {
                        listData.clear();
                        adapter.notifyDataSetChanged();

                        String getObject = jObj.getString(TAG_RESULTS);
                        JSONArray jsonArray = new JSONArray(getObject);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            DataModel data = new DataModel();

                            data.setId(obj.getString(TAG_IDkoment));
                            data.setNama(obj.getString(TAG_ISI));

                            listData.add(data);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idnews);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(koment.this, home.class);

        intent.putExtra(TAG_ID, id);
        intent.putExtra(TAG_NAMA, username);
        finish();
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        cariData(idnews);
    }




}