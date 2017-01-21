package com.softwaresparks.mob.downloadit_beta.AppViews;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.Toast;


import com.softwaresparks.mob.downloadit_beta.AppTransactions.OkHttpAT;
import com.softwaresparks.mob.downloadit_beta.AppUtilities.ModelBucketResponse;
import com.softwaresparks.mob.downloadit_beta.AppUtilities.OnGetBucketResponse;
import com.softwaresparks.mob.downloadit_beta.AppUtilities.OnSetBucketRequest;
import com.softwaresparks.mob.downloadit_beta.R;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Created by xLipse on 1/19/2017.
 */

public class LobbyActivity extends Activity implements OnSetBucketRequest, OnGetBucketResponse {

    private String response;

    private BottomNavigationView navigationView;

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        if (shouldAskPermissions()) {
            askPermissions();
        }

        /**FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FileFragment fileFragment = new FileFragment();
        fragmentTransaction.add(R.id.fragment_container, fileFragment).commit();**/

        /**AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomBar);
        AHBottomNavigationAdapter navigationAdapter = new AHBottomNavigationAdapter(LobbyActivity.this, R.menu.menu);
        navigationAdapter.setupWithBottomNavigation(bottomNavigation);**/

        navigationView = (BottomNavigationView) findViewById(R.id.bottomBar);


        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.file_menu:
                        resetMenu();
                        item.setIcon(R.drawable.ic_files_select);
                        break;

                    case R.id.category_menu:
                        resetMenu();
                        item.setIcon(R.drawable.ic_categories_select);
                        break;

                    case R.id.subscription_menu:
                        resetMenu();
                        item.setIcon(R.drawable.ic_subscription_select);
                        break;

                    case R.id.help_menu:
                        resetMenu();
                        item.setIcon(R.drawable.ic_help_select);
                        break;

                    case R.id.settings_menu:
                        resetMenu();
                        item.setIcon(R.drawable.ic_settings_select);
                        break;
                }
                return false;
            }
        });




    }

    private void resetMenu() {
        int[] drawables = {
                R.drawable.ic_files_normal,
                R.drawable.ic_categories_normal,
                R.drawable.ic_subscription_normal,
                R.drawable.ic_help_normal,
                R.drawable.ic_settings_normal
        };

        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            MenuItem item = navigationView.getMenu().getItem(i);
            item.setIcon(drawables[i]);
        }
    }

    @Override
    public void setBucket(String req) {
        if (req.equals("GET")) {
            new OkHttpAT(LobbyActivity.this, "GET").execute();
        }
    }

    @Override
    public void getBucket(String response) {
        this.response = response;

        XmlParserCreator parserCreator = new XmlParserCreator() {
            @Override
            public XmlPullParser createParser() {
                try {
                    return XmlPullParserFactory.newInstance().newPullParser();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        GsonXml gsonXml = new GsonXmlBuilder()
                .setXmlParserCreator(parserCreator)
                .create();

        ModelBucketResponse model = gsonXml.fromXml(response, ModelBucketResponse.class);

        msg(model.getContents().toString());

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    private void msg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
