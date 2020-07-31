package com.example.meetplan.gallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meetplan.MainActivity;
import com.example.meetplan.databinding.ItemPictureBinding;
import com.google.common.collect.ImmutableList;
import com.parse.ParseFile;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context context;
    private ImmutableList<ParseFile> pictures;

    public GalleryAdapter(Context context, ImmutableList<ParseFile> pictures) {
        this.context = context;
        this.pictures = pictures;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPictureBinding binding = ItemPictureBinding.inflate(((MainActivity) context).getLayoutInflater());
        View view = binding.getRoot();
        return new ViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.ViewHolder holder, int position) {
        ParseFile picture = pictures.get(position);
        holder.bind(picture);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public void updateData(ImmutableList<ParseFile> pictures) {
        this.pictures = pictures;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final ItemPictureBinding binding;

        public ViewHolder(@NonNull View itemView, ItemPictureBinding bind) {
            super(itemView);
            bind.getRoot();
            binding = bind;
        }

        public void bind(final ParseFile picture) {
            binding.picture.setVisibility(View.GONE);
            String url = picture.getUrl();
            Glide.with(context).load(picture.getUrl()).into(binding.picture);
            binding.picture.setVisibility(View.VISIBLE);
        }

    }
}
