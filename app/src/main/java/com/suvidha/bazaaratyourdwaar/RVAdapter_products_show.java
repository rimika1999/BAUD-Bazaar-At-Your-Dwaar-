package com.suvidha.bazaaratyourdwaar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RVAdapter_products_show extends RecyclerView.Adapter<RVAdapter_products_show.RVView> {

    Context context;

    public RVAdapter_products_show(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RVView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_product_show, parent, false);
        return new RVView(v);
    }

    @NonNull

    @Override
    public void onBindViewHolder(@NonNull RVView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class RVView extends RecyclerView.ViewHolder
    {
        public RVView(@NonNull View itemView) {
            super(itemView);
        }
    }
}
