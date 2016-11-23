package teamthat.com.onemusic.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import teamthat.com.onemusic.R;

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
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,Constant.listfavoriteSong);
        listfavorite = (ListView) findViewById(R.id.listfavorite);
        listfavorite.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Constant.type=2;
        listfavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Constant.name = Constant.listfavoriteSong.get(position).getNameMusic();
                Constant.path =LoginActivity.LOGIN_API+Constant.listfavoriteSong.get(position).getMusicPath();
                Log.d("favorite","path "+Constant.path);
                Intent intent = new Intent(FavouriteMusicActivity.this,PlayerActivity.class);
                intent.putExtra("name",true);
                Constant.index = position;
                startActivity(intent);
            }
        });


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
