package com.yakrm.codeclinic.yakrm.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.ListView;
import android.widget.TextView;

import com.nex3z.flowlayout.FlowLayout;
import com.yakrm.codeclinic.yakrm.Fragments.AuctionTabFragment;
import com.yakrm.codeclinic.yakrm.Fragments.BuyTabFragment;
import com.yakrm.codeclinic.yakrm.Fragments.MyWalletTabFragment;
import com.yakrm.codeclinic.yakrm.Fragments.RecievedTabFragment;
import com.yakrm.codeclinic.yakrm.Fragments.ReplaceTabFragment;
import com.yakrm.codeclinic.yakrm.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static DrawerLayout drawer;
    private static int lastCheckedPos = -1;
    ListView filter_recyclerview;
    ArrayList<String> arrayList = new ArrayList<>();
    TextView tv[];
    int i;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button lastChecked = null;
    private int selectedPosition = -1;

    @SuppressLint({"ClickableViewAccessibility", "ResourceType", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = findViewById(R.id.drawer_layout);


        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_small_menu_icon, getTheme());
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
        View header = navigationView2.getHeaderView(0);
        FlowLayout flowLayout = header.findViewById(R.id.main_flow_layout);
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


        tv = new TextView[arrayList.size()];
        for (i = 0; i < arrayList.size(); i++) {
            tv[i] = new Button(this);
            tv[i].setText(arrayList.get(i));
            tv[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 30));
            tv[i].setId(i);
            tv[i].setPadding(25, 0, 25, 0);
            tv[i].setGravity(Gravity.CENTER);
            tv[i].setBackground(getResources().getDrawable(R.drawable.flow_layout_text_background));
            tv[i].setTextColor(getResources().getColor(R.color.white));
            flowLayout.addView(tv[i]);

         /*   if (selectedPosition == i) {
                tv[i].setPressed(true);
            } else {
                btn[i].setPressed(false);
            }*/
            tv[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = v.getId();
                    if (pos == selectedPosition) {
                        selectedPosition = -1;
                        tv[pos].setBackground(getResources().getDrawable(R.drawable.flow_layout_text_background));
                    } else {
                        tv[pos].setBackground(getResources().getDrawable(R.drawable.flow_layout_text_background));
                        selectedPosition = pos;
                    }
                }
            });
        }


        navigationView.setNavigationItemSelectedListener(this);
        navigationView2.setNavigationItemSelectedListener(this);


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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            return true;
        } else if (id == R.id.action_basket) {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
            return true;
        } else if (id == R.id.action_user) {
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
