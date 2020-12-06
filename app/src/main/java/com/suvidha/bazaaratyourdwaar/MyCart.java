package com.suvidha.bazaaratyourdwaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyCart extends AppCompatActivity implements View.OnClickListener{

    RecyclerView recycler_view;
    Button shop_now;
    ImageView back;
    Context context = this;
    RequestQueue queue;
    SharedPreferences sp;
    DatabaseReference refDatabase;
    ConstraintLayout layoutId;
    View gifLayout;

    List<Item>cart_items = new ArrayList<>();


    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);


        recycler_view = findViewById(R.id.cart_rv_orders);
        shop_now = findViewById(R.id.button_shop_now);
        back = findViewById(R.id.cart_iv_back);


        shop_now.setOnClickListener(this);
        back.setOnClickListener(this);

        queue = Volley.newRequestQueue(context);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);

        sp= getSharedPreferences(Constants.sharedPref,MODE_PRIVATE);
        final String usercartId = sp.getString(Constants.sp_usercart_key,"");

        refDatabase = FirebaseDatabase.getInstance().getReference("UserItems");



        refDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {

                List<String> user_items;
                int number ;

                HelperClass_cart cart;
                cart = snapshot.child(usercartId).getValue(HelperClass_cart.class);
                number = cart.number_of_items;
                user_items = cart.products;


                if(number>0)
                {
                    gifLayout = findViewById(R.id.cart_gif_layout);
                    gifLayout.setVisibility(View.GONE);
                    recycler_view.setAdapter(new RVAdapter_cart(context,user_items));
                }

               /* if(number>0)
                {

                    for(int i=0;i<number;i++)
                    {
                        String id;
                        id = user_items.get(i);
                        showOutput(id);
                    }
                    gifLayout.setVisibility(View.GONE);

                }
                else
                {
                    gifLayout.setVisibility(View.VISIBLE);
                }

                Log.e("TAG",""+cart_items);
                */

            }

            @Override
            public void onCancelled( @NonNull DatabaseError error ) {

            }
        });


    }




    public void showOutput( String product_id)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://bazaaratyourdwaar.000webhostapp.com/fetchitems.php?id=" + product_id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse( JSONArray response ) {
                        Item product = new Item();
                        try {
                            JSONArray jsonArray = response.getJSONArray(0);
                            product.setItemName(jsonArray.get(2).toString());
                            product.setItemId(jsonArray.get(20).toString());
                            product.setItemImageURL(jsonArray.get(18).toString());

                            Double cprice = Double.parseDouble(product.getItemPrice())* 73.99;
                            Double discountVal = Double.parseDouble(product.getItemDiscount());

                            String sprice,discount;
                            sprice = "\u20B9"+ Math.round(cprice);
                            discount = Math.round(discountVal) + "%off";

                            product.setItemPrice(sprice);
                            product.setItemDiscount(discount);

                            Log.e("TAG","product:"+jsonArray);

                            cart_items.add(product);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse( VolleyError error ) {

            }
        });
        //queue.add(jsonArrayRequest);
    }

    @Override
    public void onClick( View v ) {

        switch (v.getId())
        {
            case R.id.cart_iv_back:
            {
                finish();
                break;
            }
            case R.id.button_shop_now:
            {
                if(!checkConnection())
                {
                    layoutId = findViewById(R.id.cart_layout_id);
                    Snackbar.make(layoutId,"No Internet Connection, please try again later",Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    startActivity(new Intent(context,Payments.class));
                }
                break;
            }
        }
    }

    public static class Item{

        private String itemImageURL, itemId, itemName, itemPrice, itemDiscount;

        public Item() {
        }

        public Item( String itemImageURL, String itemId, String itemName, String itemPrice, String itemDiscount ) {
            this.itemImageURL = itemImageURL;
            this.itemId = itemId;
            this.itemName = itemName;
            this.itemPrice = itemPrice;
            this.itemDiscount = itemDiscount;
        }


        public String getItemImageURL() {
            return itemImageURL;
        }

        public void setItemImageURL( String itemImage ) {
            this.itemImageURL = itemImage;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId( String itemId ) {
            this.itemId = itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName( String itemName ) {
            this.itemName = itemName;
        }

        public String getItemPrice() {
            return itemPrice;
        }

        public void setItemPrice( String itemPrice ) {
            this.itemPrice = itemPrice;
        }

        public String getItemDiscount() {
            return itemDiscount;
        }

        public void setItemDiscount( String itemDiscount ) {
            this.itemDiscount = itemDiscount;
        }
    }

}