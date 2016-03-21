package com.boha.golfpractice.library.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.boha.golfpractice.library.R;
import com.boha.golfpractice.library.dto.ClubUsedDTO;
import com.boha.golfpractice.library.dto.HoleDTO;
import com.boha.golfpractice.library.dto.HoleStatDTO;
import com.boha.golfpractice.library.util.MonLog;

/**
 * A simple {@link Fragment} subclass.
 */
public class HoleStatViewerFragment extends Fragment implements PageFragment {

    static final String LOG = HoleStatViewerFragment.class.getSimpleName();

    HoleStatDTO currentHoleStat;
    View view;
    private CheckBox fairwayHit;
    private CheckBox fairwayBunkerHit;
    private TextView distanceToPin, holeNumber;
    private CheckBox greenInRegulation;
    private TextView numberOfPutts;
    private CheckBox greensideBunkerHit;
    private TextView score, clubTeeShot, clubToGreen;
    private CheckBox inRough;
    private CheckBox inWater;
    private CheckBox outOfBounds;
    private TextView lengthOfPutt;

    public HoleStatViewerFragment() {
        // Required empty public constructor
    }

    public static HoleStatViewerFragment newInstance(HoleStatDTO holeStat) {
        HoleStatViewerFragment f = new HoleStatViewerFragment();
        Bundle b = new Bundle();
        b.putSerializable("holeStat",holeStat);
        f.setArguments(b);
        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(LOG, "*********** onCreate");
        if (getArguments() != null) {
            currentHoleStat = (HoleStatDTO) getArguments().getSerializable("holeStat");

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, "*********** onCreateView");
        view = inflater.inflate(R.layout.holestat_viewer, container, false);
        setFields();
        return view;
    }
    private void setFields() {
        Log.d(LOG, "*********** setFields");
        clubTeeShot = (TextView) view.findViewById(R.id.clubTeeShot);
        clubToGreen = (TextView) view.findViewById(R.id.clubGreenInReg);
        holeNumber = (TextView) view.findViewById(R.id.holeNumber);
        holeNumber.setText("" + currentHoleStat.getHole().getHoleNumber());

        fairwayHit = (CheckBox) view.findViewById(R.id.chkFairwayHit);
        fairwayBunkerHit = (CheckBox) view.findViewById(R.id.chkFairwayBunker);
        greensideBunkerHit = (CheckBox) view.findViewById(R.id.chkGreensideBunker);
        greenInRegulation = (CheckBox) view.findViewById(R.id.chkGreenInReg);
        outOfBounds = (CheckBox) view.findViewById(R.id.chkOutOfBounds);
        inRough = (CheckBox) view.findViewById(R.id.chkInRough);
        inWater = (CheckBox) view.findViewById(R.id.chkInWater);

        distanceToPin = (TextView) view.findViewById(R.id.distanceToPin);
        numberOfPutts = (TextView) view.findViewById(R.id.putts);
        lengthOfPutt = (TextView) view.findViewById(R.id.puttLength);
        score = (TextView) view.findViewById(R.id.score);


        setHoleStatFields();
        disableFields();

    }

    private void disableFields() {
        fairwayBunkerHit.setEnabled(false);
        fairwayHit.setEnabled(false);
        outOfBounds.setEnabled(false);
        inRough.setEnabled(false);
        inWater.setEnabled(false);
        greenInRegulation.setEnabled(false);
        greensideBunkerHit.setEnabled(false);
        distanceToPin.setEnabled(false);
        numberOfPutts.setEnabled(false);
        lengthOfPutt.setEnabled(false);
        score.setEnabled(false);
    }

    private void setHoleStatFields() {
        if (currentHoleStat == null) {
            currentHoleStat = new HoleStatDTO();
        }
        Log.w(LOG, "*********** setHoleStatFields, " +
                "holeNumber: " + currentHoleStat.getHole().getHoleNumber());
        if (currentHoleStat.getFairwayBunkerHit() == false) {
            fairwayBunkerHit.setChecked(false);
        } else {
            fairwayBunkerHit.setChecked(true);
        }

        if (currentHoleStat.getOutOfBounds() == false) {
            outOfBounds.setChecked(false);
        } else {
            outOfBounds.setChecked(true);
        }

        if (currentHoleStat.getInRough() == false) {
            inRough.setChecked(false);
        } else {
            inRough.setChecked(true);
        }
        if (currentHoleStat.getInWater() == false) {
            inWater.setChecked(false);
        } else {
            inWater.setChecked(true);
        }

        if (currentHoleStat.getGreensideBunkerHit() == false) {
            greensideBunkerHit.setChecked(false);
        } else {
            greensideBunkerHit.setChecked(true);
        }

        if (currentHoleStat.getGreenInRegulation() == false) {
            greenInRegulation.setChecked(false);
        } else {
            greenInRegulation.setChecked(true);
        }


        if (currentHoleStat.getFairwayHit() == false) {
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
            MonLog.e(getActivity(), LOG, "Hole: " + h.getHoleNumber() +
                    " Par: " + h.getPar() + " score: " + currentHoleStat.getScore());
            if (h.getPar().intValue() > currentHoleStat.getScore().intValue()) {
                score.setTextColor(ContextCompat.getColor(getActivity(), R.color.red_500));
            }
            if (h.getPar().intValue() == currentHoleStat.getScore().intValue()) {
                score.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            }
            if (h.getPar().intValue() < currentHoleStat.getScore().intValue()) {
                score.setTextColor(ContextCompat.getColor(getActivity(), R.color.blue_400));
            }
        } else {
            MonLog.e(getActivity(), LOG, "---------------------current HoleDTO is NULL");
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
            clubTeeShot.setText("No info on Club");
            clubToGreen.setText("No info on Club");
        }

    }

    String title = "Hole Statistics";
    @Override
    public String getPageTitle() {
        return title;
    }
}
