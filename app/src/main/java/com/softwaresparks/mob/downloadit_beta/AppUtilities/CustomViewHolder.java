package com.softwaresparks.mob.downloadit_beta.AppUtilities;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.softwaresparks.mob.downloadit_beta.R;

/**
 * Created by John Michael Palo on 1/20/2017.
 */

public class CustomViewHolder {

    TextView filename_tv;
    TextView filedate_tv;
    ImageView thumbnail_img;
    ImageView link_img;
    ImageView send_img;
    ImageView delete_img;

    CustomViewHolder(View v) {
        filename_tv = (TextView) v.findViewById(R.id.tv_filename);
        filedate_tv = (TextView) v.findViewById(R.id.tv_filepath);
        thumbnail_img = (ImageView) v.findViewById(R.id.img_filethumbnail);
        link_img = (ImageView) v.findViewById(R.id.img_link);
        send_img = (ImageView) v.findViewById(R.id.img_send);
        delete_img = (ImageView) v.findViewById(R.id.img_delete);
    }

}
