package com.suvidha.bazaaratyourdwaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    SharedPreferences sp;
    ConstraintLayout layoutId;

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tabLayout = findViewById(R.id.dashboard_tab);
        textView_search_products = findViewById(R.id.dashboard_tv_search);
        recyclerView_categories = findViewById(R.id.dashboard_rv_categories);
        textView_deal_of_the_day_time_left = findViewById(R.id.dashboard_tv_deal_time_left);
        cardView_search_products = findViewById(R.id.dashboard_card_searchproducts);

        layoutId = findViewById(R.id.dashboard_layoutid);

        sp = getSharedPreferences(Constants.sharedPref, MODE_PRIVATE);


        if(!checkConnection())
        {
            Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
        }


        completeProfile_check();

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
                        if(!checkConnection())
                        {
                            Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                        }
                        else
                        {
                            refresh();
                        }
                        break;
                    }
                    case 1:
                    {
                        if(!checkConnection())
                        {
                            Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                        }
                        else
                        {
                            startActivity(new Intent(context,Categories.class));
                        }
                        break;
                    }
                    case 2:
                    {
                        if(!checkConnection())
                        {
                            Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                        }
                        else
                        {
                            startActivity(new Intent(context,MyCart.class));
                        }
                        break;
                    }
                    case 3:
                    {
                        if(!checkConnection())
                        {
                            Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                        }
                        else
                        {
                            startActivity(new Intent(context,Favourite.class));
                        }
                        break;
                    }
                    case 4:
                    {
                        if(!checkConnection())
                        {
                            Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                        }
                        else
                        {
                            startActivity(new Intent(context,Settings.class));
                        }
                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition())
                {
                    case 0:
                    {
                        if(!checkConnection())
                        {
                            Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                        }
                        else
                        {
                            refresh();
                        }
                        break;
                    }
                    case 1:
                    {
                        if(!checkConnection())
                        {
                            Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                        }
                        else
                        {
                            startActivity(new Intent(context,Categories.class));
                        }
                        break;
                    }
                    case 2:
                    {
                        if(!checkConnection())
                        {
                            Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                        }
                        else
                        {
                            startActivity(new Intent(context,MyCart.class));
                        }
                        break;
                    }
                    case 3:
                    {
                        if(!checkConnection())
                        {
                            Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                        }
                        else
                        {
                            startActivity(new Intent(context,Favourite.class));
                        }
                        break;
                    }
                    case 4:
                    {
                        if(!checkConnection())
                        {
                            Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                        }
                        else
                        {
                            startActivity(new Intent(context,Settings.class));
                        }
                        break;
                    }
                }
            }
        });

        textView_search_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(Dashboard.this,cardView_search_products,"st_searchProducts");
                if(!checkConnection())
                {
                    Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    startActivity(new Intent(context,SearchProducts.class),activityOptionsCompat.toBundle());
                }

            }
        });


    }

    private void completeProfile_check()
    {


        final String user_id_Key = sp.getString(Constants.sp_userkey,"");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserProfile");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {

                String username = snapshot.child(user_id_Key).child("name").getValue(String.class);
                String profile_dialogonce = sp.getString(Constants.profile_dialog_once,"");
                if(username==null && profile_dialogonce.equals("false"))
                {

                    //dialog for profile completion
                    Dialog dialog_profile_completion = new Dialog(context);
                    dialog_profile_completion.setContentView(R.layout.activity_profile_completion_dialog);
                    dialog_profile_completion.show();

                    SharedPreferences.Editor sp_editor = sp.edit();
                    sp_editor.putString(Constants.profile_dialog_once,"true");
                    sp_editor.commit();

                    Button edit_profile;
                    edit_profile = dialog_profile_completion.findViewById(R.id.editProfile_button);

                    edit_profile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick( View v ) {
                            startActivity(new Intent(context,Profile.class));
                            finish();
                        }
                    });


                }
                else
                {
                    return;
                }
            }

            @Override
            public void onCancelled( @NonNull DatabaseError error ) {

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