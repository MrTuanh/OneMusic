package teamthat.com.onemusic.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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

import teamthat.com.onemusic.R;
import teamthat.com.onemusic.model.User;

import static teamthat.com.onemusic.activity.LoginActivity.LOGIN_API;

public class SignupActivity extends AppCompatActivity {

    EditText edtName, edtUsername, edtEmail, edtPassword, edtReEnterPassword;
    Button btnSignup;
    TextView tvLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        addControl();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        tvLogin.setPaintFlags(tvLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
              //  overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        edtReEnterPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    signUp();
                    return true;
                }
                return false;
            }
        });
    }

    private void addControl() {
        edtName = (EditText) findViewById(R.id.input_name);
        edtUsername = (EditText) findViewById(R.id.input_username);
        edtEmail = (EditText) findViewById(R.id.input_email);
        edtPassword = (EditText) findViewById(R.id.input_password);
        edtReEnterPassword = (EditText) findViewById(R.id.input_reEnterPassword);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        tvLogin = (TextView) findViewById(R.id.link_login);
    }

    public void signUp() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        btnSignup.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onSignupSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onSignupSuccess() {
        btnSignup.setEnabled(true);
        User user = new User();
        user.setName(edtName.getText().toString());
        user.setUsername(edtUsername.getText().toString());
        user.setEmail(edtEmail.getText().toString());
        user.setPassword(edtPassword.getText().toString());

        new SignUp().execute(user);


        setResult(RESULT_OK, null);

        //finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Đăng kí thất bại !", Toast.LENGTH_LONG).show();
        btnSignup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = edtName.getText().toString();
        String address = edtUsername.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String reEnterPassword = edtReEnterPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            edtName.setError("Tên ít nhất có 3 kí tự");
            valid = false;
        } else {
            edtName.setError(null);
        }

        if (address.isEmpty()) {
            edtUsername.setError("Nhập tên đăng nhập");
            valid = false;
        } else {
            edtUsername.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Nhập địa chỉ email hợp lệ");
            valid = false;
        } else {
            edtEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            edtPassword.setError("Mật khẩu ít nhất có 4 kí tự");
            valid = false;
        } else {
            edtPassword.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || !(reEnterPassword.equals(password))) {
            edtReEnterPassword.setError("Mật khẩu không phù hợp");
            valid = false;
        } else {
            edtReEnterPassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
      //  overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            finish();
        }
    }



    //-----------------------------------------


    class SignUp extends AsyncTask<User,String,String> {
        public boolean parseJsonResponse(String json){
            Log.d("loglogin","da vao parse json");
            try{
                JSONObject response = new JSONObject(json);
                String error = response.optString("error");
                // String message = response.optString("message");


                switch(error){
                    case "true":

                        String message = response.optString("message");
                        Log.d("loglogin","dang ky that bai "+message);
                        // toast.makeText(context,message,Toast.LENGTH_LONG);
                        onSignupFailed();
                        return false;


                    case "false":
                        Log.d("loglogin","dang ky thanh cong ");
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
                        Log.d("loglogin","dn than cong "+Name);
                        Constant.user = new User(UserId,Name,UserName,Password,Birthday,Address,Gender,Phone,Level,Email,Vip,Image);
                        // databaseHelper.addUser(Constant.user);
                        Constant.editor.putString("Id",UserId);
                        Constant.editor.putString("Username",UserName);
                        Constant.editor.putString("Image",Image);
                        Constant.editor.putString("Email",Email);
                        Constant.editor.putString("Name",Name);
                        Constant.editor.commit();
                        // toast.makeText(context,"Đăng ký thành công",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                        startActivityForResult(intent,10);
                        return true;




                    default:
                        return false;

                }
                // Log.d("mydebug",error);



            }catch (JSONException e){
                e.printStackTrace();
                //toast.makeText(context,"Có lỗi xảy ra "+e,Toast.LENGTH_LONG).show();

                return false;

            }
        }
        public String getJson(String name,String username,String email,String password) {
            String data = null;
            try {
                data = URLEncoder.encode("dkjson", "UTF-8")
                        + "=" + URLEncoder.encode("", "UTF-8");

                data += "&" + URLEncoder.encode("name", "UTF-8") + "="
                        + URLEncoder.encode(name, "UTF-8");

                data += "&" + URLEncoder.encode("username", "UTF-8") + "="
                        + URLEncoder.encode(username, "UTF-8");

                data += "&" + URLEncoder.encode("email", "UTF-8") + "="
                        + URLEncoder.encode(email, "UTF-8");

                data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                        + URLEncoder.encode(password, "UTF-8");
                data += "&" + URLEncoder.encode("ngaysinh", "UTF-8") + "="
                        + URLEncoder.encode("11/03/1993", "UTF-8");




            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

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
            return text;}
        @Override
        protected String doInBackground(User... users) {
            String json = getJson(users[0].getName(),users[0].getUsername(),users[0].getEmail(),users[0].getPassword());

            return json;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            parseJsonResponse(s);
        }
    }
}
