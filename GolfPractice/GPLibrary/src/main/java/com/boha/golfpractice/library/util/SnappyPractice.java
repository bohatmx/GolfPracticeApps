package com.boha.golfpractice.library.util;

import android.os.AsyncTask;
import android.util.Log;

import com.boha.golfpractice.library.activities.MonApp;
import com.boha.golfpractice.library.dto.PracticeSessionDTO;
import com.boha.golfpractice.library.dto.ResponseDTO;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 3/18/16.
 */
public class SnappyPractice {

    private static DB snappydb;
    private static MonApp app;
    private static DBReadListener dbReadListener;
    private static DBWriteListener dbWriteListener;
    private static String LOG = SnappyPractice.class.getSimpleName();


    static final int ADD_PRACTICES = 1,
            GET_PRACTICE = 2,
            GET_PRACTICE_LIST = 3,
            ADD_FAVOURITE_PRACTICES = 4,
            GET_FAVOURITE_PRACTICES = 5,
            ADD_CURRENT_PRACTICE = 6,
            GET_CURRENT_PRACTICE = 7, DELETE_CURRENT_PRACTICE = 8;
    static final String GOLF_PRACTICE = "GOLF_PRACTICE",
            CURRENT_PRACTICE = "CURRENT_PRACTICE",
            FAVOURITE_GOLF_PRACTICE = "FAV_GOLF_PRACTICE";


    public static void addPracticeSessions(MonApp monApp, List<PracticeSessionDTO> list, DBWriteListener listener) {
        getDatabase(monApp);
        dbWriteListener = listener;
        PracticeSessionTask task = new PracticeSessionTask(list);
        task.execute();
    }

    public static void addFavoritePracticeSessions(MonApp monApp, List<PracticeSessionDTO> list, DBWriteListener listener) {
        getDatabase(monApp);
        dbWriteListener = listener;
        PracticeSessionTask task = new PracticeSessionTask(list, true);
        task.execute();
    }
    public static void addCurrentPracticeSession(MonApp monApp, PracticeSessionDTO session, DBWriteListener listener) {
        getDatabase(monApp);
        dbWriteListener = listener;
        PracticeSessionTask task = new PracticeSessionTask(session);
        task.execute();
    }

    public static void getPracticeSessions(MonApp monApp, DBReadListener listener) {
        getDatabase(monApp);
        dbReadListener = listener;
        PracticeSessionTask task = new PracticeSessionTask();
        task.execute();
    }

    public static void getFavouritePracticeSessions(MonApp monApp, DBReadListener listener) {
        getDatabase(monApp);
        dbReadListener = listener;
        PracticeSessionTask task = new PracticeSessionTask(true);
        task.execute();
    }
    public static void getCurrentPracticeSession(MonApp monApp, DBReadListener listener) {
        getDatabase(monApp);
        dbReadListener = listener;
        PracticeSessionTask task = new PracticeSessionTask(true,true);
        task.execute();
    }
    public static void deleteCurrentPracticeSession(MonApp monApp, DBReadListener listener) {
        getDatabase(monApp);
        dbReadListener = listener;
        PracticeSessionTask task = new PracticeSessionTask(3);
        task.execute();
    }

    private static class PracticeSessionTask extends AsyncTask<Void, Void, List<PracticeSessionDTO>> {
        List<PracticeSessionDTO> practiceSessionList;
        PracticeSessionDTO practiceSession;
        int type;
        boolean isError;

        public PracticeSessionTask() {
            type = GET_PRACTICE_LIST;
        }

        public PracticeSessionTask(boolean getFavouriteCourses) {
            type = GET_FAVOURITE_PRACTICES;
        }

        public PracticeSessionTask(List<PracticeSessionDTO> list) {
            type = ADD_PRACTICES;
            practiceSessionList = list;
        }

        public PracticeSessionTask(List<PracticeSessionDTO> list, boolean addFavouriteCourses) {
            type = ADD_FAVOURITE_PRACTICES;
            practiceSessionList = list;
        }

        public PracticeSessionTask(PracticeSessionDTO practiceSession) {
            type = ADD_CURRENT_PRACTICE;
            this.practiceSession = practiceSession;
        }

        public PracticeSessionTask(boolean getCurrentSession, boolean confirmed) {
            type = GET_CURRENT_PRACTICE;
        }

        public PracticeSessionTask(int delete) {
            type = DELETE_CURRENT_PRACTICE;
        }


        @Override
        protected List<PracticeSessionDTO> doInBackground(Void... params) {

            switch (type) {
                case ADD_PRACTICES:
                    for (PracticeSessionDTO gc : practiceSessionList) {
                        try {
                            snappydb.put(GOLF_PRACTICE + gc.getPracticeSessionID(), gc);
                        } catch (SnappydbException e) {
                            Log.e(LOG, "Failed practiceSession write", e);
                            isError = true;
                            break;
                        }
                    }
                    MonLog.d(app.getApplicationContext(), LOG, "practiceSessions added to cache: " + practiceSessionList.size());
                    break;
                case ADD_CURRENT_PRACTICE:
                    try {
                        snappydb.put(CURRENT_PRACTICE, practiceSession);
                    } catch (SnappydbException e) {
                        Log.e(LOG, "Failed practiceSession write", e);
                        isError = true;
                        break;
                    }

                    MonLog.e(app.getApplicationContext(), LOG, "current practiceSession added to cache: " + practiceSession);
                    break;
                case ADD_FAVOURITE_PRACTICES:
                    for (PracticeSessionDTO gc : practiceSessionList) {
                        try {
                            snappydb.put(FAVOURITE_GOLF_PRACTICE + gc.getPracticeSessionID(), gc);
                        } catch (SnappydbException e) {
                            Log.e(LOG, "Failed practiceSession write", e);
                            isError = true;
                            break;
                        }
                    }
                    MonLog.d(app.getApplicationContext(), LOG, "FAVOURITE_GOLF_PRACTICE added to cache: " + practiceSessionList.size());
                    break;
                case DELETE_CURRENT_PRACTICE:
                    try {
                        snappydb.del(CURRENT_PRACTICE);
                    } catch (SnappydbException e) {
                        Log.e(LOG, "Failed current practiceSession delete", e);
                        isError = true;
                        break;
                    }
                    MonLog.d(app.getApplicationContext(), LOG,
                            "DELETE_CURRENT_PRACTICE deleted from cache");
                    break;
                case GET_CURRENT_PRACTICE:
                    try {
                        practiceSessionList = new ArrayList<>();
                        PracticeSessionDTO x = snappydb.getObject(CURRENT_PRACTICE, PracticeSessionDTO.class);
                        practiceSessionList.add(x);

                    } catch (SnappydbException e) {
                        Log.e(LOG, "Failed to get practiceSession list", e);
                        isError = true;
                        break;
                    }
                    MonLog.d(app.getApplicationContext(), LOG, "GET_CURRENT_PRACTICE " + practiceSessionList.size());
                    break;
                case GET_PRACTICE_LIST:
                    try {
                        practiceSessionList = new ArrayList<>();
                        String[] keys = snappydb.findKeys(GOLF_PRACTICE);
                        for (String key : keys) {
                            PracticeSessionDTO gc = snappydb.getObject(key, PracticeSessionDTO.class);
                            practiceSessionList.add(gc);
                        }
                    } catch (SnappydbException e) {
                        Log.e(LOG, "Failed to get practiceSession list", e);
                        isError = true;
                        break;
                    }
                    MonLog.d(app.getApplicationContext(), LOG, "GOLF_PRACTICE list from cache: " + practiceSessionList.size());
                    break;
                case GET_FAVOURITE_PRACTICES:
                    try {
                        practiceSessionList = new ArrayList<>();
                        String[] keys = snappydb.findKeys(FAVOURITE_GOLF_PRACTICE);
                        for (String key : keys) {
                            PracticeSessionDTO gc = snappydb.getObject(key, PracticeSessionDTO.class);
                            practiceSessionList.add(gc);
                        }
                    } catch (SnappydbException e) {
                        Log.e(LOG, "Failed to get fav practiceSession list", e);
                        isError = true;
                    }
                    MonLog.d(app.getApplicationContext(), LOG, "FAVOURITE_GOLF_PRACTICE list from cache: " + practiceSessionList.size());
                    break;
                case GET_PRACTICE:

                    break;
            }

            return practiceSessionList;
        }

        @Override
        protected void onPostExecute(List<PracticeSessionDTO> list) {
            switch (type) {
                case ADD_PRACTICES:
                    if (dbWriteListener != null) {
                        if (isError) {
                            dbWriteListener.onError("Failed to add practiceSession to cache");
                        } else {
                            dbWriteListener.onDataWritten();
                        }

                    }
                    break;
                case ADD_CURRENT_PRACTICE:
                    if (dbWriteListener != null) {
                        if (isError) {
                            dbWriteListener.onError("Failed to add current practiceSession to cache");
                        } else {
                            dbWriteListener.onDataWritten();
                        }

                    }
                    break;
                case GET_CURRENT_PRACTICE:
                    if (dbReadListener != null) {
                        if (isError) {
                            dbReadListener.onError("Failed to get practiceSessions from cache");
                        } else {
                            ResponseDTO w = new ResponseDTO();
                            w.setPracticeSessionList(list);
                            dbReadListener.onDataRead(w);
                        }

                    }
                    break;
                case ADD_FAVOURITE_PRACTICES:
                    if (dbWriteListener != null) {
                        if (isError) {
                            dbWriteListener.onError("Failed to add practiceSession to cache");
                        } else {
                            dbWriteListener.onDataWritten();
                        }

                    }
                    break;
                case GET_PRACTICE_LIST:
                    if (dbReadListener != null) {
                        if (isError) {
                            dbReadListener.onError("Failed to get practiceSessions from cache");
                        } else {
                            ResponseDTO w = new ResponseDTO();
                            w.setPracticeSessionList(list);
                            dbReadListener.onDataRead(w);
                        }

                    }
                case GET_FAVOURITE_PRACTICES:
                    if (dbReadListener != null) {
                        if (isError) {
                            dbReadListener.onError("Failed to get practiceSessions from cache");
                        } else {
                            ResponseDTO w = new ResponseDTO();
                            w.setPracticeSessionList(list);
                            dbReadListener.onDataRead(w);
                        }

                    }

                    break;
                case GET_PRACTICE:

                    break;
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
