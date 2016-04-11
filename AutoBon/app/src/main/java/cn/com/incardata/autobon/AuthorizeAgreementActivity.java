package cn.com.incardata.autobon;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * 技师认证服务协议
 */
public class AuthorizeAgreementActivity extends BaseActivity {
    private WebView webView;
    private WebSettings webViewSettings;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize_agreement);

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
