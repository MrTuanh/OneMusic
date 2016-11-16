package teamthat.com.onemusic.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import teamthat.com.onemusic.model.Artist;
import teamthat.com.onemusic.model.ArtistMusic;

/**
 * Created by ASUS on 11/15/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {



    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "music";

    // Table Names
    private static final String TABLE_SONG = "songs";
    private static final String TABLE_ARTIST = "artists";


    // Common column names
    private static final String KEY_ID = "id";


    // NOTES Table - column nmaes
    private static final String KEY_SONG_ID = "song_id";
    private static final String KEY_SONG_NAME = "song_name";
    private static final String KEY_SONG_PATH = "song_path";
    private static final String KEY_ARTIST_ID = "artist_id";

    // TAGS Table - column names
    private static final String KEY_ARTIST_NAME = "artist_name";
    private static final String KEY_ARTIST_IMAGE = "artist_image";
    private static final String KEY_ARTIST_DES = "artist_des";



    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_SONG = "CREATE TABLE "
            + TABLE_SONG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"+ KEY_SONG_ID + " INTEGER,"  + KEY_SONG_NAME
            + " TEXT," + KEY_SONG_PATH + " TEXT," + KEY_ARTIST_ID
            + " INTEGER" + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_ARTIST = "CREATE TABLE " + TABLE_ARTIST
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ARTIST_NAME + " TEXT,"+ KEY_ARTIST_ID + " TEXT,"
            + KEY_ARTIST_IMAGE + " TEXT," + KEY_ARTIST_DES + " TEXT" + ")";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ARTIST);
        db.execSQL(CREATE_TABLE_SONG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        onCreate(db);
    }
    public long createArtist(Artist artist) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ARTIST_ID, artist.getId());
        values.put(KEY_ARTIST_NAME, artist.getName());
        values.put(KEY_ARTIST_IMAGE, artist.getImage());
        values.put(KEY_ARTIST_DES, artist.getDes());

        // insert row
        long artist_id = db.insert(TABLE_ARTIST, null, values);

        return artist_id;
    }


    public ArrayList<Artist> getAllArtists() {
        ArrayList<Artist> artists = new ArrayList<Artist>();
        String selectQuery = "SELECT  * FROM " + TABLE_ARTIST;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Artist artist = new Artist();
                artist.setId(c.getInt((c.getColumnIndex(KEY_ARTIST_ID)))+"");
                artist.setName(c.getString(c.getColumnIndex(KEY_ARTIST_NAME)));
                artist.setImage(c.getString(c.getColumnIndex(KEY_ARTIST_IMAGE)));
                artist.setDes(c.getString(c.getColumnIndex(KEY_ARTIST_DES)));


                // adding to tags list
                artists.add(artist);
            } while (c.moveToNext());
        }
        return artists;
    }
    public boolean checkExistArtist(String artistid){
        String selectQuery = "SELECT  * FROM " + TABLE_ARTIST;
        Log.d("mydebug","id trong check la "+artistid);
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                if(c.getString(c.getColumnIndex(KEY_ARTIST_ID)).equals(artistid)){
                    return true;
                }
            } while (c.moveToNext());
            return false;
        }else{
            return false;
        }
    }
    public boolean checkExistSong(String songid){
        String selectQuery = "SELECT  * FROM " + TABLE_SONG;
        Log.d("mydebug","id trong check la "+songid);
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                if(c.getString(c.getColumnIndex(KEY_SONG_ID)).equals(songid)){
                    Log.d("mydebug","id trong csdl la "+c.getColumnIndex(KEY_SONG_ID));
                    return true;
                }
            } while (c.moveToNext());
            return false;
        }else{
            Log.d("mydebug","khong co phan tu nao trong bang");
            return false;
        }
    }
    public long createSong(ArtistMusic song,int artistid,int SongId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SONG_NAME, song.getNameMusic());
        values.put(KEY_SONG_PATH, song.getMusicPath());
        values.put(KEY_ARTIST_ID, artistid);
        values.put(KEY_SONG_ID, SongId);
        Log.d("mydebug","id luu la "+artistid);
        Log.d("mydebug","id song la "+SongId);
        // insert row
        long song_id = db.insert(TABLE_SONG, null, values);


        return song_id;
    }
    public ArrayList<ArtistMusic> getAllSongOfArtist(String artistid) {
        ArrayList<ArtistMusic> songs = new ArrayList<ArtistMusic>();
        String selectQuery = "SELECT  * FROM " + TABLE_SONG+" WHERE "+KEY_ARTIST_ID+" = "+artistid;
        Log.d("mydebug",selectQuery);
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ArtistMusic song = new ArtistMusic();
                song.setMusicPath(c.getString(c.getColumnIndex(KEY_SONG_PATH)));
                song.setNameMusic(c.getString(c.getColumnIndex(KEY_SONG_NAME)));
                Log.d("mydebug",c.getString(c.getColumnIndex(KEY_SONG_PATH))+"  "+c.getString(c.getColumnIndex(KEY_SONG_NAME)));


                // adding to tags list
                songs.add(song);
            } while (c.moveToNext());
        }
        return songs;
    }
    public ArrayList<ArtistMusic> getAllSongs() {
        ArrayList<ArtistMusic> songs = new ArrayList<ArtistMusic>();
        String selectQuery = "SELECT  * FROM " + TABLE_SONG;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ArtistMusic song = new ArtistMusic();
                song.setMusicPath(c.getString(c.getColumnIndex(KEY_SONG_PATH)));
                song.setNameMusic(c.getString(c.getColumnIndex(KEY_SONG_NAME)));
                Log.d("mydebug",c.getString(c.getColumnIndex(KEY_SONG_PATH))+"  "+c.getString(c.getColumnIndex(KEY_SONG_NAME))+c.getString(c.getColumnIndex(KEY_ARTIST_ID)));


                // adding to tags list
                songs.add(song);
            } while (c.moveToNext());
        }
        return songs;
    }
}
