package com.boha.golfpractice.library.util;

import android.os.AsyncTask;
import android.util.Log;

import com.boha.golfpractice.library.activities.MonApp;
import com.boha.golfpractice.library.dto.ClubDTO;
import com.boha.golfpractice.library.dto.ResponseDTO;
import com.boha.golfpractice.library.dto.ShotShapeDTO;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.util.List;

/**
 * Created by aubreymalabie on 3/18/16.
 */
public class SnappyGeneral {

    private static DB snappydb;
    private static MonApp app;
    private static DBReadListener dbReadListener;
    private static DBWriteListener dbWriteListener;
    private static String LOG = SnappyGeneral.class.getSimpleName();


    static final int
            ADD_CLUBS = 1,
            ADD_SHOT_SHAPES = 2,
            GET_ALL_LOOKUPS = 3;

    static final String
            CLUB = "CLUB",
            SHOT_SHAPE = "SHOT_SHAPE";


    public static void addClubs(MonApp monApp, List<ClubDTO> list, DBWriteListener listener) {
        getDatabase(monApp);
        dbWriteListener = listener;
        GeneralTask task = new GeneralTask(list,true);
        task.execute();
    }

    public static void addShotShapes(MonApp monApp, List<ShotShapeDTO> list, DBWriteListener listener) {
        getDatabase(monApp);
        dbWriteListener = listener;
        GeneralTask task = new GeneralTask(list);
        task.execute();
    }


    public static void getLookups(MonApp monApp, DBReadListener listener) {
        getDatabase(monApp);
        dbReadListener = listener;
        GeneralTask task = new GeneralTask();
        task.execute();
    }

    private static class GeneralTask extends AsyncTask<Void, Void, ResponseDTO> {
        List<ClubDTO> clubList;
        List<ShotShapeDTO> shotShapeList;
        int type;
        boolean isError;

        public GeneralTask() {
            type = GET_ALL_LOOKUPS;
        }


        public GeneralTask(List<ClubDTO> list, boolean x) {
            this.clubList = list;
            type = ADD_CLUBS;
        }
        public GeneralTask(List<ShotShapeDTO> list) {
            this.shotShapeList = list;
            type = ADD_SHOT_SHAPES;
        }



        @Override
        protected ResponseDTO doInBackground(Void... params) {
            ResponseDTO resp = new ResponseDTO();
            switch (type) {
                case ADD_CLUBS:
                    for (ClubDTO gc : clubList) {
                        try {
                            snappydb.put(CLUB + gc.getClubID(), gc);
                        } catch (SnappydbException e) {
                            Log.e(LOG, "Failed club write", e);
                            isError = true;
                            break;
                        }
                    }
                    MonLog.d(app.getApplicationContext(), LOG, "clubs added to cache: " + clubList.size());
                    break;
                case ADD_SHOT_SHAPES:
                    for (ShotShapeDTO gc : shotShapeList) {
                        try {
                            snappydb.put(SHOT_SHAPE + gc.getShotShapeID(), gc);
                        } catch (SnappydbException e) {
                            Log.e(LOG, "Failed shotShape write", e);
                            isError = true;
                            break;
                        }
                    }
                    MonLog.d(app.getApplicationContext(), LOG, "shotShapes added to cache: " + shotShapeList.size());
                    break;

                case GET_ALL_LOOKUPS:
                    try {

                        String[] keys = snappydb.findKeys(CLUB);
                        for (String key : keys) {
                            ClubDTO gc = snappydb.getObject(key, ClubDTO.class);
                            resp.getClubList().add(gc);
                        }
                        keys = snappydb.findKeys(SHOT_SHAPE);
                        for (String key : keys) {
                            ShotShapeDTO gc = snappydb.getObject(key, ShotShapeDTO.class);
                            resp.getShotShapeList().add(gc);
                        }
                    } catch (SnappydbException e) {
                        Log.e(LOG, "Failed to get lookups list", e);
                        isError = true;
                        break;
                    }
                    MonLog.d(app.getApplicationContext(), LOG, "clubs and shotShapes list from cache: ");
                    break;

            }

            return resp;
        }

        @Override
        protected void onPostExecute(ResponseDTO resp) {
            switch (type) {
                case ADD_CLUBS:
                    if (dbWriteListener != null) {
                        if (isError) {
                            dbWriteListener.onError("Failed to add clubs to cache");
                        } else {
                            dbWriteListener.onDataWritten();
                        }

                    }
                    break;
                case ADD_SHOT_SHAPES:
                    if (dbWriteListener != null) {
                        if (isError) {
                            dbWriteListener.onError("Failed to add shotShapes to cache");
                        } else {
                            dbWriteListener.onDataWritten();
                        }

                    }
                    break;

                case GET_ALL_LOOKUPS:
                    if (dbReadListener != null) {
                        if (isError) {
                            dbReadListener.onError("Failed to get practiceSessions from cache");
                        } else {
                            dbReadListener.onDataRead(resp);
                        }

                    }

            }
        }
    }

    public interface DBWriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface DBReadListener {
        void onDataRead(ResponseDTO response);

        void onError(String message);
    }

    private static void getDatabase(MonApp monApp) {
        app = monApp;
        try {
            if (snappydb == null || !snappydb.isOpen()) {
                snappydb = app.getSnappyDB();
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }
}
