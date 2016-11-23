package teamthat.com.onemusic.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import teamthat.com.onemusic.R;
import teamthat.com.onemusic.Util.Util;
import teamthat.com.onemusic.activity.Constant;
import teamthat.com.onemusic.activity.Image;
import teamthat.com.onemusic.activity.LoginActivity;
import teamthat.com.onemusic.other.CircleTransform;

import static teamthat.com.onemusic.R.id.edPassword;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    BroadcastReceiver setValue=null;
    IntentFilter filter=new IntentFilter("setValue");
    RelativeLayout localBanner, favbanner, folderBanner;


    Button btChangeAvatar;
    AlertDialog.Builder alertDialog;
    LinearLayout linearLayout;
    EditText editText;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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
            setValue = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    setValue();
                    Toast.makeText(getActivity(),"da nhan ",Toast.LENGTH_LONG).show();
                }
            };
            getActivity().registerReceiver(setValue,filter);

        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Constant.isHome=0;
        View rootView = inflater.inflate(R.layout.activity_profile,container, false);

        Constant.edName = (EditText) rootView.findViewById(R.id.edName);
        Constant.edEmail = (EditText) rootView.findViewById(R.id.edEmail);
        Constant.edPassword = (EditText) rootView.findViewById(edPassword);

        Constant. edUsername = (EditText) rootView.findViewById(R.id.edUsername);
        Constant.imgAvartar1 = (ImageView) rootView.findViewById(R.id.avatar);
        btChangeAvatar = (Button) rootView.findViewById(R.id.edImage);
       setValue();
        if(!Constant.sharedPreferences.getString("Image","").equals(""))
            Glide.with(this).load(LoginActivity.LOGIN_API + Constant.sharedPreferences.getString("Image",""))
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getActivity()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Constant.imgAvartar1);
        btChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Image.class));
            }
        });
        Constant.edName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                showDialog(v.getContext(),0);
                return true;
            }
        });

        Constant.edEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                    showDialog(v.getContext(),1);
                return true;
            }
        });
        Constant.edUsername.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                    showDialog(v.getContext(),2);
                return true;
            }
        });
        Constant.edPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                    showDialog(v.getContext(),3);
                return false;
            }
        });

        return rootView;}



    public void setValue(){
        Constant.edName.setText(Constant.sharedPreferences.getString("Name",""));
        Log.d("profile1","ed hien tai la "+Constant.sharedPreferences.getString("Name",""));
        Constant.edUsername.setText(Constant.sharedPreferences.getString("Username",""));
        Constant.edEmail.setText(Constant.sharedPreferences.getString("Email",""));
        Constant.edPassword.setText(Constant.sharedPreferences.getString("Password",""));
        Constant.txtEmail.setText(Constant.sharedPreferences.getString("Email",""));
        Constant.txtName.setText(Constant.sharedPreferences.getString("Name",""));


    }
    public void showDialog(final Context context,final int index){
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        editText= new EditText(context);
        alertDialog = new AlertDialog.Builder(context);

        linearLayout.addView(editText);
        alertDialog.setView(linearLayout);
        switch (index){
            case 0:
                alertDialog.setTitle("Sửa tên");
                break;
            case 1:
                alertDialog.setTitle("Sửa Email");
                break;
            case 2:
                alertDialog.setTitle("Sửa Tên đăng nhập");
                break;
            case 3:
                editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                alertDialog.setTitle("Nhập lại mật khẩu");
                break;
            default:
                editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                alertDialog.setTitle("Nhập mật khẩu mới");
                break;
        }

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Util util = new Util();
                switch (index){
                    case 0:

                            Log.d("profile",editText.getText().toString());
                          util.changeName(context,Constant.sharedPreferences.getString("Id",""),editText.getText().toString());


                        break;
                    case 1:
                       util.changeEmail(context,Constant.sharedPreferences.getString("Id",""),editText.getText().toString());

                        break;
                    case 2:
                       util.changeUsername(context,Constant.sharedPreferences.getString("Id",""),editText.getText().toString());


                        break;
                    case 3:
                           if(editText.getText().toString().equals("")){
                               Toast.makeText(context,"Không được để trống trường mật khẩu",Toast.LENGTH_LONG).show();
                           } else if(!editText.getText().toString().equals(Constant.sharedPreferences.getString("Password",""))){
                               Toast.makeText(context,"Mật khẩu không khớp",Toast.LENGTH_LONG).show();}
                            else{
                             //  Toast.makeText(context,"Sẵn sàng để sửa mật khẩu",Toast.LENGTH_LONG).show();
                               showDialog(context,4);

                           }
                                Log.d("profile2","mk moi "+editText.getText()+"mk cu "+Constant.sharedPreferences.getString("Password",""));
                        break;
                    case 4:
                        util.changePassword(context,Constant.sharedPreferences.getString("Id",""),editText.getText().toString());

                        //  Toast.makeText(context,"chinh mat khau moi",Toast.LENGTH_LONG).show();


                }

            }
        });
        alertDialog.create();
        alertDialog.show();
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
