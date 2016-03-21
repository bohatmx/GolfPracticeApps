package com.boha.golfpractice.library.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.boha.golfpractice.library.R;
import com.boha.golfpractice.library.activities.MonApp;
import com.boha.golfpractice.library.dto.PlayerDTO;
import com.boha.golfpractice.library.dto.PracticeSessionDTO;
import com.boha.golfpractice.library.dto.RequestDTO;
import com.boha.golfpractice.library.dto.ResponseDTO;
import com.boha.golfpractice.library.util.MonLog;
import com.boha.golfpractice.library.util.OKHttpException;
import com.boha.golfpractice.library.util.OKUtil;
import com.boha.golfpractice.library.util.SnappyPractice;

import java.text.DecimalFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SessionSummaryFragment extends Fragment implements PageFragment{

    protected TextView golfCourse, sessionDate, count, date;
    protected TextView numHoles, numStrokes, numUnder, numOver, par, numMistakes;
    protected TextView numHolesAvg, numStrokesAvg, numUnderAvg, numOverAvg, parAvg, numMistakesAvg;
    ImageView iconSearch;
    SeekBar seekBar;
    View view;
    MonApp app;
    PlayerDTO player;
    List<PracticeSessionDTO> practiceSessionList;
    static final String LOG = SessionSummaryFragment.class.getSimpleName();

    public SessionSummaryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.session_summary, container, false);
        setFields();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        MonLog.w(getActivity(),LOG,"########### onResume");
        SnappyPractice.getPracticeSessions(app, new SnappyPractice.DBReadListener() {
            @Override
            public void onDataRead(ResponseDTO response) {
                practiceSessionList = response.getPracticeSessionList();
                if (view != null) {
                    setSummaryData();
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void setApp(MonApp app) {
        this.app = app;
        MonLog.d(getActivity(),LOG,"########### setApp");

    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }

    private void setSummaryData() {
        MonLog.d(getActivity(),LOG,"########### setSummaryData");
        count.setText(df.format(practiceSessionList.size()));
        int totalStrokes = 0,
                totalPar = 0,
                totalUnderPar = 0,
                totalOverPar = 0,
                totalHoles = 0,
                totalMistakes = 0;

        for (PracticeSessionDTO p: practiceSessionList) {
            totalHoles += p.getNumberOfHoles();
            totalStrokes += p.getTotalStrokes();
            totalPar += p.getPar();
            totalUnderPar += p.getUnderPar();
            totalOverPar += p.getOverPar();
            totalMistakes += p.getTotalMistakes();
        }
        numHoles.setText(df.format(totalHoles));
        numStrokes.setText(df.format(totalStrokes));
        numUnder.setText(df.format(totalUnderPar));
        numOver.setText(df.format(totalOverPar));
        par.setText(df.format(totalPar));
        numMistakes.setText(df.format(totalMistakes));

        Double hAvg = Double.parseDouble("" + totalHoles) / Double.parseDouble("" + practiceSessionList.size());
        numHolesAvg.setText(dfAvg.format(hAvg));

        Double strokeAvg = Double.parseDouble("" + totalStrokes) / Double.parseDouble("" + totalHoles);
        numStrokesAvg.setText(dfAvg.format(strokeAvg));

        Double underAvg = Double.parseDouble("" + totalUnderPar) / Double.parseDouble("" + totalHoles) * 100;
        numUnderAvg.setText(dfAvg.format(underAvg) + "%");

        Double overAvg = Double.parseDouble("" + totalOverPar) / Double.parseDouble("" + totalHoles) * 100;
        numOverAvg.setText(dfAvg.format(overAvg) + "%");

        Double pAvg = Double.parseDouble("" + totalPar) / Double.parseDouble("" + totalHoles) * 100;
        parAvg.setText(dfAvg.format(pAvg) + "%");

        Double misAvg = Double.parseDouble("" + totalMistakes) / Double.parseDouble("" + totalHoles) * 100;
        numMistakesAvg.setText(dfAvg.format(misAvg) + "%");
    }
    static final DecimalFormat df = new DecimalFormat("###,###,###");
    static final DecimalFormat dfAvg = new DecimalFormat("###,###,##0.00");
    private void setFields() {
        iconSearch = (ImageView) view
                .findViewById(R.id.iconSearch);
        iconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRemoteData();
            }
        });
        seekBar = (SeekBar) view
                .findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                date.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        date = (TextView) view
                .findViewById(R.id.date);
        golfCourse = (TextView) view
                .findViewById(R.id.golfCourse);
        sessionDate = (TextView) view
                .findViewById(R.id.date);
        numHoles = (TextView) view
                .findViewById(R.id.holeCount);
        numStrokes = (TextView) view
                .findViewById(R.id.strokes);

        numUnder = (TextView) view
                .findViewById(R.id.underPar);
        numOver = (TextView) view
                .findViewById(R.id.overpar);
        par = (TextView) view
                .findViewById(R.id.par);
        numMistakes = (TextView) view
                .findViewById(R.id.mistakes);
        count = (TextView) view
                .findViewById(R.id.count);

        numHolesAvg = (TextView) view
                .findViewById(R.id.holeAverage);
        numStrokesAvg = (TextView) view
                .findViewById(R.id.strokeAverage);

        numUnderAvg = (TextView) view
                .findViewById(R.id.underParAverage);
        numOverAvg = (TextView) view
                .findViewById(R.id.overParAverage);
        parAvg = (TextView) view
                .findViewById(R.id.parAverage);
        numMistakesAvg = (TextView) view
                .findViewById(R.id.mistakeAverage);
    }

    @Override
    public String getPageTitle() {
        return "";
    }
    private void getRemoteData() {
        RequestDTO w = new RequestDTO(RequestDTO.GET_SESSIONS_IN_PERIOD);
        w.setPlayerID(player.getPlayerID());
        w.setDays(seekBar.getProgress());
        w.setZipResponse(true);

        OKUtil util = new OKUtil();
        try {
            util.sendGETRequest(getActivity(), w, getActivity(), new OKUtil.OKListener() {
                @Override
                public void onResponse(ResponseDTO response) {
                    practiceSessionList = response.getPracticeSessionList();
                    setSummaryData();
                }

                @Override
                public void onError(String message) {

                }
            });
        } catch (OKHttpException e) {
            e.printStackTrace();
        }

    }
}
