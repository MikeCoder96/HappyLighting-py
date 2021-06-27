package com.qh.blelight;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

public class WebActivity extends MyActivity {
  private RelativeLayout lin_back;
  
  private MyApplication mMyApplication;
  
  private WebView myWeb;
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131361844);
    this.mMyApplication = (MyApplication)getApplication();
    String str = getIntent().getStringExtra("URL");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("URL=");
    stringBuilder.append(str);
    Log.e("URL", stringBuilder.toString());
    this.myWeb = (WebView)findViewById(2131231274);
    WebSettings webSettings = this.myWeb.getSettings();
    webSettings.setJavaScriptEnabled(false);
    webSettings.setAllowFileAccess(false);
    webSettings.setAllowUniversalAccessFromFileURLs(false);
    webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
    webSettings.setLoadWithOverviewMode(false);
    webSettings.setAppCacheEnabled(false);
    webSettings.setDomStorageEnabled(false);
    webSettings.setSupportZoom(true);
    webSettings.setBuiltInZoomControls(true);
    webSettings.setDisplayZoomControls(false);
    webSettings.setUseWideViewPort(true);
    webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    webSettings.setLoadWithOverviewMode(true);
    this.myWeb.setWebViewClient(new WebViewClient() {
          public boolean shouldOverrideUrlLoading(WebView param1WebView, String param1String) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("url = ");
            stringBuilder.append(param1String);
            Log.e("", stringBuilder.toString());
            param1WebView.loadUrl(param1String);
            return true;
          }
        });
    this.myWeb.setWebChromeClient(new WebChromeClient() {
          public void onProgressChanged(WebView param1WebView, int param1Int) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("newProgress = ");
            stringBuilder.append(param1Int);
            Log.e("", stringBuilder.toString());
          }
        });
    this.myWeb.loadUrl(str);
  }
  
  protected void onDestroy() {
    super.onDestroy();
    if (this.myWeb != null)
      this.myWeb.destroy(); 
  }
}


/* Location:              C:\Users\mikel\Desktop\Projects\LEDStripController\HappyLighting_base-dex2jar.jar!\com\qh\blelight\WebActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */