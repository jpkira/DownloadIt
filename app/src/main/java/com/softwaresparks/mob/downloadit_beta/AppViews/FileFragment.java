package com.softwaresparks.mob.downloadit_beta.AppViews;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.softwaresparks.mob.downloadit_beta.AppUtilities.CustomViewAdapter;
import com.softwaresparks.mob.downloadit_beta.AppUtilities.OnSetBucketRequest;
import com.softwaresparks.mob.downloadit_beta.R;

/**
 * Created by xLipse on 1/19/2017.
 */

public class FileFragment extends Fragment {

    OnSetBucketRequest onSetBucketRequest;

    TextView explorer_tv;
    TextView emptyfiles_tv;
    ImageButton explorer_imgb;
    ListView files_lv;

    private String filenames[] = null;
    private String filepaths[] = null;
    private String filesizes[] = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onSetBucketRequest = (OnSetBucketRequest) context;
        } catch (ClassCastException cce) {
            throw new ClassCastException(context.toString()
                    + " must implement OnGetBucketRequest");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_files, container, false);

    }

}
