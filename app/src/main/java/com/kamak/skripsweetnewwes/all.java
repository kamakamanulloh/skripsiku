package com.kamak.skripsweetnewwes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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


public class all extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener  {

    ListView list;
    SwipeRefreshLayout swipe;
    List<NewsData> newsList = new ArrayList<>();

    private static final String TAG = MainActivity.class.getSimpleName();

    private static String url_list   =  Server.URL +"berita.php?offset=";

    private int offSet = 0;

    int no;

    NewsAdapter adapter;

    public static final String TAG_NO       = "no";
    public static final String TAG_IDnews      = "id";
    public static final String TAG_ALAMAT    = "alamat";
    public static final String TAG_TGL      = "tgl";
    public static final String TAG_ISI      = "isi";
    public static final String TAG_GAMBAR   = "gambar";
    public static final String TAG_NAMA   = "nama";
    public static final String TAG_STATUS   = "status";

    SharedPreferences sharedpreferences;

    public static final String TAG_ID = "iduser";
    public static final String TAG_USERNAME = "username";
    TextView txt;
    Handler handler;
    Runnable runnable;
    String id,iduser, username,alamat,nohp,idnews;

    public final static String TAG_alamat = "alamat";
    public final static String TAG_NOHP = "nohp";

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            sharedpreferences = getSharedPreferences(MainActivity.my_shared_preferences, Context.MODE_PRIVATE);

            alamat = getIntent().getStringExtra(TAG_ALAMAT);
            nohp= getIntent().getStringExtra(TAG_NOHP);
            iduser = getIntent().getStringExtra(TAG_ID);
            idnews = getIntent().getStringExtra(TAG_IDnews);
            username = getIntent().getStringExtra(TAG_USERNAME);
            switch (item.getItemId()) {
                case R.id.back:

                    Intent intent=new Intent(all.this,home.class);
                    intent.putExtra(TAG_ID, iduser);
                    intent.putExtra(TAG_USERNAME, username);
                    intent.putExtra(TAG_ALAMAT, alamat);
                    intent.putExtra(TAG_NOHP, nohp);
                    finish();
                    startActivity(intent);
                    break;

                case R.id.home:

                    intent=new Intent(all.this,home.class);
                    intent.putExtra(TAG_ID, iduser);
                    intent.putExtra(TAG_USERNAME, username);
                    intent.putExtra(TAG_ALAMAT, alamat);
                    intent.putExtra(TAG_NOHP, nohp);
                    finish();
                    startActivity(intent);
                    break;
                case R.id.post:
                    intent=new Intent(all.this,foto.class);
                    intent.putExtra(TAG_ID, iduser);
                    intent.putExtra(TAG_USERNAME, username);
                    intent.putExtra(TAG_ALAMAT, alamat);
                    intent.putExtra(TAG_NOHP, nohp);
                    finish();
                    startActivity(intent);
                    break;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_perbaikan);

        alamat = getIntent().getStringExtra(TAG_ALAMAT);
        nohp= getIntent().getStringExtra(TAG_NOHP);
        iduser = getIntent().getStringExtra(TAG_ID);
        idnews = getIntent().getStringExtra(TAG_IDnews);
        username = getIntent().getStringExtra(TAG_USERNAME);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list = (ListView) findViewById(R.id.list_news);
        newsList.clear();
        sharedpreferences = getSharedPreferences(MainActivity.my_shared_preferences, Context.MODE_PRIVATE);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(all.this, DetailNews.class);
                intent.putExtra(TAG_ID, iduser);
                intent.putExtra(TAG_USERNAME, username);
                intent.putExtra(TAG_IDnews, newsList.get(position).getId());

                startActivity(intent);
            }
        });

        adapter = new NewsAdapter(all.this, newsList);
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

                                    news.setId(obj.getString(TAG_IDnews));
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
        Intent intent = new Intent(all.this, home.class);
        intent.putExtra(TAG_ID, iduser);
        intent.putExtra(TAG_USERNAME, username);
        setResult(home.RESULT_OK, intent);
        finish();
        super.onBackPressed();

        finish();
        startActivity(intent);
    }
}
