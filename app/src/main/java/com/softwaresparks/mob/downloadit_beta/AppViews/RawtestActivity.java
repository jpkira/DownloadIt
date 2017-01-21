package com.softwaresparks.mob.downloadit_beta.AppViews;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.softwaresparks.mob.downloadit_beta.AppTransactions.OkHttpAT;
import com.softwaresparks.mob.downloadit_beta.AppTransactions.S3Constants;
import com.softwaresparks.mob.downloadit_beta.R;

public class RawtestActivity extends Activity {

    public TextView respo_tv;
    public Button httpGet_btn;
    public Button httpPut_btn;
    public ProgressBar upload_pb;

    private static final int FILE_SELECT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rawtest);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        if (shouldAskPermissions()) {
            askPermissions();
        }

        httpGet_btn = (Button) findViewById(R.id.btn_httpGet);
        httpPut_btn = (Button) findViewById(R.id.btn_httpPut);
        respo_tv = (TextView) findViewById(R.id.tv_response);
        upload_pb = (ProgressBar) findViewById(R.id.pb_upload);

        httpGet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new OkHttpAT(this, "GET").execute();
            }
        });

        httpPut_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    private void showFileChooser() {

        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, FILE_SELECT_CODE);
        Log.d(S3Constants.LOBBY_TAG, "FILE CHOOSING START");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case FILE_SELECT_CODE : {

                Log.d(S3Constants.LOBBY_TAG, "--? REQUEST CODE: " + requestCode);
                Log.d(S3Constants.LOBBY_TAG, "--? RESULT CODE: " + resultCode);
                Log.d(S3Constants.LOBBY_TAG, "--? Activity Result:: " + Activity.RESULT_OK);

                if (resultCode == Activity.RESULT_OK) {

                    Uri uri = data.getData();

                    Log.d(S3Constants.LOBBY_TAG, "--> @ Chosen File Directory: " + data.getData().getPath());
                    msg("File Source: " + data.getData().getPath());

                    //new OkHttpAT(this, "PUT", uri).execute();

                } else {
                    Log.d(S3Constants.LOBBY_TAG, "Uploading file failed.");

                }

                break;
            }

        }

    }

//    public String getPath(Uri uri) {
//
//        String path = null;
//        String[] projection = { MediaStore.Files.FileColumns.DATA };
//        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//
//        if (cursor == null){
//            path = uri.getPath();
//        } else {
//            cursor.moveToFirst();
//            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
//            path = cursor.getString(column_index);
//            cursor.close();
//        }
//
//        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
//
//    }

    // ---- Utility Methods ----

    private void msg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
