package com.boha.golfpractice.library.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.boha.golfpractice.library.R;
import com.boha.golfpractice.library.dto.HoleStatDTO;
import com.boha.golfpractice.library.dto.PracticeSessionDTO;
import com.boha.golfpractice.library.fragments.HoleStatViewerFragment;
import com.boha.golfpractice.library.fragments.PageFragment;
import com.boha.golfpractice.library.util.MonLog;
import com.boha.golfpractice.library.util.Util;

import java.util.ArrayList;
import java.util.List;

public class HoleStatViewerActivity extends AppCompatActivity {

    static List<PageFragment> pageFragmentList;
    List<HoleStatDTO> holeStatList;
    static PracticeSessionDTO practiceSession;
    HoleStatPagerAdapter adapter;
    ViewPager mPager;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getApplicationContext();
        practiceSession  = (PracticeSessionDTO)getIntent()
                .getSerializableExtra("session");
        setContentView(R.layout.activity_hole_stat_viewer);

        mPager = (ViewPager)findViewById(R.id.viewpager);
        buildPages();
        Util.setCustomActionBar(ctx, getSupportActionBar(),
                "TGolf", "Practice Session History",
                ContextCompat.getDrawable(ctx, com.boha.golfpractice.library.R.drawable.golfball48));

    }

    private void buildPages() {
        MonLog.e(getApplicationContext(),"HoleStatViewerActivity",
                "@@@@@@@@@@@@@@@@@@@@@@ Pages to build: " + practiceSession.getHoleStatList().size());
        pageFragmentList = new ArrayList<>();

        for (HoleStatDTO hs: practiceSession.getHoleStatList()) {
            HoleStatViewerFragment f = HoleStatViewerFragment.newInstance(hs);
            pageFragmentList.add(f);
        }
        adapter = new HoleStatPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);


    }
    private static class HoleStatPagerAdapter extends FragmentStatePagerAdapter {

        public HoleStatPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {

            return (android.support.v4.app.Fragment) pageFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return pageFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            HoleStatDTO hs = practiceSession.getHoleStatList().get(position);
            String title = "Hole " + hs.getHole().getHoleNumber();
            return title;
        }
    }
}
