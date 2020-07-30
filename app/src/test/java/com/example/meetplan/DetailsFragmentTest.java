package com.example.meetplan;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;

import com.example.meetplan.details.DetailsFragment;
import com.example.meetplan.models.Meetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk=21)
public class DetailsFragmentTest {

    @Mock Meetup meetup;
    DetailsFragment fragment;
    Bundle fragmentArgs;
    FragmentFactory factory;
    FragmentScenario scenario;

    @Before
    public void setUp() throws Exception {
        meetup = new Meetup();
        fragment = DetailsFragment.newInstance(meetup);
        fragmentArgs = new Bundle();
        fragmentArgs.putParcelable("meetup", meetup);
        factory = new FragmentFactory();
        scenario = FragmentScenario.launch(DetailsFragment.class, fragmentArgs, factory);
    }

    @Test
    public void checkNotNull() {
        assertNotNull(fragment);
    }

    @Test
    public void expenseButtonAction() {
        scenario.onFragment(new ExpenseAction());
    }

    @Test
    public void correctButtonVisibility() {
        scenario.onFragment(new ButtonViews());
    }

    @Test
    public void optionsButtonsFunctionality() {
        scenario.onFragment(new OptionButtons());
    }

    private static class ExpenseAction implements FragmentScenario.FragmentAction<DetailsFragment> {
        @Override
        public void perform(@NonNull DetailsFragment fragment) {
            Button expenseButton = fragment.getBinding().expenseButton;
            assertNotNull(expenseButton);
            assertEquals(View.VISIBLE, expenseButton.getVisibility());
        }
    }

    private static class ButtonViews implements FragmentScenario.FragmentAction<DetailsFragment> {
        private Button submitButton;
        private Button editButton;

        @Override
        public void perform(@NonNull DetailsFragment fragment) {
           submitButton = fragment.getBinding().submitButton;
           validSubmitButton();

           editButton = fragment.getBinding().editButton;
            validEditButton();

            editButton.performClick();
            assertEquals(View.VISIBLE, submitButton.getVisibility());
            assertEquals(View.GONE, editButton.getVisibility());
        }

        private void validSubmitButton() {
            assertNotNull(submitButton);
            assertEquals(View.GONE, submitButton.getVisibility());
        }

        private void validEditButton() {
            assertNotNull(editButton);
            assertEquals(View.VISIBLE, editButton.getVisibility());
        }
    }

    private static class OptionButtons implements FragmentScenario.FragmentAction<DetailsFragment> {

        private Button timeButton;
        private Button dateButton;
        private Button inviteButton;

        @Override
        public void perform(@NonNull DetailsFragment fragment) {
            fragment.getBinding().editButton.performClick();
            timeButton = fragment.getBinding().timeButton;
            dateButton = fragment.getBinding().dateButton;
            inviteButton = fragment.getBinding().inviteDialogButton;
            checkValidity(timeButton);
            checkValidity(dateButton);
            checkValidity(inviteButton);
        }

        private void checkValidity(Button button) {
            assertNotNull(button);
            assertEquals(View.VISIBLE, button.getVisibility());
            button.performClick();
        }
    }
}
