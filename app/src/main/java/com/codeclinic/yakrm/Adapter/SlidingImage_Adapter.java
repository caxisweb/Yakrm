package com.codeclinic.yakrm.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.codeclinic.yakrm.Models.GiftCategoryBannersModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SlidingImage_Adapter extends PagerAdapter {


    private List<GiftCategoryBannersModel> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImage_Adapter(Context context, List<GiftCategoryBannersModel> IMAGES) {
        this.context = context;
        this.IMAGES = IMAGES;
        inflater = LayoutInflater.from(context);


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout
                .findViewById(R.id.image);

        Picasso.with(context).load(ImageURL.gift_category_banner + IMAGES.get(position).getGiftCategoryBanner()).error(R.drawable.app_logo).into(imageView);

        view.addView(imageLayout, 0);


        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
