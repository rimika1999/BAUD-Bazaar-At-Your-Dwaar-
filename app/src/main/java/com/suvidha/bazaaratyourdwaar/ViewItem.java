package com.suvidha.bazaaratyourdwaar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ViewItem extends AppCompatActivity {

    ViewPager viewPager,viewPager_DR;
    Context context = this;
    TabLayout tabLayout_DR;
    List<String> images;
    RecyclerView recyclerView_products_show;
    TextView textView_heading,textView_item_name,textView_price,textView_discount;
    ImageView imageView_share,imageView_add_fav;
    boolean isItemFav = false;

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
        textView_discount = findViewById(R.id.view_item_tv_discount);
        textView_heading = findViewById(R.id.view_item_tv_item_heading);
        textView_item_name = findViewById(R.id.view_item_tv_item_name);
        textView_price = findViewById(R.id.view_item_tv_price);
        imageView_share = findViewById(R.id.view_item_iv_share);
        imageView_add_fav = findViewById(R.id.view_item_iv_add_fav);

        images = new ArrayList<>();

        tabLayout_DR = findViewById(R.id.view_item_tabLayout);
        tabLayout_DR.addTab(tabLayout_DR.newTab().setText("Description"));
        tabLayout_DR.addTab(tabLayout_DR.newTab().setText("Reviews"));
        tabLayout_DR.setTabGravity(TabLayout.GRAVITY_FILL);
        ViewPagerAdapter_ItemsDesc viewPagerAdapter_itemsDesc = new ViewPagerAdapter_ItemsDesc(getSupportFragmentManager(),tabLayout_DR.getTabCount());

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

        fetchData(getIntent().getStringExtra("itemId"));

        imageView_add_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isItemFav)
                {
                    imageView_add_fav.setImageResource(R.drawable.icon_fav_not_selected);
                    isItemFav = !isItemFav;
                }
                else
                {
                    imageView_add_fav.setImageResource(R.drawable.icon_fav_selected);
                    isItemFav = !isItemFav;
                }
            }
        });

    }

    private void fetchData(String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://bazaaratyourdwaar.000webhostapp.com/fetchitems.php?id=" + id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse( JSONArray response ) {
                        ProductOutlook product = new ProductOutlook();
                        try {
                            JSONArray jsonArray = response.getJSONArray(0);
                            product.setCategory(jsonArray.get(0).toString());
                            product.setSubcategory(jsonArray.get(1).toString());
                            product.setName(jsonArray.get(2).toString());
                            product.setCurrent_price(jsonArray.get(3).toString());
                            product.setRaw_price(jsonArray.get(4).toString());
                            product.setCurrency(jsonArray.get(5).toString());
                            product.setDiscount(jsonArray.get(6).toString());
                            product.setLikes_count(jsonArray.get(7).toString());
                            product.setIs_new(jsonArray.get(8).toString());
                            product.setBrand(jsonArray.get(9).toString());
                            product.setBrand_url(jsonArray.get(10).toString());
                            product.setCodcountry(jsonArray.get(11).toString());
                            product.setVariation_0_color(jsonArray.get(12).toString());
                            product.setVariation_1_color(jsonArray.get(13).toString());
                            product.setVariation_0_thumbnail(jsonArray.get(14).toString());
                            product.setVariation_0_image(jsonArray.get(15).toString());
                            product.setVariation_1_thumbnail(jsonArray.get(16).toString());
                            product.setVariation_1_image(jsonArray.get(17).toString());
                            product.setImage_url(jsonArray.get(18).toString());
                            product.setUrl(jsonArray.get(19).toString());
                            product.setId(jsonArray.get(20).toString());
                            product.setModel(jsonArray.get(21).toString());

                            double price = Double.parseDouble(product.getCurrent_price()) * 73.99;
                            double discount = Double.parseDouble(product.getDiscount());

                            textView_price.setText("\u20B9" + Math.round(price));
                            textView_discount.setText(Math.round(discount) + "% off");
                            textView_heading.setText(product.getName());
                            textView_item_name.setText(product.getName());
                            viewPager = findViewById(R.id.view_item_vp_product_images);
                            images.add(product.getImage_url());
                            if (!product.getVariation_0_image().isEmpty())
                                images.add(product.getVariation_0_image());
                            if (!product.getVariation_1_image().isEmpty())
                                images.add(product.getVariation_1_image());
                            ViewPagerAdapterProducts viewPagerAdapterProducts = new ViewPagerAdapterProducts(context, images);
                            viewPager.setAdapter(viewPagerAdapterProducts);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse( VolleyError error ) {

                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
    }