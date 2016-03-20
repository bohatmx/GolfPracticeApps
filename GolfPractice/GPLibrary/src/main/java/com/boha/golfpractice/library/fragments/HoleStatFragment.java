package com.boha.golfpractice.library.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.boha.golfpractice.library.R;
import com.boha.golfpractice.library.activities.MonApp;
import com.boha.golfpractice.library.dto.ClubDTO;
import com.boha.golfpractice.library.dto.ClubUsedDTO;
import com.boha.golfpractice.library.dto.HoleDTO;
import com.boha.golfpractice.library.dto.HoleStatDTO;
import com.boha.golfpractice.library.dto.PracticeSessionDTO;
import com.boha.golfpractice.library.dto.ShotShapeDTO;
import com.boha.golfpractice.library.services.PracticeUploadService;
import com.boha.golfpractice.library.util.HoleCounter;
import com.boha.golfpractice.library.util.MonLog;
import com.boha.golfpractice.library.util.SnappyPractice;

import java.util.Collections;
import java.util.List;

public class HoleStatFragment extends Fragment {
    private HoleStatListener mListener;
    private CheckBox fairwayHit;
    private CheckBox fairwayBunkerHit;
    private TextView distanceToPin;
    private CheckBox greenInRegulation;
    private TextView numberOfPutts;
    private CheckBox greensideBunkerHit ;
    private TextView score, clubTeeShot, clubToGreen;
    private String remarks;
    private CheckBox inRough;
    private CheckBox inWater ;
    private CheckBox outOfBounds;
    private TextView lengthOfPutt;
    FloatingActionButton fab;
    private int currentHoleNumber;

    private View distanceLayout, lengthPuttLayout, scoreLayout, numberPuttsLayout;
    private HoleCounter holeCounter;
    private List<ShotShapeDTO> shotShapeList;
    private List<ClubDTO> clubList;

    static final String LOG = HoleStatFragment.class.getSimpleName();


    public HoleStatFragment() {
    }

    public static HoleStatFragment newInstance(PracticeSessionDTO session) {
        HoleStatFragment f = new HoleStatFragment();
        Bundle b = new Bundle();
        b.putSerializable("session", session);
        f.setArguments(b);
        return f;
    }

    private PracticeSessionDTO practiceSession;
    private View view;
    private MonApp app;

    public void setApp(MonApp app) {
        this.app = app;
    }

    public void setPracticeSession(PracticeSessionDTO practiceSession) {
        this.practiceSession = practiceSession;
        if (practiceSession.getHoleStatList().isEmpty()) {
            for (HoleDTO h: practiceSession.getGolfCourse().getHoleList()) {
                HoleStatDTO hs = new HoleStatDTO();
                hs.setHole(h);
                hs.setHoleNumber(h.getHoleNumber());
                practiceSession.getHoleStatList().add(hs);
            }
            Collections.sort(practiceSession.getHoleStatList());
            MonLog.d(getActivity(),LOG,"HoleStats sorted by holeNumber");

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(LOG,"*********** onCreate");
        if (getArguments() != null) {
            practiceSession = (PracticeSessionDTO)getArguments().getSerializable("session");
            setPracticeSession(practiceSession);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w(LOG,"*********** onCreate");
        view = inflater.inflate(R.layout.holestat, container, false);
        setFields();
        currentHoleNumber = 1;
        setHoleStatFields();
        SnappyPractice.addCurrentPracticeSession(app,practiceSession,null);
        gDetect = new GestureDetectorCompat(getActivity(), new GestureListener(new SwipeListener() {
            @Override
            public void onForwardSwipe() {
                Log.i(LOG,"onForwardSwipe .............");

            }

            @Override
            public void onBackwardSwipe() {
                Log.d(LOG,"onBackwardSwipe .............");
            }
        }));
        puttsMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gDetect.onTouchEvent(motionEvent);
                return true;
            }
        });
        mistakesMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gDetect.onTouchEvent(motionEvent);
                return true;
            }
        });
        goodShitMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gDetect.onTouchEvent(motionEvent);
                return true;
            }
        });

        //receive notification when PracticeUploadService has completed work
        IntentFilter mStatusIntentFilter = new IntentFilter(
                PracticeUploadService.BROADCAST_PUS);
        PracticeBroadcastReceiver rec = new PracticeBroadcastReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                rec,mStatusIntentFilter);
        MonLog.w(getActivity(),LOG,"PracticeBroadcastReceiver has been registered");

        return view;
    }

    View goodShitMain,mistakesMain,puttsMain;
    private void setFields() {
        Log.w(LOG,"*********** setFields");
        clubTeeShot = (TextView)view.findViewById(R.id.clubTeeShot);
        clubToGreen = (TextView)view.findViewById(R.id.clubGreenInReg);
        goodShitMain = view.findViewById(R.id.goodShitMain);
        mistakesMain = view.findViewById(R.id.mistakesMain);
        puttsMain = view.findViewById(R.id.puttsMain);

        clubTeeShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTeeClub();
            }
        });
        clubToGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGreenClub();
            }
        });

        fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonLog.d(getActivity(),LOG,"PracticeUploadService starting ...");
                Intent m = new Intent(getActivity(), PracticeUploadService.class);
                getActivity().startService(m);

            }
        });
        distanceLayout = view.findViewById(R.id.distanceToGreenLayout);
        lengthPuttLayout = view.findViewById(R.id.puttLayout);
        numberPuttsLayout = view.findViewById(R.id.numberPuttsLayout);
        scoreLayout = view.findViewById(R.id.scoreLayout);

        holeCounter = (HoleCounter)view.findViewById(R.id.holeCounter);
        holeCounter.setListener(new HoleCounter.HoleCounterListener() {

            @Override
            public void onHoleNumberChanged(int number) {
                currentHoleNumber = number;
                MonLog.d(getActivity(),LOG,"............ onHoleNumberChanged: " + number);
                setHoleStatFields();
            }
        });
        fairwayHit = (CheckBox)view.findViewById(R.id.chkFairwayHit);
        fairwayBunkerHit = (CheckBox)view.findViewById(R.id.chkFairwayBunker);
        greensideBunkerHit = (CheckBox)view.findViewById(R.id.chkGreensideBunker);
        greenInRegulation = (CheckBox)view.findViewById(R.id.chkGreenInReg);
        outOfBounds = (CheckBox)view.findViewById(R.id.chkOutOfBounds);
        inRough = (CheckBox)view.findViewById(R.id.chkInRough);
        inWater = (CheckBox)view.findViewById(R.id.chkInWater);

        fairwayHit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getCurrentHoleStat().setFairwayHit(isChecked);
                mListener.onHoleStatUpdated(practiceSession);
            }
        });
        fairwayBunkerHit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getCurrentHoleStat().setFairwayBunkerHit(isChecked);
                mListener.onHoleStatUpdated(practiceSession);
            }
        });
        greenInRegulation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getCurrentHoleStat().setGreenInRegulation(isChecked);
                mListener.onHoleStatUpdated(practiceSession);
            }
        });
        outOfBounds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getCurrentHoleStat().setOutOfBounds(isChecked);
                mListener.onHoleStatUpdated(practiceSession);
            }
        });
        inRough.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getCurrentHoleStat().setInRough(isChecked);
                mListener.onHoleStatUpdated(practiceSession);
            }
        });
        inWater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getCurrentHoleStat().setInWater(isChecked);
                mListener.onHoleStatUpdated(practiceSession);
            }
        });
        greensideBunkerHit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getCurrentHoleStat().setGreensideBunkerHit(isChecked);
                mListener.onHoleStatUpdated(practiceSession);
            }
        });
        outOfBounds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getCurrentHoleStat().setOutOfBounds(isChecked);
                mListener.onHoleStatUpdated(practiceSession);
            }
        });

        distanceToPin = (TextView) view.findViewById(R.id.distanceToPin);
        numberOfPutts = (TextView) view.findViewById(R.id.putts);
        lengthOfPutt = (TextView) view.findViewById(R.id.puttLength);
        score = (TextView) view.findViewById(R.id.score);

        distanceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberDialog d = new NumberDialog();
                d.setDialogTitle("Distance to Pin");
                d.setNumberType(NumberDialog.DISTANCE_TO_PIN);
                d.setListener(new NumberDialog.NumberDialogListener() {
                    @Override
                    public void onNumberSelected(int number) {
                        getCurrentHoleStat().setDistanceToPin(number);
                        distanceToPin.setText("" + number);
                        mListener.onHoleStatUpdated(practiceSession);
                    }
                });
                d.show(getFragmentManager(),"DTP");
            }
        });
        numberPuttsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberDialog d = new NumberDialog();
                d.setDialogTitle("Number of Putts");
                d.setNumberType(NumberDialog.NUMBER_OF_PUTTS);
                d.setListener(new NumberDialog.NumberDialogListener() {
                    @Override
                    public void onNumberSelected(int number) {
                        getCurrentHoleStat().setNumberOfPutts(number);
                        numberOfPutts.setText("" + number);
                        mListener.onHoleStatUpdated(practiceSession);
                    }
                });
                d.show(getFragmentManager(),"NOP");
            }
        });
        lengthPuttLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberDialog d = new NumberDialog();
                d.setDialogTitle("Length of First Putt");
                d.setNumberType(NumberDialog.PUTT_LENGTH);
                d.setListener(new NumberDialog.NumberDialogListener() {
                    @Override
                    public void onNumberSelected(int number) {
                        getCurrentHoleStat().setLengthOfPutt(Double.valueOf("" + number));
                        lengthOfPutt.setText("" + number);
                        mListener.onHoleStatUpdated(practiceSession);
                    }
                });
                d.show(getFragmentManager(),"LOP");
            }
        });
        scoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberDialog d = new NumberDialog();
                d.setDialogTitle("Score");
                d.setNumberType(NumberDialog.SCORE);
                d.setListener(new NumberDialog.NumberDialogListener() {
                    @Override
                    public void onNumberSelected(int number) {
                        getCurrentHoleStat().setScore(number);
                        score.setText("" + number);
                        setHoleStatFields();
                        mListener.onHoleStatUpdated(practiceSession);
                    }
                });
                d.show(getFragmentManager(),"SCR");
            }
        });

    }

    private void clearFields() {
        fairwayBunkerHit.setChecked(false);
        outOfBounds.setChecked(false);
        inRough.setChecked(false);
        inWater.setChecked(false);
        greenInRegulation.setChecked(false);
        greensideBunkerHit.setChecked(false);
        distanceToPin.setText("0");
        numberOfPutts.setText("0");
        lengthOfPutt.setText("0");
        score.setText("0");
    }
    private void setHoleStatFields() {
        HoleStatDTO currentHoleStat = getCurrentHoleStat();
        if (currentHoleStat == null) {
            currentHoleStat = new HoleStatDTO();
        }
        Log.w(LOG,"*********** setHoleStatFields, " +
                "holeNumber: " + currentHoleStat.getHoleNumber());
        if (currentHoleStat.getFairwayBunkerHit() == Boolean.FALSE) {
            fairwayBunkerHit.setChecked(false);
        } else {
            fairwayBunkerHit.setChecked(true);
        }

        if (currentHoleStat.getOutOfBounds() == Boolean.FALSE) {
            outOfBounds.setChecked(false);
        } else {
            outOfBounds.setChecked(true);
        }

        if (currentHoleStat.getInRough() == Boolean.FALSE) {
            inRough.setChecked(false);
        } else {
            inRough.setChecked(true);
        }
        if (currentHoleStat.getInWater() == Boolean.FALSE) {
            inWater.setChecked(false);
        } else {
            inWater.setChecked(true);
        }

        if (currentHoleStat.getGreensideBunkerHit() == Boolean.FALSE) {
            greensideBunkerHit.setChecked(false);
        } else {
            greensideBunkerHit.setChecked(true);
        }

        if (currentHoleStat.getGreenInRegulation() == Boolean.FALSE) {
            greenInRegulation.setChecked(false);
        } else {
            greenInRegulation.setChecked(true);
        }


        if (currentHoleStat.getFairwayHit() == Boolean.FALSE) {
            fairwayHit.setChecked(false);
        } else {
            fairwayHit.setChecked(true);
        }


        score.setText("" + currentHoleStat.getScore());
        lengthOfPutt.setText("" + currentHoleStat.getLengthOfPutt());
        numberOfPutts.setText("" + currentHoleStat.getNumberOfPutts());
        distanceToPin.setText("" + currentHoleStat.getDistanceToPin());

        if (currentHoleStat.getHole() != null) {
            HoleDTO h = currentHoleStat.getHole();
            MonLog.e(getActivity(),LOG,"Hole: " + h.getHoleNumber() +
                    " Par: " + h.getPar() + " score: " + currentHoleStat.getScore());
            if (h.getPar().intValue() > currentHoleStat.getScore().intValue()) {
                score.setTextColor(ContextCompat.getColor(getActivity(),R.color.red_500));
            }
            if (h.getPar().intValue() == currentHoleStat.getScore().intValue()) {
                score.setTextColor(ContextCompat.getColor(getActivity(),R.color.black));
            }
            if (h.getPar().intValue() < currentHoleStat.getScore().intValue()) {
                score.setTextColor(ContextCompat.getColor(getActivity(),R.color.blue_400));
            }
        } else {
            MonLog.e(getActivity(),LOG,"---------------------current HoleDTO is NULL");
        }

        if (!currentHoleStat.getClubUsedList().isEmpty()) {
            try {
                ClubUsedDTO cu = currentHoleStat.getClubUsedList().get(0);
                clubTeeShot.setText(cu.getClub().getClubName()
                        + ", " + cu.getShotShape().getShape());
                if (currentHoleStat.getClubUsedList().size() > 1) {
                    cu = currentHoleStat.getClubUsedList().get(1);
                    clubToGreen.setText(cu.getClub().getClubName()
                            + ", " + cu.getShotShape().getShape());
                }
            } catch (Exception e) {

            }
        } else {
            clubTeeShot.setText("Club Used");
            clubToGreen.setText("Club Used");
        }

     }

    private HoleStatDTO getCurrentHoleStat() {
        for (HoleStatDTO hs: practiceSession.getHoleStatList()) {
            if (hs.getHoleNumber().intValue() == currentHoleNumber) {
                return hs;
            }
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HoleStatListener) {
            mListener = (HoleStatListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HoleStatListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void selectTeeClub() {
        ClubsAndShapesDialog d = new ClubsAndShapesDialog();
        d.setApp(app);
        d.setListener(new ClubsAndShapesDialog.ClubsAndShapesListener() {
            @Override
            public void onSelected(ClubUsedDTO clubUsed) {
                getCurrentHoleStat().getClubUsedList().add(clubUsed);
                clubTeeShot.setText(clubUsed.getClub().getClubName()
                        + ", " + clubUsed.getShotShape().getShape());
                mListener.onHoleStatUpdated(practiceSession);
            }
        });
        d.show(getFragmentManager(),"CSH");

    }
    private void selectGreenClub() {
        ClubsAndShapesDialog d = new ClubsAndShapesDialog();
        d.setApp(app);
        d.setListener(new ClubsAndShapesDialog.ClubsAndShapesListener() {
            @Override
            public void onSelected(ClubUsedDTO clubUsed) {
                getCurrentHoleStat().getClubUsedList().add(clubUsed);
                clubToGreen.setText(clubUsed.getClub().getClubName()
                        + ", " + clubUsed.getShotShape().getShape());
                mListener.onHoleStatUpdated(practiceSession);
            }
        });
        d.show(getFragmentManager(),"CSH");

    }

    private class PracticeBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            MonLog.e(getActivity(),LOG,"$$$$$$$$$$$$ PracticeBroadcastReceiver onReceive");
            PracticeSessionDTO m = (PracticeSessionDTO)intent.getSerializableExtra("session");
            practiceSession.setPracticeSessionID(m.getPracticeSessionID());
        }
    }


    public interface HoleStatListener {
        void onHoleStatUpdated(PracticeSessionDTO session);
    }
    public interface SwipeListener {
        void onForwardSwipe();

        void onBackwardSwipe();
    }

    private GestureDetectorCompat gDetect;

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private float flingMin = 100;
        private float velocityMin = 100;
        private SwipeListener listener;

        public GestureListener(SwipeListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            boolean forward = false;
            boolean backward = false;
            //calculate the change in X position within the fling gesture
            float horizontalDiff = event2.getX() - event1.getX();
            float verticalDiff = event2.getY() - event1.getY();

            float absHDiff = Math.abs(horizontalDiff);
            float absVDiff = Math.abs(verticalDiff);
            float absVelocityX = Math.abs(velocityX);
            float absVelocityY = Math.abs(velocityY);

            if (absHDiff > absVDiff && absHDiff > flingMin && absVelocityX > velocityMin) {
                if (horizontalDiff > 0)
                    backward = true;
                else
                    forward = true;
            } else if (absVDiff > flingMin && absVelocityY > velocityMin) {
                if (verticalDiff > 0)
                    backward = true;
                else
                    forward = true;
            }
            if (forward) {
                listener.onForwardSwipe();
            } else if (backward) {
                listener.onBackwardSwipe();
            }

            return true;
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

    }
}
