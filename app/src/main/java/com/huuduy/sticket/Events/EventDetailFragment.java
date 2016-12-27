package com.huuduy.sticket.Events;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huuduy.sticket.R;
import com.nostra13.universalimageloader.core.ImageLoader;

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

public class EventDetailFragment extends Fragment {

    private static final String TAG = "EventDetailFragment";
    private static final String API = "https://sticket.herokuapp.com/api/event/";
    private String mEvent;
    private APILoadEventTask mLoadEventTask;

    private ImageView mCover;
    private TextView mTitle;
    private TextView mTime;
    private TextView mInformation;
    private TextView mLocation;

    private String cover;
    private String title;
    private String time;
    private String information;
    private String location;

    public EventDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEvent = getArguments().getString("idEvent");
        loadData();
    }

    private void loadData() {
        if (mLoadEventTask == null) {
            mLoadEventTask = new APILoadEventTask();
            mLoadEventTask.execute((Void) null);
        }
    }

    void setData() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(cover, mCover);
        if (mTitle != null)
            mTitle.setText(title);
        if (mTime != null)
            mTime.setText(time);
        if (mInformation != null)
            mInformation.setText(information);
        if (mLoadEventTask != null)
            mLocation.setText(location);
    }

    void onLoadSuccess(EventModel event) {
        cover = event.getImage();
        title = event.getTitle();
        time = event.getTime();
        information = event.getInformation();
        location = event.getLocation();
        setData();
    }

    void onLoadFailed(int code, String message) {
        Log.d(TAG, "onLoadFailed: code: " + code);
        Toast toast = Toast.makeText(getContext(), "Lấy thông tin event thất bại\n" + message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events_detail, container, false);
        mCover = (ImageView) rootView.findViewById(R.id.cover);
        mTitle = (TextView) rootView.findViewById(R.id.title);
        mTime = (TextView) rootView.findViewById(R.id.time);
        mInformation = (TextView) rootView.findViewById(R.id.information);
        mLocation = (TextView) rootView.findViewById(R.id.location);

        setData();
        return rootView;
    }

    public class APILoadEventTask extends AsyncTask<Void, Void, Boolean> {

        private String mResponse;

        APILoadEventTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d(TAG, "doInBackground: start");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(API + mEvent)
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
            mLoadEventTask = null;
            if (success) {
                //Parse data ra
                try {
                    JSONObject jsonObject = new JSONObject(mResponse);
                    Log.d(TAG, "onPostExecute: " + mResponse);
                    String idEvent = jsonObject.getString("idEvent");
                    String title = jsonObject.getString("title");
                    String time = jsonObject.getString("time");
                    String information = jsonObject.getString("information");
                    String image = jsonObject.getString("image");
                    int price = jsonObject.getInt("price");
                    String location = jsonObject.getString("location");
                    String type = jsonObject.getString("type");
                    int numberTicket = jsonObject.getInt("numberTicket");
                    String tags[] = null;
                    EventModel event = new EventModel(idEvent, title, time, information, image, price, location, type, numberTicket, tags);

                    onLoadSuccess(event);
                } catch (JSONException e) {
                    e.printStackTrace();
                    onLoadFailed(-1, "Rút dữ liệu bị lỗi");
                }
            } else {
                onLoadFailed(-2, "Xảy ra lỗi khi lấy thông tin sự kiện");
            }
        }

        @Override
        protected void onCancelled() {
            mLoadEventTask = null;
        }
    }
}
