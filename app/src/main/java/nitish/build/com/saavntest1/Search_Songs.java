package nitish.build.com.saavntest1;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;
import com.onesignal.OneSignal;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Search_Songs extends AppCompatActivity {
    String query=" ",searchRes;
    int listSize=0;
    JSONArray searchList;
    ListView resList ;
    ProgressDialog progressDialog;
    Button btn_search1;

    AdView mAdView;
    InterstitialAd mInterstitialAd;
    SharedPreferences pref_main;

    TextView info2,info3;
    ImageView info1;


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
            finishAffinity();
            finish();
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
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
        setContentView(R.layout.activity_search__songs);
        //--------------------------------------------------------------------------------//
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        mAdView = findViewById(R.id.adView_search);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //--------------------------------------------------------------------------------//

        //----------------One Signal------------------------//
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        //--------------------------------------------------//



        if (!isNetworkAvailable()){
            new AlertDialog.Builder(Search_Songs.this)
                    .setTitle("Not Connected to internet?")
                    .setMessage("This app requires active internet connetion otherwise there is a fair chance for app crash.")
                    .setPositiveButton("Ok",null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show();

        }

        pref_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_main),Context.MODE_PRIVATE);
        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
            @Override
            public void onAdLoaded() {
                int tempCount=pref_main.getInt(getResources().getString(R.string.pref_counter),0);
                if (tempCount>=4){
                    tempCount=0;
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                    //Toast.makeText(getApplicationContext(), "Add...", Toast.LENGTH_SHORT).show();
                }
                SharedPreferences.Editor editor=pref_main.edit();
                editor.putInt(getResources().getString(R.string.pref_counter),tempCount).apply();
            }
        });

        ViewPager viewPager = findViewById(R.id.vp_searchPage);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = findViewById(R.id.tl_searchPage);
        tabLayout.setupWithViewPager(viewPager);

        info1 = findViewById(R.id.ms_info1);
        info2 = findViewById(R.id.ms_info2);
        info3 = findViewById(R.id.ms_info3);


        exitToast = Toast.makeText(getApplicationContext(), "click BACK again to exit", Toast.LENGTH_SHORT);


        //-----------Permission part------------------//
        String permission1 = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String permission2 = android.Manifest.permission.READ_EXTERNAL_STORAGE;
        if (getApplicationContext().checkCallingOrSelfPermission(permission1)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(Search_Songs.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        if (getApplicationContext().checkCallingOrSelfPermission(permission2)== PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(Search_Songs.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        //---------------Done Permission--------------//
        Boolean displayed = pref_main.getBoolean(getResources().getString(R.string.pref_update),false);
        if (!displayed)
            new Updater().execute();

        btn_search1=findViewById(R.id.btn_searchBox);
        EditText et_SearchBox=findViewById(R.id.et_searchBox);
        btn_search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);
                //-------------------------//
                info1.setVisibility(View.GONE);
                info2.setVisibility(View.GONE);
                info3.setVisibility(View.GONE);

                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                int tempCount=pref_main.getInt(getResources().getString(R.string.pref_counter),0);
                if (tempCount>=4){
                    tempCount=0;
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                    //Toast.makeText(getApplicationContext(), "Add...", Toast.LENGTH_SHORT).show();
                }
                tempCount=tempCount+1;
                SharedPreferences.Editor editor=pref_main.edit();
                editor.putInt(getResources().getString(R.string.pref_counter),tempCount).apply();

                query=et_SearchBox.getText().toString();
                query=query.replace(" ", "");
                if(query.length()>0)
                    viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));



            }
        });


        et_SearchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    btn_search1.callOnClick();
                    return true;
                }
                return false;
            }
        });




    }

    public class Updater extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            return DataHandlers.getContent("https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1yxVd1HRTBbO5ZjVHggjSEC3cLviRdJsMxojHODl6hSU&sheet=Sheet1");
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.contains("YES_THERE_IS")){
                Log.i("UPD_TEST","YES");
                try {
                    JSONObject obj = new JSONObject(s);
                    JSONArray jsonArray = obj.getJSONArray("Sheet1");
                    JSONObject mainObj = jsonArray.getJSONObject(0);
                    String url =mainObj.getString("URL");
                    String description = mainObj.getString("Description");
                    String version = mainObj.getString("Version");
                    String date = mainObj.getString("Date");

                    ViewDialog viewDialog=new ViewDialog(version+"   "+date,description,url);
                    viewDialog.showDialog(Search_Songs.this);
                    SharedPreferences pref_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_main),Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref_main.edit();
                    editor.putBoolean(getResources().getString(R.string.pref_update),true).apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Log.i("UPD_TEST","NO UPdate");
            }
        }
    }

    public class ViewDialog {
        String head,des,url;
        ViewDialog(String head,String des,String url){
            this.head=head;
            this.des=des;
            this.url=url;
        }

        public void showDialog(Activity activity){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_update);

            TextView tv_head = dialog.findViewById(R.id.dialog_ud_head);
            TextView tv_des = dialog.findViewById(R.id.dialog_ud_des);
            Button btn_update = dialog.findViewById(R.id.dialog_ud_btn_ud);
            Button close = dialog.findViewById(R.id.ud_dialog_close);

            tv_des.setText(des);
            tv_head.setText(head);

            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //------Animation-----------//
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(500);
                    v.startAnimation(animation1);
                    //-------------------------//
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //------Animation-----------//
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(500);
                    v.startAnimation(animation1);
                    //-------------------------//
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                //ChildFragment1 at position 0
                case 0:
                    return new FragSearchSongs(); //ChildFragment2 at position 1
                case 1:
                    return new FragSearchAlbums(); //ChildFragment3 at position 2
                case 2:
                    return new FragSearchPlaylists();
                case 3:
                    return new FragSearchTop();
            }
            return null; //does not happen
        }

        @Override
        public int getCount() {
            return 4; //three fragments
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position)
            {

                case 0:
                    return "Songs";
                case 1:
                    return "Albums";
                case 2:
                    return "Playlists";
                case 3:
                    return "Top";
            }
            return null;
        }
    }




    public void sBtmSrch(View v){
        //------Animation-----------//
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1000);
        v.startAnimation(animation1);
        //-------------------------//
//        startActivity(new Intent(getApplicationContext(),Search_Songs.class));
    }
    public void sBtmBrws(View v){
        //------Animation-----------//
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1000);
        v.startAnimation(animation1);
        //-------------------------//
        startActivity(new Intent(getApplicationContext(),SaavnWebView.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    public void sBtmDown(View v){
        //------Animation-----------//
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1000);
        v.startAnimation(animation1);
        //-------------------------//
        startActivity(new Intent(getApplicationContext(),Downloads_Page.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    public void sBtmMore(View v){
        //------Animation-----------//
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1000);
        v.startAnimation(animation1);
        //-------------------------//
        startActivity(new Intent(getApplicationContext(),MorePage.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
