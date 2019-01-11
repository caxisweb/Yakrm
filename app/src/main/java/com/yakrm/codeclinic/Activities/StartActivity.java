package com.yakrm.codeclinic.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yakrm.codeclinic.R;
import com.yakrm.codeclinic.Utils.SessionManager;

import java.util.Locale;


public class StartActivity extends AppCompatActivity {

    private ViewPager view_pager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;

    Button btn_sign_in, btn_start;
    TextView tv_skip, tv_login, tv_language, tv_user_agree;
    SessionManager sessionManager;

    public boolean isEmpty(CharSequence character) {
        return character == null || character.length() == 0;
    }


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                //btn_next.setText(getString(R.string.next));
                /*     btn_previous.setVisibility(View.GONE);*/
            } else {
                // still pages are left
                //btn_next.setText(getString(R.string.next));
                //btn_skip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        view_pager = findViewById(R.id.vi_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_start = findViewById(R.id.btn_start);
        tv_skip = findViewById(R.id.tv_skip);
        tv_login = findViewById(R.id.tv_login);
        tv_language = findViewById(R.id.tv_language);
        tv_language = findViewById(R.id.tv_language);
        String textToHighlight = getResources().getString(R.string.log_in);

        sessionManager = new SessionManager(this);

        // Construct the formatted text
        String replacedWith = "<font color='red'>" + textToHighlight + "</font>";
        // Get the text from TextView
        String originalString = tv_login.getText().toString();
        // Replace the specified text/word with formatted text/word
        String modifiedString = originalString.replaceAll(textToHighlight, replacedWith);
        // Update the TextView text
        tv_login.setText(Html.fromHtml(modifiedString));


        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, NewAccountActivity.class));
                finish();
            }
        });

        layouts = new int[]{R.layout.intro_screen1, R.layout.intro_screen2, R.layout.intro_screen3, R.layout.intro_screen4, R.layout.intro_screen5, R.layout.intro_screen6};

        addBottomDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        view_pager.setAdapter(myViewPagerAdapter);
        view_pager.addOnPageChangeListener(viewPagerPageChangeListener);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });


        tv_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language_name = "";
                if (tv_language.getText().equals("النسخة العربية")) {
                    language_name = "ar";
                } else {
                    language_name = "en";
                }
                sessionManager.putLanguage("Language", language_name);
                Locale locale = new Locale(language_name);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

                recreate();
            }
        });

    }


    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return view_pager.getCurrentItem() + i;
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}
