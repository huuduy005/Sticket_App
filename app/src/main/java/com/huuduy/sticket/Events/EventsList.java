package com.huuduy.sticket.Events;

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
 * Created by huudu on 22/12/2016.
 */

public class EventsList extends Fragment {

    private static final String TAG = "EventsList";
    private static final String API = "https://sticket.herokuapp.com/api/events";
    private static final String API_LIST = "https://sticket.herokuapp.com/api/events";
    private static final String API_HOT = "https://sticket.herokuapp.com/api/events/trending";

    private APIEventsTask mLoadEventsTask = null;
    SwipeRefreshLayout mSwipeRefreshEvents;
    RecyclerView mRecyclerEvents;
    ArrayList<EventModel> mEventsList;
    EventsAdapter mEventsAdapter;

    public EventsList() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_list, container, false);
        Log.d(TAG, "onCreateView: ");
        mSwipeRefreshEvents = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshTickets);
        mSwipeRefreshEvents.setColorSchemeResources(R.color.orange, R.color.green_, R.color.blue);
        mSwipeRefreshEvents.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshEvents();
            }
        });

        mRecyclerEvents = (RecyclerView) view.findViewById(R.id.list_events);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerEvents.setLayoutManager(mLayoutManager);

        mEventsList = new ArrayList<>();
        mEventsAdapter = new EventsAdapter(mEventsList);
        mRecyclerEvents.setAdapter(mEventsAdapter);
        mRecyclerEvents.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecyclerEvents, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                EventModel event = mEventsList.get(position);
                Toast.makeText(getContext(), "Events\n" + event.getInformation(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                intent.putExtra("idEvent", event.getIdEvent());
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
        if (mLoadEventsTask == null)
            refreshEvents();
    }

    private void refreshEvents() {
        mLoadEventsTask = new APIEventsTask();
        mLoadEventsTask.execute((Void) null);
    }

    void onLoadSuccess(ArrayList<EventModel> events) {
        mEventsList = new ArrayList<EventModel>(events);
        mEventsAdapter = new EventsAdapter(mEventsList);
        mRecyclerEvents.setAdapter(mEventsAdapter);
        mSwipeRefreshEvents.setRefreshing(false);
    }

    void onLoadFailed(int code, String message) {
        Log.d(TAG, "onLoadFailed: code: " + code);
        Toast toast = Toast.makeText(getContext(), "Lấy danh sách events thất bại\n" + message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
        mSwipeRefreshEvents.setRefreshing(false);
    }

    public class APIEventsTask extends AsyncTask<Void, Void, Boolean> {

        private String mResponse;

        APIEventsTask() {
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
            mLoadEventsTask = null;
            if (success) {
                //Parse data ra
                ArrayList<EventModel> events = new ArrayList<EventModel>();
                try {
                    JSONArray mainObject = new JSONArray(mResponse);
                    for (int i = 0; i < mainObject.length(); i++) {
                        JSONObject jsonObject = mainObject.getJSONObject(i);
                        String idEvent = jsonObject.getString("idEvent");
                        String information = jsonObject.getString("information");
                        String image = jsonObject.getString("image");
                        int price = jsonObject.getInt("price");
                        String location = jsonObject.getString("location");
                        String type = jsonObject.getString("type");
                        int numberTicket = jsonObject.getInt("numberTicket");
                        String tags[] = null;
                        EventModel event = new EventModel(idEvent, information, image, price, location, type, numberTicket, tags);
                        events.add(event);
                    }
                    onLoadSuccess(events);
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
            mLoadEventsTask = null;
        }
    }
}
