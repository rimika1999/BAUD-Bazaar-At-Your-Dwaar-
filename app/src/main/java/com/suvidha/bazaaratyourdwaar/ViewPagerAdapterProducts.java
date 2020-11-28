package com.suvidha.bazaaratyourdwaar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerAdapterProducts extends PagerAdapter {
    Context context;
    List<String> images;
    public ViewPagerAdapterProducts(Context context) {
        this.context = context;
    }

    public ViewPagerAdapterProducts(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.viewpager_item_product_images, null);

        ImageView imageView = layout.findViewById(R.id.viewpager_item_product_img);
        Picasso.get().load(images.get(position)).into(imageView);

        ViewPager viewPager = (ViewPager) collection;
        viewPager.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
        ViewPager viewPager = (ViewPager) container;
        View view1 = (View) view;
        viewPager.removeView(view1);
    }

}
