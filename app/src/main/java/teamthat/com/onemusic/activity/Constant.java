package teamthat.com.onemusic.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    public static String CHANGENAME_API = "http://nghiahoang.net/api/appmusic/?function=updatename";
    public static String CHANGEUSERNAME_API = "http://nghiahoang.net/api/appmusic/?function=updateusername";
    public static String CHANGEEMAIL_API = "http://nghiahoang.net/api/appmusic/?function=updateemail";
    public static String CHANGEPASSWORD_API = "http://nghiahoang.net/api/appmusic/?function=updatepassword";
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
    public static EditText edName,edEmail,edUsername,edPassword;
    public static TextView txtName,txtEmail;
    public static ImageView imgAvatar,imgAvartar1;
    public static ArrayList<ArtistMusic> listsongLocal= new ArrayList<>();
    public static ArtistMusic artistMusic = new ArtistMusic();
    public static int index;
    public static boolean Ramdom;
    public static int type;
    public static int isHome;
}
