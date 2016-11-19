package teamthat.com.onemusic.activity;

import android.content.SharedPreferences;

import java.util.ArrayList;

import teamthat.com.onemusic.model.Artist;
import teamthat.com.onemusic.model.User;

/**
 * Created by thietit on 11/3/2016.
 */

public class Constant {
    static long a;
    static String path;
    static String music_id;
    static String name;
    static String artist_id;
    static String artist_image;
    static Artist artist;
    static String path_music_downloading;
    static ArrayList<String> listpath_music_downloading = new ArrayList<String>();
    static  boolean internetConnect = false;
    static User user;
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
}
