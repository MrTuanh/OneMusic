package teamthat.com.onemusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import teamthat.com.onemusic.R;
import teamthat.com.onemusic.model.Song;

/**
 * Created by thietit on 11/24/2016.
 */

public class SongAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    //song list and layout
    private ArrayList<Song> songs;

    //constructor
    public SongAdapter(Context context,int layout, ArrayList<Song> theSongs){
        songs=theSongs;
        myContext = context;
        myLayout = layout;
        songs = theSongs;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(myLayout, null);


        //get title and artist views
        ImageView ivSong = (ImageView) convertView.findViewById(R.id.iv_song);
        TextView songView = (TextView)convertView.findViewById(R.id.song_title);
        TextView artistView = (TextView)convertView.findViewById(R.id.song_artist);
        //get song using position
        Song currSong = songs.get(position);
        //get title and artist strings
        ivSong.setImageResource(R.drawable.ic_default);
        songView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());
        //set position as tag
        convertView.setTag(position);
        return convertView;
    }

}
