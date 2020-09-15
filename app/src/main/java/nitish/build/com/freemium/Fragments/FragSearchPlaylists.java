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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;

import nitish.build.com.freemium.Activities.AlbumSongList;
import nitish.build.com.freemium.Handlers.DataHandlers;
import nitish.build.com.freemium.R;

public class FragSearchPlaylists extends Fragment {
    TextView info2,info3;
    ImageView info1;
    ProgressDialog progressDialog;
    Button btn_search;
    EditText et_SearchBox;
    ArrayList<String> res_out;
    String query;
    int listSize=0;

    RecyclerView rv_fragAlbum;
    RecyclerView.LayoutManager layoutManager;

    RecViewSetup recViewSetup;

    @Override
    public void onStop() {
        super.onStop();

        if(recViewSetup != null && recViewSetup.getStatus() == AsyncTask.Status.RUNNING)
            recViewSetup.cancel(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView=inflater.inflate(R.layout.frag_search_playlists,container,false);

        info1 = rootView.findViewById(R.id.fs_info1);
        info2 = rootView.findViewById(R.id.fs_info2);
        info3 = rootView.findViewById(R.id.fs_info3);

        btn_search=getActivity().findViewById(R.id.btn_searchBox);

        et_SearchBox=getActivity().findViewById(R.id.et_searchBox);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        rv_fragAlbum = rootView.findViewById(R.id.rv_fragplaylists);
        layoutManager = new LinearLayoutManager(getContext());
        rv_fragAlbum.setLayoutManager(layoutManager);

        res_out =  new ArrayList<>();

        query=et_SearchBox.getText().toString();
        query=query.replace(" ", "%20");
        recViewSetup = new RecViewSetup();
        if(query.length()>0)
            recViewSetup.execute(query);

        return rootView;

    }

    class RecViewSetup extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            info1.setImageResource(R.drawable.ic_loading);
            info1.setVisibility(View.VISIBLE);
            info2.setText("Hang On!");
            info2.setVisibility(View.VISIBLE);
            info3.setText("We are searching for your happiness...");
            info3.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("FAILED")){
                listSize=0;
                info1.setImageResource(R.drawable.ic_err_flag);
                info1.setVisibility(View.VISIBLE);
                info2.setText("No playlists found for '"+query.replace("%20"," ")+"'");
                info2.setVisibility(View.VISIBLE);
                info3.setText("Please check you have the right spelling, or try different keywords.");
                info3.setVisibility(View.VISIBLE);
            }else{
                info1.setVisibility(View.GONE);
                info2.setVisibility(View.GONE);
                info3.setVisibility(View.GONE);

                RecAdapter recAdapter = new RecAdapter();
                rv_fragAlbum.setAdapter(recAdapter);

            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                res_out = DataHandlers.playlistExtractor(strings[0]);
            }catch (Exception e){
                return "FAILED";
            }
            if (res_out.size()>0) {
                if (res_out.get(0).equals("FAILED"))
                    return "FAILED";
                listSize=(res_out.size())/4;


                return res_out.get(0);
            }else
                return "FAILED";

        }
    }

    class ShowAlbumPage extends AsyncTask<String,Void,String>{
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!s.equals("FAILED")){
                Intent toSongList=new Intent(getActivity().getApplicationContext(), AlbumSongList.class);
                toSongList.putExtra("TYPE","PLAYLIST");
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


    }

    class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder>{
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_search_view,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Glide.with(getContext()).asBitmap().load(res_out.get((listSize*3)+position)).into(holder.img_art);
            holder.tv_head.setText(StringEscapeUtils.unescapeXml(res_out.get(position*3)));
            holder.tv_subhead.setText(res_out.get(position*3+1));
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(1000);
                    v.startAnimation(animation1);
//                    new ShowAlbumPage().execute(res_out.get(position*3+2));

                    Intent toSongList=new Intent(getActivity().getApplicationContext(), AlbumSongList.class);
                    toSongList.putExtra("TYPE","PLAYLIST");
                    toSongList.putExtra("TYPE_ID",res_out.get(position*3+2));
                    toSongList.putExtra("PREV_ACT","SEARCH_ACT");
                    startActivity(toSongList);
                    getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                }
            });
        }

        @Override
        public int getItemCount() {
            return listSize;
        }


        class ViewHolder extends RecyclerView.ViewHolder{
            TextView tv_head,tv_subhead;
            ImageView img_art;
            ConstraintLayout parentLayout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_head = itemView.findViewById(R.id.cus_songName_frag);
                tv_subhead = itemView.findViewById(R.id.cus_artist_frag);
                img_art = itemView.findViewById(R.id.cus_img_frag69);
                parentLayout = itemView.findViewById(R.id.cust_search_view);
            }
        }

    }
}
