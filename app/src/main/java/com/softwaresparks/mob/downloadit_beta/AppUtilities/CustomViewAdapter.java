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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.customview_filelist, viewGroup, false);

        ImageView thumbnail_img = (ImageView) row.findViewById(R.id.img_filethumbnail);
        ImageView link_img = (ImageView) row.findViewById(R.id.img_link);
        ImageView send_img = (ImageView) row.findViewById(R.id.img_send);
        ImageView delete_img = (ImageView) row.findViewById(R.id.img_delete);
        TextView filename_tv = (TextView) row.findViewById(R.id.tv_filename);
        TextView filepath_tv = (TextView) row.findViewById(R.id.tv_filepath);

        SingleRow tmp = list.get(i);

        filename_tv.setText(tmp.filename);
        filepath_tv.setText(tmp.filepath);


        link_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        send_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return row;

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
