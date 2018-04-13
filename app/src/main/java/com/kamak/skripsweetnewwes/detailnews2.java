package com.kamak.skripsweetnewwes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.kamak.skripsweetnewwes.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chukamak on 27/11/2017.
 */
public class detailnews2 extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    NetworkImageView thumb_image;
    TextView nama, tgl, keterangan,alamat,status;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SwipeRefreshLayout swipe;
    String id_news,iduser;

    private static final String TAG = detailnews2.class.getSimpleName();

    public static final String TAG_ID       = "id";
    public static final String TAG_IDuser       = "iduser";
    public static final String TAG_username       = "username";
    public static final String TAG_NAMA    = "nama";
    public static final String TAG_TGL      = "tgl";
    public static final String TAG_KETERANGAN      = "keterangan";
    public static final String TAG_GAMBAR   = "gambar";
    public static final String TAG_ALAMAT   = "alamat";
    public static final String TAG_STATUS   = "status";
    public static final String TAG_like   = "like";
    String Nama,id,username;
    private static final String url_detail  = Server.URL + "detailnews.php";
    String tag_json_obj = "json_obj_req";
    EditText txtkomen;
    Button btnkomen;
    Button btnlike;
    TextView txtlike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailnews2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detail");
        id = getIntent().getStringExtra(TAG_ID);
        iduser = getIntent().getStringExtra(TAG_IDuser);
        username = getIntent().getStringExtra(TAG_username);
       txtlike=(TextView)findViewById(R.id.txtjml);
        thumb_image = (NetworkImageView) findViewById(R.id.gambar_news);
        nama       = (TextView) findViewById(R.id.judul_news);
        tgl         = (TextView) findViewById(R.id.tgl_news);
        keterangan         = (TextView) findViewById(R.id.isi_news);
        alamat         = (TextView) findViewById(R.id.txt_komen);
        status         = (TextView) findViewById(R.id.txtstatus);
        swipe       = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        id_news = getIntent().getStringExtra(TAG_ID);
        btnlike=(Button)findViewById(R.id.btnlike);
        btnkomen=(Button)findViewById(R.id.btnkomen);
        btnkomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(detailnews2.this, lvkomentar.class);

                intent.putExtra(TAG_ID, id);
                intent.putExtra(TAG_IDuser, iduser);
                intent.putExtra(TAG_username, Nama);
                finish();
                startActivity(intent);
            }
        });

        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           if (!id_news.isEmpty()) {
                               callDetailNews(id_news);
                           }
                       }
                   }
        );

    }

    private void callDetailNews(final String id){
        swipe.setRefreshing(true);

        StringRequest strReq = new StringRequest(Request.Method.POST, url_detail, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + response.toString());
                swipe.setRefreshing(false);

                try {
                    JSONObject obj = new JSONObject(response);

                   Nama   = obj.getString(TAG_NAMA);
                    String Gambar   = obj.getString(TAG_GAMBAR);
                    String Tgl      = obj.getString(TAG_TGL);
                    String Keterangan      = obj.getString(TAG_KETERANGAN);
                    String Alamat      = obj.getString(TAG_ALAMAT);
                    String Status      = obj.getString(TAG_STATUS);
                    String like      = obj.getString(TAG_like);



                    nama.setText("Post By "+Nama);
                    tgl.setText(Tgl);
                    alamat.setText("Alamat "+Alamat);
                    status.setText("Status "+Status);
                    keterangan.setText("Keterangan "+Html.fromHtml(Keterangan));
                    txtlike.setText(like+" suka");

                    if (obj.getString(TAG_GAMBAR)!=""){
                        thumb_image.setImageUrl(Gambar, imageLoader);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Detail News Error: " + error.getMessage());
                Toast.makeText(detailnews2.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                swipe.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(detailnews2.this, all.class);

        intent.putExtra(TAG_IDuser, iduser);
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
        callDetailNews(id_news);
    }

    private void addlike(){





        class AddEmployee extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(detailnews2.this,"Mengirim...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(detailnews2.this,s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();



                params.put(konfigurasi.KEY_IDNEWS,id);



                reqhandler rh = new reqhandler();
                String res = rh.sendPostRequest(konfigurasi.URL_like, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();
    }




}