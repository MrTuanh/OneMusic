package teamthat.com.onemusic.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import teamthat.com.onemusic.R;

public class ListPlaylistActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView vui, buon,thich;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_music);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vui = (ImageView)  findViewById(R.id.vui);
        vui.setOnClickListener((View.OnClickListener) this);
        buon = (ImageView) findViewById(R.id.buon);
        buon.setOnClickListener((View.OnClickListener) this);
        thich = (ImageView) findViewById(R.id.thich);
        thich.setOnClickListener((View.OnClickListener) this);

    }


    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.vui:
                if (Constant.sharedPreferences.getString("Id","") == "")
                {
                    Toast.makeText(ListPlaylistActivity.this, "Vui lòng đăng nhập", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ListPlaylistActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else
                {

                    Intent intent = new Intent(ListPlaylistActivity.this, HappyActivity.class);
                    intent.putExtra("vui",(String.valueOf(1)));
                    startActivity(intent);
                }
                break;
            case R.id.buon:
                if (Constant.sharedPreferences.getString("Id","") == "")
                {

                    Toast.makeText(ListPlaylistActivity.this, "Vui lòng đăng nhập", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ListPlaylistActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else
                {
                    Intent intent = new Intent(ListPlaylistActivity.this, SadActivity.class);
                    intent.putExtra("vui",(String.valueOf(2)));
                    startActivity(intent);
                }
                break;
            case R.id.thich:
                if (Constant.sharedPreferences.getString("Id","") == "")
                {
                    Toast.makeText(ListPlaylistActivity.this, "Vui lòng đăng nhập", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ListPlaylistActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else
                {
                    Intent intent = new Intent(ListPlaylistActivity.this, AngelActivity.class);
                    intent.putExtra("vui",(String.valueOf(3)));
                    startActivity(intent);
                }
                break;

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
        super.onBackPressed();
    }
}
