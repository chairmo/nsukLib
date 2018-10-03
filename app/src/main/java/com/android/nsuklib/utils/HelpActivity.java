package com.android.nsuklib.utils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.android.nsuklib.R;

public class HelpActivity extends AppCompatActivity {
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        WebView browser = findViewById(R.id.webView);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.loadUrl("file:///android_asset/help");
     //   setContentView(browser);
    }
}
