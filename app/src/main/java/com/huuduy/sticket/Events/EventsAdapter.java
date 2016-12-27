package com.huuduy.sticket.Events;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huuduy.sticket.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by huudu on 25/12/2016.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {
    private List<EventModel> mEventsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView text_title, text_price, text_location;
        public String idEvent;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image_event);
            text_title = (TextView) view.findViewById(R.id.text_title);
            text_price = (TextView) view.findViewById(R.id.text_price);
            text_location = (TextView) view.findViewById(R.id.text_location);
        }
    }

    public EventsAdapter(List<EventModel> events) {
        this.mEventsList = events;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);

        return new EventsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EventModel eventModel = mEventsList.get(position);

        holder.idEvent = eventModel.getIdEvent();
        holder.text_title.setText(eventModel.getInformation() + "");
        holder.text_price.setText(eventModel.getPrice() + "");
        holder.text_location.setText(eventModel.getLocation() + "");

        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        imageLoader.displayImage(eventModel.getImage(), holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mEventsList.size();
    }
}
