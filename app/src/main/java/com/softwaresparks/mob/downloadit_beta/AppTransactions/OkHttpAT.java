package com.softwaresparks.mob.downloadit_beta.AppTransactions;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.softwaresparks.mob.downloadit_beta.AppUtilities.OnGetBucketResponse;
import com.softwaresparks.mob.downloadit_beta.AppViews.LobbyActivity;
import com.softwaresparks.mob.downloadit_beta.AppViews.RawtestActivity;
import com.softwaresparks.mob.downloadit_beta.R;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by xLipse on 1/17/2017.
 */

public class OkHttpAT extends AsyncTask<String, Integer, Integer> {

    private String generatedUrl;
    private String pre_signature;
    private String authorization;
    private String gmt_date;
    private boolean gmt_acquire;
    private boolean data_acquire;
    private boolean connectionSuccess;

    private ContentResolver cr;

    private RawtestActivity raw;
    private LobbyActivity lobby;
    private OkHttpClient pre_client;
    private Request request;

    private String body;
    private String reqtyp;
    private Uri uri;

    private String result_toast;

    OnGetBucketResponse onGetBucketResponse;

    ProgressDialog progress;

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    public OkHttpAT(LobbyActivity lobby, String reqtyp) {
        clientInit(lobby, reqtyp);
    }

    public OkHttpAT(LobbyActivity lobby, String reqtyp, Uri uri) {
        clientInit(lobby, reqtyp);
        cr = lobby.getContentResolver();
        this.uri = uri;
    }

    public OkHttpAT(OnGetBucketResponse onGetBucketResponse) {
        this.onGetBucketResponse = onGetBucketResponse;
    }

    private void clientInit(LobbyActivity lobby, String reqtyp) {
        this.lobby = lobby;
        this.reqtyp = reqtyp;

        authorization = lobby.getResources().getString(R.string.auth);
        gmt_date = lobby.getResources().getString(R.string.gmtdate);

        connectionSuccess = false;

        pre_client = new OkHttpClient();

    }

//    public OkHttpAT(RawtestActivity raw, String reqtyp) {
//        clientInit(raw, reqtyp);
//    }
//
//    public OkHttpAT(RawtestActivity raw, String reqtyp, Uri uri) {
//        clientInit(raw, reqtyp);
//        cr = lobby.getContentResolver();
//        this.uri = uri;
//    }
//
//    private void clientInit(RawtestActivity raw, String reqtyp) {
//        this.raw = raw;
//        this.reqtyp = reqtyp;
//
//        authorization = lobby.getResources().getString(R.string.auth);
//        gmt_date = lobby.getResources().getString(R.string.gmtdate);
//
//        connectionSuccess = false;
//
//        pre_client = new OkHttpClient();
//
//    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (reqtyp.equals("GET")) {
            progress = ProgressDialog.show(lobby, "Connecting...", "Loading Contents...");
        } else if (reqtyp.equals("PUT")) {
            //raw.upload_pb.setMax(100);
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        //raw.upload_pb.setProgress(values[0]);
    }

    @Override
    protected Integer doInBackground(String... params) {

        gmt_acquire = false;
        data_acquire = false;

        request = new Request.Builder()
                .url(S3Constants.SERVER_URL)
                .build();

        try (Response response = pre_client.newCall(request).execute()) {
            connectionSuccess = true;

            Log.d(S3Constants.LOBBY_TAG, String.valueOf(response) + " ---- 1");
            body = response.body().string();

            gmt_acquire = true;
            gmt_date = response.header("Date");

        } catch (IOException e) {
            connectionSuccess = false;
            Log.d(S3Constants.LOBBY_TAG, String.valueOf(e));

        }

        if (gmt_acquire) {

            S3Authorization auth = new S3Authorization(lobby);

            String filetype = lobby.getResources().getString(R.string.filetype);
            String object_uri = lobby.getResources().getString(R.string.objectfile);

            if (reqtyp.equals("GET")) {
                Log.d(S3Constants.LOBBY_TAG, "OkHttpAT GET");
                generatedUrl = auth.getGenerateURL(reqtyp, gmt_date, "/");

            } else if (reqtyp.equals("PUT")) {
                Log.d(S3Constants.LOBBY_TAG, "OkHttpAT PUT");

                if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                    filetype = MimeTypeMap.getSingleton().getExtensionFromMimeType(lobby.getContentResolver().getType(uri));
                } else {
                    String ext = MimeTypeMap.getFileExtensionFromUrl(new File(uri.getPath()).getPath());
                    filetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
                }

                object_uri = uri.getLastPathSegment();

                Log.d(S3Constants.LOBBY_TAG, " >>>>>>> CONTENT-TYPE: " + filetype);
                Log.d(S3Constants.LOBBY_TAG, " >>>>>>> FILENAME: " + object_uri);

                generatedUrl = auth.getGenerateURL(reqtyp, filetype, gmt_date, object_uri);

            }

            pre_signature = auth.getPreSignature();

            if (!pre_signature.equals(lobby.getResources().getString(R.string.s3presig))) {

                authorization = "AWS " + S3Constants.ACCESS_KEY + ":" + pre_signature;

                if (reqtyp.equals("GET")) {
                    request = new Request.Builder()
                            .url(S3Constants.SERVER_URL + S3Constants.BUCKET + "/")
                            .addHeader("Authorization", authorization)
                            .addHeader("Date", gmt_date)
                            .build();

                } else if (reqtyp.equals("PUT")) {

                    MediaType content_type = MediaType.parse(filetype);
                    RequestBody file_body = RequestBody.create(content_type, new File(uri.getPath()));

                    request = new Request.Builder()
                            .addHeader("Authorization", authorization)
                            .addHeader("Date", gmt_date)
                            .addHeader("Content-Type", filetype)
                            .url(S3Constants.SERVER_URL + S3Constants.BUCKET + "/" + object_uri)
                            .put(file_body)
                            .build();

                }

                try (Response response = pre_client.newCall(request).execute()) {
                    Log.d(S3Constants.LOBBY_TAG, String.valueOf(response) + " ---- 2");

                    body = response.body().string();

                    result_toast = body;
                    data_acquire = true;

                } catch (IOException e) {
                    Log.d(S3Constants.LOBBY_TAG, " --> " + String.valueOf(e));
                    result_toast = String.valueOf(e);
                }

                Log.d(S3Constants.LOBBY_TAG, "----------------------- Pre Signature SUCCESS!");
                Log.d(S3Constants.LOBBY_TAG, "pre_signature = " + pre_signature);
                Log.d(S3Constants.LOBBY_TAG, "Authorization = " + "AWS " + S3Constants.ACCESS_KEY + ":" + pre_signature);
                Log.d(S3Constants.LOBBY_TAG, "Date = " + gmt_date);

            } else {
                Log.d(S3Constants.LOBBY_TAG, "----------------------- Pre Signature FAILED!");
                Log.d(S3Constants.LOBBY_TAG, "pre_signature = " + pre_signature);
                Log.d(S3Constants.LOBBY_TAG, "Authorization = " + "AWS " + S3Constants.ACCESS_KEY + ":" + pre_signature);
                Log.d(S3Constants.LOBBY_TAG, "Date = " + gmt_date);

            }

        }

        return null;

    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        if (!connectionSuccess) {
            msg("Connection to " + S3Constants.SERVER_URL + " had failed! Unable to proceed request.");

        } else {

            if (reqtyp.equals("GET")) {
                if (data_acquire) {
                    msg("Connection to " + S3Constants.SERVER_URL + " is a success! Getting Data Request. " + gmt_date + " --> " + pre_signature);
                    //raw.respo_tv.setText(body);

                } else {
                    msg("Connection to " + S3Constants.SERVER_URL + " is a success! Unable to Get Data Request. " + gmt_date);
                    //raw.respo_tv.setText(body);

                }
                progress.dismiss();

            } else if (reqtyp.equals("PUT")) {
                if (data_acquire) {
                    msg("Connection to " + S3Constants.SERVER_URL + " is a success! Uploading Data Request SUCCESS.");
                    //raw.respo_tv.setText(body);

                } else {
                    msg("Connection to " + S3Constants.SERVER_URL + " is a success! Uploading Data Request FAILED.");
                    //raw.respo_tv.setText(body);

                }

            }

            msg(result_toast);
            onGetBucketResponse.getBucket(body);

        }

    }

    // ---- Utility Methods ----

    private void msg(String msg) {
        Toast.makeText(lobby, msg, Toast.LENGTH_LONG).show();
    }

}







    // ---------- Recyclers

//    private OkHttpClient.Builder client;

//    private void clientInit() {
//        client = new OkHttpClient().newBuilder().authenticator(new Authenticator() {
//            @Override
//            public Request authenticate(Route route, Response response) throws IOException {
//                Log.d(S3Constants.LOBBY_TAG, "Auth Respo = " + response);
//                Log.d(S3Constants.LOBBY_TAG, "Challenges = " + response.challenges());
//
//                return response.request().newBuilder()
//                        .addHeader("Authorization", authorization)
//                        .addHeader("Date", gmt_date)
//                        .build();
//
//            }
//        });

//              ----- first get -------
//            try (Response response = client.build().newCall(request).execute()) {
//                connectionSuccess = true;
//
//                Log.d(S3Constants.LOBBY_TAG, String.valueOf(response) + " ---- 1");
//                body = response.body().string();
//
//                gmt_acquire = true;
//                gmt_date = response.header("Date");
//
//            } catch (IOException e) {
//                connectionSuccess = false;
//                Log.d(S3Constants.LOBBY_TAG, String.valueOf(e));
//
//            }

//              ----- second get -------
//            try (Response response = client.build().newCall(request).execute()) {
//                Log.d(S3Constants.LOBBY_TAG, String.valueOf(response) + " ---- 2");
//                body = response.body().string();
//                data_acquire = true;
//
//            } catch (IOException e) {
//                Log.d(S3Constants.LOBBY_TAG, String.valueOf(e));
//            }


//              ---- ProgressBar ----
//            Log.d(S3Constants.LOBBY_TAG, "Upload Sequence");
//
//            for (int i = 0; i < 100; i++) {
//                publishProgress(i);
//
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException ie) {
//                    Log.d(S3Constants.LOBBY_TAG, String.valueOf(ie));
//                }
//
//            }
