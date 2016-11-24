package teamthat.com.onemusic.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import teamthat.com.onemusic.DatabaseHelper.DatabaseHelper;
import teamthat.com.onemusic.R;
import teamthat.com.onemusic.Util.Blur;
import teamthat.com.onemusic.Util.Util;
import teamthat.com.onemusic.model.User;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;

    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView tvSinup;
    ImageView imageview;
    ProgressDialog progressDialog;
    DatabaseHelper databaseHelper;
    BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter;
    public static final String LOGIN_API = "http://nghiahoang.net/api/appmusic/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControl();
        databaseHelper = new DatabaseHelper(this);
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    login();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        tvSinup.setPaintFlags(tvSinup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvSinup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
              //  overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    try {
                        login();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });
        intentFilter = new IntentFilter();
        intentFilter.addAction("loginsucess");
        intentFilter.addAction("loginfailure");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()){
                    case "loginsucess":
                        Toast.makeText(getApplicationContext(),"Đăng nhập thành công ",Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                        Log.d("mydebug","json image "+SplashActivity.user.getImage());
                        startActivityForResult(intent1,1);
                        break;
                    case "loginfailure":
                        onLoginFailed();
                        break;
                    default:
                        onLoginFailed();
                        break;
                }

            }
        };
        registerReceiver(broadcastReceiver,intentFilter);

        // Blur Image
        Blur blur = new Blur(this);
        BitmapDrawable drawable = (BitmapDrawable) imageview.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Bitmap blurred = blur.blurRenderScript(bitmap, 23 );//second parametre is radius
        imageview.setImageBitmap(blurred);
    }

    private void addControl() {
        edtEmail = (EditText) findViewById(R.id.input_email);
        edtPassword = (EditText) findViewById(R.id.input_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvSinup = (TextView) findViewById(R.id.link_signup);
        imageview = (ImageView) findViewById(R.id.img);
    }

    public void login() throws JSONException {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        btnLogin.setEnabled(false);
        onLoginSuccess();
}

    public void onLoginSuccess() throws JSONException {
        Util util = new Util(LoginActivity.this);
       util.Login(edtEmail.getText().toString(),edtPassword.getText().toString());
        btnLogin.setEnabled(true);
        //finish();
    }

    public void onLoginFailed() {

        btnLogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if (email.isEmpty() ) {
            edtEmail.setError("Nhập địa chỉ email hợp lệ");
            valid = false;
        } else {
            edtEmail.setError(null);
        }

        if (password.isEmpty() || password.length() <1) {
            edtPassword.setError("Mật khẩu ít nhất có 4 kí tự");
            valid = false;
        } else {
            edtPassword.setError(null);
        }

        return valid;
    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        super.onBackPressed();}

    // Asyntask to connecto to Server
    public class connectToServer extends AsyncTask<Void,Void,String>{

        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        // This method to parse json
        public void parseJsonResponse(String json){
            Log.d("mydebug","da vao parse json");
            try{
                JSONObject response = new JSONObject(json);
                String error = response.optString("error");
               // String message = response.optString("message");


                switch(error){
                    case "true":
                        Log.d("mydebug","dang nhap that bai");
                        String message = response.optString("message");
                        onLoginFailed();

                        break;
                    case "false":
                        Log.d("mydebug","dang nhap thanh cong ");
                        JSONObject userInfor = response.optJSONObject("message");
                        String UserId = userInfor.optString("Id");
                        String Name = userInfor.optString("Name");
                        String UserName = userInfor.optString("UserName");
                        String Password = userInfor.optString("Password");
                        String Image = userInfor.optString("image");
                        String Birthday = userInfor.optString("birthday");
                        String Address = userInfor.optString("address");
                        String Vip = userInfor.optString("vip");
                        String Email = userInfor.optString("email");
                        String Phone = userInfor.optString("phone");
                        String Gender = userInfor.optString("gender");
                        String Level = userInfor.optString("level");
                        Log.d("mydebug","dn than cong "+Name);
                        try {
                            Constant.user = new User(UserId, Name, UserName, Password, Birthday, Address, Gender, Phone, Level, Email, Vip, Image);
                            // databaseHelper.addUser(Constant.user);
                            Constant.editor.putString("Id", UserId);
                            Constant.editor.putString("Username", UserName);
                            Constant.editor.putString("Image", Image);
                            Constant.editor.putString("Email", Email);
                            Constant.editor.putString("Name", Name);
                            Constant.editor.putString("Password", Password);
                            Constant.editor.putString("Phone", Phone);
                            Constant.editor.commit();
                        }catch (NullPointerException e){
                            Constant.sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
                            Constant.editor.putString("Id", UserId);
                            Constant.editor.putString("Username", UserName);
                            Constant.editor.putString("Image", Image);
                            Constant.editor.putString("Email", Email);
                            Constant.editor.putString("Name", Name);
                            Constant.editor.putString("Password", Password);
                            Constant.editor.putString("Phone", Phone);
                            Constant.editor.commit();
                        }

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        Log.d("mydebug","json image "+SplashActivity.user.getImage());
                        startActivityForResult(intent,1);


                        break;
                    default:
                        break;
                }
                Log.d("mydebug",error);



            }catch (JSONException e){
                e.printStackTrace();

            }
        }
      // This method to get json from server
        public String makeLoginUrl(String email,String password) {
            String data = null;
            Log.d("mydebug","make login url");
            try {
                data = URLEncoder.encode("dnjson", "UTF-8")
                        + "=" + URLEncoder.encode("", "UTF-8");
                data += "&" + URLEncoder.encode("username", "UTF-8") + "="
                        + URLEncoder.encode(email, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                        + URLEncoder.encode(password, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.d("mydebug","get duoc url "+data);


            String text = "";
            BufferedReader reader=null;
            URL url = null;
            try {
                url = new URL(LOGIN_API);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + "\n");
                }
                text = sb.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally
            {
                try
                {

                    reader.close();
                }

                catch(Exception ex) {}

            }

            return text;

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            Log.d("mydebug","proexcute");
        }

        @Override
        protected void onPostExecute(String aVoid) {

            super.onPostExecute(aVoid);
            parseJsonResponse(aVoid);
            progressDialog.dismiss();
            Log.d("mydebug",aVoid);
        }

        @Override
        protected String doInBackground(Void... params) {
           String json = makeLoginUrl(email,password);
            return json;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode==1){
           finish();
       }
    }
}
