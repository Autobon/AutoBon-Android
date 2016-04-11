package cn.com.incardata.autobon;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by Administrator on 2016/2/17.
 * 服务协议
 */
public class ServiceProtocalActivity extends BaseActivity{
    private WebView webView;
    private WebSettings webViewSettings;
    private String mUrl;

    @SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_protocal);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView = (WebView) findViewById(R.id.webview);
        mUrl = "file:///android_asset/serviceagreement.html";
        webViewSettings = webView.getSettings();
        webView.loadUrl(mUrl);
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setSupportZoom(false);
        webViewSettings.setTextZoom(300);
        webViewSettings.setBuiltInZoomControls(false);
        webViewSettings.setUseWideViewPort(true);
        webViewSettings.setLoadWithOverviewMode(true);
        webViewSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }
}
