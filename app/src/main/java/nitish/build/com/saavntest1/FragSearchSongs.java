package nitish.build.com.saavntest1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
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

import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class FragSearchSongs extends Fragment {
    TextView info2,info3;
    ImageView info1;
    ProgressDialog progressDialog;
    Button btn_search;
    EditText et_SearchBox;
    ListView resList;
    ArrayList<String> res_heads,res_subH,res_dur,res_imgs,res_srcs;
    String query;
    int listSize=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView=inflater.inflate(R.layout.frag_search_songs,container,false);
        info1 = rootView.findViewById(R.id.fs_info1);
        info2 = rootView.findViewById(R.id.fs_info2);
        info3 = rootView.findViewById(R.id.fs_info3);

        btn_search=getActivity().findViewById(R.id.btn_searchBox);
        resList =rootView.findViewById(R.id.lv_fragSongSrch);
        et_SearchBox=getActivity().findViewById(R.id.et_searchBox);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        res_heads =  new ArrayList<>();
        res_subH =  new ArrayList<>();
        res_dur =  new ArrayList<>();
        res_imgs =  new ArrayList<>();
        res_srcs =  new ArrayList<>();

        query=et_SearchBox.getText().toString();
        query=query.replace(" ", "");
        if(query.length()>0)
            new JsonSetup().execute(query);

        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                view.startAnimation(animation1);
                //-------------------------//

                new ShowDownloads().execute(position);
            }
        });

        return rootView;
    }

    public class ShowDownloads extends AsyncTask<Integer,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(!s.equals("FAILED")){
                Intent toSongList=new Intent(getActivity().getApplicationContext(),Album_Song_List.class);
                toSongList.putExtra("TYPE","ALBUM");
                toSongList.putExtra("TYPE_ID",s);
                toSongList.putExtra("PREV_ACT","SEARCH_ACT");
                progressDialog.dismiss();
                startActivity(toSongList);
                getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
            progressDialog.dismiss();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            return DataHandlers.getSongAlbumID(res_srcs.get(integers[0]));
        }
    }

    public  class JsonSetup extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            info1.setImageResource(R.drawable.ic_loading);
            info1.setVisibility(View.VISIBLE);
            info2.setText("Please wait...");
            info2.setVisibility(View.VISIBLE);
            info3.setText("We are searching for your happiness...");
            info3.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... strings) {

            try {
                res_heads=DataHandlers.getSongsSearchJson(strings[0],"HEADS");
                res_subH=DataHandlers.getSongsSearchJson(strings[0],"SUB_HEADS");
//                res_dur=DataHandlers.getSongsSearchJson(strings[0],"DURATION");
                res_imgs=DataHandlers.getSongsSearchJson(strings[0],"IMGS");
                res_srcs=DataHandlers.getSongsSearchJson(strings[0],"SRCS");
                listSize=res_heads.size();

            }catch (Exception e){
                e.printStackTrace();
            }

            if ((res_heads!=null)&&(res_srcs!=null)&&(res_subH!=null)&&(res_imgs!=null))
                return "OK";
            else
                return "FAILED";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("FAILED")){
                info1.setImageResource(R.drawable.ic_err_flag);
                info1.setVisibility(View.VISIBLE);
                info2.setText("No results found for '"+query+"'");
                info2.setVisibility(View.VISIBLE);
                info3.setText("Please check you have the right spelling, or try different keywords.");
                info3.setVisibility(View.VISIBLE);
            }else{
                info1.setVisibility(View.GONE);
                info2.setVisibility(View.GONE);
                info3.setVisibility(View.GONE);

                CustomAdapter customAdapter = new CustomAdapter();
                resList.setAdapter(customAdapter);
                resList.setVisibility(View.VISIBLE);

            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }


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
            convertView = getLayoutInflater().inflate(R.layout.custom_download_list,null);

            TextView cus_songName = convertView.findViewById(R.id.cus_songName);
            TextView cus_artist = convertView.findViewById(R.id.cus_artist);
            ImageView cus_listImg = convertView.findViewById(R.id.cus_listImg);
            TextView cus_duration = convertView.findViewById(R.id.cus_duration);
            ImageView down_icon_shit = convertView.findViewById(R.id.down_icon_shit);
            TextView btm_img_shit = convertView.findViewById(R.id.btm_img_shit);


            btm_img_shit.setVisibility(View.INVISIBLE);
            down_icon_shit.setVisibility(View.GONE);

            cus_songName.setText(res_heads.get(position));
            cus_artist.setText(res_subH.get(position));
            Glide.with(getActivity()).load(res_imgs.get(position)).into(cus_listImg);
            cus_duration.setVisibility(View.GONE);

            return convertView;
        }
    }


}
