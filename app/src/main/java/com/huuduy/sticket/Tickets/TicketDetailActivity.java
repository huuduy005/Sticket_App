package com.huuduy.sticket.Tickets;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.huuduy.sticket.Events.EventDetailFragment;
import com.huuduy.sticket.Events.EventsList;
import com.huuduy.sticket.R;

import java.util.ArrayList;
import java.util.List;

public class TicketDetailActivity extends AppCompatActivity {

    private static final String TAG = "TicketDetailActivity";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String mTicket;
    private String mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeBlue);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        setTitle(getIntent().getStringExtra("title"));

        Toast.makeText(getApplicationContext(), getIntent().getStringExtra("title"), Toast.LENGTH_LONG).show();
        mTicket = getIntent().getStringExtra("idTicket");
        mEvent = getIntent().getStringExtra("idEvent");
        Log.d(TAG, "onCreate: ticket: " + mTicket + " - event: " + mEvent);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        initTab();

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void initTab() {
        Bundle bTiclet = new Bundle();
        bTiclet.putString("idTicket", mTicket);

        Fragment ticket = new TicketQRCode();
        ticket.setArguments(bTiclet);
        mSectionsPagerAdapter.addFragment(ticket, "Vé");

        Bundle bEvent = new Bundle();
        bEvent.putString("idEvent", mEvent);

        Fragment event = new EventDetailFragment();
        event.setArguments(bEvent);
        mSectionsPagerAdapter.addFragment(event, "Chi tiết sự kiện");
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments = new ArrayList<>();
        private List<String> mTitles = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);

        }

        @Override
        public int getCount() {
            return mFragments.size();

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);

        }
    }
}
