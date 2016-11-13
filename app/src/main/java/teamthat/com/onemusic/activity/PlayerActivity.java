package teamthat.com.onemusic.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.squareup.picasso.Picasso;

import java.io.IOException;

import teamthat.com.onemusic.R;
import teamthat.com.onemusic.model.ArtistMusic;

/**
 * Created by thietit on 11/2/2016.
 */

public class PlayerActivity extends AppCompatActivity {
               int i,j=0;
    ImageButton ibPlay, ibPrevious, ibNext, ibFavourite, ibDowload;
  static  SeekBar seekBar;
   static TextView tvMinTime,tvMaxTime;
    int k =0;
    static int index;
    int max;
    ArtistMusic song;
    final int HOUR = 60*60*1000;
    final int MINUTE = 60*1000;
    final int SECOND = 1000;

    boolean isPlayed = false;
    String path;
    Intent intent;
    BoundService boundService;
    Bundle bundle;
    ImageView image;
      public static final String ACTION_PLAY = "com.example.action.PLAY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        addCtrols();
        Picasso.with(this) //Context
                .load(LoginActivity.LOGIN_API+ArtistFragment.artist.getImage()) //URL/FILE
                .into(image);//an ImageView Object to show the loaded image;

        clickImgButton();
        index = ArtistMusicActivity.index;
        max = ArtistMusicActivity.max-1;
        boundService = new BoundService();
        intent = new Intent(PlayerActivity.this,BoundService.class);
        intent.setAction(ACTION_PLAY);
        ibPlay.setImageResource(R.drawable.ic_pause);

        getSupportActionBar().setTitle(Constant.name);
        // nếu chưa có service chạy, thì khởi tạo service
        if(!isServiceRunning()){
             startService(intent);
        }else{
            stopService(intent);
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


            }
        });

        ibDowload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    public int MyStart() throws IOException {
        Log.d("process1","Main Activity MyStart");
        try{

        }catch (IllegalStateException e){

        }
        return 0;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
    public class openService extends AsyncTask<Void,Integer,Integer>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                Log.d("process1","Main Activity Pre "+MyStart());
                setMaxTime(MyStart());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Integer doInBackground(Void... params) {
            Log.d("process1","Main Activity vao doInBackgroud");
            boolean t = true;

                 try{
                while(t){

                    Thread.sleep(1000);
                }

            }catch (IllegalStateException e){

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            SetProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    public class startService extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            startService(intent);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new openService().execute();
        }
    }

    public class stopMusic extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params){

            stopService(intent);
            startService(intent);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new openService().execute();
        }
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

}
