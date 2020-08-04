package com.example.meetplan.gallery;

import android.content.Context;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.ItemPictureBinding;
import com.example.meetplan.details.DetailsFragment;
import com.example.meetplan.models.Meetup;
import com.google.common.collect.ImmutableList;
import com.parse.ParseFile;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context context;
    private ImmutableList<ParseFile> pictures;
    private Meetup meetup;

    public GalleryAdapter(Context context, ImmutableList<ParseFile> pictures, Meetup meetup) {
        this.context = context;
        this.pictures = pictures;
        this.meetup = meetup;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ItemPictureBinding binding;

        public ViewHolder(@NonNull View itemView, ItemPictureBinding bind) {
            super(itemView);
            bind.getRoot();
            binding = bind;
            itemView.setOnClickListener(this);
        }

        public void bind(final ParseFile picture) {
            binding.picture.setVisibility(View.GONE);
            Glide.with(context).load(picture.getUrl()).placeholder(R.drawable.camera_shadow_fill).into(binding.picture);
            binding.picture.setVisibility(View.VISIBLE);
            binding.picture.setTransitionName(String.valueOf(meetup.getPicture().get(getAdapterPosition())));
        }

        @Override
        public void onClick(View view) {
            // Update the position.
            MainActivity.currentPosition = getAdapterPosition();
            Fragment imagePager = ImagePagerFragment.newInstance(meetup);
            ((MainActivity) context).getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).addSharedElement(binding.picture, binding.picture.getTransitionName()).addToBackStack(null).replace(R.id.flContainer, imagePager, ImagePagerFragment.class.getSimpleName()).commit();

        }
    }
}
