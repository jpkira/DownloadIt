package com.softwaresparks.mob.downloadit_beta.AppUtilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.softwaresparks.mob.downloadit_beta.R;

import java.util.ArrayList;

/**
 * Created by xLipse on 1/19/2017.
 */

public class CustomViewAdapter extends BaseAdapter {

    Context context;

    ArrayList<SingleRow> list;

    public CustomViewAdapter(Context context, String[] filenames, String[] filepaths, String[] filesizes) {

        this.context = context;

        list = new ArrayList<SingleRow>();

        for (int i = 0; i < filenames.length; i++) {
            list.add(new SingleRow(filenames[i], filepaths[i], filesizes[i]));
        }

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        return null;
    }

    private class SingleRow {

        String filename;
        String filepath;
        String filesize;

        SingleRow(String filename, String filepath, String filesize) {
            this.filename = filename;
            this.filepath = filepath;
            this.filesize = filesize;
        }

    }


}
