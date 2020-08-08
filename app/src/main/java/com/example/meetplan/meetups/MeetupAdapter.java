package com.example.meetplan.meetups;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.ItemMeetupBinding;
import com.example.meetplan.details.DetailsFragment;
import com.example.meetplan.models.Meetup;
import com.google.common.collect.ImmutableList;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Adapter for the recyclerview of meetups for the current user.
 * Each item holds information about a meetup, including name, description, and date.
 * */
public class MeetupAdapter extends RecyclerView.Adapter<MeetupAdapter.ViewHolder> {

    /** Context of the meetup fragment. */
    private Context context;

    /** ImmutableList of meetups the adapter holds. */
    private ImmutableList<Meetup> meetups;

    /** Boolean to determine whether the adapter is for an invited meetup or an already accepted meetup. */
    private Boolean invited;

    /** Layout manager for the recyclerview of meetups. */
    private FragmentManager fragmentManager;

    public MeetupAdapter(Context context, ImmutableList<Meetup> meetups, Boolean invited, FragmentManager fragmentManager) {
        this.context = context;
        this.meetups = meetups;
        this.invited = invited;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMeetupBinding binding = ItemMeetupBinding.inflate(((MainActivity) context).getLayoutInflater());
        View view = binding.getRoot();
        return new ViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meetup meetup = meetups.get(position);
        holder.bind(meetup);
    }

    @Override
    public int getItemCount() {
        return meetups.size();
    }

    /** Updates the data for the adapter by setting the data list to the new list and notifying the adapter.
     * @param meetups new list of meetups with updated information
     * */
    public void updateData(ImmutableList<Meetup> meetups) {
        this.meetups = meetups;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for the container of views of one meetup.
     * Binds the views in the ViewHolder to the corresponding meetup information.
     * */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /** View binding for the meetup item. */
        final ItemMeetupBinding binding;

        public ViewHolder(@NonNull View itemView, ItemMeetupBinding bind) {
            super(itemView);
            bind.getRoot();
            binding = bind;
            itemView.setOnClickListener(this);
        }

        /** Click listener to show details fragment of the meetup when the itme's view holder is selected. */
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Meetup meetup = meetups.get(position);
                Fragment fragment = DetailsFragment.newInstance(meetup);
                fragmentManager.beginTransaction().addToBackStack(MeetupsFragment.class.getSimpleName()).replace(R.id.flContainer, fragment).commit();

            }
        }

        /** Binds the meetup information to the views in the ViewHolder,
         * including name, description, and date.
         * @param meetup meetup for which its data is binded to
         * */
        public void bind(final Meetup meetup) {
            binding.title.setText(meetup.getName());
            binding.description.setText(meetup.getDescription());
            if (meetup.getDescription() == null || meetup.getDescription().isEmpty()) {
                binding.description.setText("No description");
                binding.description.setTypeface(null, Typeface.ITALIC);
            }
            if (meetup.getDate() != null) {
                binding.date.setText(Meetup.getDateFormatted(meetup));
                binding.dot.setVisibility(View.VISIBLE);
            } else {
                binding.dot.setVisibility(View.GONE);
            }
            if (invited) {
                binding.acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Meetup meetup = meetups.get(getAdapterPosition());
                        respondInvite(meetup,true);
                    }
                });
                binding.declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Meetup meetup = meetups.get(getAdapterPosition());
                        respondInvite(meetup, false);
                    }
                });
            } else {
                binding.acceptButton.setVisibility(View.GONE);
                binding.declineButton.setVisibility(View.GONE);
            }
        }

        /** Responds to the invited meetup by updating the invites and members lists accordingly,
         * and updating the adapter to display changes.
         * @param meetup meetup which the user is invited to
         * @param accepted boolean of whether the user accepted the meetup */
        private void respondInvite(Meetup meetup, boolean accepted) {
            ArrayList<String> invites = meetup.getInvites();
            invites.remove(ParseUser.getCurrentUser().getUsername());
            meetup.setInvites(invites);
            if (accepted) {
                ArrayList<String> members = meetup.getMembers();
                members.add(ParseUser.getCurrentUser().getUsername());
                meetup.setMembers(members);
            }
            meetup.saveInBackground();
            updateData(meetups);
            Fragment fragment = new MeetupsFragment();
            ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
        }
    }
}
