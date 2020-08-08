package com.example.meetplan.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.meetplan.MainActivity;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentDetailsBinding;
import com.example.meetplan.gallery.GalleryFragment;
import com.example.meetplan.models.Meetup;
import com.google.common.collect.ImmutableList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;

/**
 * Displays all the filled out details of the fragment in the view mode.
 * Allows user to change to edit mode and select new information for the meetup,
 * including title, descirption, date, time, tasks, and invitied members.
 */
public class DetailsFragment extends Fragment implements PassNewInfo {

    /** Key for the meetup in the arguments. */
    private static final String KEY_MEETUP = "meetup";

    /** Key for the username in the Parse database. */
    private static final String KEY_USERNAME = "username";

    /** Format for displaying the date of a Date object. */
    private static final String DATE_FORMAT = "EE, MMM d, y";

    /** Format for displaying the time of a Date object. */
    private static final String TIME_FORMAT = "h:mm a";

    /** Default text if there are no invites. */
    private static final String NONE_STRING = "None";

    /** Click listener that shows the date picker dialog fragment. */
    private DatePickerClickListener datePickerClickListener;

    /** Click listener that shows the time picker dialog fragment. */
    private TimePickerClickListener timePickerClickListener;

    /** Click listener that shows the edit mode of the details fragment. */
    private EditDetailsClickListener editDetailsClickListener;

    /** Click listener that shows the view mode of the details fragment. */
    private SubmitDetailsClickListener submitDetailsClickListener;

    /** Click listener that invites the selected members in the spinner dialog. */
    private InviteItemClick inviteItemClick;

    /** Click listener that shows the spinner dialog of all users. */
    private InviteClickListener inviteClickListener;

    /** Click listener to browse tasks for the selected meetup. */
    private BrowseClickListener browseClickListener;

    /** Click listener to show expenses for the selected meetup. */
    private ExpenseClickListener expenseClickListener;

    /** List of invited members. */
    private ArrayList<String> inviteUsernames = new ArrayList<>();

    /** Spinner dialog to invite members from all users. */
    private SpinnerDialog spinnerDialog;

    /** View binding for this fragment. */
    FragmentDetailsBinding binding;

    /** Meetup for which details are being displayed. */
    Meetup meetup;

    private FragmentManager fm;
    private final Fragment thisFragment = this;

    /** Required empty constructor. */
    public DetailsFragment() {}

    /** Creates a new instance of the details fragment with view  mode. */
    public static DetailsFragment newInstance(Meetup meetup) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_MEETUP, meetup);
        fragment.setArguments(args);
        return fragment;
    }

    /** Creates a new instance of the details fragment with edit  mode. */
    public static DetailsFragment newInstance(Meetup meetup, boolean resume) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_MEETUP, meetup);
        args.putBoolean("resume", resume);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        return view;
    }

    /** Queries relevant information for the selected meetup and display the information
     * in the correct mode. Sets the on click listeners for the respective views. */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getContext()).showBottomNavigation(false);

        meetup = getArguments().getParcelable(KEY_MEETUP);
        fm = ((MainActivity) getContext()).getSupportFragmentManager();

        queryIncludes(meetup.getObjectId());

        if (getArguments().getBoolean("resume") == true) {
            changeMode(View.VISIBLE, meetup, true, binding);
        } else {
            changeMode(View.GONE, meetup, false, binding);
            displayMembers();
            dispalyInvites();
            setDateTime();
            updateInviteUsernames();
        }

        spinnerDialog = new SpinnerDialog(getActivity(), inviteUsernames, getString(R.string.invite_title), getString(R.string.cancel));
        spinnerDialog.setCancellable(true);
        spinnerDialog.setShowKeyboard(false);

        inviteItemClick = new InviteItemClick(binding, inviteUsernames, meetup);
        spinnerDialog.bindOnSpinerListener(inviteItemClick);

        inviteClickListener = new InviteClickListener(spinnerDialog);
        binding.inviteDialogButton.setOnClickListener(inviteClickListener);

        datePickerClickListener = new DatePickerClickListener(getActivity().getSupportFragmentManager(), meetup);
        binding.dateButton.setOnClickListener(datePickerClickListener);

        timePickerClickListener = new TimePickerClickListener(getActivity().getSupportFragmentManager(), meetup);
        binding.timeButton.setOnClickListener(timePickerClickListener);

        editDetailsClickListener = new EditDetailsClickListener(binding, meetup);
        binding.editButton.setOnClickListener(editDetailsClickListener);

        submitDetailsClickListener = new SubmitDetailsClickListener(binding, meetup);
        binding.submitButton.setOnClickListener(submitDetailsClickListener);

        browseClickListener = new BrowseClickListener(getContext(), meetup);
        binding.browseButton.setOnClickListener(browseClickListener);

        expenseClickListener = new ExpenseClickListener(getActivity().getSupportFragmentManager(), getContext(), meetup);
        binding.expenseButton.setOnClickListener(expenseClickListener);

        binding.galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GalleryFragment fragment = GalleryFragment.newInstance(meetup);
                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .addToBackStack(DetailsFragment.class.getSimpleName())
                        .replace(R.id.flContainer, fragment)
                        .commit();
            }
        });

        binding.titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditTitleFragment fragment = EditTitleFragment.newInstance(meetup, "Name");
                fragment.setTargetFragment(thisFragment, 0);
                fragment.show(fm, "fragment_edit_name");
            }
        });

        binding.descriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                EditTitleFragment fragment = EditTitleFragment.newInstance(meetup, "Description");
                fragment.setTargetFragment(thisFragment, 0);
                fragment.show(fm, "fragment_edit_name");
            }
        });

    }

    /** Queries meetup information to include the meetup's task and expense objects.
     * @param objectId the id for the meetup to query information for. */
    private void queryIncludes(String objectId) {
        ParseQuery<Meetup> query = ParseQuery.getQuery(Meetup.class);
        query.whereEqualTo("objectId", objectId);
        query.include("task");
        query.include("expenses");
        try {
            List<Meetup> objects  = query.find();
            if (objects.size() > 0) {
                meetup = objects.get(0);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /** Changes mode of the details view based on the given arguments.
     * @param visibility visibility of the option to change details buttons
     * @param meetup meetup of which the details are
     * @param edit boolean or whether the changed mode is edit mode
     * @param binding view binding of the details fragment */
    protected static void changeMode(int visibility, Meetup meetup, boolean edit, FragmentDetailsBinding binding) {
        binding.title.setVisibility(View.VISIBLE);
        binding.title.setText(meetup.getName());
        if (edit || (meetup.getDescription() != null && !meetup.getDescription().isEmpty())) {
            binding.description.setVisibility(View.VISIBLE);
            binding.description.setText(meetup.getDescription());
            binding.descriptionLabel.setVisibility(View.VISIBLE);
        } else {
            binding.description.setVisibility(View.GONE);
            binding.descriptionLabel.setVisibility(View.GONE);
        }
        if (meetup.getDate() != null && !Meetup.getDateFormatted(meetup).isEmpty()) {
            binding.date.setVisibility(View.VISIBLE);
            binding.date.setText(Meetup.getDateFormatted(meetup));
            binding.dateLabel.setVisibility(View.VISIBLE);
        } else {
            binding.date.setVisibility(View.GONE);
            binding.dateLabel.setVisibility(View.GONE);
        }
        if (meetup.getDate() != null && !Meetup.getTimeFormatted(meetup).isEmpty()) {
            binding.time.setVisibility(View.VISIBLE);
            binding.time.setText(Meetup.getTimeFormatted(meetup));
            binding.timeLabel.setVisibility(View.VISIBLE);
        } else {
            binding.time.setVisibility(View.GONE);
            binding.timeLabel.setVisibility(View.GONE);
        }
        changeModeTask(meetup, edit, binding);
        binding.browseButton.setVisibility(visibility);
        binding.inviteDialogButton.setVisibility(visibility);
        binding.dateButton.setVisibility(visibility);
        binding.timeButton.setVisibility(visibility);
        binding.submitButton.setVisibility(visibility);
        binding.titleButton.setVisibility(visibility);
        binding.descriptionButton.setVisibility(visibility);
        if (edit) {
            binding.editButton.setVisibility(View.GONE);
            binding.galleryButton.setVisibility(View.GONE);
            binding.expenseButton.setVisibility(View.GONE);
            binding.dateLabel.setVisibility(View.VISIBLE);
            binding.timeLabel.setVisibility(View.VISIBLE);
        } else {
            binding.editButton.setVisibility(View.VISIBLE);
            binding.galleryButton.setVisibility(View.VISIBLE);
            binding.expenseButton.setVisibility(View.VISIBLE);
        }
    }

    /** Changes the views of a given meetup on the given arguments.
     * @param meetup meetup of which the task is
     * @param edit boolean or whether the changed mode is edit mode
     * @param binding view binding of the details fragment */
    protected static void changeModeTask(Meetup meetup, boolean edit, FragmentDetailsBinding binding) {
        if (edit || meetup.getTask() != null) {
            binding.location.setVisibility(View.VISIBLE);
            binding.activity.setVisibility(View.VISIBLE);
            binding.activityLabel.setVisibility(View.VISIBLE);
        } else {
            binding.activity.setVisibility(View.GONE);
            binding.activityLabel.setVisibility(View.GONE);
            binding.location.setVisibility(View.GONE);
            binding.locationLabel.setVisibility(View.GONE);
            binding.address.setVisibility(View.GONE);
        }
        if (meetup.getTask() != null) {
            binding.activity.setText(meetup.getTask().getName());
            binding.location.setText(meetup.getTask().getPlace());
            binding.address.setText(meetup.getTask().getAddress());
        }
    }

    /** Queries all users and adds them to the list to be displayed as options when inviting a new memeber. */
    private void updateInviteUsernames() {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.include(KEY_USERNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                for (ParseUser user : objects) {
                    inviteUsernames.add(user.getUsername());
                }
            }
        });
    }

    /** Formates the Date object using the date and time formats and sets the respective views
     * with the information. */
    private void setDateTime() {
        if (meetup.getDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            String dateFormatted = dateFormat.format(meetup.getDate());
            binding.date.setText(dateFormatted);

            SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
            String timeFormatted = timeFormat.format(meetup.getDate());
            binding.time.setText(timeFormatted);
        }
    }

    /** Retrieves and displays all the members of the current meetup in a list format. */
    private void displayMembers() {
        ImmutableList<String> members = ImmutableList.<String>builder().addAll(meetup.getMembers()).build();
        String allMembers = "";
        if (members != null) {
            for (String username : members) {
                if (!members.get(members.size() - 1).equals(username)) {
                    allMembers += String.format("%s; ", username);
                } else {
                    allMembers += username;
                }
            }
        }
        binding.members.setText(allMembers);
    }

    /** Retrieves and displays all the invites of the current meetup in a list format. */
    private void dispalyInvites() {
        ImmutableList<String> invites = ImmutableList.<String>builder().addAll(meetup.getInvites()).build();
        String allInvites = "";
        if (invites != null && !invites.isEmpty()) {
            for (String username : invites) {
                if (!invites.get(invites.size() - 1).equals(username)) {
                    allInvites += String.format("%s; ", username);
                } else {
                    allInvites += username;
                }
            }
            binding.invites.setText(allInvites);
        } else {
            binding.invites.setText(NONE_STRING);
        }
    }

    /** @return the view binding of this fragment, which is made available for unit testing. */
    @VisibleForTesting (otherwise = VisibleForTesting.PRIVATE)
    public FragmentDetailsBinding getBinding() {
        return binding;
    }

    /** Passes the new title or description, and its corresponding changed value,
     * to be saved int he meetup.
     * @param type the key for the type of information - title or description
     * @param info the new value to be saved for the key. */
    @Override
    public void passMeetupInformation(String type, String info) {
        meetup.put(type, info);
        meetup.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                changeMode(View.VISIBLE, meetup, true, binding);
            }
        });
    }
}