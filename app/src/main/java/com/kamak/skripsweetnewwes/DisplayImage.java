package com.kamak.skripsweetnewwes;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class DisplayImage extends Activity {
    private MyImage image;
    private ImageView imageView;
    private TextView description;
    private String jstring;
    Bitmap bitmap, decoded;
    int success;
    private String UPLOAD_URL = "https://kamakamanulloh10.000webhostapp.com/uploadnew.php";

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
    EditText txt_ket,txtalamat,txt_lattitude,txt_longitude;
    String text;
    String alamat,nohp;
    String longi ;
    Toolbar toolbar;
    String lat;
    String id;
    String username;
    String status,belum;
    String tag_json_obj = "json_obj_req";
    SharedPreferences sharedpreferences;
    Button btnUpload;
    FloatingActionButton buttonUpload;
    Button btndraft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        imageView = (ImageView) findViewById(R.id.display_image_view);
        status="menunggu";
        belum="belum";
        txt_ket = (EditText) findViewById(R.id.txtket);
        txtalamat = (EditText) findViewById(R.id.txt_komen);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            jstring = extras.getString("IMAGE");
        }
        image = getMyImage(jstring);
        description.setText(image.toString());
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        imageView.setImageBitmap(ImageResizer
                .decodeSampledBitmapFromFile(image.getPath(), width, height));
    }

    private MyImage getMyImage(String image) {
        try {
            JSONObject job = new JSONObject(image);
            return (new MyImage(job.getString("title"),
                    job.getString("description"), job.getString("path"),
                    job.getLong("datetimeLong")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * go back to main activity
     *
     * @param v
     */
    public void btnBackOnClick(View v) {
        Intent intent=new Intent(DisplayImage.this,draft.class);
        intent.putExtra(TAG_ID, id);
        intent.putExtra(TAG_USERNAME, username);
        startActivity(intent);
        finish();
    }

    /**
     * delete the current item;
     *
     * @param v
     */
    public void btnDeleteOnClick(View v) {
        DAOdb db = new DAOdb(this);
        db.deleteImage(image);
        db.close();
        startActivity(new Intent(this, draft.class));
        finish();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        // Save the user's current game state
        if (jstring != null) {
            outState.putString("jstring", jstring);
        }
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        if (savedInstanceState.containsKey("jstring")) {
            jstring = savedInstanceState.getString("jstring");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
