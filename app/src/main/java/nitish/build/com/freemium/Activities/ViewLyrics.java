package nitish.build.com.freemium.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import mehdi.sakout.fancybuttons.FancyButton;
import nitish.build.com.freemium.Handlers.DataHandlers;
import nitish.build.com.freemium.R;

public class ViewLyrics extends AppCompatActivity {
    private String lyrics="Not Found";
    private String fname="FAILED";
    private String dir="FAILED";
    private String sname="FAILED";
    private String sngID="FAILED";

    private TextView tv_lyrics;
    private FancyButton download;
    private DownloadButton downloadButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lyrics);

        fetchData();
        fetchViews();
        setupViews();
        setUpListeners();
        setUpDownloadButton();

    }

    private void setUpDownloadButton() {
        if (download == null) downloadButton = new DownloadButton();
        downloadButton.execute(DataHandlers.lyrics_api + sngID);
    }

    private void setUpListeners() {
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewLyrics.this, "Loading Please Wait and Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupViews() {
        ((TextView)findViewById(R.id.tv_lyrics_head)).setText(sname);
    }

    private void fetchViews() {
        download=findViewById(R.id.btn_down_lyrics);
        tv_lyrics=findViewById(R.id.tv_all_lyrics);
    }

    private void fetchData() {
        Intent intent = getIntent();
        sngID=intent.getStringExtra("ID");
        fname=intent.getStringExtra("FNAME");
        dir=intent.getStringExtra("DIR");
        sname=intent.getStringExtra("SNAME");
    }

    public class DownloadButton extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            return DataHandlers.getContent(strings[0]);
        }

        @Override
        protected void onPostExecute(String htmlData) {
            super.onPostExecute(htmlData);
            try {
                String lyrics = getLyricsAsString(htmlData);
                tv_lyrics.setText(lyrics);
                setUpDownloadButton(lyrics);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ViewLyrics.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }

        private void setUpDownloadButton(String lyrics) {
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        saveLyrics(lyrics,fname,dir);
                        Toast.makeText(ViewLyrics.this, "Download Success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        @NotNull
        private String getLyricsAsString(String htmlData) throws JSONException {
            JSONObject lrcObj = new JSONObject(htmlData);
            String lycs = lrcObj.getString("lyrics");
            String creds = lrcObj.getString("lyrics_copyright");
            String fin_lrc = lycs + "<br><br><br><br>"+creds+"<br><br>"+"Downloaded Using FREEMIUM MUSIC app";
            fin_lrc=fin_lrc.replace("<br>","\n");
            return fin_lrc;
        }
    }

    void saveLyrics(String lrc, String fname, String dir) throws IOException {
            File root = new File(dir);
            File gpxfile = new File(root, fname);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(lrc);
            writer.flush();
            writer.close();
    }
}
