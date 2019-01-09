package com.yakrm.codeclinic.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nex3z.flowlayout.FlowLayout;
import com.yakrm.codeclinic.Fragments.AuctionTabFragment;
import com.yakrm.codeclinic.Fragments.BuyTabFragment;
import com.yakrm.codeclinic.Fragments.MyWalletTabFragment;
import com.yakrm.codeclinic.Fragments.RecievedTabFragment;
import com.yakrm.codeclinic.Fragments.ReplaceTabFragment;
import com.yakrm.codeclinic.Fragments.SupportContactFragment;
import com.yakrm.codeclinic.R;
import com.yakrm.codeclinic.Utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static DrawerLayout drawer;
    public static ViewPager viewPager;

    ArrayList<String> arrayList = new ArrayList<>();
    Button btn[];
    int i;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;

    private TabLayout tabLayout;
    Drawable drawable;

    public static int back_flag = 0;
    LinearLayout llayout_tab;
    CoordinatorLayout main_content;

    SessionManager sessionManager;

    @SuppressLint({"ClickableViewAccessibility", "ResourceType", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        main_content = findViewById(R.id.main_content);
        llayout_tab = findViewById(R.id.llayout_tab);

        sessionManager = new SessionManager(this);


        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        if (getIntent().hasExtra("view_pos")) {
            viewPager.setCurrentItem(Integer.parseInt(getIntent().getStringExtra("view_pos")));
        }

        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();


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
        NavigationView navigationView2 = findViewById(R.id.nav_view2);
        View header2 = navigationView2.getHeaderView(0);
        View header1 = navigationView.getHeaderView(0);
        FlowLayout flowLayout = header2.findViewById(R.id.main_flow_layout);
        LinearLayout llayout_main_page = header1.findViewById(R.id.llayout_main_page);
        LinearLayout layout_personal_account = header1.findViewById(R.id.layout_personal_account);
        LinearLayout llayout_fav_voucher = header1.findViewById(R.id.llayout_fav_voucher);
        LinearLayout llayout_active_voucher = header1.findViewById(R.id.llayout_active_voucher);
        LinearLayout llayout_best_brands = header1.findViewById(R.id.llayout_best_brands);
        LinearLayout llayout_finance_records = header1.findViewById(R.id.llayout_finance_records);
        LinearLayout llayout_auctions = header1.findViewById(R.id.llayout_auctions);
        LinearLayout llayout_support_contact = header1.findViewById(R.id.llayout_support_contact);
        LinearLayout llayout_about_app = header1.findViewById(R.id.llayout_about_app);
        LinearLayout llayout_instruction_conditions = header1.findViewById(R.id.llayout_instruction_conditions);
        LinearLayout llayout_signout = header1.findViewById(R.id.llayout_signout);
        LinearLayout llayout_english = header1.findViewById(R.id.llayout_english);

        arrayList.add(getResources().getString(R.string.Cuisine));
        arrayList.add(getResources().getString(R.string.books_and_magazines));
        arrayList.add(getResources().getString(R.string.coffee));
        arrayList.add(getResources().getString(R.string.Stores_and_groceries));
        arrayList.add(getResources().getString(R.string.children_toys));
        arrayList.add(getResources().getString(R.string.fashion_and_uniforms));
        arrayList.add(getResources().getString(R.string.Care_and_makeup_centers));
        arrayList.add(getResources().getString(R.string.Hotels_and_tourism));
        arrayList.add(getResources().getString(R.string.Sport_clothes));
        arrayList.add(getResources().getString(R.string.phones_and_electronics));
        arrayList.add(getResources().getString(R.string.sport_tools));
        arrayList.add(getResources().getString(R.string.Clothes));
        arrayList.add(getResources().getString(R.string.jewelries_and_golden));
        arrayList.add(getResources().getString(R.string.others));

        btn = new Button[arrayList.size()];
        for (i = 0; i < arrayList.size(); i++) {
            btn[i] = new Button(this);
            final float scale = getResources().getDisplayMetrics().density;
            int pixels_height = (int) (30 * scale + 0.5f);
            int pixels_padding = (int) (10 * scale + 0.5f);
            btn[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, pixels_height));
            btn[i].setId(i);
            btn[i].setPadding(pixels_padding, 0, pixels_padding, 0);
            btn[i].setGravity(Gravity.CENTER);
            btn[i].setBackground(getResources().getDrawable(R.drawable.flow_layout_text_background));
            btn[i].setTextColor(getResources().getColor(R.color.white));
            btn[i].setTextSize(10);
            btn[i].setText(arrayList.get(i));
            btn[i].setTextColor(getResources().getColor(R.color.black));

            final Handler mHandler = new Handler();
            btn[i].setOnClickListener(new View.OnClickListener() {
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
                    } else {
                        button.setTextColor(getResources().getColor(R.color.black));
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
            flowLayout.addView(btn[i]);
        }

        navigationView.setNavigationItemSelectedListener(this);
        navigationView2.setNavigationItemSelectedListener(this);

        llayout_main_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                llayout_tab.setVisibility(View.VISIBLE);
                if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                    setTitle(getResources().getString(R.string.title_activity_main));
                }


            }
        });

        layout_personal_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                llayout_tab.setVisibility(View.VISIBLE);
                if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                    setTitle(getResources().getString(R.string.title_activity_main));
                }
                startActivity(new Intent(MainActivity.this, PersonalDataActivity.class));
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
                startActivity(new Intent(MainActivity.this, FavouritesActivity.class));
            }
        });

        llayout_active_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                llayout_tab.setVisibility(View.VISIBLE);
                if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                    setTitle(getResources().getString(R.string.title_activity_main));
                }
                viewPager.setCurrentItem(2);
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
                startActivity(new Intent(MainActivity.this, FinancialTransactionsRecordActivity.class));
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
                viewPager.setCurrentItem(3);
            }
        });


        llayout_support_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                llayout_tab.setVisibility(View.GONE);
                findViewById(R.id.frame_contaner).setVisibility(View.VISIBLE);
                toolbar.setTitle(getResources().getString(R.string.Support_And_Contact));
                SupportContactFragment fragment = null;
                fragment = new SupportContactFragment();
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_contaner, fragment).commit();
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
                drawer.closeDrawer(GravityCompat.START);
                llayout_tab.setVisibility(View.VISIBLE);
                if (findViewById(R.id.frame_contaner).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.frame_contaner).setVisibility(View.GONE);
                    setTitle(getResources().getString(R.string.title_activity_main));
                }
                sessionManager.logoutUser();
                finish();
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
                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                setTitle(getResources().getString(R.string.title_activity_main));
            }
        });


    }


    private void createTabIcons() {

        final View view1 = getLayoutInflater().inflate(R.layout.custom_tab_view, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.mipmap.ic_buytab_icon);
        TextView tv_item1 = view1.findViewById(R.id.tv_item);
        tv_item1.setText(getResources().getString(R.string.Buy));
        tabLayout.getTabAt(0).setCustomView(view1);

        final View view2 = getLayoutInflater().inflate(R.layout.custom_tab_view, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.mipmap.ic_recievedtab_icon);
        TextView tv_item2 = view2.findViewById(R.id.tv_item);
        tv_item2.setText(getResources().getString(R.string.Received));
        tabLayout.getTabAt(1).setCustomView(view2);

        final View view3 = getLayoutInflater().inflate(R.layout.custom_tab_view, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.mipmap.ic_replacetab_icon);
        TextView tv_item3 = view3.findViewById(R.id.tv_item);
        tv_item3.setText(getResources().getString(R.string.Replace));
        tabLayout.getTabAt(2).setCustomView(view3);

        final View view4 = getLayoutInflater().inflate(R.layout.custom_tab_view, null);
        view4.findViewById(R.id.icon).setBackgroundResource(R.mipmap.ic_auctionetab_icon);
        TextView tv_item4 = view4.findViewById(R.id.tv_item);
        tv_item4.setText(getResources().getString(R.string.Auction));
        tabLayout.getTabAt(3).setCustomView(view4);

        final View view5 = getLayoutInflater().inflate(R.layout.custom_tab_view, null);
        view5.findViewById(R.id.icon).setBackgroundResource(R.mipmap.ic_wallettab_icon);
        TextView tv_item5 = view5.findViewById(R.id.tv_item);
        tv_item5.setText(getResources().getString(R.string.My_Wallet));
        tabLayout.getTabAt(4).setCustomView(view5);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new BuyTabFragment(), getResources().getString(R.string.Buy));
        adapter.addFrag(new RecievedTabFragment(), getResources().getString(R.string.Received));
        adapter.addFrag(new ReplaceTabFragment(), getResources().getString(R.string.Replace));
        adapter.addFrag(new AuctionTabFragment(), getResources().getString(R.string.Auction));
        adapter.addFrag(new MyWalletTabFragment(), getResources().getString(R.string.My_Wallet));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_flag == 0) {
                super.onBackPressed();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            final LayoutInflater inflater = getLayoutInflater();
            dialogBuilder = new AlertDialog.Builder(MainActivity.this);
            final View dialogView = inflater.inflate(R.layout.custom_search_layout, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setCancelable(true);

            final ImageView img_cancel = dialogView.findViewById(R.id.img_search);

            alertDialog = dialogBuilder.create();
            WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
            wmlp.gravity = Gravity.TOP | Gravity.CENTER;
            wmlp.y = 200;
            alertDialog.show();
            return true;
        } else if (id == R.id.action_fav) {
            startActivity(new Intent(MainActivity.this, FavouritesActivity.class));
            return true;
        } else if (id == R.id.action_notification) {
            startActivity(new Intent(MainActivity.this, NotificationsActivity.class));
            return true;
        } else if (id == R.id.action_basket) {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
            return true;
        } else if (id == R.id.action_user) {
            startActivity(new Intent(MainActivity.this, PersonalDataActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
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
