package com.suvidha.bazaaratyourdwaar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    Context context=this;
    TabLayout tabLayout;
    RecyclerView recyclerView_categories;
    TextView textView_deal_of_the_day_time_left,textView_search_products;
    int currentHour,currentMin,currentSeconds;
    Handler handler_deal;
    CardView cardView_search_products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tabLayout = findViewById(R.id.dashboard_tab);
        textView_search_products = findViewById(R.id.dashboard_tv_search);
        recyclerView_categories = findViewById(R.id.dashboard_rv_categories);
        textView_deal_of_the_day_time_left = findViewById(R.id.dashboard_tv_deal_time_left);
        cardView_search_products = findViewById(R.id.dashboard_card_searchproducts);

        //calculateDealTime();

        List<String> name = new ArrayList<>();
        name.add("Accessories");
        name.add("Bags");
        name.add("Beauty");
        name.add("House");
        name.add("Jewellery");
        name.add("Kids");
        name.add("Mens");
        name.add("Shoes");
        name.add("Women");

        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.icon_accessories);
        images.add(R.drawable.icon_bags);
        images.add(R.drawable.icon_beauty);
        images.add(R.drawable.icon_house);
        images.add(R.drawable.icon_jewellery);
        images.add(R.drawable.icon_kids);
        images.add(R.drawable.icon_men);
        images.add(R.drawable.icon_shoes);
        images.add(R.drawable.icon_women);


        RVAdapter_Categories rvAdapter_categories = new RVAdapter_Categories(context,name,images);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView_categories.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView_categories.setAdapter(rvAdapter_categories);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition())
                {
                    case 0:
                    {
                        refresh();
                        break;
                    }
                    case 1:
                    {
                        startActivity(new Intent(context,Categories.class));
                        break;
                    }
                    case 2:
                    {
                        startActivity(new Intent(context,MyCart.class));
                        break;
                    }
                    case 3:
                    {
                        startActivity(new Intent(context,Favourite.class));
                        break;
                    }
                    case 4:
                    {
                        startActivity(new Intent(context,Settings.class));
                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        textView_search_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(Dashboard.this,cardView_search_products,"st_searchProducts");
                startActivity(new Intent(context,SearchProducts.class),activityOptionsCompat.toBundle());
            }
        });


    }

    private void refresh()
    {
        Toast.makeText(context,"Refreshed",Toast.LENGTH_LONG).show();
    }

    private void calculateDealTime()
    {
        handler_deal = new Handler();

        handler_deal.postDelayed(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMin = calendar.get(Calendar.MINUTE);
                currentSeconds = calendar.get(Calendar.SECOND);

                String leftHour,leftMin,leftSeconds;
                leftHour = Integer.toString(24 - currentHour);
                leftMin = Integer.toString(60 - currentMin);
                leftSeconds = Integer.toString(60 - currentSeconds);

                if(leftHour.length()==1)
                {
                    leftHour = "0" + leftMin;
                }
                if(leftMin.length()==1)
                {
                    leftMin = "0" + leftMin;
                }
                if(leftSeconds.length()==1)
                {
                    leftSeconds = "0" + leftSeconds;
                }
                Log.e("tag",currentHour + ":" + currentMin + ":" + currentSeconds);
                //textView_deal_of_the_day_time_left.setText(leftHour + ":" + leftMin + ":" + leftSeconds);
                handler_deal.postDelayed(this,1000);
            }
        },1000);
    }
}