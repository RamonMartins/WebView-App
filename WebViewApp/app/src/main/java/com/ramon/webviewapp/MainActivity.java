package com.ramon.webviewapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
//import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout refreshLayout;
    private WebView webView;
    ProgressBar pBar;
    String lastURL = "https://www.google.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshLayout = findViewById(R.id.swipe_refresh_layout);
        webView = findViewById(R.id.webViewLayout);
        pBar = findViewById(R.id.pBar);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.google.com/");
        webView.setWebViewClient(new MyBrowser());
        //Toast.makeText(MainActivity.this, "001", Toast.LENGTH_SHORT).show();
        //int count = 1;
        //String count2 = Integer.toString(count);
        //Toast.makeText(MainActivity.this, count2, Toast.LENGTH_SHORT).show();
        //Toast.makeText(MainActivity.this, lastURL+">>active", Toast.LENGTH_SHORT).show();

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if(progress < 100 && pBar.getVisibility() == ProgressBar.GONE){
                    pBar.setVisibility(ProgressBar.VISIBLE);
                }
                pBar.setProgress(progress);
                if(progress == 100) {
                    pBar.setVisibility(ProgressBar.GONE);
                }
            }
        });

        if(!webView.getUrl().equals("file:///android_asset/offline2.html")){
            lastURL = webView.getUrl();
        }

        if( !isNetworkAvailable() ) {
            webView.loadUrl("file:///android_asset/offline2.html");
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!webView.getUrl().equals("file:///android_asset/offline2.html")){
                    lastURL = webView.getUrl();
                }
                webView.loadUrl(lastURL);

                webView.setWebChromeClient(new WebChromeClient() {
                    public void onProgressChanged(WebView view, int progress) {
                        if(progress < 100 && pBar.getVisibility() == ProgressBar.GONE){
                            if ( !isNetworkAvailable() ) {
                                webView.loadUrl("file:///android_asset/offline2.html");
                            }
                        }
                        pBar.setProgress(progress);
                        if(progress == 100) {
                            if ( !isNetworkAvailable() ) {
                                webView.loadUrl("file:///android_asset/offline2.html");
                            }
                            refreshLayout.setRefreshing(false);
                            webView.setWebViewClient(new MyBrowser());
                        }
                    }
                });
            }
        });

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            if(!webView.getUrl().equals("file:///android_asset/offline2.html")){
                lastURL = webView.getUrl();
            }

            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    if(progress < 100 && pBar.getVisibility() == ProgressBar.GONE){
                        pBar.setVisibility(ProgressBar.VISIBLE);
                        if ( !isNetworkAvailable() ) {
                            webView.loadUrl("file:///android_asset/offline2.html");
                        }
                    }
                    pBar.setProgress(progress);
                    if(progress == 100) {
                        pBar.setVisibility(ProgressBar.GONE);
                    }
                }
            });

            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}