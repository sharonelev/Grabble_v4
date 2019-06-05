package com.example.android.grabble_v4.utilities;


import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import com.example.android.grabble_v4.R;

public class PrivacyPolicyActivity extends Activity {
  WebView web;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_privacy_policyactivity);

    web =(WebView)findViewById(R.id.webView);
    web.loadUrl("https://appsbysha.wixsite.com/website/about");

  }

}