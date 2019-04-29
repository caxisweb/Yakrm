package com.codeclinic.yakrm.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.codeclinic.yakrm.Models.FriendMobileNumberModel;
import com.codeclinic.yakrm.Models.SendVoucherToFriendModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SendToFriendActivity extends AppCompatActivity {

    Button btn_complete;
    ImageView img_back, img_voucher, img_search;
    private final int CAMERA_IMAGE = 1;
    EditText edt_mobile, ed_description;
    private final int PICK_IMAGE_GALLERY = 3;
    private static final String VIDEO_DIRECTORY = "/yakrm/";
    String camera_or_gallery = "0", mediatype;
    TextView tv_itemname, tv_price, tv_name, tv_email, tv_sending_date, tv_add_video;
    String decodableString;

    API apiService;
    JSONObject jsonObject_mobile = new JSONObject();
    JSONObject jsonObject = new JSONObject();
    SessionManager sessionManager;
    ProgressDialog progressDialog;

    String mobile_number;
    Uri selectedImage;
    File sourceFile_sign, compressed_Image;
    boolean value;
    private int GALLERY = 1, CAMERA = 2;
    Compressor compressedImage;
    MultipartBody.Part body = null;
    private VideoView videoView;

    public boolean isEmpty(CharSequence character) {
        return character == null || character.length() == 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_friend);

        StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder1.build());
        compressedImage = new Compressor(this);
        apiService = RestClass.getClient().create(API.class);
        videoView = findViewById(R.id.vv);
        btn_complete = findViewById(R.id.btn_complete);

        progressDialog = new ProgressDialog(this);

        sessionManager = new SessionManager(this);

        img_back = findViewById(R.id.img_back);
        img_voucher = findViewById(R.id.img_voucher);
        img_search = findViewById(R.id.img_search);

        tv_itemname = findViewById(R.id.tv_itemname);
        tv_price = findViewById(R.id.tv_price);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_sending_date = findViewById(R.id.tv_sending_date);
        tv_add_video = findViewById(R.id.tv_add_video);

        edt_mobile = findViewById(R.id.edt_mobile);
        ed_description = findViewById(R.id.ed_description);


        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Picasso.with(this).load(ImageURL.Vendor_voucher_image + VoucherDetailActivity.v_image).into(img_voucher);
        tv_itemname.setText(VoucherDetailActivity.voucher_name);
        tv_price.setText(VoucherDetailActivity.price + " " + getResources().getString(R.string.SR_currency));

        tv_sending_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmpty(edt_mobile.getText().toString())) {
                    Toast.makeText(SendToFriendActivity.this, "Please verify your friends mobile number", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    try {
                        jsonObject_mobile.put("phone", edt_mobile.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<FriendMobileNumberModel> friendMobileNumberModelCall = apiService.FRIEND_MOBILE_NUMBER_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject_mobile.toString());
                    friendMobileNumberModelCall.enqueue(new Callback<FriendMobileNumberModel>() {
                        @Override
                        public void onResponse(Call<FriendMobileNumberModel> call, Response<FriendMobileNumberModel> response) {
                            progressDialog.dismiss();
                            String status = response.body().getStatus();
                            if (status.equals("1")) {
                                tv_name.setText(response.body().getName());
                                tv_email.setText(response.body().getEmail());
                                mobile_number = edt_mobile.getText().toString();
                            } else {
                                Toast.makeText(SendToFriendActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FriendMobileNumberModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(SendToFriendActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        tv_add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connection_Detector.isInternetAvailable(SendToFriendActivity.this)) {
                    if (isEmpty(mobile_number)) {
                        Toast.makeText(SendToFriendActivity.this, "Please verify your friends mobile number", Toast.LENGTH_SHORT).show();
                    } else if (isEmpty(ed_description.getText().toString())) {
                        Toast.makeText(SendToFriendActivity.this, "Please Enter Description", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.setMessage("Please Wait");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        RequestBody voucherID = RequestBody.create(MediaType.parse("text/plain"), VoucherDetailActivity.voucher_id);
                        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), mobile_number);
                        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), ed_description.getText().toString());
                        RequestBody voucher_payment_id = RequestBody.create(MediaType.parse("text/plain"), VoucherDetailActivity.v_payment_id);
                        RequestBody scan_v_type = null;
                        switch (VoucherDetailActivity.scan_voucher_type) {
                            case "p":
                                scan_v_type = RequestBody.create(MediaType.parse("text/plain"), "purchase_voucher");
                                break;
                            case "r":
                                scan_v_type = RequestBody.create(MediaType.parse("text/plain"), "replace_voucher");
                                break;
                            case "g":
                                scan_v_type = RequestBody.create(MediaType.parse("text/plain"), "gift_voucher");
                                break;
                        }
                        if (sourceFile_sign != null) {
                            RequestBody reqFile = RequestBody.create(MediaType.parse("*/*"), sourceFile_sign);
                            body = MultipartBody.Part.createFormData("video_or_image", sourceFile_sign.getName(), reqFile);
                        }
                        Call<SendVoucherToFriendModel> sendVoucherToFriendModelCall = apiService.SEND_VOUCHER_TO_FRIEND_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), voucherID, mobile, description, voucher_payment_id, scan_v_type, body);
                        sendVoucherToFriendModelCall.enqueue(new Callback<SendVoucherToFriendModel>() {
                            @Override
                            public void onResponse(Call<SendVoucherToFriendModel> call, Response<SendVoucherToFriendModel> response) {
                                progressDialog.dismiss();
                                VoucherDetailActivity.voucher_name = "";
                                VoucherDetailActivity.date = "";
                                VoucherDetailActivity.barcode = "";
                                VoucherDetailActivity.pincode = "";
                                VoucherDetailActivity.price = "";
                                VoucherDetailActivity.v_image = "";
                                VoucherDetailActivity.v_payment_id = "";
                                VoucherDetailActivity.voucher_id = "";
                                VoucherDetailActivity.v_image = "";
                                VoucherDetailActivity.scan_voucher_type = "";
                                startActivity(new Intent(SendToFriendActivity.this, MainActivity.class));
                                Toast.makeText(SendToFriendActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                VoucherDetailActivity.voucher_detail_activity.finish();
                                ExchangeVoucherActivity.ex_activity.finish();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<SendVoucherToFriendModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(SendToFriendActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    Toast.makeText(SendToFriendActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void selectImage() {
        final CharSequence[] options = {"Record video from camera", "Select video from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SendToFriendActivity.this);
        builder.setTitle("Add Video!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Record video from camera")) {
                    if (isPermissionGranted()) {
                        takeVideoFromCamera();
                    } else {
                        Toast.makeText(SendToFriendActivity.this, "Permission needed to access Camera", Toast.LENGTH_SHORT).show();
                    }
                } else if (options[item].equals("Select video from gallery")) {
                    if (isPermissionGranted()) {
                        chooseVideoFromGallary();
                    } else {
                        Toast.makeText(SendToFriendActivity.this, "Permission needed to access Gallery", Toast.LENGTH_SHORT).show();
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void chooseVideoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takeVideoFromCamera() {

        try {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            startActivityForResult(intent, CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("result", "" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERY || requestCode == CAMERA) {
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            decodableString = cursor.getString(columnIndex);
            cursor.close();
            mediatype = getContentResolver().getType(selectedVideo);
            sourceFile_sign = new File(decodableString);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.fromFile(sourceFile_sign));
            videoView.requestFocus();
            videoView.start();
        }

    }

    private void saveVideoToInternalStorage(String filePath) {

        File newfile;
        try {

            File currentFile = new File(filePath);
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + VIDEO_DIRECTORY);
            wallpaperDirectory.mkdirs();
            sourceFile_sign = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".mp4");


            if (currentFile.exists()) {

                InputStream in = new FileInputStream(currentFile);
                OutputStream out = new FileOutputStream(sourceFile_sign);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                Log.v("vii", "Video file saved successfully.");
            } else {
                Log.v("vii", "Video saving failed. Source file missing.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

      /*  if (camera_or_gallery.equals("0")) {

            try {

                File currentFile = new File(filePath);
                File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + VIDEO_DIRECTORY);
                sourceFile_sign = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".mp4");
                wallpaperDirectory.mkdirs();

                if (currentFile.exists()) {

                    InputStream in = new FileInputStream(currentFile);
                    OutputStream out = new FileOutputStream(sourceFile_sign);

                    // Copy the bits from instream to outstream
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                    Log.v("vii", "Video file saved successfully.");
                } else {
                    Log.v("vii", "Video saving failed. Source file missing.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {

                File currentFile = new File(filePath);
                File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + VIDEO_DIRECTORY);
                sourceFile_sign = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".mp4");
                wallpaperDirectory.mkdirs();

                if (currentFile.exists()) {

                    InputStream in = new FileInputStream(currentFile);
                    OutputStream out = new FileOutputStream(sourceFile_sign);

                    // Copy the bits from instream to outstream
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                    Log.v("vii", "Video file saved successfully.");
                } else {
                    Log.v("vii", "Video saving failed. Source file missing.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(SendToFriendActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SendToFriendActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SendToFriendActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                value = true;
            } else {
                ActivityCompat.requestPermissions(SendToFriendActivity.this, new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 200);
                value = false;
            }
        } else {
            value = true;
        }
        return value;
    }

    private void datePicker() {
        final Calendar c;
        c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(SendToFriendActivity.this, new DatePickerDialog.OnDateSetListener() {
            String month;

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                int m = monthOfYear + 1;

                if (m == 1) {
                    //month = getString(R.string.jaunuary);
                    month = "January";
                } else if (m == 2) {
                    //month = getString(R.string.february);
                    month = "February";
                } else if (m == 3) {
                    //month = getString(R.string.march);
                    month = "March";
                } else if (m == 4) {
                    //month = getString(R.string.april);
                    month = "April";
                } else if (m == 5) {
                    //month = getString(R.string.may);
                    month = "May";
                } else if (m == 6) {
//                            month = getString(R.string.june);
                    month = "June";
                } else if (m == 7) {
                    //month = getString(R.string.july);
                    month = "July";
                } else if (m == 8) {
                    //month = getString(R.string.august);
                    month = "August";
                } else if (m == 9) {
                    //month = getString(R.string.september);
                    month = "September";
                } else if (m == 10) {
                    //month = getString(R.string.octomer);
                    month = "October";
                } else if (m == 11) {
                    //month = getString(R.string.november);
                    month = "November";
                } else if (m == 12) {
                    //month = getString(R.string.december);
                    month = "December";
                }
                tv_sending_date.setText(year + "-" + m + "-" + dayOfMonth);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

}
