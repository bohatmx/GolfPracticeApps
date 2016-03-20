package com.boha.golfpractice.library.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.boha.golfpractice.library.R;
import com.boha.golfpractice.library.dto.PracticeSessionDTO;
import com.boha.golfpractice.library.fragments.HoleStatFragment;
import com.boha.golfpractice.library.util.MonLog;
import com.boha.golfpractice.library.util.SnappyPractice;
import com.boha.golfpractice.library.util.Util;

public class HoleStatActivity extends AppCompatActivity implements HoleStatFragment.HoleStatListener{

    HoleStatFragment holeStatFragment;
    FrameLayout frameLayout;
    PracticeSessionDTO practiceSession;
    static final String LOG = HoleStatActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hole_stat);
        practiceSession = (PracticeSessionDTO)getIntent().getSerializableExtra("session");
        addFragment();

        Util.setCustomActionBar(getApplicationContext(),
                getSupportActionBar(),practiceSession.getGolfCourseName(),"Practice Session",
                ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.golfball48));

    }

    private void addFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        holeStatFragment = HoleStatFragment.newInstance(practiceSession);
        holeStatFragment.setApp((MonApp)getApplication());

        ft.add(R.id.frameLayout, holeStatFragment);
        ft.commit();
    }

    @Override
    public void onHoleStatUpdated(PracticeSessionDTO session) {

        SnappyPractice.addCurrentPracticeSession((MonApp) getApplication(), session, new SnappyPractice.DBWriteListener() {
            @Override
            public void onDataWritten() {
                MonLog.i(getApplicationContext(),LOG,"current session updated in cache");
            }

            @Override
            public void onError(String message) {

            }
        });
    }
}
