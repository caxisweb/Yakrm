package com.codeclinic.yakrm.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codeclinic.yakrm.Models.FriendMobileNumberModel;
import com.codeclinic.yakrm.Models.SendVoucherToFriendModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.CameraFragmentApi;
import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.listeners.CameraFragmentControlsAdapter;
import com.github.florent37.camerafragment.listeners.CameraFragmentResultAdapter;
import com.github.florent37.camerafragment.listeners.CameraFragmentStateAdapter;
import com.github.florent37.camerafragment.listeners.CameraFragmentVideoRecordTextAdapter;
import com.github.florent37.camerafragment.widgets.CameraSettingsView;
import com.github.florent37.camerafragment.widgets.CameraSwitchView;
import com.github.florent37.camerafragment.widgets.FlashSwitchView;
import com.github.florent37.camerafragment.widgets.MediaActionSwitchView;
import com.github.florent37.camerafragment.widgets.RecordButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.zelory.compressor.Compressor;
import life.knowledge4.videotrimmer.utils.FileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendToFriendActivity extends AppCompatActivity {

    public static final String FRAGMENT_TAG = "camera";
    static final String EXTRA_VIDEO_PATH = "Video";
    private static final int REQUEST_VIDEO_TRIMMER = 0x01;
    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
    public static File sourceFile_sign;
    public static CountDownTimer countDownTimer;
    Button btn_complete;
    ImageView img_back, img_voucher, img_search;
    EditText edt_mobile, ed_description;
    TextView tv_itemname, tv_price, tv_name, tv_email, tv_sending_date, tv_add_video, recordDurationText, recordSizeText;
    MediaActionSwitchView mediaActionSwitchView;
    CameraSwitchView cameraSwitchView;
    RelativeLayout cameraLayout;
    RecordButton recordButton;
    CameraSettingsView settingsView;
    FlashSwitchView flashSwitchView;
    LinearLayout main_llayout;
    FrameLayout content;
    API apiService;
    JSONObject jsonObject_mobile = new JSONObject();
    JSONObject jsonObject = new JSONObject();
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    String mobile_number;
    boolean value;
    Compressor compressedImage;
    MultipartBody.Part body = null;

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


        btn_complete = findViewById(R.id.btn_complete);
        cameraLayout = findViewById(R.id.cameraLayout);
        recordButton = findViewById(R.id.record_button);
        recordDurationText = findViewById(R.id.record_duration_text);
        mediaActionSwitchView = findViewById(R.id.photo_video_camera_switcher);
        cameraSwitchView = findViewById(R.id.front_back_camera_switcher);
        recordSizeText = findViewById(R.id.record_size_mb_text);
        flashSwitchView = findViewById(R.id.flash_switch_view);
        settingsView = findViewById(R.id.settings_view);
        main_llayout = findViewById(R.id.main_llayout);
        content = findViewById(R.id.content);

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


        selectTodaysDate();
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
                // selectImage();
                if (Build.VERSION.SDK_INT > 16) {
                    final String[] permissions = {
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE};

                    final List<String> permissionsToRequest = new ArrayList<>();
                    for (String permission : permissions) {
                        if (ActivityCompat.checkSelfPermission(SendToFriendActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                            permissionsToRequest.add(permission);
                        }
                    }
                    if (!permissionsToRequest.isEmpty()) {
                        ActivityCompat.requestPermissions(SendToFriendActivity.this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CAMERA_PERMISSIONS);
                    } else {
                        //Toast.makeText(SendToFriendActivity.this, "Permission needed to proceed", Toast.LENGTH_SHORT).show();
                        selectImage();
                    }
                } else {
                    selectImage();
                }
            }
        });

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connection_Detector.isInternetAvailable(SendToFriendActivity.this)) {
                    if (isEmpty(mobile_number)) {
                        Toast.makeText(SendToFriendActivity.this, "Please verify your friends mobile number", Toast.LENGTH_SHORT).show();
                    } else if (isEmpty(tv_sending_date.getText().toString())) {
                        Toast.makeText(SendToFriendActivity.this, "Please Select Date", Toast.LENGTH_SHORT).show();
                    } else if (isEmpty(ed_description.getText().toString())) {
                        Toast.makeText(SendToFriendActivity.this, "Please Enter Description", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.setMessage("Please Wait");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        RequestBody voucherID = RequestBody.create(MediaType.parse("text/plain"), VoucherDetailActivity.voucher_id);
                        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), mobile_number);
                        RequestBody date = RequestBody.create(MediaType.parse("text/plain"), tv_sending_date.getText().toString());
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
                        Call<SendVoucherToFriendModel> sendVoucherToFriendModelCall = apiService.SEND_VOUCHER_TO_FRIEND_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), voucherID, mobile, description, date, voucher_payment_id, scan_v_type, body);
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

        flashSwitchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CameraFragmentApi cameraFragment = getCameraFragment();
                if (cameraFragment != null) {
                    cameraFragment.toggleFlashMode();
                }
            }
        });

        cameraSwitchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CameraFragmentApi cameraFragment = getCameraFragment();
                if (cameraFragment != null) {
                    cameraFragment.switchCameraTypeFrontBack();
                }
            }
        });
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                } else {
                    RunUpdateLoop_second();
                }
                final CameraFragmentApi cameraFragment = getCameraFragment();
                if (cameraFragment != null) {
                    cameraFragment.takePhotoOrCaptureVideo(new CameraFragmentResultAdapter() {
                                                               @Override
                                                               public void onVideoRecorded(String filePath) {
                                                                   Toast.makeText(getBaseContext(), "onVideoRecorded " + filePath, Toast.LENGTH_SHORT).show();
                                                                   sourceFile_sign = new File(filePath);
                                                                   main_llayout.setVisibility(View.VISIBLE);
                                                              /*     videoView.setVisibility(View.VISIBLE);
                                                                   videoView.setVideoPath(filePath);
                                                                   videoView.start();*/
                                                                   cameraLayout.setVisibility(View.GONE);
                                                                   content.setVisibility(View.GONE);
                                                               }

                                                               @Override
                                                               public void onPhotoTaken(byte[] bytes, String filePath) {
                                                                   Toast.makeText(getBaseContext(), "onPhotoTaken " + filePath, Toast.LENGTH_SHORT).show();
                                                               }
                                                           },
                            Environment.getExternalStorageDirectory() + "/Video/",
                            String.valueOf(Calendar.getInstance().getTimeInMillis()));
                }
            }
        });

        settingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CameraFragmentApi cameraFragment = getCameraFragment();
                if (cameraFragment != null) {
                    cameraFragment.openSettingDialog();
                }
            }
        });

        mediaActionSwitchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CameraFragmentApi cameraFragment = getCameraFragment();
                if (cameraFragment != null) {
                    cameraFragment.switchActionPhotoVideo();
                }
            }
        });


    }

    private void pickFromGallery() {
        Intent intent = new Intent();
        intent.setTypeAndNormalize("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_video)), REQUEST_VIDEO_TRIMMER);
    }

    public void RunUpdateLoop_second() {
        countDownTimer = new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;

            }

            public void onFinish() {
                recordButton.callOnClick();
            }
        }.start();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            addCamera();
        }
    }

    public void addCamera() {
        cameraSwitchView.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        main_llayout.setVisibility(View.GONE);
        cameraLayout.setVisibility(View.VISIBLE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        final CameraFragment cameraFragment = CameraFragment.newInstance(new Configuration.Builder()
                .setCamera(Configuration.CAMERA_FACE_REAR).build());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, cameraFragment, FRAGMENT_TAG)
                .commitAllowingStateLoss();

        if (cameraFragment != null) {

            cameraFragment.setStateListener(new CameraFragmentStateAdapter() {

                @Override
                public void onCurrentCameraBack() {
                    cameraSwitchView.displayBackCamera();
                }

                @Override
                public void onCurrentCameraFront() {
                    cameraSwitchView.displayFrontCamera();
                }

                @Override
                public void onFlashAuto() {
                    flashSwitchView.displayFlashAuto();
                }

                @Override
                public void onFlashOn() {
                    flashSwitchView.displayFlashOn();
                }

                @Override
                public void onFlashOff() {
                    flashSwitchView.displayFlashOff();
                }

                @Override
                public void onCameraSetupForPhoto() {
                    mediaActionSwitchView.displayActionWillSwitchVideo();

                    recordButton.displayPhotoState();
                    flashSwitchView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCameraSetupForVideo() {
                    mediaActionSwitchView.displayActionWillSwitchPhoto();

                    recordButton.displayVideoRecordStateReady();
                    flashSwitchView.setVisibility(View.GONE);
                }

                @Override
                public void shouldRotateControls(int degrees) {
                    ViewCompat.setRotation(cameraSwitchView, degrees);
                    ViewCompat.setRotation(mediaActionSwitchView, degrees);
                    ViewCompat.setRotation(flashSwitchView, degrees);
                    ViewCompat.setRotation(recordDurationText, degrees);
                    ViewCompat.setRotation(recordSizeText, degrees);
                }

                @Override
                public void onRecordStateVideoReadyForRecord() {
                    recordButton.displayVideoRecordStateReady();
                }

                @Override
                public void onRecordStateVideoInProgress() {

                    recordButton.displayVideoRecordStateInProgress();
                }

                @Override
                public void onRecordStatePhoto() {
                    recordButton.displayPhotoState();
                }

                @Override
                public void onStopVideoRecord() {
                    recordSizeText.setVisibility(View.GONE);
                    //cameraSwitchView.setVisibility(View.VISIBLE);
                    settingsView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onStartVideoRecord(File outputFile) {
                    RunUpdateLoop_second();
                    cameraSwitchView.setVisibility(View.GONE);
                }
            });

            cameraFragment.setControlsListener(new CameraFragmentControlsAdapter() {
                @Override
                public void lockControls() {
                    cameraSwitchView.setEnabled(false);
                    recordButton.setEnabled(false);
                    settingsView.setEnabled(false);
                    flashSwitchView.setEnabled(false);
                }

                @Override
                public void unLockControls() {
                    cameraSwitchView.setEnabled(true);
                    recordButton.setEnabled(true);
                    settingsView.setEnabled(true);
                    flashSwitchView.setEnabled(true);
                }

                @Override
                public void allowCameraSwitching(boolean allow) {
                    cameraSwitchView.setVisibility(allow ? View.VISIBLE : View.GONE);
                }

                @Override
                public void allowRecord(boolean allow) {
                    recordButton.setEnabled(allow);
                }

                @Override
                public void setMediaActionSwitchVisible(boolean visible) {
                    mediaActionSwitchView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                }
            });

            cameraFragment.setTextListener(new CameraFragmentVideoRecordTextAdapter() {
                @Override
                public void setRecordSizeText(long size, String text) {
                    recordSizeText.setText(text);
                }

                @Override
                public void setRecordSizeTextVisible(boolean visible) {
                    recordSizeText.setVisibility(visible ? View.VISIBLE : View.GONE);
                }

                @Override
                public void setRecordDurationText(String text) {
                    recordDurationText.setText(text);
                }

                @Override
                public void setRecordDurationTextVisible(boolean visible) {
                    recordDurationText.setVisibility(visible ? View.VISIBLE : View.GONE);
                }
            });
        }
    }

    private CameraFragmentApi getCameraFragment() {
        return (CameraFragmentApi) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    private void selectImage() {
        final CharSequence[] options = {getResources().getString(R.string.recordvideofromcamera), getResources().getString(R.string.selectvideofromgallery), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(SendToFriendActivity.this);
        builder.setTitle(getResources().getString(R.string.addvideo));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getResources().getString(R.string.recordvideofromcamera))) {
                    if (isPermissionGranted()) {
                        addCamera();
                    } else {
                        Toast.makeText(SendToFriendActivity.this, "Permission needed to access Camera", Toast.LENGTH_SHORT).show();
                    }
                } else if (options[item].equals(getResources().getString(R.string.selectvideofromgallery))) {
                    if (isPermissionGranted()) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(SendToFriendActivity.this, "Permission needed to access Gallery", Toast.LENGTH_SHORT).show();
                    }
                } else if (options[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void startTrimActivity(@NonNull Uri uri) {
        Intent intent = new Intent(this, TrimmerActivity.class);
        intent.putExtra(EXTRA_VIDEO_PATH, FileUtils.getPath(this, uri));
        startActivity(intent);
    }

    public void selectTodaysDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        int m = mMonth + 1;

        tv_sending_date.setText(mYear + "-" + m + "-" + mDay);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("result", "" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == REQUEST_VIDEO_TRIMMER) {
            final Uri selectedUri = data.getData();
            if (selectedUri != null) {
                startTrimActivity(selectedUri);
            } else {
                Toast.makeText(SendToFriendActivity.this, R.string.toast_cannot_retrieve_selected_video, Toast.LENGTH_SHORT).show();
            }
        }

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
