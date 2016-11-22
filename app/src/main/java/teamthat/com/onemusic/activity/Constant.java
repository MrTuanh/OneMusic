package teamthat.com.onemusic.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.util.ArrayList;

import teamthat.com.onemusic.model.Artist;
import teamthat.com.onemusic.model.ArtistMusic;
import teamthat.com.onemusic.model.User;

/**
 * Created by thietit on 11/3/2016.
 */

public class Constant {
    public static String GETALLFAVORITE_API = "http://nghiahoang.net/api/appmusic/?function=getallfavoritesong";
    public static String CHECKFAVORITED_API = "http://nghiahoang.net/api/appmusic/?function=checkfavorite";
    public static String CHANGEFAVORITE_API = "http://nghiahoang.net/api/appmusic/?function=favoritesong";
    public static ArrayList<ArtistMusic> listfavoriteSong= new ArrayList<>();

    public  static long a;
    public  static String path;
    public  static String music_id;
    public  static String name;
    public  static String artist_id;
    public static String artist_image;
    public  static Artist artist;
    public  static String path_music_downloading;
    public static ArrayList<String> listpath_music_downloading = new ArrayList<String>();
    public static  boolean internetConnect = false;
   public static User user;
    public static Bitmap artistImage;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
}
