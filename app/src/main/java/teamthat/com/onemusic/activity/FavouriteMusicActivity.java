package teamthat.com.onemusic.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import teamthat.com.onemusic.R;
import teamthat.com.onemusic.Util.Util;

public class FavouriteMusicActivity extends AppCompatActivity {
ListView listfavorite;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("favorite","come here");
        setContentView(R.layout.activity_favourite_music);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Constant.sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        Util util = new Util();
        util.loadFavorite();
        listfavorite = (ListView) findViewById(R.id.listfavorite);
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,Constant.listfavoriteSong);
        listfavorite.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
