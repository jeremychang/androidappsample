package com.example.jeremy.appsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class WebActivity extends AppCompatActivity {

    public final static String TAG = "WebActivity";
    private String mUrl = "http://google.com";

    @BindView(R.id.button_submit)
    Button mButtonSubmit;
    @BindView(R.id.edit_text)
    EditText mEditText;
    @BindView(R.id.webview)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        setTitle("Nanohttpd");
        ButterKnife.bind(this);
        // Embedded WebView in the this activity
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.getSettings().getJavaScriptEnabled();
        mWebView.loadUrl(mUrl);
    }

    @OnEditorAction(R.id.edit_text)
    public boolean onEditorAction (TextView v, int actionId, KeyEvent event) {
        // To specify the keyboard action button, use the android:imeOptions attribute
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            mButtonSubmit.performClick();
            return true;
        }
        return false;
    }

    @OnClick(R.id.button_submit)
    public void button_submit(View view) {
        mUrl = mEditText.getText().toString();
        mWebView.loadUrl(mUrl);
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
