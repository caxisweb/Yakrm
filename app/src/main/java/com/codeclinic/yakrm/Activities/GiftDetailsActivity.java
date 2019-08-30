package com.codeclinic.yakrm.Activities;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codeclinic.yakrm.Adapter.GiftDetailListAdapter;
import com.codeclinic.yakrm.BuildConfig;
import com.codeclinic.yakrm.LocalNotification.NotificationHelper;
import com.codeclinic.yakrm.Models.AddToFavouritesModel;
import com.codeclinic.yakrm.Models.VoucherDetailsListItemModel;
import com.codeclinic.yakrm.Models.VoucherDetailsListModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiftDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GiftDetailListAdapter giftDetailListAdapter;
    List<VoucherDetailsListItemModel> arrayList = new ArrayList<>();
    ArrayList<String> ar_add_cart_value = new ArrayList<>();
    API apiService;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    Button btn_complete;
    TextView tv_brand_name, tv_description, tv_brand_type, tv_more;
    ImageView img_brand_img, img_back, img_fav, img_share;
    String brand_id, token, is_fav;
    int status_expand = 0;

    LinearLayout bottom_layout;
    ArrayList<String> arrayList_type = new ArrayList<>();
    String brand_types;

    JSONObject jsonObject = new JSONObject();
    JSONObject jsonObject_fav = new JSONObject();

    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    int fav_value = 0;
    public int complete_purchase = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_details);

        tv_more = findViewById(R.id.tv_more);
        recyclerView = findViewById(R.id.recyclerView);
        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        img_fav = findViewById(R.id.img_fav);
        img_share = findViewById(R.id.img_share);
        img_brand_img = findViewById(R.id.img_brand_img);
        btn_complete = findViewById(R.id.btn_complete);
        bottom_layout = findViewById(R.id.bottom_layout);

        tv_brand_name = findViewById(R.id.tv_brand_name);
        tv_description = findViewById(R.id.tv_description);
        tv_brand_type = findViewById(R.id.tv_brand_type);

        sessionManager = new SessionManager(this);
        apiService = RestClass.getClient().create(API.class);
        progressDialog = new ProgressDialog(this);
        brand_id = getIntent().getStringExtra("brand_id");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.dot_light_intro))
                .sizeResId(R.dimen.divider)
                .build());


        if (Connection_Detector.isInternetAvailable(this)) {
            progressDialog.setMessage("Please Wait");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            try {
                jsonObject.put("brand_id", brand_id);
                if (sessionManager.isLoggedIn()) {
                    jsonObject.put("is_logged_in", "true");
                    token = sessionManager.getUserDetails().get(SessionManager.User_Token);
                } else {
                    jsonObject.put("is_logged_in", "false");
                    token = "";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Call<VoucherDetailsListModel> voucherDetailsListModelCall = apiService.VOUCHER_DETAILS_LIST_MODEL_CALL(token, jsonObject.toString());
            voucherDetailsListModelCall.enqueue(new Callback<VoucherDetailsListModel>() {
                @Override
                public void onResponse(Call<VoucherDetailsListModel> call, Response<VoucherDetailsListModel> response) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    if (status.equals("1")) {
                        Picasso.with(GiftDetailsActivity.this).load(ImageURL.Vendor_brand_image + response.body().getBrandImage()).into(img_brand_img);
                        tv_brand_name.setText(response.body().getBrandName());
                        tv_description.setText(response.body().getDescription());


                        arrayList = response.body().getData();
                        for (int j = 0; j < arrayList.size(); j++) {
                            if (arrayList_type.size() == 0) {
                                arrayList_type.add(arrayList.get(j).getVoucherType());
                                brand_types = arrayList.get(j).getVoucherType();
                            } else {
                                if (!arrayList_type.contains(arrayList.get(j).getVoucherType())) {
                                    arrayList_type.add(arrayList.get(j).getVoucherType());
                                    brand_types = brand_types + "," + arrayList.get(j).getVoucherType();
                                }
                            }
                        }
                        tv_brand_type.setText(brand_types);
                        if (sessionManager.isLoggedIn()) {
                            is_fav = response.body().getIsFavourite();
                            if (is_fav.equals("true")) {
                                img_fav.setImageDrawable(getResources().getDrawable(R.drawable.fav_filled));
                            }
                        }
                        if (arrayList != null) {
                            for (int i = 0; i < arrayList.size(); i++) {
                                ar_add_cart_value.add("0");
                            }
                        }
                        if (countChar(response.body().getDescription()) > 55) {
                            tv_more.setVisibility(View.VISIBLE);
                        } else {
                            tv_more.setVisibility(View.GONE);
                        }
                        giftDetailListAdapter = new GiftDetailListAdapter(arrayList, GiftDetailsActivity.this, apiService, ar_add_cart_value, sessionManager, complete_purchase);
                        recyclerView.setAdapter(giftDetailListAdapter);
                    } else {
                        Toast.makeText(GiftDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<VoucherDetailsListModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(GiftDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*final LayoutInflater inflater = getLayoutInflater();
                dialogBuilder = new AlertDialog.Builder(GiftDetailsActivity.this);
                final View dialogView = inflater.inflate(R.layout.custom_share_layout, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(true);

                ImageView img_facebook = dialogView.findViewById(R.id.img_facebook);
                ImageView img_twitter = dialogView.findViewById(R.id.img_twitter);
                ImageView img_sms = dialogView.findViewById(R.id.img_sms);
                ImageView img_mail = dialogView.findViewById(R.id.img_mail);
                ImageView img_contact = dialogView.findViewById(R.id.img_contact);

                img_facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mIntent = new Intent(Intent.ACTION_SEND);
                        mIntent.setType("text/plain");
                        mIntent.setPackage("com.facebook.katana");
                        mIntent.putExtra(Intent.EXTRA_TEXT, "Let me recommend you this application");
                        try {
                            startActivity(mIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(GiftDetailsActivity.this, "Facebook Not installed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alertDialog = dialogBuilder.create();
                alertDialog.show();*/
                doShareLink();
            }
        });

        if (!sessionManager.isLoggedIn()) {
            img_fav.setVisibility(View.GONE);
            bottom_layout.setVisibility(View.GONE);
        }

        img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    jsonObject_fav.put("brand_id", Integer.parseInt(brand_id));
                    if (is_fav.equals("true")) {
                        fav_value = 0;
                        jsonObject_fav.put("is_favourite", fav_value);
                        AlertDialog.Builder alert = new AlertDialog.Builder(GiftDetailsActivity.this, R.style.CustomDialogFragment);
                        alert.setMessage("Are you sure you want to remove from Favourites?");
                        alert.setCancelable(false);
                        alert.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @SuppressLint("StaticFieldLeak")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Call<AddToFavouritesModel> addToFavouritesModelCall = apiService.ADD_TO_FAVOURITES_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject_fav.toString());
                                addToFavouritesModelCall.enqueue(new Callback<AddToFavouritesModel>() {
                                    @Override
                                    public void onResponse(Call<AddToFavouritesModel> call, Response<AddToFavouritesModel> response) {
                                        String status = response.body().getStatus();
                                        if (status.equals("1")) {
                                            is_fav = "false";
                                            img_fav.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fav_icon));
                                            Toast.makeText(GiftDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(GiftDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<AddToFavouritesModel> call, Throwable t) {
                                        Toast.makeText(GiftDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();

                    } else {
                        fav_value = 1;
                        jsonObject_fav.put("is_favourite", fav_value);
                        Call<AddToFavouritesModel> addToFavouritesModelCall = apiService.ADD_TO_FAVOURITES_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject_fav.toString());
                        addToFavouritesModelCall.enqueue(new Callback<AddToFavouritesModel>() {
                            @Override
                            public void onResponse(Call<AddToFavouritesModel> call, Response<AddToFavouritesModel> response) {
                                String status = response.body().getStatus();
                                if (status.equals("1")) {
                                    is_fav = "true";
                                    img_fav.setImageDrawable(getResources().getDrawable(R.drawable.fav_filled));
                                    Toast.makeText(GiftDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(GiftDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<AddToFavouritesModel> call, Throwable t) {
                                Toast.makeText(GiftDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (complete_purchase == 1) {
                    Toasty.custom(GiftDetailsActivity.this, getResources().getString(R.string.Added_to_the_buying_basket_successfully), getResources().getDrawable(R.mipmap.ic_tick_inside), getResources().getColor(R.color.toast_color), 2000, true, true).show();
                    startActivity(new Intent(GiftDetailsActivity.this, CartActivity.class));
                    complete_purchase = 0;
                    sessionManager.setReminderStatus(true);
                    NotificationHelper.scheduleRepeatingElapsedNotification(getApplicationContext());
                    NotificationHelper.enableBootReceiver(getApplicationContext());
                } else {
                    Toast.makeText(GiftDetailsActivity.this, "Please add item to the cart", Toast.LENGTH_SHORT).show();
                }
            }
        });


        tv_more.setText(getResources().getString(R.string.More));
        status_expand = 0;
        CollapsedByMaxLines(tv_description, 2);

        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status_expand == 0) {
                    status_expand = 1;
                    expandByMaxLines(tv_description);
                    tv_more.setText(getResources().getString(R.string.Less));
                } else {
                    tv_more.setText(getResources().getString(R.string.More));
                    status_expand = 0;
                    CollapsedByMaxLines(tv_description, 2);
                }
            }
        });

    }


    public int countChar(String str) {
        int count = 0;

        for (int i = 0; i < str.length(); i++) {
            count++;
        }

        return count;
    }

    private void doShareLink() {
        String shareMessage = "Let me recommend you this application";
        String link = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        Intent chooserIntent = Intent.createChooser(shareIntent, "Share Via");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage + " " + link);
            Bundle bundle = new Bundle();
            bundle.putString(Intent.EXTRA_TEXT, link);
            Bundle replacement = new Bundle();
            chooserIntent.putExtra(Intent.EXTRA_REPLACEMENT_EXTRAS, replacement);
        } else {
            shareIntent.putExtra(Intent.EXTRA_TEXT, link);
        }
        chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(chooserIntent);
    }

    @SuppressLint("Range")
    public void expandByMaxLines(@NonNull final TextView text) {
        final int height = text.getMeasuredHeight();
        text.setHeight(height);
        text.setMaxLines(Integer.MAX_VALUE); //expand fully
        text.measure(View.MeasureSpec.makeMeasureSpec(text.getMeasuredWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, View.MeasureSpec.UNSPECIFIED));
        final int newHeight = text.getMeasuredHeight();
        ObjectAnimator animation = ObjectAnimator.ofInt(text, "height", height, newHeight);
        animation.setDuration(250).start();
    }

    @SuppressLint("Range")
    public void CollapsedByMaxLines(@NonNull final TextView text, int line) {
        final int height = text.getMeasuredHeight();
        text.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        text.setMaxLines(line); //expand fully
        text.measure(View.MeasureSpec.makeMeasureSpec(text.getMeasuredWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, View.MeasureSpec.UNSPECIFIED));
        final int newHeight = text.getMeasuredHeight();
        ObjectAnimator animation = ObjectAnimator.ofInt(text, "maxLines", line);
        animation.setDuration(250).start();
    }

}
