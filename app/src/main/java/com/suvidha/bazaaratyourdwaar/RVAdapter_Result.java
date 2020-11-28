package com.suvidha.bazaaratyourdwaar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RVAdapter_Result extends RecyclerView.Adapter<RVAdapter_Result.RVView>{
    Context context;
    List<SearchProducts.Product> products;
    public RVAdapter_Result()
    {

    }

    public RVAdapter_Result( Context context, List<SearchProducts.Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public RVView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_search_result, parent, false);
        return new RVView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVView holder, int position) {
        final SearchProducts.Product product = products.get(position);
        holder.item_name.setText(product.getProductName());
        holder.item_category.setText(product.getProductCategory());
        Picasso.get().load(product.getProductIcon()).into(holder.item_image);

        holder.item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent = new Intent(context,ViewItem.class);
                intent.putExtra("itemId",product.getProductId());
                context.startActivity(intent);
            }
        });

        holder.item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent = new Intent(context,ViewItem.class);
                intent.putExtra("itemId",product.getProductId());
                context.startActivity(intent);
            }
        });

        holder.item_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent = new Intent(context,ViewItem.class);
                intent.putExtra("itemId",product.getProductId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class RVView extends RecyclerView.ViewHolder
    {
        TextView item_name,item_category;
        ImageView item_image;
        public RVView(@NonNull View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.rv_item_search_result_tv_item_name);
            item_category = itemView.findViewById(R.id.rv_item_search_result_tv_item_categroy);
            item_image = itemView.findViewById(R.id.rv_item_search_result_iv_icon);
        }


    }
}
