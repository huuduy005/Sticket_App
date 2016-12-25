package com.huuduy.sticket.Tickets;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huuduy.sticket.R;
import com.huuduy.sticket.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by huudu on 23/12/2016.
 */

public class TicketsList extends Fragment {

    private static final String TAG = "TicketsList";
    private static final String API = "https://sticket.herokuapp.com/api/events";
    private static final String API_ONGOING = "https://sticket.herokuapp.com/api/tickets";
    private static final String API_PAST = "https://sticket.herokuapp.com/api/tickets";

    private APITicketsTask mLoadTicketsTask = null;
    SwipeRefreshLayout mSwipeRefreshTickets;
    RecyclerView mRecyclerTickets;
    ArrayList<TicketModel> mTicketsList;
    TicketsAdapter mTicketsAdapter;

    public TicketsList() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
//        loadData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets_re, container, false);
        Log.d(TAG, "onCreateView: ");
        mSwipeRefreshTickets = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshTickets);
        mSwipeRefreshTickets.setColorSchemeResources(R.color.orange, R.color.green_, R.color.blue);
        mSwipeRefreshTickets.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTickets();
            }
        });

        mRecyclerTickets = (RecyclerView) view.findViewById(R.id.re_tickets);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerTickets.setLayoutManager(mLayoutManager);

        mTicketsList = new ArrayList<>();

        mTicketsAdapter = new TicketsAdapter(mTicketsList);
        mRecyclerTickets.setAdapter(mTicketsAdapter);
        mRecyclerTickets.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecyclerTickets, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TicketModel ticket = mTicketsList.get(position);
                Toast.makeText(getContext(), ticket.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), TicketDetailActivity.class);
                intent.putExtra("idEvent", "d");
                intent.putExtra("title", ticket.getTitle());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        Log.d(TAG, "onCreateView: new recyclerview");
        loadData();
        return view;
    }

    private void loadData() {
        int idEvent = 1;
        String title = "GGGGG";
        String location = "TP.HCM";
        String time = "Thông tin";

        for (int i = 0; i < 10; i++) {
            TicketModel ticket = new TicketModel(idEvent, title, location, time);
            ticket.setTitle("ssss");
            mTicketsList.add(ticket);
        }

        if (mLoadTicketsTask == null){
            refreshTickets();
        }
    }

    private void refreshTickets() {
        mLoadTicketsTask = new APITicketsTask();
        mLoadTicketsTask.execute((Void) null);
    }

    void onLoadSuccess(ArrayList<TicketModel> tickets) {
        mTicketsList = new ArrayList<TicketModel>(tickets);
        mTicketsAdapter = new TicketsAdapter(mTicketsList);
        mRecyclerTickets.setAdapter(mTicketsAdapter);
        mSwipeRefreshTickets.setRefreshing(false);
    }

    void onLoadFailed(int code, String message) {
        Log.d(TAG, "onLoadFailed: code: " + code);
        Toast toast = Toast.makeText(getContext(), "Lấy danh sách tickets thất bại\n" + message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
        mSwipeRefreshTickets.setRefreshing(false);
    }

    public class APITicketsTask extends AsyncTask<Void, Void, Boolean> {

        private String mResponse;

        APITicketsTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d(TAG, "doInBackground: start");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(API)
                    .get()
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("device", "Android")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                mResponse = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            Log.d(TAG, "doInBackground: end");
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mLoadTicketsTask = null;
            if (success) {
                //Parse data ra
                ArrayList<TicketModel> tickets = new ArrayList<TicketModel>();
                try {
                    JSONArray mainObject = new JSONArray(mResponse);
                    for (int i = 0; i < mainObject.length(); i++) {
                        JSONObject jsonObject = mainObject.getJSONObject(i);
                        int idEvent = jsonObject.getInt("idEvent");
                        String title = jsonObject.getString("title");
                        String location = jsonObject.getString("location");
                        String time = jsonObject.getString("time");
                        TicketModel event = new TicketModel(idEvent, title, location, time);
                        tickets.add(event);
                    }
                    onLoadSuccess(tickets);
                } catch (JSONException e) {
                    e.printStackTrace();
                    onLoadFailed(-1, "Rút dữ liệu bị lỗi");
                }
            } else {
                onLoadFailed(-2, "Xảy ra lỗi khi lấy danh sách sự kiện");
            }
        }

        @Override
        protected void onCancelled() {
            mLoadTicketsTask = null;
        }
    }
}
