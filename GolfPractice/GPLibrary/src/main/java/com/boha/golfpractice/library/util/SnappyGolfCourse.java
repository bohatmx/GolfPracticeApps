package com.boha.golfpractice.library.util;

import android.os.AsyncTask;
import android.util.Log;

import com.boha.golfpractice.library.activities.MonApp;
import com.boha.golfpractice.library.dto.GolfCourseDTO;
import com.boha.golfpractice.library.dto.ResponseDTO;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by aubreymalabie on 3/18/16.
 */
public class SnappyGolfCourse {

    private static DB snappydb;
    private static MonApp app;
    private static DBReadListener dbReadListener;
    private static DBWriteListener dbWriteListener;
    private static String LOG = SnappyGolfCourse.class.getSimpleName();


    static final int ADD_COURSES = 1, GET_COURSE = 2, GET_COURSE_LIST = 3, ADD_FAVOURITE_COURSES = 4, GET_FAVOURITE_COURSES = 5;
    static final String GOLF_COURSE = "GOLF_COURSE", FAVOURITE_GOLF_COURSE = "FAV_GOLF_COURSE", PRACTICE_SESSION = "PRACTICE_SESSION", COACH = "COACH";


    public static void addGolfCourses(MonApp monApp, List<GolfCourseDTO> list,DBWriteListener listener) {
        getDatabase(monApp);
        dbWriteListener = listener;
        GolfCourseTask task = new GolfCourseTask(list);
        task.execute();
    }
    public static void addFavoriteGolfCourses(MonApp monApp, List<GolfCourseDTO> list,DBWriteListener listener) {
        getDatabase(monApp);
        dbWriteListener = listener;
        GolfCourseTask task = new GolfCourseTask(list,true);
        task.execute();
    }
    public static void getGolfCourses(MonApp monApp,DBReadListener listener) {
        getDatabase(monApp);
        dbReadListener = listener;
        GolfCourseTask task = new GolfCourseTask();
        task.execute();
    }
    public static void getFavouriteGolfCourses(MonApp monApp,DBReadListener listener) {
        getDatabase(monApp);
        dbReadListener = listener;
        GolfCourseTask task = new GolfCourseTask(true);
        task.execute();
    }

    private static class GolfCourseTask extends AsyncTask<Void, Void, List<GolfCourseDTO>> {
        Integer golfCourseID;
        List<GolfCourseDTO> golfCourseList;
        int type;
        boolean isError;

        public GolfCourseTask() {
            type = GET_COURSE_LIST;
        }

        public GolfCourseTask(boolean getFavouriteCourses) {
            type = GET_FAVOURITE_COURSES;
        }

        public GolfCourseTask(List<GolfCourseDTO> list) {
            type = ADD_COURSES;
            golfCourseList = list;
        }

        public GolfCourseTask(List<GolfCourseDTO> list, boolean addFavouriteCourses) {
            type = ADD_FAVOURITE_COURSES;
            golfCourseList = list;
        }

        public GolfCourseTask(Integer golfCourseID) {
            type = ADD_COURSES;
            this.golfCourseID = golfCourseID;
        }


        @Override
        protected List<GolfCourseDTO> doInBackground(Void... params) {
            switch (type) {
                case ADD_COURSES:
                    for (GolfCourseDTO gc : golfCourseList) {
                        try {
                            snappydb.put(GOLF_COURSE + gc.getGolfCourseID(), gc);
                        } catch (SnappydbException e) {
                            Log.e(LOG, "Failed golf course write", e);
                            isError = true;
                            break;
                        }
                    }
                    MonLog.d(app.getApplicationContext(),LOG,"GolfCourses added to cache: " + golfCourseList.size());
                    break;
                case ADD_FAVOURITE_COURSES:
                    for (GolfCourseDTO gc : golfCourseList) {
                        try {
                            snappydb.put(FAVOURITE_GOLF_COURSE + gc.getGolfCourseID(), gc);
                        } catch (SnappydbException e) {
                            Log.e(LOG, "Failed golf course write", e);
                            isError = true;
                            break;
                        }
                    }
                    MonLog.d(app.getApplicationContext(),LOG,"FAVOURITE_GOLF_COURSE added to cache: " + golfCourseList.size());
                    break;
                case GET_COURSE_LIST:
                    try {
                        golfCourseList = new ArrayList<>();
                        String[] keys = snappydb.findKeys(GOLF_COURSE);
                        for (String key : keys) {
                            GolfCourseDTO gc = snappydb.getObject(key, GolfCourseDTO.class);
                            golfCourseList.add(gc);
                        }
                    } catch (SnappydbException e) {
                        Log.e(LOG, "Failed to get golf course list", e);
                        isError = true;
                        break;
                    }
                    MonLog.d(app.getApplicationContext(),LOG,"GOLF_COURSE list from cache: " + golfCourseList.size());
                    break;
                case GET_FAVOURITE_COURSES:
                    try {
                        golfCourseList = new ArrayList<>();
                        String[] keys = snappydb.findKeys(FAVOURITE_GOLF_COURSE);
                        for (String key : keys) {
                            GolfCourseDTO gc = snappydb.getObject(key, GolfCourseDTO.class);
                            golfCourseList.add(gc);
                        }
                    } catch (SnappydbException e) {
                        Log.e(LOG, "Failed to get golf course list", e);
                        isError = true;
                    }
                    MonLog.d(app.getApplicationContext(),LOG,"FAVOURITE_GOLF_COURSE list from cache: " + golfCourseList.size());
                    break;
                case GET_COURSE:

                    break;
            }

            return golfCourseList;
        }

        @Override
        protected void onPostExecute(List<GolfCourseDTO> list) {
            if (list != null) {
                Collections.sort(list);
            }
            switch (type) {
                case ADD_COURSES:
                    if (dbWriteListener != null) {
                        if (isError) {
                            dbWriteListener.onError("Failed to add golf courses to cache");
                        } else {
                            dbWriteListener.onDataWritten();
                        }

                    }
                    break;
                case ADD_FAVOURITE_COURSES:
                    if (dbWriteListener != null) {
                        if (isError) {
                            dbWriteListener.onError("Failed to add golf courses to cache");
                        } else {
                            dbWriteListener.onDataWritten();
                        }

                    }
                    break;
                case GET_COURSE_LIST:
                    if (dbReadListener != null) {
                        if (isError) {
                            dbReadListener.onError("Failed to get golf courses from cache");
                        } else {
                            ResponseDTO w = new ResponseDTO();
                            w.setGolfCourseList(list);
                            dbReadListener.onDataRead(w);
                        }

                    }
                case GET_FAVOURITE_COURSES:
                    if (dbReadListener != null) {
                        if (isError) {
                            dbReadListener.onError("Failed to get golf courses from cache");
                        } else {
                            ResponseDTO w = new ResponseDTO();
                            w.setGolfCourseList(list);
                            dbReadListener.onDataRead(w);
                        }

                    }

                    break;
                case GET_COURSE:

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
