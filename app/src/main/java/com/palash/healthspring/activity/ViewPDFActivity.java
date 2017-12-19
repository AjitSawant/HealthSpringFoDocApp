package com.palash.healthspring.activity;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.palash.healthspring.R;
import com.palash.healthspring.utilities.LocalSetting;

public class ViewPDFActivity extends AppCompatActivity {

    private Context context;
    private RelativeLayout network_error;
    private RelativeLayout webviewLayout;
    private WebView webView;
    private ProgressBar progressBar;
    private LocalSetting localSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        initialize();
        setWebView();
    }

    private void initialize() {
        context = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        localSetting = new LocalSetting();

        webviewLayout = (RelativeLayout) findViewById(R.id.webview_layout);
        network_error = (RelativeLayout) findViewById(R.id.network_error);
        webView = (WebView) findViewById(R.id.more_info_webview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        webView.setWebViewClient(new webViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);
    }

    private void setWebView() {
        String url = getIntent().getStringExtra("url");
        Log.e("PDF Url : ",url);
        if (localSetting.isNetworkAvailable(context)) {
            network_error.setVisibility(View.GONE);
            webviewLayout.setVisibility(View.VISIBLE);
            webView.loadUrl(url);
            //webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);
        } else {
            network_error.setVisibility(View.VISIBLE);
            webviewLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class webViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }
}