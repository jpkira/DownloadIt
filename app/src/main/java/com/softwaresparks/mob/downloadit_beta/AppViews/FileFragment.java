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

    private String filenames[] = {"A","B"};
    private String filedates[] = {"A","B"};
    private String filesizes[] = {"A","B"};


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

        View view;

        view = inflater.inflate(R.layout.fragment_listfile, container, false);

        explorer_tv = (TextView) view.findViewById(R.id.tv_explorer);
        explorer_imgb = (ImageButton) view.findViewById(R.id.imgb_explorer);
        files_lv = (ListView) view.findViewById(R.id.lv_files);

        files_lv.setAdapter(new CustomViewAdapter(getActivity(), filenames, filedates, filesizes));

        return view;

    }

}
