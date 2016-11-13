package teamthat.com.onemusic.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import teamthat.com.onemusic.R;

public class BoundService extends Service implements MediaPlayer.OnPreparedListener {
static MediaPlayer mediaPlayer;
    NotificationManager manager;
    Notification myNotication;
    PlayerActivity playerActivity;
    public static final String ACTION_PLAY = "com.example.action.PLAY";
    public start mstart;
    public updateProgress mudapteProgress;


    @Override
    public void onCreate() {
        super.onCreate();
        playerActivity = new PlayerActivity();
        mstart = new start();
        mudapteProgress = new updateProgress();

 }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null){
            if(intent.getAction().equals(ACTION_PLAY)){
                mstart.execute();
            }

        }
 return START_STICKY;
    }
    public void start(){
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, Uri.parse(Constant.path));
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();


            startForeground();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        Log.d("mydebug","destroy service");
        if(mediaPlayer!=null){
            mediaPlayer.release();
        }

        mediaPlayer = null;
        if(manager!=null)
        manager.cancel(11);
        mstart.cancel(true);
        mudapteProgress.cancel(true);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        playerActivity.setMaxTime(mp.getDuration());
        playerActivity.seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
             if(event.getAction()==MotionEvent.ACTION_MOVE){
                 playerActivity.seekBar.setProgress(playerActivity.seekBar.getProgress());
                 mediaPlayer.seekTo(playerActivity.seekBar.getProgress());

                 return false;
             }
                Log.d("mydebug", "Touched , Progress :" + playerActivity.seekBar.getProgress());
              return true;
            }
        });
        mudapteProgress.execute(mp);

        boolean t = true;
//        while(t){
//            PlayerActivity.seekBar.setProgress(mediaPlayer.getCurrentPosition());
//            PlayerActivity.tvMinTime.setText(setTimeFormat(mediaPlayer.getCurrentPosition()));
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }


    }
    public void startForeground(){
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this,PlayerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);

        Notification.Builder builder = new Notification.Builder(getApplicationContext());

        builder.setAutoCancel(false);
        builder.setTicker(Constant.name);
        builder.setContentTitle(Constant.name);
        builder.setContentText(ArtistFragment.artist.getName());



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
        manager.notify(11, myNotication);
 }

    public class start extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            start();

            return null;
        }
    }
    public class updateProgress extends AsyncTask<MediaPlayer,Integer,Void>{

        @Override
        protected Void doInBackground(MediaPlayer... mp) {
            boolean t = true;
            while(t){
               // playerActivity.SetProgress(mp[0].getCurrentPosition());
                publishProgress(mp[0].getCurrentPosition());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            playerActivity.SetProgress(values[0]);

        }
    }


}
