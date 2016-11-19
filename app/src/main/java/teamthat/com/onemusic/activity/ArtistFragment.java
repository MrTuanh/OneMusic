package teamthat.com.onemusic.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
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

import teamthat.com.onemusic.R;
import teamthat.com.onemusic.adapter.ArtistBaseAdapter;
import teamthat.com.onemusic.model.Artist;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArtistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArtistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public final String GET_ALL_ARTIST_API = "http://nghiahoang.net/api/appmusic/?function=getallartist";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static Artist artist= null;

    GridView gvArtist;
   static ArrayList<Artist> listartist;
    ArrayAdapter adapter;
    ArtistBaseAdapter artistAdapter;
    ProgressDialog dialog;
    getAllArtist mygetallartist;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ArtistFragment() {
        // Required empty public constructor
    }

    public static ArtistFragment newInstance(String param1, String param2) {

        ArtistFragment fragment = new ArtistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      mygetallartist = new getAllArtist();
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        final ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo !=null){

           mygetallartist.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,GET_ALL_ARTIST_API);
        }else{
            Toast.makeText(getActivity().getApplicationContext(),"Khong co ket noi mang",Toast.LENGTH_SHORT).show();



        }


    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_artist, container, false);
        gvArtist = (GridView) rootview.findViewById(R.id.gvArtist);
        listartist =  new ArrayList<>();
       artistAdapter = new ArtistBaseAdapter(getActivity(), R.layout.item_artist, listartist);
        gvArtist.setAdapter(artistAdapter);
        goToArtist();



        return rootview;
    }

    public void goToArtist() {
    gvArtist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
           // Toast.makeText(getActivity(), listartist.get(position).getName(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), ArtistMusicActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("image",listartist.get(position).getImage());
            bundle.putString("id",listartist.get(position).getId());
            bundle.putString("name",listartist.get(position).getName());
            intent.putExtra("bundle",bundle);
           artist = listartist.get(position);
            Constant.artist=listartist.get(position);
            startActivity(intent);
        }
    });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
       dialog = ProgressDialog.show(context, "",
                "Loading. Please wait...", true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    // Connecto To Server
    public class getAllArtist extends AsyncTask<String,Artist,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("mydebug","bat dau vao tai");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("mydebug","vào background");
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                URL url = new URL(params[0]);
                Log.d("mydebug","url "+url);
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.d("mydebug","json la "+ forecastJsonStr);
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

            //--------------------------------------
            Log.d("mydebug","vao day");
            try{

                JSONArray array = new JSONArray(forecastJsonStr);

                for(int i=0; i< array.length();i++ ){
                    JSONObject cm = array.optJSONObject(i);
                    String artistId = cm.optString("ArtistId");
                    String artistName = cm.optString("ArtistName");
                    String image = cm.getString("Image");
                    String des = cm.getString("Description");
                    String love = cm.getString("Love");
                    Log.d("mydebug",artistName);
                    Artist artist = new Artist(artistId,artistName,image,love,des);

                    publishProgress(artist);

                }



            }catch (JSONException e){
                e.printStackTrace();

            }
            //--------------------------------------
            return null;
        }

        @Override
        protected void onProgressUpdate(Artist... values) {
            super.onProgressUpdate(values);
            listartist.add(values[0]);
            artistAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            dialog.dismiss();
           // Log.d("mydebug",json);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mygetallartist.cancel(true);
    }
}
