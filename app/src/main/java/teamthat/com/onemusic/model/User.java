package teamthat.com.onemusic.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ASUS on 11/1/2016.
 */
public class User {
    String id;
    String name;
    String username;
    String password;
    String birthday;
    String address;
    String gender;
    String phone;
    String level;
    String email;
    String vip;
    String image;
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public User() {
    }

    public User(Context context,String id, String name, String username, String password, String birthday, String address, String gender, String phone, String level, String email, String vip, String image) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.address = address;
        this.gender = gender;
        this.phone = phone;
        this.level = level;
        this.email = email;
        this.vip = vip;
        this.image = image;
        editor.putString("userid",id);
        editor.putString("username",username);
        editor.putString("name",name);
        editor.putString("password",password);
        editor.putString("birthday",birthday);
        editor.putString("address",address);
        editor.putString("gender",gender);
        editor.putString("phone",phone);
        editor.putString("level",level);
        editor.putString("email",email);
        editor.putString("vip",vip);
        editor.putString("image",image);
        editor.commit();
    }
    public void createSession(String id, String name, String username, String password, String birthday, String address, String gender, String phone, String level, String email, String vip, String image) {

        editor.putString("userid",id);
        editor.putString("username",username);
        editor.putString("name",name);
        editor.putString("password",password);
        editor.putString("birthday",birthday);
        editor.putString("address",address);
        editor.putString("gender",gender);
        editor.putString("phone",phone);
        editor.putString("level",level);
        editor.putString("email",email);
        editor.putString("vip",vip);
        editor.putString("image",image);
        editor.commit();
    }

    public User(Context context){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();

    }

    public String getId() {

        return prefs.getString("userid",null);
    }

    public void setId(String id) {
        this.id = id;
        editor.putString("userid",id);
        editor.commit();
    }

    public String getName() {
        return prefs.getString("name",null);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return prefs.getString("username",null);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return prefs.getString("password",null);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return prefs.getString("birthday",null);
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return prefs.getString("address",null);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return prefs.getString("gender",null);
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return prefs.getString("phone",null);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLevel() {
        return prefs.getString("level",null);
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getEmail() {
        return prefs.getString("email",null);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVip() {
        return prefs.getString("vip",null);
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getImage() {
        return prefs.getString("image",null);
    }

    public void setImage(String image) {
        this.image = image;
    }


}
