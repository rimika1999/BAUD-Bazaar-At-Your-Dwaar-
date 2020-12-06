package com.suvidha.bazaaratyourdwaar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RVAdapter_cart extends RecyclerView.Adapter<RVAdapter_cart.RVView> {
    Context context;
    List<String>items_id;
    RequestQueue queue;
    FirebaseDatabase refDatabase;



    public RVAdapter_cart() {

    }

    public RVAdapter_cart( Context context, List<String> items_id) {
        this.context = context;
        this.items_id=items_id;
    }

    @NonNull
    @Override
    public RVView onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_cart_product,parent,false);
        return new RVView(view);
    }

    @Override
    public void onBindViewHolder( @NonNull final RVView holder, int position ) {


        queue = Volley.newRequestQueue(context);
        final String id = items_id.get(position);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://bazaaratyourdwaar.000webhostapp.com/fetchitems.php?id=" + id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse( JSONArray response ) {

                        try {
                            JSONArray jsonArray = response.getJSONArray(0);

                            holder.item_name.setText(jsonArray.get(2).toString());
                            Picasso.get().load(jsonArray.get(18).toString()).into(holder.item_image);


                            Double cprice = Double.parseDouble(jsonArray.get(3).toString())* 73.99;
                            Double discountVal = Double.parseDouble(jsonArray.get(6).toString());

                            String sprice,discount;
                            sprice = "\u20B9"+ Math.round(cprice);
                            discount = Math.round(discountVal) + "%off";

                            holder.item_price.setText(sprice);
                            holder.item_discount.setText(discount);


                            Log.e("TAG","product:"+jsonArray);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse( VolleyError error ) {

            }
        });
        queue.add(jsonArrayRequest);




        holder.item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent = new Intent(context,ViewItem.class);
                intent.putExtra("itemId",id);
                context.startActivity(intent);
            }

        });

        holder.item_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent = new Intent(context,ViewItem.class);
                intent.putExtra("itemId",id);
                context.startActivity(intent);
            }

        });

        holder.item_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent = new Intent(context,ViewItem.class);
                intent.putExtra("itemId",id);
                context.startActivity(intent);
            }

        });

        holder.item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent = new Intent(context,ViewItem.class);
                intent.putExtra("itemId",id);
                context.startActivity(intent);
            }

        });

        holder.remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                items_id.remove(holder.getAdapterPosition());
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return items_id.size();
    }


    public class RVView extends RecyclerView.ViewHolder{

        ImageView item_image;
        TextView item_name,item_price,item_discount;
        Button remove_item;

        public RVView(View itemView){
            super(itemView);

            item_image = itemView.findViewById(R.id.cart_product_images);
            item_name = itemView.findViewById(R.id.cart_product_name);
            item_price = itemView.findViewById(R.id.cart_product_price);
            item_discount = itemView.findViewById(R.id.cart_product_discount);
            remove_item = itemView.findViewById(R.id.cart_item_remove);
        }

    }

}


