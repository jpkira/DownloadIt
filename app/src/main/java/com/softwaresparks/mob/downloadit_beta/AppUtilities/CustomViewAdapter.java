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

    CustomViewHolder viewHolder;

    public CustomViewAdapter(Context context, String[] filenames, String[] filedate, String[] filesizes) {

        this.context = context;

        list = new ArrayList<SingleRow>();

        for (int i = 0; i < filenames.length; i++) {
            list.add(new SingleRow(filenames[i], filedate[i], filesizes[i]));
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

        View row = view;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.customview_filelist, viewGroup, false);
            viewHolder = new CustomViewHolder(row);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CustomViewHolder) row.getTag();
        }

        SingleRow temp = list.get(i);

        viewHolder.filename_tv.setText(temp.filename);
        viewHolder.filedate_tv.setText(temp.filedate);

        viewHolder.link_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        viewHolder.send_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        viewHolder.delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return row;

    }
}
