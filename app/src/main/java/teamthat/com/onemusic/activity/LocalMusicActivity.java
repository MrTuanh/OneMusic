package teamthat.com.onemusic.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import teamthat.com.onemusic.DatabaseHelper.DatabaseHelper;
import teamthat.com.onemusic.R;
import teamthat.com.onemusic.model.ArtistMusic;

public class LocalMusicActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<ArtistMusic> listSongs;
    ArrayAdapter adapter;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        listView = (ListView) findViewById(R.id.listview);
        databaseHelper = new DatabaseHelper(this);
        listSongs = new ArrayList<>();
        getlistAritst(id);
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listSongs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Constant.path = listSongs.get(position).getMusicPath();

               // index =i;
                Constant.name = listSongs.get(position).getNameMusic();
              //  artistMusic = listMusic.get(i);
              //  Constant.artist_image = ArtistFragment.artist.getImage();
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);

                intent.putExtra("name",true);
                intent.putExtra("online",false);

                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

public void getlistAritst(String id){
    listSongs = databaseHelper.getAllSongOfArtist(id);

}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
