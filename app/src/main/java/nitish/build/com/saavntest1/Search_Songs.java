package nitish.build.com.saavntest1;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Search_Songs extends AppCompatActivity {
    String query=" ",searchRes;
    int listSize=0;
    JSONArray searchList;
    ListView resList ;
    ProgressDialog progressDialog;
    AdView mAdView;
    InterstitialAd mInterstitialAd;
    SharedPreferences pref_main;


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

         pref_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_main),MODE_PRIVATE);

        mInterstitialAd = new InterstitialAd(this);
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

        if (!isNetworkAvailable()){
            new AlertDialog.Builder(Search_Songs.this)
                    .setTitle("Not Connected to internet?")
                    .setMessage("This app requires active internet connetion otherwise there is a fair chance for app crash.")
                    .setPositiveButton("Ok",null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show();

        }

        EditText et_SearchBox=findViewById(R.id.et_searchBox);
        Button btn_search=findViewById(R.id.btn_searchBox);

        resList =findViewById(R.id.list_searchres);
        resList.setVisibility(View.GONE);

        progressDialog = new ProgressDialog(Search_Songs.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.dismiss();
        TextView tv_ins = findViewById(R.id.tv_ins);

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


        TextView tv_resStatus=findViewById(R.id.tv_urResStatus);
        tv_resStatus.setText("Your search results appear here:");
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                tv_resStatus.setText("search for ..."+query+"...");
                resList.setVisibility(View.INVISIBLE);

                try {
                    if(query.length()==0){
                        tv_resStatus.setText("Type something and press Search button..");
                    }else{
                    searchRes=DataHandlers.getSearchResult(query);
                    if (searchRes.equals("[]")){
                        tv_resStatus.setText("No matches found for :"+query);

                    }else{

                    searchList = new JSONArray(searchRes);

                    listSize=searchList.length();


                    tv_resStatus.setText("Found: "+listSize+" Matches");
                    CustomAdapter customAdapter = new CustomAdapter();
                    resList.setAdapter(customAdapter);
                    resList.setVisibility(View.VISIBLE);
                    tv_ins.setVisibility(View.GONE);
                    }}


                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                    btn_search.callOnClick();
                    return true;
                }
                return false;
            }
        });


        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                progressDialog.show();
                try {
                    JSONObject songJson = new JSONObject(searchList.getString(position));
                    String dataType = songJson.getString("type").toUpperCase();
                    String dataID;
                    if(dataType.equals("SONG")){
                        dataID = songJson.getString("url");
                        dataID= DataHandlers.getSongID(dataID);
                    }
                    else
                        dataID = songJson.getString("id");
                    Intent toSongList=new Intent(getApplicationContext(),Album_Song_List.class);
                    toSongList.putExtra("TYPE",dataType);
                    toSongList.putExtra("TYPE_ID",dataID);
                    toSongList.putExtra("PREV_ACT","SEARCH_ACT");
                    progressDialog.dismiss();
                    startActivity(toSongList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            //Toast.makeText(syllabus_select_course.this, COURSES.length, Toast.LENGTH_SHORT).show();
            return (listSize);
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.custom_search_list,null);

            TextView songName= convertView.findViewById(R.id.csl_songName);
            TextView songInfo = convertView.findViewById(R.id.csl_info);
            TextView songType = convertView.findViewById(R.id.csl_type);
            TextView songLangYear = convertView.findViewById(R.id.csl_yearLang);

            try {
                String type="";
                JSONObject curSong = new JSONObject(searchList.getString(position));
                type=curSong.getString("type");
                songName.setText(curSong.getString("title"));
                songInfo.setText(curSong.getString("description"));
                songType.setText(type);
                if (type.equals("album")){
                    JSONObject moreInfo = new JSONObject(curSong.getString("more_info"));
                    String tempYL=moreInfo.getString("language")+" • "+moreInfo.getString("year");
                    songLangYear.setText(tempYL);
                    songLangYear.setVisibility(View.VISIBLE);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return convertView;
        }
    }

    public void sBtmSrch(View v){
//        startActivity(new Intent(getApplicationContext(),Search_Songs.class));
    }
    public void sBtmBrws(View v){
        startActivity(new Intent(getApplicationContext(),SaavnWebView.class));
    }
    public void sBtmDown(View v){
        startActivity(new Intent(getApplicationContext(),Downloads_Page.class));
    }
    public void sBtmMore(View v){
        startActivity(new Intent(getApplicationContext(),MorePage.class));
    }
}
