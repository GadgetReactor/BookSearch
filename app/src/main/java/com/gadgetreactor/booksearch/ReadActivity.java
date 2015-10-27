package com.gadgetreactor.booksearch;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;

/**
 * Created by ASUS on 19/12/2014.
 */
public class ReadActivity extends ActionBarActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Tell the activity which XML layout is right
            setContentView(R.layout.activity_read);

            String readUrl = this.getIntent().getExtras().getString("readUrl");

            WebView myWebView = (WebView) findViewById(R.id.read_webview);
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.loadUrl(readUrl);
        }
}
