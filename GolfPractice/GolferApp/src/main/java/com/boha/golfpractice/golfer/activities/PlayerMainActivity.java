package com.boha.golfpractice.golfer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.golfpractice.golfer.R;
import com.boha.golfpractice.library.activities.GolfCourseListActivity;
import com.boha.golfpractice.library.activities.HoleStatActivity;
import com.boha.golfpractice.library.activities.HoleStatViewerActivity;
import com.boha.golfpractice.library.activities.MonApp;
import com.boha.golfpractice.library.dto.PracticeSessionDTO;
import com.boha.golfpractice.library.dto.RequestDTO;
import com.boha.golfpractice.library.dto.ResponseDTO;
import com.boha.golfpractice.library.fragments.PageFragment;
import com.boha.golfpractice.library.fragments.SessionListFragment;
import com.boha.golfpractice.library.fragments.SessionSummaryFragment;
import com.boha.golfpractice.library.util.DepthPageTransformer;
import com.boha.golfpractice.library.util.OKHttpException;
import com.boha.golfpractice.library.util.OKUtil;
import com.boha.golfpractice.library.util.SharedUtil;
import com.boha.golfpractice.library.util.SnappyGeneral;
import com.boha.golfpractice.library.util.SnappyPractice;
import com.boha.golfpractice.library.util.Util;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerMainActivity extends AppCompatActivity implements SessionListFragment.SessionListListener {


    private CharSequence mTitle;

    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    TextView navText;
    ImageView navImage;
    CircleImageView circleImage;
    Context ctx;
    ViewPager mPager;
    StaffPagerAdapter adapter;
    int currentPageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_main);
        ctx = getApplicationContext();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) mDrawerLayout.findViewById(R.id.nav_view);

        circleImage = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.NAVHEADER_logo);
        navImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.NAVHEADER_image);
        navText = (TextView) navigationView.getHeaderView(0).findViewById(R.id.NAVHEADER_text);
        if (navText != null) {
            navText.setText(SharedUtil.getPlayer(ctx).getFirstName());
        }
        setMenuDestinations();
        mDrawerLayout.openDrawer(GravityCompat.START);

        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPager.setOffscreenPageLimit(4);
        PagerTitleStrip strip = (PagerTitleStrip) mPager.findViewById(R.id.pager_title_strip);
        strip.setVisibility(View.VISIBLE);
        //strip.setBackgroundColor(themeDarkColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(themeDarkColor);
            //window.setNavigationBarColor(themeDarkColor);
        }
        Util.setCustomActionBar(ctx, getSupportActionBar(), "TGolf", getString(R.string.record_prac),
                ContextCompat.getDrawable(ctx, com.boha.golfpractice.library.R.drawable.golfball48));
        snackbar = Snackbar.make(mPager, "Refreshing data from cloud server, hang on a second ...", Snackbar.LENGTH_INDEFINITE);

        getCachedPractices();
    }

    static List<PageFragment> pageFragmentList;
    SessionListFragment sessionListFragment;
    SessionSummaryFragment sessionSummaryFragment;
    List<PracticeSessionDTO> practiceSessionList;

    private void buildPages() {
        pageFragmentList = new ArrayList<>();
        sessionListFragment = SessionListFragment.newInstance(practiceSessionList);
        sessionListFragment.setApp((MonApp) getApplication());

        sessionSummaryFragment = new SessionSummaryFragment();
        sessionSummaryFragment.setApp((MonApp) getApplication());
        sessionSummaryFragment.setPlayer(SharedUtil.getPlayer(ctx));

        pageFragmentList.add(sessionListFragment);
        pageFragmentList.add(sessionSummaryFragment);

        adapter = new StaffPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setPageTransformer(true, new DepthPageTransformer());

        mPager.setCurrentItem(currentPageIndex, true);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPageIndex = position;
                //pageFragmentList.get(position).animateHeroHeight();
                PageFragment pf = pageFragmentList.get(position);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    Menu mMenu;
    Snackbar snackbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.boha.golfpractice.library.R.menu.player_main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == com.boha.golfpractice.library.R.id.action_refresh) {
            setRefreshActionButtonState(true);
            snackbar.show();
            getPracticeSessions();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static final int PRACTICE_SESSION_START = 123;

    @Override
    public void onSessionClicked(PracticeSessionDTO session) {
        if (session.getClosed() == Boolean.FALSE) {
            Intent m = new Intent(ctx, HoleStatActivity.class);
            m.putExtra("session", session);
            startActivityForResult(m, PRACTICE_SESSION_START);
        } else {
            Intent m = new Intent(ctx, HoleStatViewerActivity.class);
            m.putExtra("session", session);
            startActivity(m);
        }

    }

    static final int NEW_SESSION_REQUESTED = 657;

    @Override
    public void onNewSessionRequested() {
        Intent m = new Intent(ctx, GolfCourseListActivity.class);
        startActivityForResult(m, NEW_SESSION_REQUESTED);
    }

    PracticeSessionDTO practiceSession;

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        switch (reqCode) {
            case NEW_SESSION_REQUESTED:
                if (resCode == RESULT_OK) {
                    practiceSession = (PracticeSessionDTO) data.getSerializableExtra("practiceSession");
                    sessionListFragment.addPracticeSession(practiceSession);
                }
        }
    }

    private static class StaffPagerAdapter extends FragmentStatePagerAdapter {

        public StaffPagerAdapter(FragmentManager fm) {
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
            PageFragment pf = pageFragmentList.get(position);
            return pf.getPageTitle();
        }
    }

    private void setMenuDestinations() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                mDrawerLayout.closeDrawers();
                if (menuItem.getItemId() == R.id.nav_session) {
                    mPager.setCurrentItem(0, true);
                    return true;
                }

                if (menuItem.getItemId() == R.id.nav_profile) {
                    mPager.setCurrentItem(3, true);
                    return true;
                }
                if (menuItem.getItemId() == R.id.nav_messaging) {
                    mPager.setCurrentItem(1, true);
                    return true;
                }
                if (menuItem.getItemId() == R.id.nav_coaches) {
                    mPager.setCurrentItem(2, true);
                    return true;
                }


                return false;
            }
        });
    }

    private void getCachedPractices() {
        SnappyPractice.getPracticeSessions((MonApp) getApplication(), new SnappyPractice.DBReadListener() {
            @Override
            public void onDataRead(ResponseDTO response) {
                if (response.getPracticeSessionList().isEmpty()) {
                    getPracticeSessions();
                } else {
                    practiceSessionList = response.getPracticeSessionList();
                    snackbar.dismiss();
                    buildPages();
                }
                //getPracticeSessions();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void getPracticeSessions() {

        RequestDTO w = new RequestDTO(RequestDTO.GET_PLAYER_DATA);
        w.setPlayerID(SharedUtil.getPlayer(getApplicationContext()).getPlayerID());
        w.setZipResponse(true);
        OKUtil okUtil = new OKUtil();
        try {
            okUtil.sendGETRequest(ctx, w, this, new OKUtil.OKListener() {
                @Override
                public void onResponse(final ResponseDTO response) {
                    setRefreshActionButtonState(false);
                    snackbar.dismiss();
                    practiceSessionList = response.getPracticeSessionList();
                    if (response.getPracticeSessionList().isEmpty()) {
                        return;
                    }
                    buildPages();
                    SnappyPractice.addPracticeSessions((MonApp) getApplication(), response.getPracticeSessionList(), new SnappyPractice.DBWriteListener() {
                        @Override
                        public void onDataWritten() {
                            SnappyGeneral.addClubs((MonApp) getApplication(), response.getClubList(), null);
                            SnappyGeneral.addShotShapes((MonApp) getApplication(), response.getShotShapeList(), null);
                        }

                        @Override
                        public void onError(String message) {

                        }
                    });

                }

                @Override
                public void onError(String message) {

                }
            });
        } catch (OKHttpException e) {
            e.printStackTrace();
        }
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (mMenu != null) {
            final MenuItem refreshItem = mMenu.findItem(com.boha.golfpractice.library.R.id.action_refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(com.boha.golfpractice.library.R.layout.action_bar_progess);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }
}
