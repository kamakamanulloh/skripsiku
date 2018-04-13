package com.kamak.skripsweetnewwes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kamak.skripsweetnewwes.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


public class selectimage extends Activity {
    private MyImage image;

    private TextView description;
    private String jstring;

    int success;
    private String UPLOAD_URL = Server.URL+"uploadnew.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static final String TAG_ID = "iduser";
    public static final String TAG_USERNAME = "username";
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "keterangan";
    private String KEY_alamat = "alamat";
    private String KEY_longitude = "longitude";
    private String KEY_lattitude = "lattitude";
    private String KEY_ID = "iduser";
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
    String id;
    String username;
    String status,belum;
    String tag_json_obj = "json_obj_req";
    SharedPreferences sharedpreferences;
    Toolbar toolbar;
    Button btnUpload;
    FloatingActionButton buttonUpload;
    Button btndraft;
    Intent intent;
    Uri fileUri;
    Button btn_choose_image;
    ImageView imageView;
    Bitmap bitmap, decoded;
    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;
    private ImageAdaptersqlite ImageAdaptersqlite;
    int bitmap_size = 40; // image quality 1 - 100;
    int max_resolution_image = 800;
    private static final String TAG = upload.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image2);
        imageView = (ImageView) findViewById(R.id.display_image_view);
        description = (TextView) findViewById(R.id.text_view_description);
        Bundle extras = getIntent().getExtras();
        status="menunggu";
        belum="belum";
        txt_ket = (EditText) findViewById(R.id.txtket);
        txtalamat = (EditText) findViewById(R.id.txt_komen);

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
        decoded =ImageResizer
                .decodeSampledBitmapFromFile(image.getPath(), width, height);
        imageView.setImageBitmap(decoded);
        longi=image.getTitle();
        lat=image.getDescription();

        id = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);



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
       uploadImage();
    }

    /**
     * delete the current item;
     *
     * @param v
     */
    public void btnDeleteOnClick(View v) {
        startActivity(new Intent(this, draft.class));
        intent.putExtra(TAG_ID, id);
        intent.putExtra(TAG_USERNAME, username);

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
    private void uploadImage() {
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.e("v Add", jObj.toString());

                                Toast.makeText(selectimage.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                                kosong();

                            } else {
                                Toast.makeText(selectimage.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();

                        //menampilkan toast
                        Toast.makeText(selectimage.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put(KEY_IMAGE, getStringImage(decoded));
                params.put(KEY_NAME, txt_ket.getText().toString().trim());
                params.put(KEY_alamat, txtalamat.getText().toString().trim());
                params.put(KEY_ID, id);
                params.put(KEY_lattitude, lat);
                params.put(KEY_longitude, longi);
                params.put(KEY_USERNAME, username);
                params.put(KEY_STATUS,status);
                params.put(KEY_BELUM,belum);

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }



    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void kosong() {
        imageView.setImageResource(0);
        txt_ket.setText(null);
        txtalamat.setText(null);
        Intent intent = new Intent(selectimage.this, all.class);
        intent.putExtra(TAG_ID, id);
        intent.putExtra(TAG_USERNAME, username);
        DAOdb db = new DAOdb(this);
        db.deleteImage(image);
        db.close();

        finish();
        startActivity(intent);

    }
    public void onBackPressed() {
        Intent intent = new Intent(selectimage.this, draft.class);
        intent.putExtra(TAG_ID, id);
        intent.putExtra(TAG_USERNAME, username);


        finish();
        startActivity(intent);

    }

}
