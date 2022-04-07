package in.handwritten.android.splashscreen;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import in.handwritten.android.customviews.YoYoAnimatorWrapper;
import in.handwritten.android.utils.Utils;

public class WebViewActivity extends AppCompatActivity {


    ImageView downloadPdf;
    String webViewUrl;
    String webViewBaseUrl;
    public static String WEB_VIEW_URL = "WEB_VIEW_URL";
    public static String WEB_VIEW_BASE_URL = "WEB_VIEW_BASE_URL";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);

        if(getIntent().getExtras()!=null && getIntent().hasExtra(WEB_VIEW_URL)) {
            webViewUrl = getIntent().getExtras().getString(WEB_VIEW_URL);
            webViewBaseUrl =  getIntent().getExtras().getString(WEB_VIEW_BASE_URL);
        }else {
            Utils.showWorkInProgressToast(this,"No URL specified");
            finish();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        downloadPdf = findViewById(R.id.downloadPdf);

        downloadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openDefaultBrowser(WebViewActivity.this,webViewUrl);
            }
        });
        initiateWebViewV2(webViewBaseUrl+webViewUrl);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initiateWebView(String url){
        WebView webView = findViewById(R.id.webView);
        //webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {

            }
        });
        webView.clearView();
        webView.loadUrl(url);
    }

    private void initiateWebViewV2(String url){
        ProgressDialog progDailog = ProgressDialog.show(this, "Loading","Please wait...", true);
        progDailog.setCancelable(false);



        WebView webView = (WebView) findViewById(R.id.webView);



        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //progDailog.show();
                //view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                if (view.getTitle().equals(""))
                    view.reload();
                else {
                    progDailog.dismiss();
                    downloadPdf.setVisibility(View.VISIBLE);
                    new YoYoAnimatorWrapper(Techniques.Shake,downloadPdf,-1).safeCallToYoYo(true);
                    //YoYo.with(Techniques.Shake).playOn(downloadPdf);
                }
            }
        });

        webView.loadUrl(url);

    }
}
