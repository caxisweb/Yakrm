package com.yakrm.codeclinic.yakrm.Activities;

import android.annotation.SuppressLint;
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
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

    DrawerLayout drawer;
    private static int lastCheckedPos = -1;
    ListView filter_recyclerview;
    ArrayList<String> arrayList = new ArrayList<>();
    Button btn[];
    int i;
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
        View header = navigationView.getHeaderView(0);
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


        btn = new Button[arrayList.size()];
        for (i = 0; i < arrayList.size(); i++) {
            btn[i] = new Button(this);
            btn[i].setText(arrayList.get(i));
            btn[i].setLayoutParams(new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, 110));
            btn[i].setId(i);
            btn[i].setPadding(50, 0, 50, 0);
            btn[i].setGravity(Gravity.CENTER);
            btn[i].setBackground(getResources().getDrawable(R.drawable.flow_layout_text_background));
            btn[i].setTextColor(getResources().getColor(R.color.white));
            flowLayout.addView(btn[i]);

            if (selectedPosition == i) {
                btn[i].setPressed(true);
            } else {
                btn[i].setPressed(false);
            }
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = v.getId();
                    if (pos == selectedPosition) {
                        selectedPosition = -1;
                        btn[pos].setPressed(false);
                    } else {
                        btn[pos].setPressed(true);
                        selectedPosition = pos;
                    }
                }
            });
        }


        navigationView.setNavigationItemSelectedListener(this);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
