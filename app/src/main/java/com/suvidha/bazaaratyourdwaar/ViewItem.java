package com.suvidha.bazaaratyourdwaar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ViewItem extends AppCompatActivity {

    ViewPager viewPager,viewPager_DR;
    Context context = this;
    TabLayout tabLayout_DR;
    List<Integer> images;
    RecyclerView recyclerView_products_show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        recyclerView_products_show = findViewById(R.id.view_item_rv_like_products);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_products_show.setLayoutManager(linearLayoutManager);
        recyclerView_products_show.setAdapter(new RVAdapter_products_show(context));

        images = new ArrayList<>();
        images.add(R.drawable.sample1);
        images.add(R.drawable.sample2);
        images.add(R.drawable.sample3);
        images.add(R.drawable.sample4);
        images.add(R.drawable.sample5);

        tabLayout_DR = findViewById(R.id.view_item_tabLayout);
        tabLayout_DR.addTab(tabLayout_DR.newTab().setText("Description"));
        tabLayout_DR.addTab(tabLayout_DR.newTab().setText("Reviews"));
        tabLayout_DR.setTabGravity(TabLayout.GRAVITY_FILL);
        ViewPagerAdapter_ItemsDesc viewPagerAdapter_itemsDesc = new ViewPagerAdapter_ItemsDesc(getSupportFragmentManager(),tabLayout_DR.getTabCount());


        viewPager = findViewById(R.id.view_item_vp_product_images);
        ViewPagerAdapterProducts viewPagerAdapterProducts = new ViewPagerAdapterProducts(this,images);
        viewPager.setAdapter(viewPagerAdapterProducts);

        viewPager_DR = findViewById(R.id.view_item_vp_DR);
        viewPager_DR.setAdapter(viewPagerAdapter_itemsDesc);
        viewPager_DR.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout_DR));
        tabLayout_DR.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager_DR.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}