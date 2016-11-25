package teamthat.com.onemusic.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import teamthat.com.onemusic.R;

import static teamthat.com.onemusic.R.drawable.ic_angel;

public class AngelActivity extends AppCompatActivity {

    JSONObject jsonobject;
    JSONArray jsonarray;
    ListView listview;
    Button btn1;
    EditText edit;
    ListViewAdapter adapter;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    static String ID = "id";
    static String COUNTRY = "UserId";
    static String DUONGDAN = "SongName";
    static String MUSICFILE = "path";
    static String PlayID = "PlaylistId";
    String dl,thumb;
    ImageView img;
    String vui;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        new AngelActivity.DownloadJSON().execute();
        Intent i = getIntent();
        vui = i.getStringExtra("vui");
        if(Constant.sharedPreferences.getString("Id","").equals("")){
            Toast.makeText(AngelActivity.this,"Vui lòng đăng nhập",Toast.LENGTH_LONG).show();
            startActivity(new Intent(AngelActivity.this, LoginActivity.class));
        }
        img = (ImageView)findViewById(R.id.img1);
        img.setImageResource(ic_angel);
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(AngelActivity.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
            Toast.makeText(getApplicationContext(), Constant.sharedPreferences.getString("Name",""), Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            arraylist = new ArrayList<HashMap<String, String>>();
            jsonobject = JSONfunctions
                    .getJSONfromURL("http://nghiahoang.net/api/appmusic/songplaylist.php");

            try {
                jsonarray = jsonobject.getJSONArray("song");

                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);
                    map.put("UserId", jsonobject.getString("UserId"));
                    map.put("SongName", jsonobject.getString("SongName"));
                    map.put("path", jsonobject.getString("MusicFile"));
                    map.put("PlaylistId", jsonobject.getString("PlaylistId"));
                    arraylist.add(map);
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            listview = (ListView) findViewById(R.id.listView);
            adapter = new ListViewAdapter(AngelActivity.this, arraylist);

            listview.setAdapter(adapter);


            ArrayList<HashMap<String, String>> arrayTemplist= new ArrayList<HashMap<String,String>>();
            String playlist = String.valueOf(2);

            for (int i = 0; i < arraylist.size(); i++)
            {
                String currentString =arraylist.get(i).get(AngelActivity.COUNTRY);
                if (Constant.sharedPreferences.getString("Name","").equalsIgnoreCase(currentString))
                {
                    String currentString2 =arraylist.get(i).get(AngelActivity.PlayID);
                    if (playlist.equalsIgnoreCase(currentString2))
                    {

                        arrayTemplist.add(arraylist.get(i));
                    }


                }
            }
            adapter = new ListViewAdapter(AngelActivity.this, arrayTemplist);
            listview.setAdapter(adapter);

            mProgressDialog.dismiss();

        }



    }

}