package nitish.build.com.freemium.Fragments;

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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nitish.build.com.freemium.Activities.AlbumSongList;
import nitish.build.com.freemium.Handlers.DataHandlers;
import nitish.build.com.freemium.R;

public class FragSearchTop extends Fragment {
    String query=" ",searchRes;
    int listSize=0;
    JSONArray searchList;
    ListView resList ;
    ProgressDialog progressDialog;
    SharedPreferences pref_main;
    InterstitialAd mInterstitialAd;

    Button btn_search1;
    EditText et_SearchBox;
    TextView info2,info3,s_info2,s_info3;
    ImageView info1,s_info1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView=inflater.inflate(R.layout.frag_search_top,container,false);

        btn_search1=getActivity().findViewById(R.id.btn_searchBox);
        resList =rootView.findViewById(R.id.lv_frag_top);
        et_SearchBox=getActivity().findViewById(R.id.et_searchBox);

        info1 = rootView.findViewById(R.id.fs_info1);
        info2 = rootView.findViewById(R.id.fs_info2);
        info3 = rootView.findViewById(R.id.fs_info3);
        s_info1 = getActivity().findViewById(R.id.ms_info1);
        s_info2 = getActivity().findViewById(R.id.ms_info2);
        s_info3 = getActivity().findViewById(R.id.ms_info3);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        pref_main = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_main),Context.MODE_PRIVATE);

        mInterstitialAd = new InterstitialAd(getActivity().getApplicationContext());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.ad_inter1));
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


        query=et_SearchBox.getText().toString();
        query=query.replace(" ", "%20");
        info2.setText("No best match found for '"+query+"'");
        resList.setVisibility(View.INVISIBLE);
        if(query.length()>0){
            info1.setVisibility(View.GONE);
            info2.setVisibility(View.GONE);
            info3.setVisibility(View.GONE);
            new JsonSetup().execute(query);

        }





        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                view.startAnimation(animation1);
                //-------------------------//
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
                    Intent toSongList=new Intent(getActivity().getApplicationContext(), AlbumSongList.class);
                    toSongList.putExtra("TYPE",dataType);
                    toSongList.putExtra("TYPE_ID",dataID);
                    toSongList.putExtra("PREV_ACT","SEARCH_ACT");
                    progressDialog.dismiss();
                    startActivity(toSongList);
                    getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        return rootView;
    }



    public  class JsonSetup extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {

            try {
                searchRes=DataHandlers.getSearchResult(strings[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (searchRes.equals("[]")){
                return "FAILED";

            }else{

                try {
                    searchList = new JSONArray(searchRes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listSize=searchList.length();

            }

            return Integer.toString(listSize);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("FAILED")){
                info1.setVisibility(View.VISIBLE);
                info2.setVisibility(View.VISIBLE);
                info3.setVisibility(View.VISIBLE);
            }else{
                info1.setVisibility(View.GONE);
                info2.setVisibility(View.GONE);
                info3.setVisibility(View.GONE);

                CustomAdapter customAdapter = new CustomAdapter();
                resList.setAdapter(customAdapter);
                resList.setVisibility(View.VISIBLE);

            }

//            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
//            progressDialog.show();
        }


    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
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
                songName.setText(StringEscapeUtils.unescapeXml(curSong.getString("title")));
                songInfo.setText(StringEscapeUtils.unescapeXml(curSong.getString("description")));
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

}
