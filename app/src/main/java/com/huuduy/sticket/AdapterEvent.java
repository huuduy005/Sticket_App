package com.huuduy.sticket;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by huudu on 06/12/2016.
 */

public class AdapterEvent extends ArrayAdapter<EventModel> {

    public static class ViewHolder {
        ImageView imageView;
        TextView text_title;
        TextView text_price;
        TextView text_location;
    }

    public AdapterEvent(Context context, List<EventModel> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_event);
            viewHolder.text_title = (TextView) convertView.findViewById(R.id.text_title);
            viewHolder.text_price = (TextView) convertView.findViewById(R.id.text_price);
            viewHolder.text_location = (TextView) convertView.findViewById(R.id.text_location);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();
        EventModel eventModel = getItem(position);
        viewHolder.text_title.setText(eventModel.getInformation() + "");
        viewHolder.text_price.setText(eventModel.getPrice() + "");
        viewHolder.text_location.setText(eventModel.getLocation() + "");

//        viewHolder.imageView.setTag(eventModel.getPoster_landscape());
//        imageLoader.DisplayImage(filmModel.getPoster_landscape(), viewHolder.imageView);
        return convertView;
    }
}
