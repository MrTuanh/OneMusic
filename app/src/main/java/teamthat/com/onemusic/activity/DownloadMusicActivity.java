package teamthat.com.onemusic.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import teamthat.com.onemusic.DatabaseHelper.DatabaseHelper;
import teamthat.com.onemusic.R;

public class DownloadMusicActivity extends AppCompatActivity {
    ListView listView;

    ArrayAdapter adapter;
    DatabaseHelper databaseHelper;
    int index,max;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_music);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.listview);
        databaseHelper = new DatabaseHelper(this);
        getSupportActionBar().setTitle("Download");
        getlistAritst(id);
        Constant.type=0;
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,Constant.listsongLocal);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Constant.path = Constant.listsongLocal.get(position).getMusicPath();

               // index =i;
                Constant.name = Constant.listsongLocal.get(position).getNameMusic();

                Constant.index = position;
                Log.d("local","index "+index);
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
    Constant.listsongLocal.removeAll(Constant.listsongLocal);
    Constant.listsongLocal = databaseHelper.getAllSongOfArtist(id);
    max = Constant.listsongLocal.size()-1;
    Log.d("local","max "+max);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
