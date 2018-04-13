package com.kamak.skripsweetnewwes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class dasar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasar);
        WebView mWebView = null;
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //loads the url
        mWebView.loadUrl("https://triansyah05.wordpress.com/2014/12/01/uu-nomor-40-tahun-2006-tentang-tata-cara-penyusunan-rencana-pembangunan-nasional/");

        //cancels all hyperlinks
        mWebView.setWebViewClient(new WebViewClient() {
                                      public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                          return false;
                                      }
                                  }
        );
    }
}
