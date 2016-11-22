package teamthat.com.onemusic.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import teamthat.com.onemusic.R;

public class Image extends AppCompatActivity {
Button btShowImage,btTakeCapture;
    ImageView imageView;
    String ImageDecode;
    static final int RESULT_LOAD_IMAGE = 1;
    static final int REQUEST_CAMERA = 2;
    int serverResponseCode = 0;
    // cái này cần phải chỉnh lại cho chính xác
    final String uploadFilePath = "/mnt/sdcard/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avatar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thay đổi hình đại diện");
        btShowImage = (Button) findViewById(R.id.showImage);
        imageView = (ImageView) findViewById(R.id.imgView);
        btTakeCapture = (Button) findViewById(R.id.takeCapture);

        btShowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        btTakeCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(i, REQUEST_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                    && null != data) {
                onGetImageFromGallery(data);
            }else if (requestCode == REQUEST_CAMERA&& resultCode == RESULT_OK
                    && null != data){
                onCaptureImageResult(data);
            }

        } catch (Exception e) {
            Toast.makeText(this, "Please try again "+e, Toast.LENGTH_LONG)
                    .show();
        }
    }
    private void onGetImageFromGallery(Intent data){
        Uri URI = data.getData();
        String[] FILE = { MediaStore.Images.Media.DATA };


        Cursor cursor = getContentResolver().query(URI,
                FILE, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(FILE[0]);
        ImageDecode = cursor.getString(columnIndex);
        cursor.close();
        imageView.setImageBitmap(BitmapFactory
                .decodeFile(ImageDecode));
    }
    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(thumbnail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.avatar, menu);
        return true;}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.avatar:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
