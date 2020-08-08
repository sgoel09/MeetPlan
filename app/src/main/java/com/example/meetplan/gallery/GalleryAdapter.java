package com.example.meetplan.gallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.ItemPictureBinding;
import com.example.meetplan.models.Meetup;
import com.google.common.collect.ImmutableList;
import com.parse.ParseFile;

/**
 * Adapter for the recyclerview of photos of the selected meetup.
 * Each item holds an image view that displays the photo.
 * */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    /** Context of the gallery fragment. */
    private Context context;

    /** ImmutableList of pictures the adapter holds. */
    private ImmutableList<ParseFile> pictures;

    /** Selected meetup for which photos are being displayed. */
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

    /** Updates the data for the adapter by setting the data list to the new list and notifying the adapter.
     * @param pictures new list of pictures with updated information
     * */
    public void updateData(ImmutableList<ParseFile> pictures) {
        this.pictures = pictures;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for the container of views of one photo.
     * Binds the views in the ViewHolder to the corresponding photo information.
     * */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /** View binding for the photo item. */
        final ItemPictureBinding binding;

        public ViewHolder(@NonNull View itemView, ItemPictureBinding bind) {
            super(itemView);
            bind.getRoot();
            binding = bind;
            itemView.setOnClickListener(this);
        }

        /** Binds the photo to the image view of the item
         * @param picture ParseFile that contains the photo being displayed
         * */
        public void bind(final ParseFile picture) {
            binding.picture.setVisibility(View.GONE);
            Glide.with(context).load(picture.getUrl()).placeholder(R.drawable.camera_shadow_fill).into(binding.picture);
            binding.picture.setVisibility(View.VISIBLE);
            binding.picture.setTransitionName(String.valueOf(meetup.getPicture().get(getAdapterPosition())));
        }

        @Override
        public void onClick(View view) {
            MainActivity.currentPosition = getAdapterPosition();
            Fragment imagePager = ImagePagerFragment.newInstance(meetup);
            ((MainActivity) context)
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .addSharedElement(binding.picture, binding.picture.getTransitionName())
                    .addToBackStack(null).replace(R.id.flContainer, imagePager, ImagePagerFragment.class.getSimpleName())
                    .commit();

        }
    }
}
