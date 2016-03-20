package com.boha.golfpracticeapps.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.golfpractice.library.adapters.GolfCourseListActivity;
import com.boha.golfpractice.library.dto.PracticeSessionDTO;
import com.boha.golfpractice.library.fragments.PageFragment;
import com.boha.golfpractice.library.fragments.SessionListFragment;
import com.boha.golfpractice.library.util.DepthPageTransformer;
import com.boha.golfpractice.library.util.SharedUtil;
import com.boha.golfpractice.library.util.Util;
import com.boha.golfpracticeapps.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerMainActivity extends AppCompatActivity implements SessionListFragment.SessionListListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
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
//        if (circleImage != null) {
//            staff = SharedUtil.getCompanyStaff(getApplicationContext());
//            if (staff.getPhotoUploadList().isEmpty()) {
//                getRemotePhotos();
//            } else {
//                Picasso.with(ctx)
//                        .load(staff.getPhotoUploadList().get(0).getSecureUrl())
//                        .into(circleImage);
//            }
//        }
//        try {
//            Statics.setRobotoFontLight(getApplicationContext(), navText);
//            Drawable globe = ContextCompat.getDrawable(ctx, R.drawable.ic_action_globe);
//            globe.setColorFilter(themeDarkColor, PorterDuff.Mode.SRC_IN);
//            navigationView.getMenu().getItem(0).setIcon(globe);
//
//            Drawable face = ContextCompat.getDrawable(ctx, R.drawable.ic_action_face);
//            face.setColorFilter(themeDarkColor, PorterDuff.Mode.SRC_IN);
//            navigationView.getMenu().getItem(1).setIcon(face);
//
//            Drawable map = ContextCompat.getDrawable(ctx, R.drawable.ic_action_map);
//            map.setColorFilter(themeDarkColor, PorterDuff.Mode.SRC_IN);
//            navigationView.getMenu().getItem(2).setIcon(map);
//
//
//            navigationView.getMenu().getItem(3).getSubMenu().getItem(0).setIcon(face);
//            navigationView.getMenu().getItem(3).getSubMenu().getItem(1).setIcon(face);
//
//        } catch (Exception e) {
//            MonLog.e(ctx,LOG, "Problem colorizing menu items", e);
//

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
        Util.setCustomActionBar(ctx,getSupportActionBar(),"GolfPractice",getString(R.string.record_prac),
                ContextCompat.getDrawable(ctx, com.boha.golfpractice.library.R.drawable.golfball48));
        buildPages();
    }

    static List<PageFragment> pageFragmentList;
    SessionListFragment sessionListFragment;
    List<PracticeSessionDTO> practiceSessionList;

    private void buildPages() {
        pageFragmentList = new ArrayList<>();
        sessionListFragment = SessionListFragment.newInstance(practiceSessionList);

        pageFragmentList.add(sessionListFragment);
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

    @Override
    public void onSessionClicked(PracticeSessionDTO session) {

    }
    static final int NEW_SESSION_REQUESTED = 657;
    @Override
    public void onNewSessionRequested() {
        Intent m = new Intent(ctx, GolfCourseListActivity.class);
        startActivityForResult(m,NEW_SESSION_REQUESTED);
    }

    PracticeSessionDTO practiceSession;
    @Override
    public void onActivityResult(int reqCode,int resCode,Intent data) {
        switch (reqCode) {
            case NEW_SESSION_REQUESTED:
                if (resCode == RESULT_OK) {
                    practiceSession = (PracticeSessionDTO)data.getSerializableExtra("practiceSession");
                    sessionListFragment.addPracticeSession(practiceSession);
                    //todo cache PracticeSessionDTO in Snappy
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

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_player_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((PlayerMainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
