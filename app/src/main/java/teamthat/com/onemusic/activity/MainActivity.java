package teamthat.com.onemusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;

import java.util.Collections;

import teamthat.com.onemusic.R;
import teamthat.com.onemusic.Util.Util;
import teamthat.com.onemusic.fragment.HomeFragment;
import teamthat.com.onemusic.fragment.MusicHotFragment;
import teamthat.com.onemusic.fragment.MusicRankFragment;
import teamthat.com.onemusic.fragment.SettingsFragment;
import teamthat.com.onemusic.model.User;
import teamthat.com.onemusic.other.CircleTransform;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg;
    private Toolbar toolbar;
    private SearchView searchView;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_MUSIC_HOT = "musicHot";
    private static final String TAG_ARTIST = "artist";
    private static final String TAG_MUSIC_RANK = "musicRank";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    // Fragment
    Fragment fragment;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setCurrentIndexScreen();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SplashActivity.user = new User();
        mHandler = new Handler();
        if (Constant.sharedPreferences != null) {
        if (!Constant.sharedPreferences.getString("Id", "").equals("")) {
            try {
                Util util = new Util(this);
                util.getAllFavoriteSong(Constant.sharedPreferences.getString("Id", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        if(isLogin())
        menu.findItem(R.id.nav_login).setTitle("Đăng xuất");
        else
            menu.findItem(R.id.nav_login).setTitle("Đăng nhập");

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        Constant.txtName = (TextView) navHeader.findViewById(R.id.name);
        Constant.txtEmail = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        Constant.imgAvatar = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        // load nav menu header data
        loadNavHeader(0);

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

    }

    private void loadNavHeader(int i) {
        // name, email
try {
    Constant.txtName.setText(Constant.sharedPreferences.getString("Name",""));

    Constant.txtEmail.setText(Constant.sharedPreferences.getString("Email",""));

}catch (NullPointerException e){

}

        try {
            if(!Constant.sharedPreferences.getString("Image","").equals("")||i==1)
            Glide.with(this).load(LoginActivity.LOGIN_API + Constant.sharedPreferences.getString("Image",""))
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Constant.imgAvatar);

        }catch(NullPointerException e){
            Log.d("mydebug", "user null");
        }

        // showing dot next to notifications label
        //navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button

            return;
        }

        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Collections.reverse(Constant.listHotSong);
                 fragment = getHomeFragment();
                 fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }
    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                //
                MusicHotFragment musicHotFragment = new MusicHotFragment();
                return musicHotFragment;

            case 2:
                //
                ArtistFragment artistFragment = new ArtistFragment();
                return artistFragment;
            case 3:
                //
                MusicRankFragment musicRankFragment = new MusicRankFragment();
                return musicRankFragment;

            case 4:
                //
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_music_hot:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_MUSIC_HOT;
                        break;
                    case R.id.nav_artist:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_ARTIST;
                        break;
                    case R.id.nav_music_rank:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_MUSIC_RANK;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        finish();
                       // overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        return true;
                    case R.id.nav_login:
                        // launch new intent instead of loading fragment
                        Menu menu = navigationView.getMenu();
                        if(isLogin()){
                            Constant.editor.putString("Id","");
                            Constant.editor.putString("Username","");
                            Constant.editor.putString("Image","");
                            Constant.editor.putString("Email","");
                            Constant.editor.putString("Name","");
                            Constant.editor.commit();
                            try {
//                            if(Constant.edPassword!=null){
                                Constant.edPassword.setText("");
//                            }
//                            if(Constant.edEmail!=null){
                                Constant.edEmail.setText("");
//                            }
//                            if(Constant.edName!=null){
                                Constant.edName.setText("");
//                            }
//                            if(Constant.edUsername!=null){
                                Constant.edUsername.setText("");
//                            }
//                            if(Constant.imgAvatar!=null){
                                Constant.imgAvartar1.setImageDrawable(getResources().getDrawable(R.drawable.account));
                            }catch (NullPointerException e){}
                            menu.findItem(R.id.nav_login).setTitle("Đăng nhập");
                            loadNavHeader(1);
                        }else{
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            drawer.closeDrawers();
                            finish();
                         //   overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            return true;
                        }

                    default:
                        navItemIndex = 0;
                }

                fragment = getHomeFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    // set man hình khởi chay dau
    public void setCurrentIndexScreen(){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, new HomeFragment(), CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
       if(Constant.isHome==0){
            setCurrentIndexScreen();
           Constant.isHome=1;
           return;
       }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        moveTaskToBack(true);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);

            MenuItem itemSearch = menu.findItem(R.id.search_view);
            searchView = (SearchView) itemSearch.getActionView();
            searchView.setOnQueryTextListener(this);
        }
        return true;
    }
        public boolean isLogin(){
            if(Constant.sharedPreferences==null||Constant.sharedPreferences.getString("Id","").equals("")){
                return false;
            }else{
                return true;
            }
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


//    SearchView Music
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
