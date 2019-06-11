package nitish.build.com.saavntest1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SaavnWebView extends AppCompatActivity {
    WebView webView;
    ProgressDialog progressDialog;
    TextView tv_link;
    Button btn_Download,tv_found;
    String curUrl,songType;
    Button btn_next,btn_prev,btn_home;

    //------------------------   Double tap to Exit   ----------------------------//


    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();
    public Toast exitToast ;

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            exitToast.cancel();
            startActivity(new Intent(getApplicationContext(),Search_Songs.class));
            overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        exitToast.show();

        mHandler.postDelayed(mRunnable, 2000);
    }

    //------------------- End of Double tap to Exit code --------------------//

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saavn_web_view);
        webView=findViewById(R.id.webView);
        tv_link=findViewById(R.id.tv_test_link);
        tv_found=findViewById(R.id.btn_found);
        btn_Download=findViewById(R.id.btn_downloadSongs);
        btn_home = findViewById(R.id.btn_home_web);
        btn_next = findViewById(R.id.btn_next_web);
        btn_prev = findViewById(R.id.btn_prev_web);

        if (!isNetworkAvailable()){
            new AlertDialog.Builder(SaavnWebView.this)
                    .setTitle("Not Connected to internet?")
                    .setMessage("This app requires active internet connetion otherwise there is a fair chance for app crash.")
                    .setPositiveButton("Ok",null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show();

        }


        startWebView("https://www.jiosaavn.com/");

        exitToast = Toast.makeText(getApplicationContext(), "click BACK again to go to MAIN PAGE", Toast.LENGTH_SHORT);

        webView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int progress) {
                progressDialog.show();
                if (progress == 100) {
                    progressDialog.dismiss();
                    curUrl=webView.getUrl();
                    tv_link.setText(curUrl);

                    new SetDownloadButton().execute(curUrl);

                    if (webView.canGoForward())
                        btn_next.setAlpha(1.0f);
                    else
                        btn_next.setAlpha(0.3f);
                    if (webView.canGoBack())
                        btn_prev.setAlpha(1.0f);
                    else
                        btn_prev.setAlpha(0.3f);

                } else {
                    progressDialog.show();

                }
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);
                //-------------------------//
                startWebView("https://www.jiosaavn.com/");
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);
                //-------------------------//
                if(webView.canGoBack())
                    webView.goBack();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);
                //-------------------------//
                if (webView.canGoForward())
                    webView.goForward();
            }
        });

        btn_Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);
                //-------------------------//

                new StartIntentToAlbum().execute(curUrl);
            }
        });
    }

    public class SetDownloadButton extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            tv_found.setText("Processing Please wait...");
        }
        @Override
        protected String doInBackground(String... strings) {
            return DataHandlers.getLinkType(strings[0]);
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            songType = s;
            if(s.equals("FAILED")){
                btn_Download.setVisibility(View.GONE);
                tv_found.setText(R.string.downloads_not_founds);
            }else {
                btn_Download.setVisibility(View.VISIBLE);
                tv_found.setText(R.string.downloads_found);
            }

            progressDialog.dismiss();

        }
    }

    public class StartIntentToAlbum extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return DataHandlers.getDirectID(strings[0]);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent toDownloadList = new Intent(getApplicationContext(),Album_Song_List.class);
            toDownloadList.putExtra("TYPE_ID",s);
            toDownloadList.putExtra("TYPE",songType);
            toDownloadList.putExtra("PREV_ACT","WEB_ACT");
            progressDialog.dismiss();
            startActivity(toDownloadList);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

        }
    }


    private void startWebView(String url) {

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        progressDialog = new ProgressDialog(SaavnWebView.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), "Error:" + description, Toast.LENGTH_SHORT).show();

            }
        });
        webView.loadUrl(url);
    }

    public void wBtmSrch(View v){
        //------Animation-----------//
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1000);
        v.startAnimation(animation1);
        //-------------------------//
        startActivity(new Intent(getApplicationContext(),Search_Songs.class));
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
    }
    public void wBtmBrws(View v){
        //------Animation-----------//
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1000);
        v.startAnimation(animation1);
        //-------------------------//
//        startActivity(new Intent(getApplicationContext(),SaavnWebView.class));
    }
    public void wBtmDown(View v){
        //------Animation-----------//
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1000);
        v.startAnimation(animation1);
        //-------------------------//
        startActivity(new Intent(getApplicationContext(),Downloads_Page.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    public void wBtmMore(View v){
        //------Animation-----------//
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1000);
        v.startAnimation(animation1);
        //-------------------------//
        startActivity(new Intent(getApplicationContext(),MorePage.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
