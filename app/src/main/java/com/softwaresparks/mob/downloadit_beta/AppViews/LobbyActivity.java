package com.softwaresparks.mob.downloadit_beta.AppViews;

import android.annotation.TargetApi;
import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.softwaresparks.mob.downloadit_beta.AppUtilities.OnGetBucketResponse;
import com.softwaresparks.mob.downloadit_beta.AppUtilities.OnSetBucketRequest;
import com.softwaresparks.mob.downloadit_beta.R;

/**
 * Created by xLipse on 1/19/2017.
 */

public class LobbyActivity extends AppCompatActivity implements OnSetBucketRequest, OnGetBucketResponse {

    private String response;

    private BottomNavigationView navigationView;
    private Toolbar toolbar;
    private SearchView searchView = null;
    private Spinner spinner = null;

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


        navigationView = (BottomNavigationView) findViewById(R.id.bottomBar);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentArea, new FileFragment())
                .commit();

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                resetMenu();

                switch (item.getItemId()) {
                    case R.id.file_menu:

                        item.setIcon(R.drawable.ic_files_select);
                        selectedFragment = new FileFragment();
                        break;

                    case R.id.category_menu:

                        item.setIcon(R.drawable.ic_categories_select);
                        selectedFragment = new CategoryFragment();
                        break;

                    case R.id.subscription_menu:

                        item.setIcon(R.drawable.ic_subscription_select);
                        selectedFragment = new SubscriptionFragment();
                        break;

                    case R.id.help_menu:

                        item.setIcon(R.drawable.ic_help_select);
                        selectedFragment = new HelpFragment();
                        break;

                    case R.id.settings_menu:

                        item.setIcon(R.drawable.ic_settings_select);
                        selectedFragment = new SettingsFragment();
                        break;
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentArea, selectedFragment)
                        .commit();

                return true;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem spinnerItem = menu.findItem(R.id.action_spinner);

        spinner = (Spinner) MenuItemCompat.getActionView(spinnerItem);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.language_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if(searchItem != null) {
            searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        }



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void setBucket(String req) {
        //Get some output/data from File Fragment

    }

    @Override
    public void getBucket(String response) {
        this.response = response;
        //Get some output/data from OkHttpAT

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
