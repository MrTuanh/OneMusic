package teamthat.com.onemusic.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

import teamthat.com.onemusic.R;

@SuppressWarnings("ALL")
public class AddplaylistActivity extends AppCompatActivity {

    private static final String REGISTER_URL = "http://nghiahoang.net/api/appmusic/upload.php";
    private InputStream is;
    private JSONObject jObj;
    private String json;
    private ProgressDialog pDialog;
    private EditText user, pass ,userid;
    private Button Login, Register;
    String path;
    private ImageView image;
    private String[] states;
    private Spinner spinner;
    private TypedArray imgs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplaylist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("PlayList");
        Intent i = getIntent();
        path = i.getStringExtra("id");
        userid = (EditText) findViewById(R.id.user);
        user = (EditText) findViewById(R.id.userid);

        user.setText(Constant.sharedPreferences.getString("Name",""));
        pass = (EditText) findViewById(R.id.pass);
        pass.setText(String.valueOf(path));
        Login = (Button) findViewById(R.id.Login);
        Login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddplaylistActivity.this,
                        LoginActivity.class);
                startActivity(intent);

            }
        });
        Register = (Button) findViewById(R.id.register);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();

            }
        });


        states = getResources().getStringArray(R.array.countries_list);
        imgs = getResources().obtainTypedArray(R.array.countries_flag_list);

        image = (ImageView) findViewById(R.id.country_image);
        spinner = (Spinner) findViewById(R.id.country_spinner);

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, states);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                image.setImageResource(imgs.getResourceId(
                        spinner.getSelectedItemPosition(), +1));
                userid.setText(String.valueOf(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void registerUser() {
        String PlaylistId =    String.valueOf( userid.getText().toString());
        String SongId = String.valueOf(pass.getText().toString());

        String UserId = user.getText().toString();


        register(PlaylistId, SongId, UserId);
    }

    private void register(String PlaylistId, String SongId, String UserId) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddplaylistActivity.this, null,"Loading...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("PlaylistId", params[0]);
                data.put("SongId", params[1]);
                data.put("UserId", params[2]);

                String result = ruc.sendPostRequest(REGISTER_URL, data);

                return result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(PlaylistId, SongId, UserId);
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
