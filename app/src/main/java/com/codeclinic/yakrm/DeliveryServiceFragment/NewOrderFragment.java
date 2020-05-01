package com.codeclinic.yakrm.DeliveryServiceFragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.se.omapi.Session;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.codeclinic.yakrm.Activities.LoginActivity;
import com.codeclinic.yakrm.DeliveryModel.ImageUploadModel;
import com.codeclinic.yakrm.DeliveryModel.PlaceOrderModel;
import com.codeclinic.yakrm.DeliveryService.OrderDetailActivity;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.codeclinic.yakrm.Utils.UserDeliveryInfo;
import com.schibstedspain.leku.LocationPickerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.TIME_ZONE_DISPLAY_NAME;
import static com.schibstedspain.leku.LocationPickerActivityKt.TIME_ZONE_ID;
import static com.schibstedspain.leku.LocationPickerActivityKt.TRANSITION_BUNDLE;
import static com.schibstedspain.leku.LocationPickerActivityKt.ZIPCODE;

public class NewOrderFragment extends Fragment {

    SessionManager sessionManager;
    ProgressDialog progressDialog;
    API apiService;

    private View mainView;

    private int MAP_BUTTON_REQUEST_CODE = 1;
    private int MAP_LOCATION_OPTION = 1;//1 for Pickup and 2 for Destination

    private final int CAMERA_IMAGE = 1;
    private final int PICK_IMAGE_GALLERY = 3;
    Uri selectedImage;
    File sourceFile;
    boolean value;

    CardView btn_addproduct, btn_home_address, btn_shop_address,btn_upload_photo,card_image;
    LinearLayout lv_productlist,lv_order;
    EditText edt_pname, edt_pqty,edt_notes;
    EditText tv_home_address, tv_shop_address;
    ImageView img_product,img_delete;

    ArrayList<String> product_name = new ArrayList<>();
    ArrayList<String> product_qty = new ArrayList<>();

    String order_id;

    String str_home_address="null";
    double str_home_lat=0, str_home_long=0;
    String str_notes;
    String str_store_address="non";
    double str_store_lat=0, str_store_long=0;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.new_order_fragment, null);

        StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder1.build());

        sessionManager=new SessionManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        apiService = RestClass.getClientDelivery().create(API.class);

        lv_productlist = mainView.findViewById(R.id.lv_productlist);

        btn_addproduct = mainView.findViewById(R.id.btn_add_product);
        btn_upload_photo = mainView.findViewById(R.id.btn_upload_pic);
        btn_home_address = mainView.findViewById(R.id.btn_home_address);
        btn_shop_address = mainView.findViewById(R.id.btn_shop_address);

        card_image = mainView.findViewById(R.id.card_image);
        img_product=mainView.findViewById(R.id.img_product);
        img_delete=mainView.findViewById(R.id.img_delete);

        edt_pname = mainView.findViewById(R.id.edt_product);
        edt_pqty = mainView.findViewById(R.id.edt_quantity);
        edt_notes = mainView.findViewById(R.id.edt_notes);

        tv_home_address = mainView.findViewById(R.id.tv_home_address);
        tv_shop_address = mainView.findViewById(R.id.tv_shop_address);

        lv_order = mainView.findViewById(R.id.lv_order);

        btn_addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    String name = edt_pname.getText().toString().trim();
                    String qty = edt_pqty.getText().toString().trim();

                    if (name.equals("")) {
                        edt_pname.setError("Please enter Product name");
                    } else if (qty.equals("")) {
                        edt_pqty.setError("Please enter Quantity");
                    } else if (qty.equals("0")) {
                        edt_pqty.setError("0 Quantity is not acceptable");
                    } else {

                        edt_pqty.setText("");
                        edt_pname.setText("");

                        final View custLayout = getActivity().getLayoutInflater().inflate(R.layout.custome_product_view, null);

                        TextView tv_product = (TextView) custLayout.findViewById(R.id.tv_productname);
                        TextView tv_qty = (TextView) custLayout.findViewById(R.id.tv_qty);
                        ImageView img_remove = (ImageView) custLayout.findViewById(R.id.img_remove);

                        tv_product.setText(name);
                        tv_qty.setText(qty);

                        img_remove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {

                                    product_name.remove(lv_productlist.indexOfChild(custLayout));
                                    product_qty.remove(lv_productlist.indexOfChild(custLayout));
                                    lv_productlist.removeView(custLayout);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        product_name.add(name);
                        product_qty.add(qty);

                        lv_productlist.addView(custLayout);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        });

        btn_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                card_image.setVisibility(View.GONE);
                sourceFile=null;
            }
        });

        btn_home_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MAP_LOCATION_OPTION = 1;
                    openMapPicker();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_shop_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    MAP_LOCATION_OPTION = 2;
                    openMapPicker();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        lv_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_notes=edt_notes.getText().toString().trim();
                str_home_address=tv_home_address.getText().toString().trim();
                str_store_address=tv_shop_address.getText().toString().trim();

                if(product_name.size()==0){
                    Toast.makeText(getActivity(),"Please Add Product",Toast.LENGTH_LONG).show();
                }else if(str_home_lat==0){
                    Toast.makeText(getActivity(),"Please select Delivery address",Toast.LENGTH_LONG).show();
                }else{

                    try {

                        progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        JSONObject data=new JSONObject();
                        data.put("user_address",str_home_address);
                        data.put("user_latitude",str_home_lat);
                        data.put("user_longitude",str_home_long);

                        data.put("shop_address",str_store_address);
                        data.put("shop_latitude",str_store_lat);
                        data.put("shop_longitude",str_store_long);

                        JSONArray productlist = new JSONArray();

                        for(int i=0;i<product_name.size();i++){

                            JSONObject products=new JSONObject();
                            products.put("product_title",product_name.get(i));
                            products.put("quantity",product_qty.get(i));
                            productlist.put(products);
                        }

                        data.put("order_detail",productlist);
                        data.put("notes",str_notes);

                        Log.i("order_data",data.toString());

                        Call<PlaceOrderModel> placeOrder=apiService.placeOrder(sessionManager.getUserDetails().get(SessionManager.User_Token),data.toString());
                        placeOrder.enqueue(new Callback<PlaceOrderModel>() {
                            @Override
                            public void onResponse(Call<PlaceOrderModel> call, Response<PlaceOrderModel> response) {

                                if(response.body().getStatus().equals("1")){

                                    if(sourceFile==null) {

                                        progressDialog.dismiss();

                                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                                        tv_home_address.setText("");
                                        tv_shop_address.setText("");
                                        lv_productlist.removeAllViews();
                                        edt_notes.setText("");
                                        str_home_address = "null";
                                        str_store_address = "null";
                                        str_home_lat = 0;
                                        str_home_long = 0;
                                        str_store_lat = 0;
                                        str_store_long = 0;

                                        Intent i_detail = new Intent(getActivity(), OrderDetailActivity.class);
                                        i_detail.putExtra("order_id", response.body().getOrder_id());
                                        startActivity(i_detail);

                                    }else{

                                        order_id=response.body().getOrder_id();
                                        callImageUpload();
                                    }

                                }else{
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PlaceOrderModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(),"server error",Toast.LENGTH_LONG).show();
                            }
                        });

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });

        return mainView;
    }

    public void openMapPicker() {
        Intent locationPickerIntent = new LocationPickerActivity.Builder().withLocation(UserDeliveryInfo.latitude, UserDeliveryInfo.longitude)
                .withGeolocApiKey("AIzaSyDuio1SeutMjrH3YpZux-J7ahjolmQohrM")
                .withGooglePlacesEnabled()
                .withGoogleTimeZoneEnabled()
                .withUnnamedRoadHidden()
                .build(getActivity())
                .putExtra("test", "this is a test");
        startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE);
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    if (isPermissionGranted()) {
                        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/RapidVets/";
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
                        Toast.makeText(getActivity(), "Permission needed to access Camera", Toast.LENGTH_SHORT).show();
                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    if (isPermissionGranted()) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, PICK_IMAGE_GALLERY);
                    } else {
                        Toast.makeText(getActivity(), "Permission needed to access Gallery", Toast.LENGTH_SHORT).show();
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                value = true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 200);
                value = false;
            }
        } else {
            value = true;
        }
        return value;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(getActivity(), "You have to allow all the permissions to access content from camera and gallery", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RESULT****", "OK");
            if (requestCode == 1) {

                if (MAP_LOCATION_OPTION == 1) {
                    str_home_lat = data.getDoubleExtra(LATITUDE, 0.0);
                    tv_home_address.setEnabled(true);
                } else {
                    str_store_lat = data.getDoubleExtra(LATITUDE, 0.0);
                    tv_shop_address.setEnabled(true);
                }


                if (MAP_LOCATION_OPTION == 1) {
                    str_home_long = data.getDoubleExtra(LONGITUDE, 0.0);
                } else {
                    str_store_long = data.getDoubleExtra(LONGITUDE, 0.0);
                }

                String address = data.getStringExtra(LOCATION_ADDRESS);
                String postalcode = data.getStringExtra(ZIPCODE);

                Bundle bundle = data.getBundleExtra(TRANSITION_BUNDLE);

                String timeZoneId = data.getStringExtra(TIME_ZONE_ID);
                if (timeZoneId != null) {
                }
                String timeZoneDisplayName = data.getStringExtra(TIME_ZONE_DISPLAY_NAME);
                if (timeZoneDisplayName != null) {
                }
                if (MAP_LOCATION_OPTION == 1) {
                    str_home_address = address;
                    tv_home_address.setText(address);
                } else {
                    str_store_address = address;
                    tv_shop_address.setText(address);
                }
            } else {

                if (requestCode == PICK_IMAGE_GALLERY) {
                    selectedImage = data.getData();
                    sourceFile = new File(getPath(selectedImage));
                } else if (requestCode == CAMERA_IMAGE) {
                    sourceFile = new File(selectedImage.getPath());
                }

                InputStream in = null;

                try {

                    final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
                    in = getActivity().getContentResolver().openInputStream(selectedImage);
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
                    in = getActivity().getContentResolver().openInputStream(selectedImage);
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

                    card_image.setVisibility(View.VISIBLE);
                    img_product.setImageBitmap(b);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("RESULT****", "CANCELLED");
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        getActivity().startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void callImageUpload() {

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), sourceFile);
        RequestBody order_id1 = RequestBody.create(MediaType.parse("text/plain"), order_id);
        MultipartBody.Part body = MultipartBody.Part.createFormData("order_image", sourceFile.getName(), reqFile);

        Call<ImageUploadModel> uploadModelCall = apiService.uploadProduct(sessionManager.getUserDetails().get(SessionManager.User_Token),order_id1, body);
        uploadModelCall.enqueue(new Callback<ImageUploadModel>() {
            @Override
            public void onResponse(Call<ImageUploadModel> call, Response<ImageUploadModel> response) {

                progressDialog.dismiss();

                if (response.body().getStatus().equals("1")) {

                    tv_home_address.setText("");
                    tv_shop_address.setText("");
                    lv_productlist.removeAllViews();
                    edt_notes.setText("");
                    str_home_address = "null";
                    str_store_address = "null";
                    str_home_lat = 0;
                    str_home_long = 0;
                    str_store_lat = 0;
                    str_store_long = 0;

                    Intent i_detail = new Intent(getActivity(), OrderDetailActivity.class);
                    i_detail.putExtra("order_id", order_id);
                    startActivity(i_detail);

                } else {

                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ImageUploadModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Server Error Please Try Again!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
