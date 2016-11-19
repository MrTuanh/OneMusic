package teamthat.com.onemusic.Util;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import teamthat.com.onemusic.activity.Constant;
import teamthat.com.onemusic.model.ArtistMusic;

/**
 * Created by ASUS on 11/20/2016.
 */

public class Util {
    public void loadFavorite(){
        //new reloadFavorite().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        try {
            if(!Constant.sharedPreferences.getString("Id","").equals("")){
                Log.d("favorite","id "+Constant.sharedPreferences.getString("Id",""));
                getAllFavoriteSong(Constant.sharedPreferences.getString("Id",""));}
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public Boolean checkFavoriteSong(String userid,String songid) throws JSONException {
        getJson mygetJson = new getJson();
        String query = makeIsFavoriteSatement(userid,songid);

        mygetJson.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);
        boolean favorited = parseJsonCheckFavorite(mygetJson.json);
        return favorited;
    }
    public Boolean changeFavoriteSong(String userid,String songid) throws JSONException {
        Log.d("favorite","changeFavoriteSong "+songid);

        String query = makeChangeFavoriteSatement(userid,songid);
        new getJson().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);
        Log.d("favorite","query la "+query);

        return true;
    }
    public void getAllFavoriteSong(String userid) throws JSONException {
        String query = makeGetAllFavoriteSatement(userid);


        new getJson(){
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
//                    Constant.listfavoriteSong.removeAll(Constant.listfavoriteSong);
                    Constant.listfavoriteSong = parseJsonGetAllFavorited(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);

    }
    public String makeChangeFavoriteSatement(String userid,String songid){
        StringBuilder builder = new StringBuilder(Constant.CHANGEFAVORITE_API);
        builder.append("&userid=").append(userid);
        builder.append("&songid=").append(songid);

        return builder.toString();
    }
    public String makeIsFavoriteSatement(String userid,String songid){
        StringBuilder builder = new StringBuilder(Constant.CHECKFAVORITED_API);
        builder.append("&userid=").append(userid);
        builder.append("&songid=").append(songid);

        return builder.toString();
    } public String makeGetAllFavoriteSatement(String userid){
        StringBuilder builder = new StringBuilder(Constant.GETALLFAVORITE_API);
        builder.append("&userid=").append(userid);

        return builder.toString();
    }

    public Boolean parseJsonCheckFavorite(String json) throws JSONException {
        JSONObject response = new JSONObject(json);
        Boolean isFavorited = response.optBoolean("favorited");
        return isFavorited;

    }
    public Boolean parseJsonChangeFavorite(String json) throws JSONException {
        JSONObject response = new JSONObject(json);
        Boolean isFavorited = response.optBoolean("error");
        Log.d("favorite",isFavorited+"");
        return isFavorited;

    }
    public ArrayList<ArtistMusic> parseJsonGetAllFavorited(String json) throws JSONException {
        Log.d("favorite","vafo parse json");
        ArrayList<ArtistMusic> listMusic = new ArrayList<>();
        try{
            JSONArray array = new JSONArray(json);
            for(int i=0; i< array.length();i++ ){
                JSONObject cm = array.optJSONObject(i);
                String nameMusic = cm.optString("name");
                String pathMusic = cm.optString("musicpath");
                String id = cm.optString("id");
                ArtistMusic music = new ArtistMusic(nameMusic, pathMusic,id);
                Constant.listfavoriteSong.add(music);
                Log.d("favorite","name "+nameMusic);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return listMusic;
    }


   public class getJson extends AsyncTask<String,String,String>{
       String json="";



       @Override
        protected String doInBackground(String... params) {
            Log.d("favorite","vao doInBackground");
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                URL url = new URL(params[0]);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.d("mydebug",forecastJsonStr);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }


            return forecastJsonStr;
        }

   }
    public class reloadFavorite extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                if(!Constant.sharedPreferences.getString("Id","").equals("")){
                   getAllFavoriteSong(Constant.sharedPreferences.getString("Id",""));}
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


}
