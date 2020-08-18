package com.codeclinic.yakrm.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.codeclinic.yakrm.BuildConfig;
import com.codeclinic.yakrm.DeliveryService.DeliveryMain;
import com.codeclinic.yakrm.Fragments.BuyTabFragment;
import com.codeclinic.yakrm.Fragments.SupportContactFragment;
import com.codeclinic.yakrm.LocalNotification.NotificationHelper;
import com.codeclinic.yakrm.Models.AllVouchersListModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.CommonMethods;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.franmontiel.localechanger.LocaleChanger;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static DrawerLayout drawer;
    public static ViewPager viewPager;
    public static TextView textCartItemCount;
    public static ArrayList<String> cat_arrayList_id = new ArrayList<>();
    public static ArrayList<String> cat_arrayList_name = new ArrayList<>();
    public static Button[] btn;
    public static NavigationView navigationView2;
    public static FlowLayout flowLayout;
    public static int back_flag = 0, filter_array = 0;
    public static String gift_category_id = "", gift_type = "", gift_order = "";
    public static ArrayList<String> category_classification_array = new ArrayList<>();

    RelativeLayout rl_buy;
    LinearLayout llayout_recieved, llayout_replace, llayout_my_wallet;

    BuyTabFragment buyTabFragment = new BuyTabFragment();


    API apiService;
    int i;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    Drawable drawable;
    LinearLayout llayout_tab;
    CoordinatorLayout main_content;
    String str_cart_count = "0";
    SessionManager sessionManager;
    private TabLayout tabLayout;

    CheckBox chk_e_gift;
    CheckBox chk_p_gift;

    RadioButton rb_m_popular;
    RadioButton rb_high_discounted;
    RadioButton rb_brand_names;

    Button btn_filter;
    Button btn_delete;

    public boolean isEmpty(CharSequence character) {
        return character == null || character.length() == 0;
    }

    @SuppressLint({"ClickableViewAccessibility", "ResourceType", "NewApi", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rl_buy = findViewById(R.id.rl_buy);
        llayout_recieved = findViewById(R.id.llayout_recieved);
        llayout_replace = findViewById(R.id.llayout_replace);
        llayout_my_wallet = findViewById(R.id.llayout_my_wallet);


        drawer = findViewById(R.id.drawer_layout);
        main_content = findViewById(R.id.main_content);
        llayout_tab = findViewById(R.id.llayout_tab);

        sessionManager = new SessionManager(this);

        apiService = RestClass.getClient().create(API.class);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(1);

        tabLayout = findViewById(R.id.tablayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                String language = String.valueOf(getResources().getConfiguration().locale);
                if (language.equals("en")) {
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        main_content.setTranslationX(slideX);
                    } else {
                        main_content.setTranslationX(-slideX);
                    }
                } else if (language.equals("en_GB")) {
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        main_content.setTranslationX(slideX);
                    } else {
                        main_content.setTranslationX(-slideX);
                    }
                } else if (language.equals("en_")) {
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        main_content.setTranslationX(slideX);
                    } else {
                        main_content.setTranslationX(-slideX);
                    }
                } else if (language.equals("en_001")) {
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        main_content.setTranslationX(slideX);
                    } else {
                        main_content.setTranslationX(-slideX);
                    }
                } else if (language.equals("en_IN")) {
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        main_content.setTranslationX(slideX);
                    } else {
                        main_content.setTranslationX(-slideX);
                    }
                } else if (language.equals("en_US")) {
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        main_content.setTranslationX(slideX);
                    } else {
                        main_content.setTranslationX(-slideX);
                    }
                } else if (language.equals("en_150")) {
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        main_content.setTranslationX(slideX);
                    } else {
                        main_content.setTranslationX(-slideX);
                    }
                } else {
                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        main_content.setTranslationX(-slideX);
                    } else {
                        main_content.setTranslationX(slideX);
                    }
                }
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);

        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("en")) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_english_menu_icon, getTheme());
        } else if (language.equals("en_GB")) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_english_menu_icon, getTheme());
        } else if (language.equals("en_")) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_english_menu_icon, getTheme());
        } else if (language.equals("en_001")) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_english_menu_icon, getTheme());
        } else if (language.equals("en_IN")) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_english_menu_icon, getTheme());
        } else if (language.equals("en_US")) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_english_menu_icon, getTheme());
        } else if (language.equals("en_150")) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_english_menu_icon, getTheme());
        } else {
            drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_small_menu_icon, getTheme());
        }

        toggle.setHomeAsUpIndicator(drawable);
        toggle.syncState();
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView2 = findViewById(R.id.nav_view2);
        View header2 = navigationView2.getHeaderView(0);
        View header1 = navigationView.getHeaderView(0);
        flowLayout = header2.findViewById(R.id.main_flow_layout);
        LinearLayout llayout_main_page = header1.findViewById(R.id.llayout_main_page);
        LinearLayout layout_personal_account = header1.findViewById(R.id.layout_personal_account);
        LinearLayout llayout_fav_voucher = header1.findViewById(R.id.llayout_fav_voucher);
        LinearLayout llayout_active_voucher = header1.findViewById(R.id.llayout_active_voucher);
        LinearLayout llayout_best_brands = header1.findViewById(R.id.llayout_best_brands);
        LinearLayout llayout_finance_records = header1.findViewById(R.id.llayout_finance_records);
        LinearLayout llayout_auctions = header1.findViewById(R.id.llayout_auctions);
        LinearLayout llayout_payment_method = header1.findViewById(R.id.llayout_payment_method);
        LinearLayout llayout_support_contact = header1.findViewById(R.id.llayout_support_contact);
        LinearLayout llayout_about_app = header1.findViewById(R.id.llayout_about_app);
        LinearLayout llayout_instruction_conditions = header1.findViewById(R.id.llayout_instruction_conditions);
        LinearLayout llayout_signout = header1.findViewById(R.id.llayout_signout);
        LinearLayout llayout_share = header1.findViewById(R.id.llayout_share);
        TextView tv_signout = header1.findViewById(R.id.tv_signout);

        chk_e_gift = header2.findViewById(R.id.chk_e_gift);
        chk_p_gift = header2.findViewById(R.id.chk_p_gift);

        rb_m_popular = header2.findViewById(R.id.rb_m_popular);
        rb_high_discounted = header2.findViewById(R.id.rb_high_discounted);
        rb_brand_names = header2.findViewById(R.id.rb_brand_names);

        btn_filter = header2.findViewById(R.id.btn_filter);
        btn_delete = header2.findViewById(R.id.btn_delete);


        if (!sessionManager.isLoggedIn()) {
            tv_signout.setText(getResources().getString(R.string.Signup) + " / " + getResources().getString(R.string.Log_in));
        }
        LinearLayout llayout_english = header1.findViewById(R.id.llayout_english);
        final TextView tv_language_version = header1.findViewById(R.id.tv_language_version);

        navigationView.setNavigationItemSelectedListener(this);


        llayout_main_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                /*llayout_tab.setVisibility(View.VISIBLE);
                if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                    setTitle(getResources().getString(R.string.title_activity_main));
                } else {
                    viewPager.setCurrentItem(0);
                }*/

                finish();

            }
        });

        layout_personal_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                llayout_tab.setVisibility(View.VISIBLE);
                if (sessionManager.isLoggedIn()) {
                    if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                        findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                        setTitle(getResources().getString(R.string.title_activity_main));
                    }
                    startActivity(new Intent(MainActivity.this, PersonalDataActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, StartActivity.class));
                }
            }
        });

        llayout_fav_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                llayout_tab.setVisibility(View.VISIBLE);
                if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                    setTitle(getResources().getString(R.string.title_activity_main));
                }
                if (sessionManager.isLoggedIn()) {
                    startActivity(new Intent(MainActivity.this, FavouritesActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, StartActivity.class));
                }
            }
        });

        llayout_active_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* drawer.closeDrawer(GravityCompat.START);
                llayout_tab.setVisibility(View.VISIBLE);
                if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                    setTitle(getResources().getString(R.string.title_activity_main));
                }
                viewPager.setCurrentItem(2);*/
                if (sessionManager.isLoggedIn()) {
                    startActivity(new Intent(MainActivity.this, AllActiveVouchersActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, StartActivity.class));
                }
            }
        });

        llayout_best_brands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                llayout_tab.setVisibility(View.VISIBLE);
                if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                    setTitle(getResources().getString(R.string.title_activity_main));
                }
                startActivity(new Intent(MainActivity.this, FavouriteVouchersActivity.class));
            }
        });

        llayout_finance_records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                llayout_tab.setVisibility(View.VISIBLE);
                if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                    setTitle(getResources().getString(R.string.title_activity_main));
                }
                if (sessionManager.isLoggedIn()) {
                    startActivity(new Intent(MainActivity.this, FinancialTransactionsRecordActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, StartActivity.class));
                }
            }
        });

        llayout_auctions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                llayout_tab.setVisibility(View.VISIBLE);
                if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                    setTitle(getResources().getString(R.string.title_activity_main));
                }
                //viewPager.setCurrentItem(3);
            }
        });


        llayout_payment_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                llayout_tab.setVisibility(View.VISIBLE);
                if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                    setTitle(getResources().getString(R.string.title_activity_main));
                }
                if (sessionManager.isLoggedIn()) {
                    startActivity(new Intent(MainActivity.this, EnterCardDetailsActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, StartActivity.class));
                }

            }
        });

        llayout_support_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLoggedIn()) {
                    drawer.closeDrawer(GravityCompat.START);
                    llayout_tab.setVisibility(View.GONE);
                    findViewById(R.id.frame_contaner).setVisibility(View.VISIBLE);
                    toolbar.setTitle(getResources().getString(R.string.Support_And_Contact));
                    SupportContactFragment fragment = null;
                    fragment = new SupportContactFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_contaner, fragment).commit();
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });

        llayout_about_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                llayout_tab.setVisibility(View.VISIBLE);
                if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                    setTitle(getResources().getString(R.string.title_activity_main));
                }
                startActivity(new Intent(MainActivity.this, AboutApplicationActivity.class));
            }
        });

        llayout_instruction_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                llayout_tab.setVisibility(View.VISIBLE);
                if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                    setTitle(getResources().getString(R.string.title_activity_main));
                }
                startActivity(new Intent(MainActivity.this, ExcahangeInstructionsActivity.class));
            }
        });

        llayout_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLoggedIn()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this, R.style.CustomDialogFragment);
                    alert.setMessage(getResources().getString(R.string.AreYouSureToLogout));
                    alert.setCancelable(false);
                    alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @SuppressLint("StaticFieldLeak")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            drawer.closeDrawer(GravityCompat.START);
                            llayout_tab.setVisibility(View.VISIBLE);
                            if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                                findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                                setTitle(getResources().getString(R.string.title_activity_main));
                            }

                            sessionManager.setReminderStatus(false);
                            NotificationHelper.cancelAlarmElapsed();
                            NotificationHelper.disableBootReceiver(getApplicationContext());

                            sessionManager.logoutUser();
                            finish();
                        }
                    }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                } else {
                    drawer.closeDrawer(GravityCompat.START);
                    llayout_tab.setVisibility(View.VISIBLE);
                    if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                        findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                        setTitle(getResources().getString(R.string.title_activity_main));
                    }

                    sessionManager.logoutUser();
                    finish();
                }
            }
        });

        llayout_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.closeDrawer(GravityCompat.START);
                llayout_tab.setVisibility(View.VISIBLE);
                if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                    setTitle(getResources().getString(R.string.title_activity_main));
                }
                String language_name = "";
                if (tv_language_version.getText().equals("النسخة العربية")) {
                    language_name = "ar";
                } else {
                    language_name = "en";
                }
                sessionManager.putLanguage("Langauage", language_name);
                if (Build.VERSION.SDK_INT > 25) {
                    if (language_name.equals("ar")) {
                        LocaleChanger.setLocale(CommonMethods.SUPPORTED_LOCALES.get(1));
                    } else {
                        LocaleChanger.setLocale(CommonMethods.SUPPORTED_LOCALES.get(0));
                    }
                    ActivityRecreationHelper.recreate(MainActivity.this, true);
                } else {

                    Locale locale = new Locale(language_name);

                    Resources resources = getResources();
                    Configuration configuration = resources.getConfiguration();
                    DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        configuration.setLocale(locale);
                    } else {
                        configuration.locale = locale;
                    }

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                        getApplicationContext().createConfigurationContext(configuration);
                    } else {
                        resources.updateConfiguration(configuration, displayMetrics);
                    }

                    finish();
                    startActivity(new Intent(getApplicationContext(), DeliveryMain.class));
                }

            }
        });

        llayout_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doShareLink();
            }
        });

        if (tv_language_version.getText().equals("النسخة العربية")) {
            setTitle("Main");
        } else {
            setTitle("الأساسية");
        }


        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFilterAPI();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFilter();
            }
        });

        setupViewPager(viewPager);
        if (getIntent().hasExtra("view_pos")) {
            viewPager.setCurrentItem(Integer.parseInt(getIntent().getStringExtra("view_pos")));
        }

        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();
        navigationView2.setNavigationItemSelectedListener(this);


        llayout_recieved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecievedActivity.class));
            }
        });
        llayout_replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReplaceActiveVouchersActivity.class));
            }
        });
        llayout_my_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyWalletActivity.class));
            }
        });


    }

    public void removeFilter() {
        flowLayout.removeAllViews();
        chk_e_gift.setChecked(false);
        chk_p_gift.setChecked(false);
        rb_m_popular.setChecked(false);
        rb_high_discounted.setChecked(false);
        rb_brand_names.setChecked(false);
        gift_order = "";
        gift_category_id = "";
        gift_type = "";
        filter_array = 0;
        drawer.closeDrawer(GravityCompat.END);
        buyTabFragment.callBuyVoucherListAPI();
    }

    public void callFilterAPI() {
        gift_category_id = "";
        if (category_classification_array != null) {
            for (int i = 0; i < category_classification_array.size(); i++) {
                int pos = cat_arrayList_name.indexOf(category_classification_array.get(i));
                gift_category_id = gift_category_id + cat_arrayList_id.get(pos) + ",";
            }
        } else {
            gift_category_id = "";
        }
        try {
            gift_category_id = gift_category_id.substring(0, gift_category_id.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (chk_e_gift.isChecked() && chk_p_gift.isChecked()) {
            gift_type = "paper gift,electronic gift";
        } else if (chk_e_gift.isChecked()) {
            gift_type = "electronic gift";
        } else if (chk_p_gift.isChecked()) {
            gift_type = "paper gift";
        } else {
            gift_type = "";
        }

        if (rb_m_popular.isChecked()) {
            gift_order = "0";
        } else if (rb_high_discounted.isChecked()) {
            gift_order = "2";
        } else if (rb_brand_names.isChecked()) {
            gift_order = "3";
        } else {
            gift_order = "";
        }

        /*    if (!isEmpty(gift_category_id) && !isEmpty(gift_type)) {*/
        if (!isEmpty(gift_category_id) || !isEmpty(gift_type) || !isEmpty(gift_order)) {
            filter_array = 1;
            drawer.closeDrawer(GravityCompat.END);
        /*    setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            createTabIcons();*/
            buyTabFragment.callFilterCategoriesAPI();

        } else {

            Toast.makeText(MainActivity.this, getResources().getString(R.string.Please_Select_atleast_one_filter_category_from_above), Toast.LENGTH_SHORT).show();
        }
    }


    private void createTabIcons() {

        final View view1 = getLayoutInflater().inflate(R.layout.custom_tab_view, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.mipmap.ic_buytab_icon);
        TextView tv_item1 = view1.findViewById(R.id.tv_item);
        tv_item1.setText(getResources().getString(R.string.Buy));
        tabLayout.getTabAt(0).setCustomView(view1);

    /*    final View view2 = getLayoutInflater().inflate(R.layout.custom_tab_view, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.mipmap.ic_recievedtab_icon);
        TextView tv_item2 = view2.findViewById(R.id.tv_item);
        tv_item2.setText(getResources().getString(R.string.Received));
        tabLayout.getTabAt(1).setCustomView(view2);

        final View view3 = getLayoutInflater().inflate(R.layout.custom_tab_view, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.mipmap.ic_replacetab_icon);
        TextView tv_item3 = view3.findViewById(R.id.tv_item);
        tv_item3.setText(getResources().getString(R.string.Replace));
        tabLayout.getTabAt(2).setCustomView(view3);

        *//*final View view4 = getLayoutInflater().inflate(R.layout.custom_tab_view, null);
        view4.findViewById(R.id.icon).setBackgroundResource(R.mipmap.ic_auctionetab_icon);
        TextView tv_item4 = view4.findViewById(R.id.tv_item);
        tv_item4.setText(getResources().getString(R.string.Auction));
        tabLayout.getTabAt(3).setCustomView(view4);*//*

        final View view5 = getLayoutInflater().inflate(R.layout.custom_tab_view, null);
        view5.findViewById(R.id.icon).setBackgroundResource(R.mipmap.ic_wallettab_icon);
        TextView tv_item5 = view5.findViewById(R.id.tv_item);
        tv_item5.setText(getResources().getString(R.string.My_Wallet));
        tabLayout.getTabAt(3).setCustomView(view5);*/

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(buyTabFragment, getResources().getString(R.string.Buy));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onDestroy() {
        ActivityRecreationHelper.onDestroy(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_flag == 0) {
                super.onBackPressed();
                filter_array = 0;
                finish();
            } else {
                back_flag = 0;
                setTitle(getResources().getString(R.string.title_activity_main));
                llayout_tab.setVisibility(View.VISIBLE);
                findViewById(R.id.frame_contaner).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            final LayoutInflater inflater = getLayoutInflater();
            dialogBuilder = new AlertDialog.Builder(MainActivity.this);
            final View dialogView = inflater.inflate(R.layout.custom_search_layout, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setCancelable(true);

            final ImageView img_search = dialogView.findViewById(R.id.img_search);
            final EditText edt_search = dialogView.findViewById(R.id.edt_search);

            alertDialog = dialogBuilder.create();
            WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
            wmlp.gravity = Gravity.TOP | Gravity.CENTER;
            wmlp.y = 200;
            alertDialog.show();
            img_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEmpty(edt_search.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Enter Search Item", Toast.LENGTH_SHORT).show();
                    } else {
                        alertDialog.dismiss();
                        Intent intent = new Intent(MainActivity.this, SearchedVoucherActivity.class);
                        intent.putExtra("search", edt_search.getText().toString());
                        startActivity(intent);
                    }
                }
            });

            edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_SEARCH)) {

                        if (isEmpty(edt_search.getText().toString())) {
                            Toast.makeText(MainActivity.this, "Enter Search Item", Toast.LENGTH_SHORT).show();
                        } else {
                            alertDialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, SearchedVoucherActivity.class);
                            intent.putExtra("search", edt_search.getText().toString());
                            startActivity(intent);
                        }

                        return true;

                    }
                    return false;
                }
            });

            return true;
        } else if (id == R.id.action_fav) {
            if (sessionManager.isLoggedIn()) {
                startActivity(new Intent(MainActivity.this, FavouritesActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, StartActivity.class));
            }
            return true;
        } else if (id == R.id.action_notification) {
            if (sessionManager.isLoggedIn()) {
                startActivity(new Intent(MainActivity.this, NotificationsActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, StartActivity.class));
            }
            return true;
        } else if (id == R.id.action_basket) {
            if (sessionManager.isLoggedIn()) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, StartActivity.class));
            }
            return true;
        } else if (id == R.id.action_user) {
            if (sessionManager.isLoggedIn()) {
                startActivity(new Intent(MainActivity.this, PersonalDataActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, StartActivity.class));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        try {
            final MenuItem menuItem1 = menu.findItem(R.id.action_search);
            final MenuItem menuItem2 = menu.findItem(R.id.action_fav);
            final MenuItem menuItem3 = menu.findItem(R.id.action_notification);
            final MenuItem menuItem4 = menu.findItem(R.id.action_basket);

            final MenuItem menuItem5 = menu.findItem(R.id.action_user);

            View actionView1 = MenuItemCompat.getActionView(menuItem1);
            View actionView2 = MenuItemCompat.getActionView(menuItem2);
            View actionView3 = MenuItemCompat.getActionView(menuItem3);
            View actionView4 = MenuItemCompat.getActionView(menuItem4);
            textCartItemCount = actionView4.findViewById(R.id.cart_badge);
            View actionView5 = MenuItemCompat.getActionView(menuItem5);

            actionView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onOptionsItemSelected(menuItem1);
                }
            });

            actionView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onOptionsItemSelected(menuItem2);
                }
            });

            actionView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onOptionsItemSelected(menuItem3);
                }
            });

            actionView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onOptionsItemSelected(menuItem4);
                }
            });
            actionView5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onOptionsItemSelected(menuItem5);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onResume() {
        super.onResume();
        ActivityRecreationHelper.onResume(this);
        if (sessionManager.isLoggedIn()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("is_login", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Call<AllVouchersListModel> allVouchersListModelCall = apiService.ALL_VOUCHERS_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
            allVouchersListModelCall.enqueue(new Callback<AllVouchersListModel>() {
                @Override
                public void onResponse(Call<AllVouchersListModel> call, Response<AllVouchersListModel> response) {
//                        Log.i("user_token", sessionManager.getUserDetails().get(SessionManager.User_Token));
                    int status = response.body().getStatus();
                    if (status == 1) {
                        str_cart_count = response.body().getTotal_cart_item();
                        textCartItemCount.setText(str_cart_count);
                    }
                }

                @Override
                public void onFailure(Call<AllVouchersListModel> call, Throwable t) {
                }
            });

            if (Integer.parseInt(str_cart_count) > 0) {
                if (!sessionManager.getReminderStatus()) {
                    sessionManager.setReminderStatus(true);
                    NotificationHelper.scheduleRepeatingElapsedNotification(getApplicationContext());
                    NotificationHelper.enableBootReceiver(getApplicationContext());
                }
            } else {
                sessionManager.setReminderStatus(false);
                NotificationHelper.cancelAlarmElapsed();
                NotificationHelper.disableBootReceiver(getApplicationContext());
            }
        } else {
            sessionManager.setReminderStatus(false);
            NotificationHelper.cancelAlarmElapsed();
            NotificationHelper.disableBootReceiver(getApplicationContext());
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
