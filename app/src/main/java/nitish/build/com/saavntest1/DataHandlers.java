package nitish.build.com.saavntest1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;


import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class DataHandlers {
    static String albumApiLink="https://www.jiosaavn.com/api.php?_format=json&__call=content.getAlbumDetails&albumid=",
                    searchApiLink="https://www.jiosaavn.com/api.php?_format=json&__call=autocomplete.get&query=",
                    playlistApiLink="https://www.jiosaavn.com/api.php?_format=json&__call=playlist.getDetails&listid=",
                    artistApiLink="",
                    songApiLink="https://www.jiosaavn.com/api.php?app_version=5.18.3&api_version=4&readable_version=5.18.3&v=79&_format=json&__call=song.getDetails&pids=",
                    songs_searchLink="https://www.jiosaavn.com/search/",
                    albums_searchLink="https://www.jiosaavn.com/search/album/",
                    playlists_searchLink="https://www.jiosaavn.com/search/playlist/",
                    dot=" • ";

    static String getContent(String finUrl){
        String finString="FAILED";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        org.apache.http.client.HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
        HttpGet httpget = new HttpGet(finUrl);
        httpget.setHeader("User-Agent","Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:49.0) Gecko/20100101 Firefox/49.0");// Set the action you want to do

        try {
            HttpResponse response = httpclient.execute(httpget); // Executeit
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent(); // Create an InputStream with the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) // Read line by line
                sb.append(line + "\n");

            finString = sb.toString(); // Result is here


            is.close(); // Close the stream
        }catch (Exception e){
            e.printStackTrace();
        }
        return finString;
    }

    static String getAlbumJson(String albumID){
        String finJson ="FAILED";
        String finUrl= albumApiLink+albumID;
        String jsonData=getContent(finUrl);
        jsonData= jsonData.substring(jsonData.indexOf("{"));
        if (jsonData.contains("title"))
            finJson=jsonData;
        return finJson;
    }

    static String getPlaylistJson(String playlistID){
        String finJson ="FAILED";
        String finUrl= playlistApiLink+playlistID;
        String jsonData=getContent(finUrl);
        jsonData= jsonData.substring(jsonData.indexOf("{"));
        if (jsonData.contains("listid"))
            finJson=jsonData;
        return finJson;
    }

    static String getAlbumID(String url){
        String resID="FAILED";
        String data =getContent(url);
        Document doc = Jsoup.parse(data);

        Elements element = doc.select(".play");

        try {
            String tempData = element.toString();
            tempData = tempData.substring(tempData.indexOf("['albumid','"),tempData.indexOf("'])"));
            tempData=tempData.replace("['albumid','","");

            resID=tempData;
        }catch (Exception e){
            resID="FAILED";
        }

//        String str_ptrn = "\\[\'albumid\',\'"+"(.*?)"+"\'\\]";
//
//        Pattern ptrn_albumId= Pattern.compile(str_ptrn);
//        Matcher mat_albId = ptrn_albumId.matcher(element.toString());
//        Pattern ptrn_albumName= Pattern.compile(str_NamePtrn);
//        Matcher mat_albName = ptrn_albumName.matcher(element.toString());

//        while (mat_albId.find())
//            resID = mat_albId.group(1);
//        while (mat_albName.find())
//            resName = mat_albName.group(1);
        resID=resID.replace(" ","");

        return resID;
    }

    static String getPlaylistID(String url){
        String resID="FAILED";
        String data=getContent(url);
        Document doc = Jsoup.parse(data);

        Elements element = doc.select(".flip-layout");
        data=element.toString();

        try {
            data=data.substring(data.indexOf("<"),data.indexOf(">")+1);
            data=data.substring(data.indexOf("data-listid=\""),data.indexOf("\">")+1);
            data=data.replace("data-listid=\"","");
            data=data.replace("\"","");

            resID=data;
            resID=resID.replace(" ","");
            if (resID.equals(""))
                return "FAILED";
            return resID;
        }catch(Exception e){
            return resID;
        }

    }

    static String getSongID(String url){
        String resID =getSongAlbumID(url);
        return resID;
    }

    static String getDirectID(String url){
        String linkType=getLinkType(url);

        if (linkType.equals("FAILED"))
            return "FAILED";
        if (linkType.equals("ALBUM"))
            return getAlbumID(url);
        if (linkType.equals("PLAYLIST"))
            return getPlaylistID(url);
        if (linkType.equals("SONG"))
            return getSongID(url);
        return "FAILED";
    }

    static String getDownloadLink(JSONObject songJsn,String kbps) throws JSONException {

        String downUrl = songJsn.getString("media_preview_url");
        if (kbps.equals("320"))
        downUrl=downUrl.replace("96_p.mp4","320.mp4");
        else if (kbps.equals("160"))
            downUrl=downUrl.replace("96_p.mp4","160.mp4");
        else if (kbps.equals("96"))
            downUrl=downUrl.replace("96_p.mp4","96.mp4");
        else
            downUrl=downUrl.replace("96_p.mp4","160.mp4");

        downUrl=downUrl.replace("preview","aac");

        return downUrl;
    }

    static String getAlbumName(JSONObject songJsn) throws JSONException {
        return songJsn.getString("album")+"-"+songJsn.getString("year");
    }



    static String getSearchResult(String query) throws JSONException {

        String jsonData=getContent(searchApiLink+query);
        jsonData=jsonData.substring(jsonData.indexOf("{"));

        JSONObject fullJson = new JSONObject(jsonData);
        JSONObject albumsJson = new JSONObject(fullJson.getString("albums"));
        JSONObject playlistsJson = new JSONObject(fullJson.getString("playlists"));
        JSONObject songlistsJson = new JSONObject(fullJson.getString("songs"));



        String al=albumsJson.getString("data"),pl=playlistsJson.getString("data"),
                sl=songlistsJson.getString("data");
        al = al.replace("[","").replace("]","");
        pl = pl.replace("[","").replace("]","");
        sl = sl.replace("[","").replace("]","");

        if (al.length()!=0)
            al=","+al;
        if (pl.length()!=0)
            pl=","+pl;
        if (sl.length()!=0)
            sl=","+sl;

        String tempResJson="";

        tempResJson = "["+ sl + al + pl + "]";
        int tempIndex=tempResJson.indexOf(",");

        if(tempIndex<1)
            return "[]";
        tempResJson=tempResJson.substring(0,tempIndex)+tempResJson.substring(tempIndex+1);
        if (tempResJson.contains(",,")){
            tempIndex=tempResJson.indexOf(",,");
            tempResJson=tempResJson.substring(0,tempIndex)+tempResJson.substring(tempIndex+1);
        }



        if(tempResJson.length()>3){
            return tempResJson;
        }
        return "[]";
    }



    static ArrayList<String> songExtractor(String query){
        String web_content=getContent(songs_searchLink+query);
        Document doc = Jsoup.parse(web_content);
        ArrayList<String> res_output = new ArrayList<>();
        Elements artElement = doc.select(".art");
        int count=0;
        for (Element div : artElement){
            count++;
            String str_head = div.select("img").attr("alt").replace(" Song","");
            String str_img = div.select("img").attr("src").replace("50x50","150x150");
            res_output.add(str_head);
            res_output.add(str_img);
        }
        int count2=0;
        Elements albumElement = doc.select(".meta-album");
        for (Element div:albumElement){
            count2++;
            String str_subH = div.text();
            String str_src = div.attr("href");
            res_output.add(str_subH);
            res_output.add(str_src);
        }
        if (count==count2)
            return res_output;
        res_output.clear();
        res_output.add("FAILED");
        return res_output;
    }

    static ArrayList<String> albumExtractor(String query){
        String web_content=getContent(albums_searchLink+query);
        Document doc = Jsoup.parse(web_content);
        ArrayList<String> res_output = new ArrayList<>();
        Elements countElement = doc.select(".art");
        int count=0;
        String str_id,str_head,str_img;
        for (Element div : countElement){
            count++;
            str_img = div.select("a").select("img").attr("src");
            String str_on_clck=div.select("a").attr("onclick");
            str_id =str_on_clck.substring(str_on_clck.indexOf("'albumid','"),str_on_clck.indexOf("'],")).replace("'albumid','","");
            str_head = str_on_clck.substring(str_on_clck.indexOf("art:click:"),str_on_clck.indexOf("','albu")).replace("art:click:","");

            res_output.add(str_head);
            res_output.add(str_img);
            res_output.add(str_id);
        }
        if (res_output.size()==count*3)
            return res_output;
        res_output.clear();
        res_output.add("FAILED");
        return res_output;
    }

    static ArrayList<String> playlistExtractor(String query){
        String web_content=getContent(playlists_searchLink+query);
        Document doc = Jsoup.parse(web_content);
        ArrayList<String> res_output = new ArrayList<>();
        Elements playlistElement = doc.select(".playlist-meta");
        Elements artElement = doc.select(".art");
        int count=0;
        for (Element div : playlistElement){
            count++;
            String str_head = div.select("h3").text();
            String str_subH = div.select("p").text();
            String str_src = div.select("h3").select("a").attr("href");
            res_output.add(str_head);
            res_output.add(str_subH);
            res_output.add(str_src);
        }
        Log.i("SRCH_T",count+"j1");
        int count2=0;
        for (Element div:artElement){
            count2++;
            String str_img = div.select("img").attr("src");
            res_output.add(str_img);
        }
        Log.i("SRCH_T",count2+"j2");

        if (count==count2)
            return res_output;
        res_output.clear();
        res_output.add("FAILED");
        return res_output;
    }



    static String getSongDuration(JSONObject songJson) throws JSONException {
        String durStr =songJson.getString("duration");
        int dur = Integer.parseInt(durStr);
        int min = dur/60;
        int sec = dur%60;
        if (sec<=9)
            return min+":0"+sec;
        else
            return min+":"+sec;

    }

    static String getLinkType(String url){
        if(url.length()<=40)
            return "FAILED";
        String temp=url.substring(0,40);
        temp=temp.toLowerCase();
        if(temp.contains("album"))
            return "ALBUM";
        else if (temp.contains("song"))
            return "SONG";
        String playlistID=getPlaylistID(url);
        if (!(playlistID.equals("FAILED")))
            return "PLAYLIST";
        return "FAILED";
    }

    static int generateNotificationID(String songId){
        int chrAscii=0;
        String resID="";
        for(int i=0;i<songId.length();i++){
           chrAscii= (int)songId.charAt(i);
           resID+=chrAscii;
        }
        try {
            int res=Integer.parseInt(resID);
            return res;
        }catch (Exception e){
            return 200;
        }
    }

    static String getSongAlbumID(String songUrl){
        String resID = "FAILED";
        String data=getContent(songUrl);
        String selector ="<meta property=\"music:album\" content=\"";
        data=data.substring(data.indexOf("<meta property=\"music:album\""));
        data=data.substring(0,data.indexOf("\"/>"));
        data=data.replace(selector,"");
        if (data.contains("album")){
            resID=getAlbumID(data);
        }

        return resID;
    }

    static void setTags2(String absPath,String tempJson,String albumArt_fName) throws Exception {
        String format = absPath.substring(absPath.lastIndexOf(".")+1);
        if (format.equals("mp3")){
            File prev = new File(absPath);
            File temp_m4a = new File(absPath.substring(0,absPath.lastIndexOf("."))+".m4a");
            prev.renameTo(temp_m4a);
            absPath=temp_m4a.getAbsolutePath();
        }
        JSONObject songJson = new JSONObject(tempJson);
        if(songJson.toString().contains("song")) {
            String ALBUM_N = songJson.getString("album");
            String YEAR = songJson.getString("year"),
                    ARTIST = songJson.getString("primary_artists"),
                    ALBUM_ARTISTS = songJson.getString("singers"),
                    COMPOSER = songJson.getString("music"),
                    DESCR = songJson.getString("starring"),
                    COPYR = songJson.getString("copyright_text");
//                    url_img=songJson.getString("image");

            File albdirectory = new File(absPath);

            AudioFile audioFile = AudioFileIO.read(albdirectory);
            Mp4Tag mp4tag = (Mp4Tag) audioFile.getTag();
            mp4tag.setField(Mp4FieldKey.ALBUM, ALBUM_N);
            mp4tag.setField(Mp4FieldKey.ARTIST, ARTIST);
            mp4tag.setField(Mp4FieldKey.ALBUM_ARTIST, ALBUM_ARTISTS);
            mp4tag.setField(Mp4FieldKey.DAY, YEAR);
            mp4tag.setField(Mp4FieldKey.COMPOSER, COMPOSER);
            mp4tag.setField(Mp4FieldKey.DESCRIPTION, DESCR);
            mp4tag.setField(Mp4FieldKey.COPYRIGHT, COPYR);
            audioFile.commit();

//            url_img=url_img.replace("150x150.jpg","500x500.jpg");
            File img_art = new File(absPath.substring(0,absPath.lastIndexOf("/"))+ "/" + albumArt_fName);
            Log.i("STAGS_2",img_art.getPath());

            Artwork artwork = ArtworkFactory.createArtworkFromFile(img_art);
            mp4tag.setField(artwork);

//            RandomAccessFile imageFile = new RandomAccessFile(img_art, "r");
//            byte[] imagedata = new byte[(int) imageFile.length()];
//            imageFile.read(imagedata);
//            mp4tag.createArtworkField(imagedata);

            audioFile.commit();
        }

        if (format.equals("mp3")){
            File prev_mp3 = new File(absPath);
            File new_mp3 = new File(absPath.substring(0,absPath.lastIndexOf("."))+".mp3");
            prev_mp3.renameTo(new_mp3);
        }
    }

    static String getFileName(String songName,String albumName,String kbps,String format,String d_FN_code){

        String res="FAILED";

        if (d_FN_code.equals("S")){
            res=songName+"."+format;

        }else if (d_FN_code.equals("SA")){
            res=songName+"["+albumName+"]"+"."+format;

        }else if (d_FN_code.equals("SAK")){
            res=songName+"["+albumName+"]"+"_"+kbps+"."+format;

        }else if (d_FN_code.equals("SK")){

            res=songName+"_"+kbps+"."+format;
        }

        return res;
    }

    static String makeDir(String folderName){
        File albdirectory = new File(Environment.getExternalStorageDirectory()+ "/FREEMIUM_DOWNLOADS/"+folderName+"/");
        if (!albdirectory.exists())
            if(albdirectory.mkdirs())
                Log.i("DIRDONE2","2DONE");
            else
                Log.i("DIRDONE2","FAILED");
        return albdirectory.getPath();
    }

    static String makeDir2(String d_dir,String folderName,Boolean isSfEnabled){
        String res = "FAILED";
        try {
            File albdirectory = new File(d_dir);
            if (isSfEnabled)
                albdirectory = new File(d_dir+folderName+"/");

            if (!albdirectory.exists())
                if(albdirectory.mkdirs())
                    Log.i("DIRDONE2","2DONE");
                else
                    Log.i("DIRDONE2","FAILED");

                res = albdirectory.getPath();

        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }


}
