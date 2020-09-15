package nitish.build.com.freemium.Activities;

//                           ____        _   _ _ _   _     _
//     /\                   |  _ \      | \ | (_) | (_)   | |
//    /  \   _ __  _ __  ___| |_) |_   _|  \| |_| |_ _ ___| |__
//   / /\ \ | '_ \| '_ \/ __|  _ <| | | | . ` | | __| / __| '_ \
//  / ____ \| |_) | |_) \__ \ |_) | |_| | |\  | | |_| \__ \ | | |
// /_/    \_\ .__/| .__/|___/____/ \__, |_| \_|_|\__|_|___/_| |_|
//          | |   | |               __/ |
//          |_|   |_|              |___/
//
//                 Freemium Music
//   Developed and Maintained by Nitish Gadangi

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import nitish.build.com.freemium.Handlers.DataHandlers;
import nitish.build.com.freemium.R;

public class SaavnWebView extends AppCompatActivity {
    WebView webView;
    ProgressDialog progressDialog;
    TextView tv_link;
    Button btn_Download,tv_found;
    String curUrl,songType;
    Button btn_next,btn_prev,btn_home,btn_web_serch,btn_web_set;
    EditText web_srch_et;
    Boolean isEtOPen=false;

    AdView mAdView;

    String banner5;

    Boolean thopu;

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
            startActivity(new Intent(getApplicationContext(), SearchSongs.class));
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

    void showAds(Boolean show,String AD_UNIT_ID){
//        mAdView = new AdView(this);
        if (show){
//            mAdView.setAdSize(AdSize.BANNER);
//            mAdView.setAdUnitId(AD_UNIT_ID);
            AdRequest adRequest = new AdRequest.Builder().build();
//            if(mAdView.getAdSize() != null || mAdView.getAdUnitId() != null)
            mAdView.loadAd(adRequest);
//            LinearLayout linearLayout = findViewById(R.id.ad_layout_browse);
//            linearLayout.addView(mAdView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saavn_web_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.darkAccent));
        }

        webView=findViewById(R.id.webView);
        tv_link=findViewById(R.id.tv_test_link);
        tv_found=findViewById(R.id.btn_found);
        btn_Download=findViewById(R.id.btn_downloadSongs);
        btn_home = findViewById(R.id.btn_home_web);
        btn_next = findViewById(R.id.btn_next_web);
        btn_prev = findViewById(R.id.btn_prev_web);

        web_srch_et = findViewById(R.id.web_srch_et);
        btn_web_serch = findViewById(R.id.btn_web_serch);
        btn_web_set= findViewById(R.id.btn_web_set);

        if (!isNetworkAvailable()){
            new AlertDialog.Builder(SaavnWebView.this)
                    .setTitle("Not Connected to internet?")
                    .setMessage("This app requires active internet connetion otherwise there is a fair chance for app crash.")
                    .setPositiveButton("Ok",null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show();

        }

        //------------------- ADVIEW --------------------------------------------//

//        mAdView = findViewById(R.id.adView_Browse);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        SharedPreferences sc_pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.server_constants),Context.MODE_PRIVATE);
        banner5 =sc_pref.getString(getResources().getString(R.string.sc_banner5),getResources().getString(R.string.banner5));
        mAdView = findViewById(R.id.adView_Browse);
        showAds(true,banner5);

        thopu = sc_pref.getBoolean(getResources().getString(R.string.sc_thope),false);
        thopu=true;

        Button btn_quotes = findViewById(R.id.btn_quotes);
        if (thopu)
            btn_quotes.setText("Enjoy HQ MUSIC");
        btn_quotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);
                //-------------------------//
                startActivity(new Intent(getApplicationContext(),MorePage.class));
            }
        });
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                btn_quotes.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                btn_quotes.setVisibility(View.VISIBLE);
            }
        });
        //------------------- ADVIEW --------------------------------------------//


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

        btn_web_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);
                //-------------------------//

                startActivity(new Intent(getApplicationContext(), SettingsAlbum.class));
                overridePendingTransition(R.anim.slide_in_down,  R.anim.slide_out_down);

            }
        });

        btn_web_serch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);
                //-------------------------//
                if(isEtOPen){
                    btn_web_serch.setBackgroundResource(R.drawable.ic_btm_search);
                    web_srch_et.setVisibility(View.GONE);
                    isEtOPen=false;
                }
                else{
                    btn_web_serch.setBackgroundResource(R.drawable.ic_close_black_24dp);
                    web_srch_et.setVisibility(View.VISIBLE);
                    isEtOPen=true;
                }

            }
        });


        web_srch_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    startWebView(web_srch_et.getText().toString());
                    btn_web_serch.callOnClick();
                    return true;
                }
                return false;
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
                tv_found.setText("");
            }
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
            Intent toDownloadList = new Intent(getApplicationContext(), AlbumSongList.class);
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
        startActivity(new Intent(getApplicationContext(), SearchSongs.class));
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
        startActivity(new Intent(getApplicationContext(), DownloadsPage.class));
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
