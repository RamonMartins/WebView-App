package com.ramon.webviewapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshLayout = findViewById(R.id.swipe_refresh_layout);
        webView = findViewById(R.id.webViewLayout);
        pBar = findViewById(R.id.pBar);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.loadUrl("http://www.google.com/");
        //Toast.makeText(MainActivity.this, "001", Toast.LENGTH_SHORT).show();
        webView.setWebViewClient(new MyBrowser());

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

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(webView.getUrl());

                webView.setWebChromeClient(new WebChromeClient() {
                    public void onProgressChanged(WebView view, int progress) {
                        pBar.setProgress(progress);
                        if(progress == 100) {
                            refreshLayout.setRefreshing(false);
                            webView.setWebViewClient(new MyBrowser());
                        }
                    }
                });
            }
        });
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

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

            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}