package com.suvidha.bazaaratyourdwaar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RVAdapter_Result extends RecyclerView.Adapter<RVAdapter_Result.RVView>{
    Context context;
    int size;
    public RVAdapter_Result()
    {

    }

    public RVAdapter_Result(Context context,int size) {
        this.context = context;
        this.size = size;
    }

    @NonNull
    @Override
    public RVView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_search_result, parent, false);
        return new RVView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class RVView extends RecyclerView.ViewHolder
    {
        public RVView(@NonNull View itemView) {
            super(itemView);
        }
    }
}
