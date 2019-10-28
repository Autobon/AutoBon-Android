package cn.com.incardata.autobon;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.HashMap;
import java.util.Map;

import cn.com.incardata.application.MyApplication;

/**
 * Created by Administrator on 2016/2/17.
 * 服务协议
 */
public class ServiceProtocalActivity extends BaseActivity {
    private WebView webView;
    private WebSettings webViewSettings;
    private String mUrl;

    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
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
                mUrl = "file:///android_asset/protocol.html";
//        mUrl = "file:///android_asset/technicianServiceProtocol.html";
        webViewSettings = webView.getSettings();


////        mUrl = "http://10.0.12.182:12345/api/web/admin/study/download?path=/uploads/study/2017062210352642YQF6.docx";
//        mUrl = "http://10.0.12.182:12345/console/study";
////        "Cookie", MyApplication.getInstance().getCookie();
////        Map<String,String> param = new HashMap<>();
//        String cookie = "autoken=\"staff:ssEoVBwJ3rSYnidORQUvhQ==@4MSCDU\"";
////        String cookie = "autoken=\"staff:ssEoVBwJ3rSYnidORQUvhQ==@8PLGBV\"";
////        param.put("Cookie",cookie);
////        //创建CookieSyncManager
//        CookieSyncManager.createInstance(this);
//        //得到CookieManager
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptCookie(true);
//        StringBuilder sbCookie = new StringBuilder();
//        sbCookie.append(String.format("Cookie=%s",cookie));
//        //使用cookieManager..setCookie()向URL中添加Cookie
//        cookieManager.setCookie(mUrl,sbCookie.toString());
//        CookieSyncManager.getInstance().sync();
//////        CookieManager.getInstance().setCookie("Cookie",MyApplication.getInstance().getCookie());
//        webView.loadUrl(mUrl);
//        webViewSettings.setJavaScriptEnabled(true);
//        webViewSettings.setAllowContentAccess(true);
//        webViewSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webViewSettings.setAllowFileAccess(true);
//        webViewSettings.setAllowFileAccessFromFileURLs(true);
//        webViewSettings.setAllowUniversalAccessFromFileURLs(true);
//        webViewSettings.setAppCacheEnabled(true);
//        webViewSettings.setDomStorageEnabled(true);
//        webViewSettings.setDatabaseEnabled(true);
//        webViewSettings.setBuiltInZoomControls(false);
//        webViewSettings.setUseWideViewPort(true);
//        webViewSettings.setLoadWithOverviewMode(false);
////        webViewSettings.setTextZoom(300);
//        webViewSettings.setSupportZoom(true);
//        webViewSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);


        webView.loadUrl(mUrl);
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setAllowContentAccess(true);
        webViewSettings.setSupportZoom(false);
        webViewSettings.setTextZoom(300);
        webViewSettings.setBuiltInZoomControls(false);
        webViewSettings.setUseWideViewPort(true);
        webViewSettings.setLoadWithOverviewMode(true);
        webViewSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }
}
