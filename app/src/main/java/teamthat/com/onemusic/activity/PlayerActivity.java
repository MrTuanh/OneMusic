package teamthat.com.onemusic.activity;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import teamthat.com.onemusic.DatabaseHelper.DatabaseHelper;
import teamthat.com.onemusic.R;
import teamthat.com.onemusic.Util.Util;
import teamthat.com.onemusic.model.Artist;
import teamthat.com.onemusic.model.ArtistMusic;

/**
 * Created by thietit on 11/2/2016.
 */

public class PlayerActivity extends AppCompatActivity {
    int i,j=0;
    ImageButton ibPlay, ibPrevious, ibNext, ibFavourite, ibDowload;
    static  SeekBar seekBar;
    static TextView tvMinTime,tvMaxTime;
    RoundImage roundImage;
    static int index;
    NotificationManager manager;
    Notification myNotication;
    DatabaseHelper databaseHelper;
    int max;
    boolean m = true;
    setImage setImage;
    boolean isPlayed = false;
    Intent intent;
    BoundService boundService;
    Bundle bundle;
    ImageView image;
    boolean h;
    public static final String ACTION_PLAY = "com.example.action.PLAY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        addCtrols();
        setImage = new setImage();
        databaseHelper = new DatabaseHelper(PlayerActivity.this);

        final ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo !=null){
            try {
                URL url = new URL(LoginActivity.LOGIN_API+Constant.artist.getImage());
                Log.d("checkimage","tải được ảnh "+LoginActivity.LOGIN_API+Constant.artist_image);
                setImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d("mydebug","có lỗi khi tải ảnh"+e);
            }
        }else{
            Log.d("mydebug","tải ảnh mặc định");
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_default);
            roundImage = new RoundImage(bitmap);
            image.setImageDrawable(roundImage);
        }
        clickImgButton();
        index = ArtistMusicActivity.index;
        max = ArtistMusicActivity.max-1;
        Intent it= getIntent();
        boolean t=false;
        h= false;
        t = it.getBooleanExtra("name",false);
        h = it.getBooleanExtra("online",false);
        if(!h){
            ibDowload.setEnabled(false);
            ibFavourite.setEnabled(false);
        }
        boundService = new BoundService();
        intent = new Intent(PlayerActivity.this,BoundService.class);
        intent.setAction(ACTION_PLAY);
        ibPlay.setImageResource(R.drawable.ic_pause);

        getSupportActionBar().setTitle(Constant.name);
        // nếu chưa có service chạy, thì khởi tạo service
        Log.d("mydebug","onCreate");
        if(isServiceRunning()&t){
            Log.d("mydebug","chay lai service 1 "+isServiceRunning());
            stopService(intent);
             startService(intent);
        }else{
            Log.d("mydebug","chay lai 2"+isServiceRunning()+" "+t);
            startService(intent);
        }

        if(BoundService.mediaPlayer!=null){
            setMaxTime(BoundService.mediaPlayer.getDuration());
            Log.d("mydebug","duration "+BoundService.mediaPlayer.getDuration());
        }
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
              if(event.getAction()==MotionEvent.ACTION_MOVE){
                  seekBar.setProgress(seekBar.getProgress());

              }
                return false;
            }
        });
     }
     public void addCtrols() {
            ibPlay = (ImageButton) findViewById(R.id.ib_play);
            ibPrevious = (ImageButton) findViewById(R.id.ib_previous);
            ibNext = (ImageButton) findViewById(R.id.ib_next);
            ibFavourite = (ImageButton) findViewById(R.id.ib_favourite);
            ibDowload = (ImageButton) findViewById(R.id.ib_dowload);
            seekBar = (SeekBar) findViewById(R.id.seekBar);

            tvMaxTime = (TextView) findViewById(R.id.tv_end);
            tvMinTime = (TextView) findViewById(R.id.tv_start);
            image = (ImageView) findViewById(R.id.iv);
        }
    public void clickImgButton() {
        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlayed){
                    i++;
                    ibPlay.setImageResource(R.drawable.ic_pause);
                        Log.d("mydebug","play "+i);
                      BoundService.mediaPlayer.start();
                        isPlayed=false;
                }else{
                    j++;
                      ibPlay.setImageResource(R.drawable.ic_play);
                     Log.d("mydebug","pause "+j);
                      BoundService.mediaPlayer.pause();

                    isPlayed=true;
                }
             }
        });

        ibPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ibPlay.setImageResource(R.drawable.ic_pause);
                int k = getIndexOfMusic(0,-1);

                Constant.path =LoginActivity.LOGIN_API+ArtistMusicActivity.listMusic.get(k).getMusicPath();
                   Constant.name = ArtistMusicActivity.listMusic.get(k).getNameMusic();
                getSupportActionBar().setTitle(Constant.name);
                Log.d("mydebug","previous "+k+" = "+Constant.path);
                if(isServiceRunning()){
                    stopService(intent);

                }
                startService(intent);
              //  new openService().execute();
                    isPlayed=false;
                 }
             });

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibPlay.setImageResource(R.drawable.ic_pause);

            int k = getIndexOfMusic(1,1);
            Constant.path =LoginActivity.LOGIN_API+ArtistMusicActivity.listMusic.get(k).getMusicPath();
                  Constant.name = ArtistMusicActivity.listMusic.get(k).getNameMusic();
                getSupportActionBar().setTitle(Constant.name);
               Log.d("mydebug","next "+k+" = "+Constant.path);
                isPlayed=false;
                        if(isServiceRunning()){
                                    stopService(intent);
                           }
                   startService(intent);

             }
        });

        ibFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("favorite","click onFavorite "+Constant.sharedPreferences.getString("Id",""));
                if(!Constant.sharedPreferences.getString("Id","").equals("")){
                    Log.d("favorite","not null");
                    Util util = new Util();
                    try {
                        util.changeFavoriteSong(Constant.sharedPreferences.getString("Id",""),Constant.music_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        ibDowload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("mydebug","bam vao day");
                Log.d("mydebug","Constant hien tai la "+Constant.music_id);
               if( databaseHelper.checkExistSong(Constant.music_id)){

                   Toast.makeText(getApplicationContext(),"Bai hat da tai ve",Toast.LENGTH_SHORT).show();
               }else{
                   for(int i=0;i<Constant.listpath_music_downloading.size();i++){
                       if(Constant.listpath_music_downloading.get(i).equals(Constant.music_id)){
                           Log.d("mydebug","Check list downloading "+Constant.listpath_music_downloading.get(i));
                           m=false;
                       }
                   }
                   if(m) {
                       Constant.listpath_music_downloading.add(Constant.music_id);
                       Log.d("mydebug","Vao thoi");
                       new downLoad().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                   }else{
                       Log.d("mydebug","bai hat dang duoc tai ve");
                       Log.d("mydebug","Constant hien tai la "+Constant.music_id);
                       Toast.makeText(getApplicationContext(),"Bai hat dang tai ve",Toast.LENGTH_SHORT).show();
                   }
               }


            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         if(item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("teamthat.com.onemusic.activity.BoundService".equals(service.service.getClassName())) {
                Log.d("process1","ten service "+service.service.getClassName());
                return true;
            }
        }
        return false;
    }
    public void startForeground() {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        Notification.Builder builder = new Notification.Builder(getApplicationContext());

        builder.setAutoCancel(false);
        builder.setTicker("Download "+Constant.name);
        builder.setContentTitle(Constant.name);
        builder.setContentText("Download");


        builder.setSmallIcon(R.drawable.icon);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                builder.setSubText("This is subtext...");   //API level 16
//            }
        builder.setNumber(100);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.build();
        }

        myNotication = builder.getNotification();
          manager.notify(Integer.parseInt(Constant.music_id), myNotication);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        setImage.cancel(true);
    }
    public void setMaxTime(long duration){
        Log.d("process1","max time "+setTimeFormat(duration));
       seekBar.setMax((int)duration);
       tvMaxTime.setText(setTimeFormat(duration));
    }
    public String setTimeFormat(long time) {
        final int HOUR = 60 * 60 * 1000;
        final int MINUTE = 60 * 1000;
        final int SECOND = 1000;
        final long hour = time / HOUR;
        final long _hour = time % HOUR;
        final long minute = _hour / MINUTE;
        final long _minute = _hour % MINUTE;
        final long second = _minute / SECOND;
        String result ="";
        String mi = "";
        String se = "";
        String ho = "";
        if (hour == 0) {
            if (minute < 10) {
                mi = "0" + minute;

            } else {
                mi = minute + "";


            }
            if (second < 10) {
                se = "0" + second;
            } else {
                se = second + "";
            }
            result =(mi + ":" + se);

        } else {
            if (minute < 10) {
                mi = "0" + minute;


            } else {

                mi = minute + "";


            }
            if (second < 10) {
                se = "0" + second;
            } else {
                se = second + "";
            }
            if (hour < 10) {
                ho = "0" + hour;
            } else {
                ho = hour + "";
            }
            result=(ho + ":" + mi + ":" + se);
        }
        return result;
    }
    public void SetProgress(long time){
        Log.d("process1","progress time "+setTimeFormat(time));
        seekBar.setProgress((int)time);
        tvMinTime.setText(setTimeFormat(time));
    }
    public int getIndexOfMusic(int k,int i){

        if(index == max&k==1){
            index =0;
        }else if(index ==0&k==0){
            index = max;
        }else{
            index +=i;
        }
        return index;

    }
    public class downLoad extends AsyncTask<Void,Void,String[]>{
      String id;
        String songname = Constant.name;
        String artistid = Constant.artist_id;

        String pathlocal ="sdcard/"+Constant.name+".mp3";
        String songid = Constant.music_id;
        public static final String GET_ARTIST_API = "http://nghiahoang.net/api/appmusic/?function=getartistbyid";
        public String makeGetSongOfArtist(String id) {
            StringBuilder builder = new StringBuilder(GET_ARTIST_API);
            builder.append("&artistid=").append(id);

            return builder.toString();
        }

        public void parseJsonResponse(String json){
            try {
                JSONObject jsonObject = new JSONObject(json);
               id = jsonObject.getString("Id");
                Log.d("mydebug","id lay xuong la "+id);
                String name = jsonObject.getString("Name");
                String image = jsonObject.getString("Image");
                String des = jsonObject.getString("Des");
                String love = jsonObject.getString("Love");
                String listen = jsonObject.getString("Listen");
                Artist artist = new Artist(id,name,image,love,des);
                databaseHelper.createArtist(artist);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                Log.d("mydebug","chuan bi vao");
            startForeground();


        }

        @Override
        protected void onPostExecute(String[] a) {
            super.onPostExecute(a);
            manager.cancel(Integer.parseInt(songid));
            ArtistMusic artistMusic = new ArtistMusic(a[0],a[1]);
            if(id!=null){
                Log.d("mydebug","bat dau luu id la 1 "+id);
                databaseHelper.createSong(artistMusic,Integer.parseInt(id),Integer.parseInt(songid));
            }else{
                Log.d("mydebug","bat dau luu id la 2 "+Constant.artist_id);
                databaseHelper.createSong(artistMusic,Integer.parseInt(Constant.artist_id),Integer.parseInt(songid));
            }
            Constant.listpath_music_downloading.remove(songid);

            Toast.makeText(getApplicationContext(),"tai da xong "+a[0],Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String[] doInBackground(Void... params) {
            Log.d("mydebug","vao doInBAckground");


            if(!databaseHelper.checkExistArtist(artistid)){
                String url = makeGetSongOfArtist(artistid);
                Log.d("mydebug",url);
                String json = Request(url);
                Log.d("mydebug",json);
                parseJsonResponse(json);
            }else{
                Log.d("mydebug","nghe si da ton tai");
            }
                 download();
            String[]a = {songname,pathlocal};
            return a;

        }
        public void download(){

            InputStream inputStream=null;
            OutputStream outputStream = null;
            HttpURLConnection httpURLConnection = null;
            Log.d("mydebug","dang tai");
            try {
                URL url = new URL(Constant.path);
                inputStream =  new BufferedInputStream(url.openStream(), 8192);;
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                if(httpURLConnection.getResponseCode()!= HttpURLConnection.HTTP_OK){

                }
                int fileLength = httpURLConnection.getContentLength();
                //file = new File("sdcard/"+Constant.name+".mp3");

                outputStream = new FileOutputStream(pathlocal);
                byte data[]= new byte[4069];
                long total = 0;
                int count;
                while((count = inputStream.read(data))!=-1){
                    if(isCancelled()){
                        inputStream.close();
                       return;
                    }
                    total += count;
                    Log.d("mydebug","tai den "+total);
                    if(fileLength>0){

                        outputStream.write(data,0,count);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try{
                    if(outputStream!=null){
                        outputStream.close();
                    }
                    if(inputStream!=null){
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(httpURLConnection!=null){
                    httpURLConnection.disconnect();
                }
            }

        }
        public String Request(String Url){
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                URL url = new URL(Url);

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
    public class setImage extends AsyncTask<URL,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(URL... params) {
            try {
                Bitmap image = BitmapFactory.decodeStream(params[0].openConnection().getInputStream());
                return image;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
            roundImage= new RoundImage(bitmap);
            image.setImageDrawable(roundImage);
        }
    }

}
