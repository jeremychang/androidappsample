package com.example.jeremy.appsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    public final static String TAG = "WebActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        WebView webView = (WebView)findViewById(R.id.webview);
        // Embedded WebView in the this activity
        webView.setWebViewClient(new MyWebViewClient());
        String url = "http://tw.yahoo.com";
        webView.getSettings().getJavaScriptEnabled();
        webView.loadUrl(url);
        
        //webView.addJavascriptInterface(new WebAppInterface(this), "Android");
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            if (request.getUrl().getHost().equals("www.example.com")) {
                // This is my web site, so do not override; let my WebView load the page
                Log.i(TAG, "return false: URL=" + request.getUrl());
                return false;
            }

            Log.i(TAG, "URL=" + request.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
            startActivity(intent);
            return super.shouldOverrideUrlLoading(view, request);
        }
    }
}
