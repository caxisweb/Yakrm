package com.codeclinic.yakrm.Fragments;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.codeclinic.yakrm.Activities.MainActivity;
import com.codeclinic.yakrm.Adapter.BuyTabListAdapter;
import com.codeclinic.yakrm.Adapter.CategoryListAdapter;
import com.codeclinic.yakrm.Adapter.FilterListAdapter;
import com.codeclinic.yakrm.Adapter.SlidingImage_Adapter;
import com.codeclinic.yakrm.Models.AllVoucherListItemModel;
import com.codeclinic.yakrm.Models.AllVouchersListModel;
import com.codeclinic.yakrm.Models.FilterListItemModel;
import com.codeclinic.yakrm.Models.FilterListModel;
import com.codeclinic.yakrm.Models.GiftCategoryBannersModel;
import com.codeclinic.yakrm.Models.GiftCategoryModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.CubeOutRotationTransformation;
import com.codeclinic.yakrm.Utils.GridSpacingItemDecoration;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.codeclinic.yakrm.Activities.MainActivity.cat_arrayList_id;
import static com.codeclinic.yakrm.Activities.MainActivity.cat_arrayList_name;
import static com.codeclinic.yakrm.Activities.MainActivity.category_classification_array;


/**
 *
 */
public class BuyTabFragment extends Fragment {

    RecyclerView recyclerView, recyclerViewCategory;
    BuyTabListAdapter buyTabListAdapter;
    FilterListAdapter filterListAdapter;
    ArrayList<GiftCategoryModel> categoryArrayList = new ArrayList<>();
    final Integer[] IMAGES = {R.drawable.demo_img_1, R.drawable.demo_img_1, R.drawable.demo_img_1, R.drawable.demo_img_1};
    List<AllVoucherListItemModel> arrayList = new ArrayList<>();
    List<FilterListItemModel> arrayList_filter = new ArrayList<>();
    API apiService;
    ProgressDialog progressDialog;
    LinearLayout layout_filter;
    SessionManager sessionManager;
    String login_flag = "0";
    JSONObject jsonObject = new JSONObject();
    ImageView imgBanner;
    List<GiftCategoryBannersModel> giftCategoryBannerArrayList = new ArrayList<>();
    RelativeLayout rl_pager;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private ViewPager mPager;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    Handler handler;
    Runnable Update;
    Timer swipeTimer;
    TimerTask task;

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.demo_img_1, R.drawable.demo_img_1, R.drawable.demo_img_1};
    private int current_Page;
    private int currentPage = 0;

    public BuyTabFragment() {
        // Required empty public constructor
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Picasso.with(getActivity()).load(ImageURL.gift_category_banner + categoryArrayList.get(position).getGift_category_banner()).error(getActivity().getResources().getDrawable(R.drawable.card_details_item_background)).into(imageView);
        }
    };
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            currentPage = position;
            addBottomDots();

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_tab, container, false);
        setRetainInstance(true);
        sessionManager = new SessionManager(getActivity());
        layout_filter = view.findViewById(R.id.layout_filter);
        dotsLayout = view.findViewById(R.id.layoutDots);
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);

        mPager = view.findViewById(R.id.image_pager);
        mPager.setPageTransformer(true, new CubeOutRotationTransformation());
        mPager.addOnPageChangeListener(viewPagerPageChangeListener);

        imgBanner = view.findViewById(R.id.imgBanner);
        rl_pager = view.findViewById(R.id.rl_pager);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerViewCategory = view.findViewById(R.id.recyclerViewCategory);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recyclerViewCategory.setHasFixedSize(true);
        apiService = RestClass.getClient().create(API.class);

        carouselView = view.findViewById(R.id.carouselView);


        progressDialog = new ProgressDialog(getActivity());
        if (sessionManager.isLoggedIn()) {
            login_flag = "1";
        }
        progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setIcon(getActivity().getResources().getDrawable(R.drawable.small_logo_icon));

        if (Connection_Detector.isInternetAvailable(getActivity())) {
            callBuyVoucherListAPI();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.No_Internet_Connection), Toast.LENGTH_SHORT).show();
        }


        layout_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.drawer.isDrawerVisible(GravityCompat.END)) {
                    MainActivity.drawer.closeDrawer(GravityCompat.END);
                } else {
                    MainActivity.drawer.openDrawer(GravityCompat.END);
                }
            }
        });

        return view;
    }

    private void addBottomDots() {
        dots = new TextView[giftCategoryBannerArrayList.size()];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);

    }

    private void animatePagerTransition(final boolean forward) {

        ValueAnimator animator = ValueAnimator.ofInt(0, mPager.getWidth() - (forward ? mPager.getPaddingLeft() : mPager.getPaddingRight()));
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mPager.endFakeDrag();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mPager.endFakeDrag();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private int oldDragPosition = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int dragPosition = (Integer) animation.getAnimatedValue();
                int dragOffset = dragPosition - oldDragPosition;
                oldDragPosition = dragPosition;
                mPager.fakeDragBy(dragOffset * (forward ? -1 : 1));
            }
        });

        animator.setDuration(5000);
        mPager.beginFakeDrag();
        animator.start();
    }

    private void setTimer() {
        try {
            handler = new Handler();
            Update = new Runnable() {
                public void run() {
                    if (currentPage == giftCategoryBannerArrayList.size()) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, false);
                }
            };

            scheduleTimerTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scheduleTimerTask() {
        swipeTimer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        };
        swipeTimer.schedule(task, 3000, 3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (swipeTimer != null) {
            scheduleTimerTask();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        swipeTimer.cancel();
        task.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(Update);
        handler = null;
        Update = null;
        swipeTimer.cancel();
    }

    public void callBuyVoucherListAPI() {
        progressDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("is_login", login_flag);
            Log.i("token",sessionManager.getUserDetails().get(SessionManager.User_Token));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<AllVouchersListModel> allVouchersListModelCall = apiService.ALL_VOUCHERS_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
        allVouchersListModelCall.enqueue(new Callback<AllVouchersListModel>() {
            @Override
            public void onResponse(Call<AllVouchersListModel> call, final Response<AllVouchersListModel> response) {
                progressDialog.dismiss();
                int status = response.body().getStatus();
                if (status == 1) {
                    if (login_flag.equals("0")) {
                        MainActivity.textCartItemCount.setVisibility(View.GONE);
                    } else {
                        MainActivity.textCartItemCount.setText(response.body().getTotal_cart_item());
                    }
                    arrayList = response.body().getData();
                    cat_arrayList_id.clear();
                    cat_arrayList_name.clear();
                    category_classification_array.clear();
                    if (categoryArrayList.size() == 0) {

                        categoryArrayList = (ArrayList<GiftCategoryModel>) response.body().getGiftCategory();
                        giftCategoryBannerArrayList = response.body().getGiftCategory().get(0).getGiftCategoryBanners();
                        if(giftCategoryBannerArrayList.size()>0) {
                            rl_pager.setVisibility(View.GONE);
                            currentPage = 0;
                            addBottomDots();
                            setTimer();
                            mPager.setAdapter(new SlidingImage_Adapter(getActivity(), response.body().getGiftCategory().get(0).getGiftCategoryBanners()));
                            recyclerViewCategory.setAdapter(new CategoryListAdapter(response.body().getGiftCategory(), getActivity(), recyclerViewCategory, new CategoryListAdapter.itemClickListener() {
                                @SuppressLint("NewApi")
                                @Override
                                public void itemClick(final int position) {
                                    if (position != -1) {
                                        category_classification_array.clear();
                                        category_classification_array.add(categoryArrayList.get(position).getGiftCategoryName());
                                        ((MainActivity) getActivity()).callFilterAPI();
                                        //imgBanner.setVisibility(View.VISIBLE);
                                        //Picasso.with(getActivity()).load(ImageURL.gift_category_banner + categoryArrayList.get(position).getGift_category_banner()).error(getActivity().getResources().getDrawable(R.drawable.card_details_item_background)).into(imgBanner);
                                        giftCategoryBannerArrayList = response.body().getGiftCategory().get(position).getGiftCategoryBanners();
                                        currentPage = 0;
                                        addBottomDots();
                                        rl_pager.setVisibility(View.VISIBLE);
                                        if (giftCategoryBannerArrayList.size() == 0) {
                                            rl_pager.setVisibility(View.GONE);
                                        }
                                        mPager.setAdapter(new SlidingImage_Adapter(getActivity(), response.body().getGiftCategory().get(position).getGiftCategoryBanners()));
                                    } else {
                                        category_classification_array.clear();
                                        ((MainActivity) getActivity()).removeFilter();
                                        //imgBanner.setVisibility(View.GONE);
                                        mPager.setAdapter(new SlidingImage_Adapter(getActivity(), response.body().getGiftCategory().get(0).getGiftCategoryBanners()));
                                        giftCategoryBannerArrayList = response.body().getGiftCategory().get(0).getGiftCategoryBanners();
                                        currentPage = 0;
                                        addBottomDots();
                                        rl_pager.setVisibility(View.GONE);
                                        if (giftCategoryBannerArrayList.size() == 0) {
                                            rl_pager.setVisibility(View.GONE);
                                        }
                                    }

                                }
                            }));
                        }
                        MainActivity.btn = new Button[categoryArrayList.size()];
                        for (int i = 0; i < categoryArrayList.size(); i++) {
                            MainActivity.btn[i] = new Button(getActivity());
                            final float scale = getResources().getDisplayMetrics().density;
                            int pixels_height = (int) (30 * scale + 0.5f);
                            int pixels_padding = (int) (10 * scale + 0.5f);
                            MainActivity.btn[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, pixels_height));
                            MainActivity.btn[i].setId(i);
                            MainActivity.btn[i].setPadding(pixels_padding, 0, pixels_padding, 0);
                            MainActivity.btn[i].setGravity(Gravity.CENTER);
                            MainActivity.btn[i].setBackground(getResources().getDrawable(R.drawable.flow_layout_text_background));
                            MainActivity.btn[i].setTextColor(getResources().getColor(R.color.white));
                            MainActivity.btn[i].setTextSize(10);
                            if (sessionManager.getLanguage("Language", "en").equals("en")) {
                                MainActivity.btn[i].setText(categoryArrayList.get(i).getGiftCategoryName());
                            } else {
                                MainActivity.btn[i].setText(categoryArrayList.get(i).getArabGiftCategoryName());
                            }
                            MainActivity.btn[i].setTextColor(getResources().getColor(R.color.black));

                            final Handler mHandler = new Handler();
                            MainActivity.btn[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Button button = (Button) view;
                                    boolean pressed = false;
                                    if (view.getTag() instanceof Boolean) {
                                        pressed = (boolean) view.getTag();
                                    }
                                    final boolean newPressed = !pressed;

                                    if (newPressed) {
                                        button.setTextColor(getResources().getColor(R.color.white));
                                        button.setBackground(getResources().getDrawable(R.drawable.flow_layout_pressed_background));
                                        category_classification_array.add(button.getText().toString());
                                    } else {
                                        button.setTextColor(getResources().getColor(R.color.black));
                                        button.setBackground(getResources().getDrawable(R.drawable.flow_layout_text_background));
                                        category_classification_array.remove(button.getText().toString());
                                    }
                                    // setTag to store state
                                    view.setTag(newPressed);
                                    final View vRun = view;
                                    Runnable run = new Runnable() {
                                        @Override
                                        public void run() {
                                            vRun.setPressed(newPressed);
                                        }
                                    };
                                    mHandler.post(run);
                                    mHandler.postDelayed(run, 100);

                                }
                            });
                            MainActivity.flowLayout.addView(MainActivity.btn[i]);
                            if (!sessionManager.getLanguage("Language", "en").equals("en")) {
                                MainActivity.flowLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                            }
                        }

                    }

                    for (int k = 0; k < categoryArrayList.size(); k++) {
                        String language = String.valueOf(getResources().getConfiguration().locale);
                        cat_arrayList_id.add(categoryArrayList.get(k).getId());
                        if (language.equals("ar")) {
                            MainActivity.cat_arrayList_name.add(categoryArrayList.get(k).getArabGiftCategoryName());
                        } else {
                            MainActivity.cat_arrayList_name.add(categoryArrayList.get(k).getGiftCategoryName());
                        }

                    }

                    int spanCount = 2; // 3 columns
                    int spacing = 10; // 50px
                    boolean includeEdge = false;
                    recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    recyclerView.setNestedScrollingEnabled(false);
                    buyTabListAdapter = new BuyTabListAdapter(arrayList, getActivity());
                    recyclerViewCategory.setNestedScrollingEnabled(false);
                    recyclerView.setAdapter(buyTabListAdapter);
                } else {
                    //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AllVouchersListModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), getResources().getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void callFilterCategoriesAPI() {
        progressDialog.show();
        try {
            jsonObject.put("gift_category_id", MainActivity.gift_category_id);
            jsonObject.put("gift_type", MainActivity.gift_type);
            jsonObject.put("gift_order", MainActivity.gift_order);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<FilterListModel> filterListModelCall = apiService.FILTER_LIST_MODEL_CALL(jsonObject.toString());
        filterListModelCall.enqueue(new Callback<FilterListModel>() {
            @Override
            public void onResponse(Call<FilterListModel> call, Response<FilterListModel> response) {
                progressDialog.dismiss();
                String status = response.body().getStatus();
                if (status.equals("1")) {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setNestedScrollingEnabled(false);
                    arrayList_filter = response.body().getData();
                    filterListAdapter = new FilterListAdapter(arrayList_filter, getActivity());
                    recyclerView.setAdapter(filterListAdapter);
                } else {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    callBuyVoucherListAPI();
                    MainActivity.filter_array = 0;
                }
            }

            @Override
            public void onFailure(Call<FilterListModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), getResources().getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
