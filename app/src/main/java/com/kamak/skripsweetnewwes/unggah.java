package com.kamak.skripsweetnewwes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class unggah extends AppCompatActivity {

    Intent intent;
    Uri fileUri;
    Button btn_choose_image;

    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;

    int bitmap_size = 40; // image quality 1 - 100;
    Toolbar toolbar;
    ImageView imageView;
    EditText txt_ket,txtalamat,txt_lattitude,txt_longitude;
    Bitmap bitmap, decoded;
    int max_resolution_image = 800;
    private static final String TAG = upload.class.getSimpleName();

    /* 10.0.2.2 adalah IP Address localhost Emulator Android Studio. Ganti IP Address tersebut dengan
    IP Address Laptop jika di RUN di HP/Genymotion. HP/Genymotion dan Laptop harus 1 jaringan! */
    private String UPLOAD_URL = "http://dinaspuprkabjbg.site/upload.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "keterangan";
    private String KEY_alamat = "alamat";
    private String KEY_longitude = "longitude";
    private String KEY_lattitude = "lattitude";
    private String KEY_ID = "id";
    private String KEY_USERNAME = "username";
    private String KEY_STATUS = "status";
    private String KEY_BELUM = "belum";
    public final static String TAG_ALAMAT = "alamat";
    public final static String TAG_NOHP = "nohp";

    String text;
    String alamat,nohp;
    String longi ;
    String lat;
    String id;
    String username;
    String status,belum;
    String tag_json_obj = "json_obj_req";
    SharedPreferences sharedpreferences;
    Button buttonChoose;
    FloatingActionButton buttonUpload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (FloatingActionButton) findViewById(R.id.buttonUpload);
        sharedpreferences = getSharedPreferences(MainActivity.my_shared_preferences, Context.MODE_PRIVATE);
        setTitle("Kirim Postigan");

        id = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);
        nohp = getIntent().getStringExtra(TAG_NOHP);
        alamat = getIntent().getStringExtra(TAG_ALAMAT);
        GPSTracker gps = new GPSTracker(unggah.this);
        lat = String.valueOf(gps.getLatitude());
        longi = String.valueOf(gps.getLongitude());
        String latitude = String.valueOf(gps.getLatitude());
        String longitude = String.valueOf(gps.getLongitude());
        txt_lattitude= (EditText) findViewById(R.id.txtlat);
        txt_longitude= (EditText) findViewById(R.id.txtlong);
        imageView = (ImageView) findViewById(R.id.imageView);
        txt_lattitude.setText(latitude);
        txt_lattitude.setEnabled(false);
        txt_longitude.setText(longitude);
        txt_longitude.setEnabled(false);
        status="menunggu";
        belum="belum";
        txt_ket = (EditText) findViewById(R.id.txtket);
        txtalamat = (EditText) findViewById(R.id.txt_komen);

        btn_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    private void selectImage() {
        imageView.setImageResource(0);
        final CharSequence[] items = {"Take Photo",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(unggah.this);
        builder.setTitle("Add Photo!");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }  else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult", "requestCode " + requestCode + ", resultCode " + resultCode);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                try {
                    Log.e("CAMERA", fileUri.getPath());

                    bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE && data != null && data.getData() != null) {
                try {
                    // mengambil gambar dari Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(unggah.this.getContentResolver(), data.getData());
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Untuk menampilkan bitmap pada ImageView
    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imageView.setImageBitmap(decoded);
    }

    // Untuk resize bitmap
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DeKa");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("Monitoring", "Oops! Failed create Monitoring directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "embongan" + timeStamp + ".jpg");

        return mediaFile;
    }

}
