package nitish.build.com.freemium.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import mehdi.sakout.fancybuttons.FancyButton;
import nitish.build.com.freemium.Handlers.DataHandlers;
import nitish.build.com.freemium.R;

public class ViewLyrics extends AppCompatActivity {
    String lyrics="Not Found";
    String fname="FAILED";
    String dir="FAILED";
    String sname="FAILED";
    String sngID="FAILED";

    TextView tv_lyrics;
    FancyButton download;

    String final_lyrics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lyrics);

        Intent intent = getIntent();
        sngID=intent.getStringExtra("ID");
        fname=intent.getStringExtra("FNAME");
        dir=intent.getStringExtra("DIR");
        sname=intent.getStringExtra("SNAME");

        ((TextView)findViewById(R.id.tv_lyrics_head)).setText(sname);

        download=findViewById(R.id.btn_down_lyrics);
        tv_lyrics=findViewById(R.id.tv_all_lyrics);

        new SetDownloadButton().execute(DataHandlers.lyrics_api+sngID);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewLyrics.this, "Loading Please Wait and Try Again", Toast.LENGTH_SHORT).show();
            }
        });



    }

    public class SetDownloadButton extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... strings) {
            Log.i("LYRICS",strings[0]);
            return DataHandlers.getContent(strings[0]);
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                Log.i("LYRICS",s);


                JSONObject lrcObj = new JSONObject(s);
                String lycs = lrcObj.getString("lyrics");
                String creds = lrcObj.getString("lyrics_copyright");
                String fin_lrc = lycs+"<br><br><br><br>"+creds+"<br><br>"+"Downloaded Using FREEMIUM MUSIC app";

                fin_lrc=fin_lrc.replace("<br>","\n");
                tv_lyrics.setText(fin_lrc);

//                Markwon markwon = Markwon.create(getApplicationContext());
//                markwon.setMarkdown(tv_lyrics, fin_lrc);

                String finalFin_lrc = fin_lrc;
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            saveLyrics(finalFin_lrc,fname,dir);
                            Toast.makeText(ViewLyrics.this, "Download Success", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.i("LYRICS","EEEEEEEEEE"+e.getMessage());
                        }

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ViewLyrics.this, "Error", Toast.LENGTH_SHORT).show();
            }


        }
    }

    void saveLyrics(String lrc, String fname, String dir) throws IOException {

            Log.i("LYRICS","AAAA"+fname);
            Log.i("LYRICS","BBBBB"+dir);

            File root = new File(dir);
            File gpxfile = new File(root, fname);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(lrc);
            writer.flush();
            writer.close();
    }
}
