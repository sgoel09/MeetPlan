package com.example.meetplan;

import android.view.View;
import android.widget.Button;

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

    @Before
    public void setUp() throws Exception {
        meetup = new Meetup();
        fragment = DetailsFragment.newInstance(meetup);
    }

    @Test
    public void checkNotNull() {
        assertNotNull(fragment);
    }

    @Test
    public void shouldHaveExpenseButton() {
        Button expenseButton = fragment.getBinding().expenseButton;
        assertNotNull(expenseButton);
        assertEquals(View.VISIBLE, expenseButton.getVisibility());
    }

    @Test
    public void onExpenseClick() {
        fragment.getBinding().expenseButton.performClick();
    }
}
