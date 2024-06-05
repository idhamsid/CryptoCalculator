package com.haryo.cryptocalculator.ui;


import static com.haryo.cryptocalculator.isConfig.Settings.URL_TIPS;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.haryo.cryptocalculator.R;

public class UsageActivity extends AppCompatActivity{
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usage_activity);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.root);
        Toolbar toolbar = findViewById(R.id.toolbar);

        try {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Future Position Size");
            }
        } catch (Exception e) {
            Log.i("adslog", "exception : " + e.getMessage());
        }


        WebView mWebView = findViewById(R.id.webview);
        mWebView.setWebViewClient(new MyBrowser());
        mWebView.setWebChromeClient(new ChromeClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true);
        mWebView.requestDisallowInterceptTouchEvent(true);
        final String s = URL_TIPS;
        if (s.startsWith("https://")) {
            mWebView.loadUrl(s);
        } else if (s.startsWith("http://")) {
            mWebView.loadUrl(s);
        } else if (s.startsWith("file:///android_asset")) {
            mWebView.loadUrl(s);
        } else {
            mWebView.loadDataWithBaseURL(null, "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>" + s, "text/html", "UTF-8", null);
        }

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true; // in both cases we handle the link manually

        }

        public void onReceivedError(WebView view, int errorCode, String description, String
                failingUrl) {
            Toast.makeText(UsageActivity.this, "please activate internet !! ", Toast.LENGTH_SHORT).show();

            view.loadUrl("about:blank");
        }

    }

    private class ChromeClient extends WebChromeClient {
        protected FrameLayout mFullscreenContainer;
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        ChromeClient() {
        }

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
