package teamthat.com.onemusic.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import teamthat.com.onemusic.R;
import teamthat.com.onemusic.activity.AddplaylistActivity;
import teamthat.com.onemusic.activity.Constant;
import teamthat.com.onemusic.activity.LoginActivity;
import teamthat.com.onemusic.activity.PlayerActivity;
import teamthat.com.onemusic.model.Artist;
import teamthat.com.onemusic.model.ArtistMusic;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MusicHotFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MusicHotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicHotFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView lvMusicHot;
    ArrayAdapter<ArtistMusic> adapter;

    private OnFragmentInteractionListener mListener;

    public MusicHotFragment() {
        // Required empty public constructor
    }

    public static MusicHotFragment newInstance(String param1, String param2) {
        MusicHotFragment fragment = new MusicHotFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_music_hot, container, false);
        Constant.artist = new Artist();
        Constant.artist.setName("Mặc định");
        lvMusicHot = (ListView) rootView.findViewById(R.id.lv_musichot);
        adapter = new ArrayAdapter<ArtistMusic>(getActivity(), android.R.layout.simple_list_item_1, Constant.listHotSong);
        lvMusicHot.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        goToMusicHot();
        goadd();
        return rootView;
    }

    public void goToMusicHot() {
        lvMusicHot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Constant.name = Constant.listHotSong.get(i).getNameMusic();
                Constant.music_id = Constant.listHotSong.get(i).getId();
                Constant.path = LoginActivity.LOGIN_API + Constant.listHotSong.get(i).getMusicPath();
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                Toast.makeText(getActivity(), String.valueOf(Constant.music_id), Toast.LENGTH_LONG).show();
                intent.putExtra("name", true);
                intent.putExtra("online", true);
                intent.putExtra("type", "hotmusic");
                Constant.type = 3;
                Constant.index = i;
                startActivity(intent);

            }
        });

    }

    public  void goadd()
    {
        lvMusicHot.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {
                Constant.name = Constant.listHotSong.get(i).getNameMusic();
                Constant.music_id = Constant.listHotSong.get(i).getId();
                Constant.path = LoginActivity.LOGIN_API + Constant.listHotSong.get(i).getMusicPath();
                Intent intent = new Intent(getActivity(), AddplaylistActivity.class);
                Toast.makeText(getActivity(), String.valueOf(Constant.music_id), Toast.LENGTH_LONG).show();
                intent.putExtra("id", String.valueOf(Constant.music_id));
                Constant.type = 3;
                Constant.index = i;
                getActivity().startActivity(intent);
                return false;
            }
        });
    }

        // TODO: Rename method, update argument and hook method into UI event
        public void onButtonPressed (Uri uri){
            if (mListener != null) {
                mListener.onFragmentInteraction(uri);
            }
        }

        @Override
        public void onAttach (Context context){
            super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
        }

        @Override
        public void onDetach () {
            super.onDetach();
            mListener = null;
        }

        public interface OnFragmentInteractionListener {
            // TODO: Update argument type and name
            void onFragmentInteraction(Uri uri);
        }


    }
