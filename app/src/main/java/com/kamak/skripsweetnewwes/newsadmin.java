package com.kamak.skripsweetnewwes;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kamak.skripsweetnewwes.adapter.NewsAdapter;
import com.kamak.skripsweetnewwes.app.AppController;
import com.kamak.skripsweetnewwes.data.NewsData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class newsadmin extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ListView list;
    SwipeRefreshLayout swipe;
    List<NewsData> newsList = new ArrayList<>();

    private static final String TAG = MainActivity.class.getSimpleName();

    private static String url_list   = "http://dinaspuprkabjbg.site/webservice/selesai.php?offset=";

    private int offSet = 0;

    int no;

    NewsAdapter adapter;

    public static final String TAG_NO       = "no";
    public static final String TAG_ID       = "id";

    public static final String TAG_TGL      = "tgl";
    public static final String TAG_ISI      = "isi";
    public static final String TAG_GAMBAR   = "gambar";
    public static final String TAG_NAMA   = "nama";
    public static final String TAG_STATUS   = "status";
    String iduser, username,alamat,nohp;
    SharedPreferences sharedpreferences;

    public static final String TAG_IDUSER = "id";
    public static final String TAG_USERNAME = "username";
    public final static String TAG_ALAMAT = "alamat";
    public final static String TAG_NOHP = "nohp";

    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsadmin);

        setTitle("Selesai Perbaikan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list = (ListView) findViewById(R.id.list_news);
        newsList.clear();
        sharedpreferences = getSharedPreferences(MainActivity.my_shared_preferences, Context.MODE_PRIVATE);

        iduser = getIntent().getStringExtra(TAG_IDUSER);
        username = getIntent().getStringExtra(TAG_USERNAME);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(newsadmin.this, detailnewsuser.class);
                intent.putExtra(TAG_IDUSER, iduser);
                intent.putExtra(TAG_USERNAME, username);
                intent.putExtra(TAG_ALAMAT, alamat);
                intent.putExtra(TAG_NOHP, nohp);
                intent.putExtra(TAG_ID, newsList.get(position).getId());

                startActivity(intent);
            }
        });

        adapter = new NewsAdapter(newsadmin.this, newsList);
        list.setAdapter(adapter);

        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           newsList.clear();
                           adapter.notifyDataSetChanged();
                           callNews(0);
                       }
                   }
        );

        list.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE) {

                    swipe.setRefreshing(true);
                    handler = new Handler();

                    runnable = new Runnable() {
                        public void run() {
                            callNews(offSet);
                        }
                    };

                    handler.postDelayed(runnable, 3000);
                }
            }

        });

    }

    @Override
    public void onRefresh() {
        newsList.clear();
        adapter.notifyDataSetChanged();
        callNews(0);
    }

    private void callNews(int page) {

        swipe.setRefreshing(true);

        // Creating volley request obj
        JsonArrayRequest arrReq = new JsonArrayRequest(url_list + page,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, response.toString());

                        if (response.length() > 0) {
                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    NewsData news = new NewsData();
                                    no = obj.getInt(TAG_NO);

                                    news.setId(obj.getString(TAG_ID));
                                    news.setAlamat(obj.getString(TAG_ALAMAT));
                                    news.setNama(obj.getString(TAG_NAMA));
                                    news.setStatus(obj.getString(TAG_STATUS));

                                    if (obj.getString(TAG_GAMBAR) != "") {
                                        news.setGambar(obj.getString(TAG_GAMBAR));
                                    }

                                    news.setDatetime(obj.getString(TAG_TGL));
                                    news.setketerangan(obj.getString(TAG_ISI));

                                    // adding news to news array
                                    newsList.add(news);

                                    if (no > offSet)
                                        offSet = no;

                                    Log.e(TAG, "offSet " + offSet);

                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }

                                // notifying list Adapter about data changes
                                // so that it renders the list view with updated data
                                adapter.notifyDataSetChanged();
                            }
                        }
                        swipe.setRefreshing(false);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }
    public void onBackPressed() {
        Intent intent = new Intent(newsadmin.this, MenuActivity.class);
        intent.putExtra(TAG_ID, iduser);

        intent.putExtra(TAG_USERNAME, username);
        intent.putExtra(TAG_ALAMAT, alamat);
        intent.putExtra(TAG_NOHP, nohp);
        finish();
        startActivity(intent);
    }



}


