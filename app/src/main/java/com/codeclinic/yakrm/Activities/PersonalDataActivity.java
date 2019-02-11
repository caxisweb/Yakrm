package com.codeclinic.yakrm.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codeclinic.yakrm.Models.ProfileImageUpload;
import com.codeclinic.yakrm.Models.ProfileUpdateModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDataActivity extends AppCompatActivity {
    ImageView img_back;
    private final int CAMERA_IMAGE = 1;
    Button btn_modify_data;
    private final int PICK_IMAGE_GALLERY = 3;
    String str_mobile, str_email;
    RoundedImageView img_profile;
    TextView tv_mobile, tv_email, tv_username, tv_name, tv_change_pass;
    RelativeLayout rl_imgprofile;
    JSONObject jsonObject = new JSONObject();

    ProgressDialog progressDialog;

    SessionManager sessionManager;
    API apiService;
    String user_id, user_name, user_token, user_email, user_number, user_profile, user_country_id;

    Uri selectedImage;
    File sourceFile_sign, compressed_Image;
    String status1 = "0", message1 = "Try Again";
    boolean value;
    Compressor compressedImage;
    String str_email_regex = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public boolean isEmpty(CharSequence character) {
        return character == null || character.length() == 0;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder1.build());
        compressedImage = new Compressor(this);

        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(PersonalDataActivity.this);
        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        img_profile = findViewById(R.id.img_profile);
        rl_imgprofile = findViewById(R.id.rl_imgprofile);

        btn_modify_data = findViewById(R.id.btn_modify_data);
        apiService = RestClass.getClient().create(API.class);

        tv_mobile = findViewById(R.id.tv_mobile);
        tv_email = findViewById(R.id.tv_email);
        tv_username = findViewById(R.id.tv_username);
        tv_name = findViewById(R.id.tv_name);
        tv_change_pass = findViewById(R.id.tv_change_pass);

        tv_mobile.setText(sessionManager.getUserDetails().get(SessionManager.USER_MOBILE));
        tv_email.setText(sessionManager.getUserDetails().get(SessionManager.User_Email));
        tv_name.setText(sessionManager.getUserDetails().get(SessionManager.User_Name));
        if (!isEmpty(sessionManager.getUserDetails().get(SessionManager.USER_Profile))) {
            Picasso.with(this).load(ImageURL.profile_img_url + sessionManager.getUserDetails().get(SessionManager.USER_Profile)).into(img_profile);
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btn_modify_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String u_name = tv_name.getText().toString();
                String u_email = tv_email.getText().toString();
                String u_mobile = tv_mobile.getText().toString();

                if (isEmpty(u_name)) {
                    Toast.makeText(PersonalDataActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                } else if (u_name.length() < 3) {
                    Toast.makeText(PersonalDataActivity.this, "Name should be  minimum of 3 characters", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(u_email)) {
                    Toast.makeText(PersonalDataActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                } else if (!u_email.matches(str_email_regex)) {
                    Toast.makeText(PersonalDataActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(u_mobile)) {
                    Toast.makeText(PersonalDataActivity.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (u_mobile.length() < 10) {
                    Toast.makeText(PersonalDataActivity.this, "Mobile Number should be minimum of 10 characters ", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    try {
                        jsonObject.put("name", u_name);
                        jsonObject.put("email", u_email);
                        jsonObject.put("phone", u_mobile);
                        jsonObject.put("country_id", sessionManager.getUserDetails().get(SessionManager.USER_COUNTRY_ID));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<ProfileUpdateModel> profileUpdateModelCall = apiService.PROFILE_UPDATE_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                    profileUpdateModelCall.enqueue(new Callback<ProfileUpdateModel>() {
                        @Override
                        public void onResponse(Call<ProfileUpdateModel> call, Response<ProfileUpdateModel> response) {
                            progressDialog.dismiss();
                            final String status = response.body().getStatus();
                            if (status.equals("1")) {
                                user_id = sessionManager.getUserDetails().get(SessionManager.User_ID);
                                user_token = sessionManager.getUserDetails().get(SessionManager.User_Token);
                                user_country_id = sessionManager.getUserDetails().get(SessionManager.USER_COUNTRY_ID);
                                user_name = response.body().getName();
                                user_email = response.body().getEmail();
                                user_number = response.body().getPhone();
                                Toast.makeText(PersonalDataActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                if (sourceFile_sign != null) {
                                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), sourceFile_sign);
                                    MultipartBody.Part body = MultipartBody.Part.createFormData("user_profile", sourceFile_sign.getName(), reqFile);
                                    Call<ProfileImageUpload> profileImageUploadCall = apiService.PROFILE_IMAGE_UPLOAD_CALL(user_token, body);
                                    profileImageUploadCall.enqueue(new Callback<ProfileImageUpload>() {
                                        @Override
                                        public void onResponse(Call<ProfileImageUpload> call, Response<ProfileImageUpload> response) {
                                            if (status.equals("1")) {
                                                sessionManager.createLoginSession(user_token, user_id, user_name, user_email, user_number, user_country_id, response.body().getUserProfile());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ProfileImageUpload> call, Throwable t) {

                                        }
                                    });
                                } else {
                                    sessionManager.createLoginSession(user_token, user_id, user_name, user_email, user_number, user_country_id, "");
                                }
                                Toast.makeText(PersonalDataActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PersonalDataActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ProfileUpdateModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(PersonalDataActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        tv_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalDataActivity.this, ChangePasswordActivity.class));
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalDataActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    if (isPermissionGranted()) {
                        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Yakrm/";
                        File newdir = new File(dir);
                        newdir.mkdirs();
                        String file = dir + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString() + ".jpg";
                        File newfile = new File(file);
                        try {
                            newfile.createNewFile();
                        } catch (IOException ignored) {
                        }
                        selectedImage = Uri.fromFile(newfile);
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
                        startActivityForResult(cameraIntent, CAMERA_IMAGE);
                    } else {
                        Toast.makeText(PersonalDataActivity.this, "Permission needed to access Camera", Toast.LENGTH_SHORT).show();
                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    if (isPermissionGranted()) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, PICK_IMAGE_GALLERY);
                    } else {
                        Toast.makeText(PersonalDataActivity.this, "Permission needed to access Gallery", Toast.LENGTH_SHORT).show();
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == CAMERA_IMAGE) {
                CropImage.activity(selectedImage)
                        .setFixAspectRatio(true)
                        .start(this);
            } else if (requestCode == PICK_IMAGE_GALLERY) {
                selectedImage = data.getData();
                CropImage.activity(selectedImage)
                        .setFixAspectRatio(true)
                        .start(this);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                selectedImage = result.getUri();
                if (resultCode == RESULT_OK) {
                    sourceFile_sign = new File(selectedImage.getPath());
                    compressed_Image = compressedImage
                            .setMaxWidth(400)
                            .setMaxHeight(400)
                            .setQuality(50)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(sourceFile_sign);
                    sourceFile_sign = new File(compressed_Image.getPath());
                    InputStream in = null;
                    try {
                        final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
                        in = getContentResolver().openInputStream(selectedImage);
                        // Decode image size
                        BitmapFactory.Options o = new BitmapFactory.Options();
                        o.inJustDecodeBounds = true;
                        BitmapFactory.decodeStream(in, null, o);
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int scale = 1;
                        while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                                IMAGE_MAX_SIZE) {
                            scale++;
                        }
                        Bitmap b = null;
                        in = getContentResolver().openInputStream(selectedImage);
                        if (scale > 1) {
                            scale--;
                            // scale to max possible inSampleSize that still yields an image
                            // larger than target
                            o = new BitmapFactory.Options();
                            o.inSampleSize = scale;
                            b = BitmapFactory.decodeStream(in, null, o);

                            // resize to desired dimensions
                            int height = b.getHeight();
                            int width = b.getWidth();

                            double y = Math.sqrt(IMAGE_MAX_SIZE
                                    / (((double) width) / height));
                            double x = (y / height) * width;

                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                                    (int) y, true);
                            b.recycle();
                            b = scaledBitmap;

                            System.gc();
                        } else {
                            b = BitmapFactory.decodeStream(in);
                        }
                        in.close();
                        img_profile.setImageBitmap(b);
                    } catch (Exception ignored) {
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(PersonalDataActivity.this, "Image Can't be cropped", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(PersonalDataActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(PersonalDataActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(PersonalDataActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                value = true;
            } else {
                ActivityCompat.requestPermissions(PersonalDataActivity.this, new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 200);
                value = false;
            }
        } else {
            value = true;
        }
        return value;
    }

}
