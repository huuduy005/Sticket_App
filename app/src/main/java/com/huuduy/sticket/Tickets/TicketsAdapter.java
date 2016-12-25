package com.huuduy.sticket.Tickets;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huuduy.sticket.R;

import java.util.List;

/**
 * Created by huudu on 25/12/2016.
 */

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.MyViewHolder> {
    private List<TicketModel> mTicketsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, time, location;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.text_title);
            time = (TextView) view.findViewById(R.id.text_time);
            location = (TextView) view.findViewById(R.id.text_location);
        }
    }


    public TicketsAdapter(List<TicketModel> tickets) {
        this.mTicketsList = tickets;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TicketModel ticket = mTicketsList.get(position);
        holder.title.setText(ticket.getTitle());
        holder.time.setText(ticket.getTime());
        holder.location.setText(ticket.getLocation());
    }

    @Override
    public int getItemCount() {
        return mTicketsList.size();
    }
}
