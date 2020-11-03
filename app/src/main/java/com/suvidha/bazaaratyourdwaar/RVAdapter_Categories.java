package com.suvidha.bazaaratyourdwaar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVAdapter_Categories extends RecyclerView.Adapter<RVAdapter_Categories.RVViewHolder> {

    Context context;
    List<String> names;
    List<Integer> images;

    public RVAdapter_Categories(Context context, List<String> names, List<Integer> images) {
        this.context = context;
        this.names = names;
        this.images = images;
    }

    @NonNull
    @Override
    public RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_categories, parent, false);
        return new RVViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RVViewHolder holder, int position) {
        holder.imageView.setImageResource(images.get(position));
        holder.textView.setText(names.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class RVViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView;
        public RVViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.rv_item_categories_image);
            textView = itemView.findViewById(R.id.rv_item_categories_name);

        }
    }
}
