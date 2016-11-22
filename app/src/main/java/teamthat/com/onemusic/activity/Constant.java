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
    public static Bitmap artistImage;
    public static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
}
