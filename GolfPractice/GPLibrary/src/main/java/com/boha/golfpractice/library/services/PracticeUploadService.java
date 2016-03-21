package com.boha.golfpractice.library.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.boha.golfpractice.library.activities.MonApp;
import com.boha.golfpractice.library.dto.HoleStatDTO;
import com.boha.golfpractice.library.dto.PracticeSessionDTO;
import com.boha.golfpractice.library.dto.RequestDTO;
import com.boha.golfpractice.library.dto.ResponseDTO;
import com.boha.golfpractice.library.util.MonLog;
import com.boha.golfpractice.library.util.OKUtil;
import com.boha.golfpractice.library.util.SnappyPractice;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PracticeUploadService extends IntentService {
        public PracticeUploadService() {
        super("PracticeUploadService");
    }

    static final String LOG = PracticeUploadService.class.getSimpleName();
    @Override
    protected void onHandleIntent(Intent intent) {
        MonLog.e(getApplicationContext(),LOG,"################ onHandleIntent");
        if (intent != null) {

            SnappyPractice.getCurrentPracticeSession(
                    (MonApp) getApplication(),
                    new SnappyPractice.DBReadListener() {
                @Override
                public void onDataRead(ResponseDTO response) {
                    sendPracticeSession(response.getPracticeSessionList().get(0));
                }

                @Override
                public void onError(String message) {

                }
            });

        }
    }

    private void sendPracticeSession(PracticeSessionDTO session) {
        //remove unused holestats
        List<HoleStatDTO> list = session.getHoleStatList();

        if (list.isEmpty()) {
            Log.w(LOG,"No HoleStats found in PracticeSession, quittin");
            return;
        } else {
            MonLog.e(getApplicationContext(),LOG,"Sending practiceSession with " + list.size() + " holeStats");
        }

        int numberOfHoles = 0;
        for (HoleStatDTO hs: list) {
            if (hs.getScore().intValue() > 0) {
                numberOfHoles++;
            }
        }
        session.setNumberOfHoles(numberOfHoles);
        session.setGolfCourseID(session.getGolfCourse().getGolfCourseID());

        aggregateSession(session);
        session.setGolfCourse(null);
        RequestDTO req = new RequestDTO(RequestDTO.ADD_PRACTICE_SESSION);
        req.setPracticeSession(session);
        req.setZipResponse(false);
        new DTask().execute(req);

    }
    private void aggregateSession(PracticeSessionDTO practiceSession) {

        int numberOfHoles = 0;
        for (HoleStatDTO hs : practiceSession.getHoleStatList()) {
            if (hs.getScore().intValue() > 0) {
                numberOfHoles++;
            }
        }
        practiceSession.setNumberOfHoles(numberOfHoles);
        practiceSession.setGolfCourseID(practiceSession.getGolfCourse().getGolfCourseID());
        int totalStrokes = 0, totalPar = 0,
                totalUnderPar = 0,
                totalOverPar = 0,
                totalMistakes = 0;
        for (HoleStatDTO hs : practiceSession.getHoleStatList()) {
            if (hs.getScore() == 0) {
                continue;
            }
            totalStrokes += hs.getScore();
            if (hs.getScore().intValue() == hs.getHole().getPar().intValue()) {
                totalPar++;
            }
            if (hs.getScore() < hs.getHole().getPar()) {
                totalUnderPar++;
            }
            if (hs.getScore() > hs.getHole().getPar()) {
                totalOverPar++;
            }

            int mistakes = 0;
            if (hs.getFairwayBunkerHit()) {
                mistakes++;
            }
            if (hs.getGreensideBunkerHit()) {
                mistakes++;
            }
            if (hs.getInRough()) {
                mistakes++;
            }
            if (hs.getInWater()) {
                mistakes++;
            }
            if (hs.getOutOfBounds()) {
                mistakes++;
            }
            if (hs.getNumberOfPutts() > 2) {
                mistakes++;
            }

            hs.setMistakes(mistakes);
            totalMistakes += mistakes;
        }
        practiceSession.setTotalStrokes(totalStrokes);
        practiceSession.setTotalMistakes(totalMistakes);


        practiceSession.setUnderPar(totalUnderPar);
        practiceSession.setOverPar(totalOverPar);
        practiceSession.setPar(totalPar);


    }
    private class DTask extends AsyncTask<RequestDTO,Void,ResponseDTO> {

        @Override
        protected ResponseDTO doInBackground(RequestDTO... params) {
            RequestDTO req = params[0];
            ResponseDTO resp = null;
            try {
                OKUtil util = new OKUtil();
                resp = util.sendSynchronousGET(getApplicationContext(),req);
                if (resp.getStatusCode() == 0) {
                    Log.w(LOG,"......Practice Session sent to server: OK");
                }
            } catch (Exception e) {
                Log.e(LOG,"Failed to do PracticeSession",e);
            }
            return resp;
        }
        @Override
        protected void onPostExecute(ResponseDTO resp) {
            if (resp == null) {
                return;
            }
            if (resp.getStatusCode() == 0) {
                MonLog.w(getApplicationContext(),LOG,
                        "Yebo! PracticeSession saved on server, broadcasting success");

                Intent m = new Intent(BROADCAST_PUS);
                m.putExtra("session",resp.getPracticeSessionList().get(0));
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(m);


            }
        }
    }
    public static final String BROADCAST_PUS = "com.boha.BROADCAST.PRACTICE";
}
