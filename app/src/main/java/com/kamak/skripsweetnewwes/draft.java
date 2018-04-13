package com.kamak.skripsweetnewwes;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class draft extends ActionBarActivity {

    private ArrayList<MyImage> images;
    private ImageAdaptersqlite imageAdapter;
    private ListView listView;
    private Uri mCapturedImageURI;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private DAOdb daOdb;
    String name;
    public static final String TAG_ID = "iduser";
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
    EditText txt_ket,txtalamat,txt_lattitude,txt_longitude;
    String text;
    String alamat,nohp;
    String longi ;
    String lat;
    String iduser;
    String username;
    String status,belum;
    String tag_json_obj = "json_obj_req";
    SharedPreferences sharedpreferences;
TextView txt;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draft);
        iduser = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);
        // Construct the data source
        images = new ArrayList();
        // Create the Adapter to convert the array to views
        imageAdapter = new ImageAdaptersqlite(this, images);
        // Attach the Adapter to a ListView
        listView = (ListView) findViewById(R.id.main_list_view);
        listView.setAdapter(imageAdapter);
        addItemClickListener(listView);
        initDB();
        GPSTracker gps = new GPSTracker(draft.this);
        lat = String.valueOf(gps.getLatitude());
        longi = String.valueOf(gps.getLongitude());


    }

    /**
     * initialize database
     */
    private void initDB() {
        daOdb = new DAOdb(this);
        //        add images from database to images ArrayList
        for (MyImage mi : daOdb.getImages()) {
            images.add(mi);
        }
    }

    public void btnAddOnClick(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_box);

        Button btnExit = (Button) dialog.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btnTakePhoto).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                activeTakePhoto();
            }
        });

        // show dialog on screen
        dialog.show();
    }

    /**
     * take a photo
     */
    private void activeTakePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            String fileName = "temp.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * to gallery
     */
    private void activeGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if (requestCode == RESULT_LOAD_IMAGE &&
                        resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    MyImage image = new MyImage();
                    image.setTitle(name);
                    image.setDescription("test choose a photo from gallery and add it to " + "list view");
                    image.setDatetime(System.currentTimeMillis());
                    image.setPath(picturePath);
                    image.setIduser(iduser);
                    image.setUsername(username);
                    //                    images.add(image);//notifyDataSetChanged does not work well sometimes
                    imageAdapter.add(image);
                    daOdb.addImage(image);
                }
            case REQUEST_IMAGE_CAPTURE:
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = managedQuery(mCapturedImageURI, projection, null, null, null);
                    int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String picturePath = cursor.getString(column_index_data);
                    MyImage image = new MyImage();
                    image.setTitle(lat);
                    image.setDescription(longi);
                    image.setDatetime(System.currentTimeMillis());
                    image.setPath(picturePath);
                    imageAdapter.add(image);
                    //                    images.add(image);
                    daOdb.addImage(image);
                }
        }
    }
    private void addItemClickListener(final ListView listView) {
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                MyImage image = (MyImage) listView.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), selectimage.class);
                intent.putExtra(TAG_ID, iduser);
                intent.putExtra(TAG_USERNAME, username);
                intent.putExtra("IMAGE", (new Gson()).toJson(image));
                startActivity(intent);
            }
        });
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        // Save the user's current game state
        if (mCapturedImageURI != null) {
            outState.putString("mCapturedImageURI", mCapturedImageURI.toString());
        }
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        if (savedInstanceState.containsKey("mCapturedImageURI")) {
            mCapturedImageURI = Uri.parse(savedInstanceState.getString("mCapturedImageURI"));
        }
    }
    public void onBackPressed() {
        Intent intent = new Intent(draft.this, home.class);
        intent.putExtra(TAG_ID, iduser);
        intent.putExtra(TAG_USERNAME, username);


        finish();
        startActivity(intent);

    }
}
