package com.muflihun.moviecatalogue5.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.muflihun.moviecatalogue5.R;
import com.muflihun.moviecatalogue5.models.Item;

import java.util.ArrayList;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Item> listItem;
    private OnItemClickCallback onClickCallback;

    public ListItemAdapter(Context context) {
        this.context = context;
        listItem = new ArrayList<>();
    }

    public void setData(ArrayList<Item> listItem){
        this.listItem.clear();
        this.listItem.addAll(listItem);
        notifyDataSetChanged();
    }

    public void setOnClickCallback(OnItemClickCallback onClickCallback) {
        this.onClickCallback = onClickCallback;
    }

    public void removeItem(int position) {
        this.listItem.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,listItem.size());
    }

    @NonNull
    @Override
    public ListItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemAdapter.ViewHolder holder, int position) {
        holder.tvTitle.setText(listItem.get(position).getTitle());
        holder.tvDesc.setText(listItem.get(position).getOverview());
        Glide.with(context).
                load("https://image.tmdb.org/t/p/w185" + listItem.get(position).getPoster()).
                into(holder.ivPoster);

        final int fposition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCallback.onClicked(view, listItem.get(fposition), fposition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPoster;
        private TextView tvTitle, tvDesc;
        public ViewHolder (View view){
            super(view);
            ivPoster = view.findViewById(R.id.iv_poster);
            tvTitle = view.findViewById(R.id.tv_title);
            tvDesc = view.findViewById(R.id.tv_description);
        }
    }

    public interface OnItemClickCallback {
        void onClicked(View v, Item item, int position);
    }
}
