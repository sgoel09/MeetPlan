package com.example.meetplan;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetplan.databinding.ItemMeetupBinding;
import com.google.common.collect.ImmutableList;
import com.parse.ParseUser;

import java.util.ArrayList;

public class MeetupAdapter extends RecyclerView.Adapter<MeetupAdapter.ViewHolder> {

    private Activity context;
    private ImmutableList<Meetup> meetups;
    private Boolean invited;

    public MeetupAdapter(Activity context,  ImmutableList<Meetup> meetups, Boolean invited) {
        this.context = context;
        this.meetups = meetups;
        this.invited = invited;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMeetupBinding binding = ItemMeetupBinding.inflate(context.getLayoutInflater());
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

    public void updateData(ImmutableList<Meetup> meetups) {
        this.meetups = meetups;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemMeetupBinding binding;

        public ViewHolder(@NonNull View itemView, ItemMeetupBinding bind) {
            super(itemView);
            bind.getRoot();
            binding = bind;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.i("Adapter", "onclick");
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Meetup meetup = meetups.get(position);
                Fragment fragment = DetailsFragment.newInstance(meetup);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
            }
        }

        public void bind(final Meetup meetup) {
            binding.tvTitle.setText(meetup.getName());
            binding.tvDescription.setText(meetup.getDescription());
            if (invited) {
                binding.btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Meetup meetup = meetups.get(getAdapterPosition());
                        respondInvite(meetup,true);
                        Fragment fragment = new MeetupsFragment();
                        ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
                    }
                });
                binding.btnDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Meetup meetup = meetups.get(getAdapterPosition());
                        respondInvite(meetup, false);
                        Fragment fragment = new MeetupsFragment();
                        ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
                    }
                });
            } else {
                binding.btnAccept.setVisibility(View.GONE);
                binding.btnDecline.setVisibility(View.GONE);
            }
        }

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
        }
    }
}
