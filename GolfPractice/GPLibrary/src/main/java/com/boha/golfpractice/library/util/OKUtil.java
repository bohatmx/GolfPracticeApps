package com.boha.golfpractice.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Environment;
import android.util.Log;

import com.boha.golfpractice.library.dto.RequestDTO;
import com.boha.golfpractice.library.dto.RequestList;
import com.boha.golfpractice.library.dto.ResponseDTO;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * Created by aubreyM on 16/01/15.
 */

/**
 * Helper class to wrap OKHttp library and provide methods
 * that access data from Monitor Platform server
 */
public class OKUtil {


    static Gson gson = new Gson();
    public static final String DEV_URL = "http://192.168.1.233:40405/gp/prac";
    public static final String PROD_URL = "http://bohamaker.com:3030/gp/prac";

    public static final String DEV_URL_CACHED = "http://192.168.1.233:40405/mp/cachedRequests";
    public static final String PROD_URL_CACHED = "http://bohamaker.com:3030/mp/cachedRequests";

    static final String FAILED_RESPONSE_NOT_SUCCESSFUL = "Request failed. Response not successful";
    static final String FAILED_DATA_EXTRACTION = "Request failed. Unable to extract data from response";
    static final String FAILED_IO = "Request failed. Communication links are not working";
    static final String FAILED_UNPACK = "Unable to unpack zipped response";

    static final String LOG = OKUtil.class.getSimpleName();

    public interface OKListener {
        void onResponse(ResponseDTO response);

        void onError(String message);
    }


    private void configureTimeouts(OkHttpClient client) {
        client.setConnectTimeout(40, TimeUnit.SECONDS);
        client.setReadTimeout(60, TimeUnit.SECONDS);
        client.setWriteTimeout(40, TimeUnit.SECONDS);

    }

    private String getURL(Context ctx) {
        boolean isDebuggable = 0 != (ctx.getApplicationInfo().flags
                &= ApplicationInfo.FLAG_DEBUGGABLE);
        if (isDebuggable) {
            return DEV_URL;
        } else {
            return PROD_URL;
        }
    }

    private String getURLCached(Context ctx) {
        boolean isDebuggable = 0 != (ctx.getApplicationInfo().flags
                &= ApplicationInfo.FLAG_DEBUGGABLE);
        if (isDebuggable) {
            return DEV_URL_CACHED;
        } else {
            return PROD_URL_CACHED;
        }
    }

    /**
     * Async GET call with OKListener to return data to caller
     *
     * @param req
     * @param listener
     * @throws OKHttpException
     */
    public void sendGETRequest(final Context ctx, final RequestDTO req, Activity activity,
                               final OKListener listener) throws OKHttpException {
        String mURL = getURL(ctx);
        OkHttpClient client = new OkHttpClient();
        configureTimeouts(client);

        HttpUrl.Builder urlBuilder = HttpUrl.parse(mURL).newBuilder();
        urlBuilder.addQueryParameter("JSON", gson.toJson(req));
        String url = urlBuilder.build().toString();

        Log.w(LOG, "### sending request to server, requestType: "
                + req.getRequestType()
                + "\n" + url);

        Request okHttpRequest = new Request.Builder()
                .url(url)
                .build();

        execute(client, okHttpRequest, req.isZipResponse(), activity,listener);
    }

    public void sendPOSTRequest(final Context ctx,
                                final RequestList req, Activity activity,
                                final OKListener listener) throws OKHttpException {

        String url = getURLCached(ctx);
        OkHttpClient client = new OkHttpClient();
        configureTimeouts(client);
        RequestBody body = new FormEncodingBuilder()
                .add("JSON", gson.toJson(req))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Log.w(LOG, "### sending request to server, requestList: "
                + req.getRequests().size()
                + "\n" + url);
        execute(client, request, false, activity,listener);


    }

    public ResponseDTO sendSynchronousGET(final Context ctx, final RequestDTO req) throws OKHttpException, IOException {
        final long start = System.currentTimeMillis();
        String mURL = getURL(ctx);
        OkHttpClient client = new OkHttpClient();
        configureTimeouts(client);

        HttpUrl.Builder urlBuilder = HttpUrl.parse(mURL).newBuilder();
        urlBuilder.addQueryParameter("JSON", gson.toJson(req));
        String url = urlBuilder.build().toString();

        Log.w(LOG, "### sending request to server, requestType: "
                + req.getRequestType()
                + "\n" + url);

        Request okHttpRequest = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(okHttpRequest).execute();
        ResponseDTO m;
        if (req.isZipResponse()) {
            m = processZipResponse(response);
        } else {
            m = processResponse(response);
        }

        final long end = System.currentTimeMillis();
        Log.e(LOG, "### Server responded, " + okHttpRequest.urlString() + "\nround trip elapsed: " + getElapsed(start, end)
                + ", server elapsed: " + m.getElapsedSeconds()
                + ", statusCode: " + m.getStatusCode()
                + "\nmessage: " + m.getMessage());
        return m;
    }

    public void sendPOSTRequest(final Context ctx,
                                final RequestDTO req, Activity activity,
                                final OKListener listener) throws OKHttpException {

        String url = getURL(ctx);
        OkHttpClient client = new OkHttpClient();
        configureTimeouts(client);
        RequestBody body = new FormEncodingBuilder()
                .add("JSON", gson.toJson(req))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Log.w(LOG, "### sending request to server, requestType: "
                + req.getRequestType()
                + "\n" + url);
        execute(client, request, req.isZipResponse(), activity,listener);


    }

    private void execute(OkHttpClient client, final Request req,
                         final boolean zipResponseRequested,
                         final Activity activity,
                         final OKListener listener) {

        final long start = System.currentTimeMillis();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                long end = System.currentTimeMillis();
                Log.e(LOG, "### Server responded with ERROR, round trip elapsed: " + getElapsed(start, end), e);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onError(FAILED_IO);
                    }
                });

            }

            @Override
            public void onResponse(Response response) throws IOException {

                if (!response.isSuccessful()) {
                    Log.e(LOG, "%%%%%% ERROR from OKHttp "
                            + response.networkResponse().toString()
                            + response.message() + "\nresponse.isSuccessful: " + response.isSuccessful());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError(FAILED_RESPONSE_NOT_SUCCESSFUL);
                            return;
                        }
                    });

                }
                final ResponseDTO serverResponse;
                if (zipResponseRequested) {
                    serverResponse = processZipResponse(response);

                } else {
                    serverResponse = processResponse(response);
                }
                response.body().close();

                long end = System.currentTimeMillis();
                Log.e(LOG, "### Server responded, " + req.urlString() + "\nround trip elapsed: " + getElapsed(start, end)
                        + ", server elapsed: " + serverResponse.getElapsedSeconds()
                        + ", statusCode: " + serverResponse.getStatusCode()
                        + "\nmessage: " + serverResponse.getMessage());

                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                if (serverResponse.getStatusCode() == 0) {
                                    listener.onResponse(serverResponse);
                                } else {
                                    listener.onError(serverResponse.getMessage());
                                }
                            }
                        }
                    });
                } else {
                    if (listener != null) {
                        if (serverResponse.getStatusCode() == 0) {
                            listener.onResponse(serverResponse);
                        } else {
                            listener.onError(serverResponse.getMessage());
                        }
                    }
                }

            }
        };

        client.newCall(req).enqueue(callback);

    }

    private ResponseDTO processResponse(Response response) {
        ResponseDTO serverResponse = new ResponseDTO();
        try {
            String json = response.body().string();
            Log.w(LOG,json);
            serverResponse = gson.fromJson(json, ResponseDTO.class);
            Log.w(LOG, "### Data received: " + getLength(json.length()));

        } catch (Exception e) {
            Log.e(LOG, "OKUtil Failed to get data from response body", e);
            serverResponse.setStatusCode(88);
            serverResponse.setMessage("Failed to get server response data");
        }
        return serverResponse;
    }

    private ResponseDTO processZipResponse(Response response) {
        final File directory = Environment.getExternalStorageDirectory();
        ResponseDTO serverResponse = new ResponseDTO();
        try {
            File file = new File(directory, "myData.zip");
            File uFile = new File(directory, "data.json");

            InputStream is = response.body().byteStream();
            OutputStream os = new FileOutputStream(file);
            IOUtils.copy(is, os);
            String json = ZipUtil.unpack(file, uFile);
            Log.i(LOG, "### Data received size: " + getLength(file.length())
                    + " ==> unpacked: " + getLength(uFile.length()));
            serverResponse = gson.fromJson(json, ResponseDTO.class);
            try {
                boolean OK1 = file.delete();
                boolean OK2 = uFile.delete();
                if (OK1 && OK2) {
                    Log.e(LOG, "Temporary unpacking files deleted OK");
                }
            } catch (Exception e) {
                Log.e(LOG, "Temporary unpacking files NOT deleted. " + e.getMessage());
            }

        } catch (Exception e) {
            Log.e(LOG, "Failed to unpack file", e);
            try {
                response.body().close();
            } catch (IOException e1) {
                Log.e(LOG, "failed", e);
            }

        }

        return serverResponse;
    }

    String getElapsed(long start, long end) {
        BigDecimal bs = new BigDecimal(start);
        BigDecimal be = new BigDecimal(end);
        BigDecimal a = be.subtract(bs).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP);

        return a.doubleValue() + " seconds";
    }

    String getLength(long length) {
        BigDecimal bs = new BigDecimal(length);
        BigDecimal a = bs.divide(new BigDecimal(1024), 2, BigDecimal.ROUND_HALF_UP);

        return a.doubleValue() + " KB";
    }

}
