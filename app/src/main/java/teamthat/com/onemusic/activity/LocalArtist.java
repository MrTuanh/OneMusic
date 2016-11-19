package teamthat.com.onemusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import teamthat.com.onemusic.DatabaseHelper.DatabaseHelper;
import teamthat.com.onemusic.R;
import teamthat.com.onemusic.model.Artist;

public class LocalArtist extends AppCompatActivity {
    ListView listArtist;
    ArrayList<Artist> listartist;
    ArrayAdapter adapter;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_artist);
        databaseHelper = new DatabaseHelper(this);
        listArtist = (ListView) findViewById(R.id.listview);
        listartist = new ArrayList<Artist>();
        getDataforListView();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listartist);
        listArtist.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listArtist.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = listartist.get(position);

                Intent intent = new Intent(LocalArtist.this,LocalMusicActivity.class);
                Constant.artist=artist;
                intent.putExtra("id",artist.getId());
                Log.d("mydebug","id la "+artist.getId());
                startActivityForResult(intent,10);
            }
        });
    }
    public void getDataforListView(){
        listartist = databaseHelper.getAllArtists();

    }
}
