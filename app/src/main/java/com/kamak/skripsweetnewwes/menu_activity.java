package com.kamak.skripsweetnewwes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kamak.skripsweetnewwes.slider.FragmentSlider;
import com.kamak.skripsweetnewwes.slider.SliderIndicator;
import com.kamak.skripsweetnewwes.slider.SliderPagerAdapter;
import com.kamak.skripsweetnewwes.slider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class menu_activity extends AppCompatActivity {
    Button btnkeluar, btn_post, btn_profil, btn_review,btn_berita;
    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;

    private SliderView sliderView;
    private LinearLayout mLinearLayout;
    String id, username;
    SharedPreferences sharedpreferences;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        sliderView = (SliderView) findViewById(R.id.sliderView);
        mLinearLayout = (LinearLayout) findViewById(R.id.pagesContainer);
        setupSlider();

        sharedpreferences = getSharedPreferences(MainActivity.my_shared_preferences, Context.MODE_PRIVATE);

        id = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);

        btn_post = (Button) findViewById(R.id.btnpost);
        btn_post.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

                Intent intent = new Intent(menu_activity.this, foto.class);

                intent.putExtra(TAG_ID, id);
                intent.putExtra(TAG_USERNAME, username);
                finish();
                startActivity(intent);
            }

        });
        btn_profil = (Button) findViewById(R.id.btnprofil);
        btn_profil.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

                Intent intent = new Intent(menu_activity.this, profil.class);

                intent.putExtra(TAG_ID, id);
                intent.putExtra(TAG_USERNAME, username);
                finish();
                startActivity(intent);
            }

        });
        btnkeluar = (Button) findViewById(R.id.btnlogout);
        btnkeluar.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {


                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(MainActivity.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_USERNAME, null);
                editor.commit();

                Intent intent = new Intent(menu_activity.this, login.class);
                finish();
                startActivity(intent);

            }

        });
        btn_berita = (Button) findViewById(R.id.btnberita);
        btn_berita.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {


                Intent intent = new Intent(menu_activity.this, berita.class);

                startActivity(intent);
            }

        });
        btn_review = (Button) findViewById(R.id.btnreview);
        btn_review.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {


                Intent intent = new Intent(menu_activity.this, review.class);

                startActivity(intent);
            }

        });


    }
    private void setupSlider() {
        sliderView.setDurationScroll(800);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentSlider.newInstance("http://dinaspuprkabjbg.site/logopuu.png"));
        fragments.add(FragmentSlider.newInstance("http://dinaspuprkabjbg.site/logopu.jpg"));
        fragments.add(FragmentSlider.newInstance("http://dinaspuprkabjbg.site/logopuu.png"));



        mAdapter = new SliderPagerAdapter(getSupportFragmentManager(), fragments);
        sliderView.setAdapter(mAdapter);
        mIndicator = new SliderIndicator(this, mLinearLayout, sliderView, R.drawable.indicator_circle);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();
    }
}
