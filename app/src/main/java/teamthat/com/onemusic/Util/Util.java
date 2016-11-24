package teamthat.com.onemusic.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import teamthat.com.onemusic.R;
import teamthat.com.onemusic.activity.Constant;
import teamthat.com.onemusic.fragment.Profile;
import teamthat.com.onemusic.model.ArtistMusic;
import teamthat.com.onemusic.model.User;

/**
 * Created by ASUS on 11/20/2016.
 */

public class Util {
    ProgressDialog dialog;
    boolean t;
    Activity context;
    int a=0;
    public Util(Activity activityCompat){
        this.context = activityCompat;
        dialog = new ProgressDialog(activityCompat, R.style.AppTheme_Dark_Dialog);
        dialog.setIndeterminate(true);
        dialog.setMessage("Loading...");
    }
    public void dismissDialog(){
        dialog.dismiss();
    }
    public void loadFavorite(final ArrayAdapter adapter){
        new reloadFavorite(){
            @Override
            protected void onPostExecute(Void aVoid) {
               super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
                dismissDialog();

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    public Boolean checkFavoriteSong(String userid,String songid) throws JSONException {
        getJson mygetJson = new getJson();
        String query = makeIsFavoriteSatement(userid,songid);

        mygetJson.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);

        return true;
    }
    public Boolean changeFavoriteSong(String userid,String songid) throws JSONException {
        Log.d("favorite","changeFavoriteSong "+songid);

        String query = makeChangeFavoriteSatement(userid,songid);
        new getJson().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);
        Log.d("favorite","query la "+query);

        return true;
    }
    public Boolean getAllHotSong() throws JSONException {
        String query = getAllSong();
        Log.d("hotsong",query);
        new getJson(){
            @Override
            protected void onPostExecute(String s) {
                Log.d("hotsong",s);
                super.onPostExecute(s);
                try {
                    parseAllHotSong(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);
        Log.d("favorite","query la "+query);

        return true;
    }
    public void Login(String username,String password) throws JSONException {
        a =1;
        String query = makeLoginStatement(username,password).replaceAll(" ","%20");;
        new getJson(){
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                try {
                    Intent intent = new Intent();
                  if(parseJsonLogin(s)) {

                      intent.setAction("loginsucess");
                  }else{
                      intent.setAction("loginfailure");
                  }
                    context.sendBroadcast(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                   }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);


    }
    public void SignUp(String username,String password,String email,String name) throws JSONException {
        a =1;
        String query = makeSignInStetement(username,password,name,email).replaceAll(" ","%20");;
        new getJson(){
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                try {
                    Intent intent = new Intent();
                    if(parseJsonLogin(s)) {

                        intent.setAction("signinsucess");
                    }else{
                        intent.setAction("signinfailure");
                    }
                    context.sendBroadcast(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);


    }
    public void getAllFavoriteSong(String userid) throws JSONException {
        String query = makeGetAllFavoriteSatement(userid);
        Log.d("favorite",query);
        new getJson(){
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {

                  parseJsonGetAllFavorited(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);}
    public boolean changeName(final Context context, String userid, final String name){
        String query = makechangeNameStatement(userid,name).replaceAll(" ","%20");

        Log.d("profile","query "+query+" \n name "+name);

//        this.context = context;
        new getJson(){
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d("profile","json la "+s);
                try {
                   t= parseJsonChangeProfile(context,0,s,name);
                } catch (JSONException e) {
                    e.printStackTrace();
                    t=false;
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);
        return t;
    }
    public boolean changePassword(final Context context, String userid, final String password){
        String query = makeChangePasswordStateent(userid,password);
        Log.d("profile","query "+query+" \n name "+password);
        if(containsWhiteSpace(password)){
            Toast.makeText(context,"Password không được chứa khoảng trắng",Toast.LENGTH_LONG).show();
            return false;
        }

//        this.context = context;
        new getJson(){
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d("profile","json la "+s);
                try {
                    t= parseJsonChangeProfile(context,3,s,password);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Sửa mật khẩu thất bại ",Toast.LENGTH_LONG).show();
                    t=false;
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);
        return t;
    }
    public boolean changeUsername(final Context context, String userid, final String username){
        String query = makechangeUserNameStatement(userid,username);
        if(containsWhiteSpace(username)){
            Toast.makeText(context,"Tên đăng nhập không được chứa khoảng trắng",Toast.LENGTH_LONG).show();
            return false;
        }
        new getJson(){
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                 t=   parseJsonChangeProfile(context,2,s,username);
                } catch (JSONException e) {
                    e.printStackTrace();
                    t=false;
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);
      return t;
    }
    public boolean changeEmail(final Context context, String userid, final String email){
       String query = makechangeEmailStatement(userid,email);
        if(!isEmailValid(email)){
            Toast.makeText(context,"Email không đúng định dạng",Toast.LENGTH_LONG).show();
            return false;
        }
        new getJson(){
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                  t=  parseJsonChangeProfile(context,1,s,email);
                } catch (JSONException e) {
                    e.printStackTrace();
                    t = false;
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);
        return t;
    }
    public boolean parseJsonChangeProfile(Context context,int i,String json,String text) throws JSONException {
        JSONObject response = new JSONObject(json);

        boolean t =response.optBoolean("error");
        String mes = response.optString("message");
        if(t){
            Toast.makeText(context,mes,Toast.LENGTH_LONG).show();
        }else{
            switch (i){
                case 0:
                    Constant.editor.putString("Name",text);

                    Log.d("profile1","Name"+Constant.sharedPreferences.getString("Name",""));

                    break;
                case 1:
                    Constant.editor.putString("Email",text);
                    break;
                case 2:
                    Constant.editor.putString("Username",text);

                    break;
                case 3:
                    Constant.editor.putString("Password",text);
                    break;
                default:
                    break;
            }
            Constant.editor.commit();
            Profile profile = new Profile();
            profile.setValue();
            Toast.makeText(context,mes,Toast.LENGTH_LONG).show();
        }
        return t;

    }

    public String makeChangeFavoriteSatement(String userid,String songid){
        StringBuilder builder = new StringBuilder(Constant.CHANGEFAVORITE_API);
        builder.append("&userid=").append(userid);
        builder.append("&songid=").append(songid);

        return builder.toString();
    }
    public String makeIsFavoriteSatement(String userid,String songid){
        StringBuilder builder = new StringBuilder(Constant.CHECKFAVORITED_API);
        builder.append("&userid=").append(userid);
        builder.append("&songid=").append(songid);

        return builder.toString();
    } public String makeGetAllFavoriteSatement(String userid){
        StringBuilder builder = new StringBuilder(Constant.GETALLFAVORITE_API);
        builder.append("&userid=").append(userid);

        return builder.toString();
    }
    public String makechangeNameStatement(String userid,String name){
        StringBuilder builder = new StringBuilder(Constant.CHANGENAME_API);
        builder.append("&id=").append(userid);
        builder.append("&name=").append(name);
        return builder.toString();
    }
    public String makechangeUserNameStatement(String userid,String username){
        StringBuilder builder = new StringBuilder(Constant.CHANGEUSERNAME_API);
        builder.append("&id=").append(userid);
        builder.append("&username=").append(username);
        return builder.toString();
    }
    public String makechangeEmailStatement(String userid,String email){
        StringBuilder builder = new StringBuilder(Constant.CHANGEEMAIL_API);
        builder.append("&id=").append(userid);
        builder.append("&email=").append(email);
        return builder.toString();
    }
    public String makeChangePasswordStateent(String userid,String password){
        StringBuilder builder = new StringBuilder(Constant.CHANGEPASSWORD_API);
        builder.append("&id=").append(userid);
        builder.append("&password=").append(password);
        return builder.toString();
    }
    public String makeLoginStatement(String username,String password){
        StringBuilder builder = new StringBuilder(Constant.LOGIN_API);
        builder.append("&username=").append(username);
        builder.append("&password=").append(password);
        return builder.toString();
    }
    public String makeSignInStetement(String username,String password,String name,String email){
        StringBuilder builder = new StringBuilder(Constant.SIGNIN_API);
        builder.append("&username=").append(username);
        builder.append("&password=").append(password);
        builder.append("&name=").append(name);
        builder.append("&email=").append(email);
        return builder.toString();
    }
    public Boolean parseJsonCheckFavorite(String json) throws JSONException {
        JSONObject response = new JSONObject(json);
        Boolean isFavorited = response.optBoolean("favorited");
        return isFavorited;

    }
    public Boolean parseJsonChangeFavorite(String json) throws JSONException {
        JSONObject response = new JSONObject(json);
        Boolean isFavorited = response.optBoolean("error");
        Log.d("favorite",isFavorited+"");
        return isFavorited;}
    public String getAllSong(){
        StringBuilder builder = new StringBuilder(Constant.GETALLSONG_API);


        return builder.toString();
    }






    public ArrayList<ArtistMusic> parseJsonGetAllFavorited(String json) throws JSONException {
        Log.d("favorite","vafo parse json "+json);
        ArrayList<ArtistMusic> listMusic = new ArrayList<>();
        Constant.listfavoriteSong.removeAll(Constant.listfavoriteSong);
        try{
            JSONArray array = new JSONArray(json);
            for(int i=0; i< array.length();i++ ){
                JSONObject cm = array.optJSONObject(i);
                String nameMusic = cm.optString("name");
                String pathMusic = cm.optString("musicpath");
                String id = cm.optString("id");
                ArtistMusic music = new ArtistMusic(nameMusic, pathMusic,id);
                Constant.listfavoriteSong.add(music);
                Log.d("favorite","name "+nameMusic);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return listMusic;
    }
    public ArrayList<ArtistMusic> parseAllHotSong(String json) throws JSONException {
        Log.d("favorite","vafo parse json "+json);
        ArrayList<ArtistMusic> listMusic = new ArrayList<>();
        Constant.listHotSong.removeAll(Constant.listHotSong);
        try{
            JSONArray array = new JSONArray(json);
            for(int i=0; i< array.length();i++ ){
                JSONObject cm = array.optJSONObject(i);
                String nameMusic = cm.optString("name");
                String pathMusic = cm.optString("musicpath");
                String id = cm.optString("id");
                ArtistMusic music = new ArtistMusic(nameMusic, pathMusic,id);
                Constant.listHotSong.add(music);
                Log.d("favorite","name "+nameMusic);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return listMusic;
    }
    public boolean parseJsonLogin(String json) throws JSONException {

        boolean t ;

        try{

            JSONObject response = new JSONObject(json);
            String error = response.optString("error");



            switch(error){

                case "true":

                    String message = response.optString("message");
                    Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                    t=false;

                 break;
                case "false":

                    t=true;


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

                    Constant.user = new User(UserId,Name,UserName,Password,Birthday,Address,Gender,Phone,Level,Email,Vip,Image);
                    // databaseHelper.addUser(Constant.user);
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
                        Toast.makeText(context,"Không lưu được vào Session",Toast.LENGTH_LONG).show();

                    }

                        break;

                     default:

                         t = false;

                         break;
            }




        }catch (JSONException e){
            e.printStackTrace();

            t = false;

        }

        return t;

    }



    public class getJson extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(a==1)
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("favorite","vao doInBackground "+params[0]);
                Log.d("favorite","mở urlconnection");
                HttpURLConnection urlConnection = null;

                BufferedReader reader = null;

                // Will contain the raw JSON response as a string.
                String forecastJsonStr = null;

                try {
                    Log.d("favorite","khai báo url "+params[0]);

                    URL url = new URL(params[0]);

                    // Create the request to OpenWeatherMap, and open the connection
                    Log.d("favorite","tạo url connection");

                    urlConnection = (HttpURLConnection) url.openConnection();
                    Log.d("favorite","set request method");

                    urlConnection.setRequestMethod("GET");
                    Log.d("favorite","kết nối tới url");

                    urlConnection.connect();

                    // Read the input stream into a String
                    int status = urlConnection.getResponseCode();
                    Log.d("favorite","khởi tạo input Stream "+status);

                    InputStream inputStream = urlConnection.getInputStream();


                    Log.d("favorite","buffer");

                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        Log.d("favorite","buffer rỗng");

                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    Log.d("favorite","lấy đk reader ");

                    String line;
                    Log.d("favorite","line");

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }
                    forecastJsonStr = buffer.toString();
                    Log.d("favorite",forecastJsonStr);
                } catch (IOException e) {
                    Log.e("PlaceholderFragment", "Error ", e);
                    return null;
                } finally{
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e("PlaceholderFragment", "Error closing stream", e);
                        }
                    }
                }


                return forecastJsonStr;
        }

   }
    public class reloadFavorite extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if(!Constant.sharedPreferences.getString("Id","").equals("")){
                   getAllFavoriteSong(Constant.sharedPreferences.getString("Id",""));}
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public static boolean containsWhiteSpace(final String testCode){
        if(testCode != null){
            for(int i = 0; i < testCode.length(); i++){
                if(Character.isWhitespace(testCode.charAt(i))){
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
