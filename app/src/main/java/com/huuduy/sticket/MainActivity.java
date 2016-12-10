package com.huuduy.sticket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private APIEventsTask mAuthTask = null;
    ListView mListViewEvents;
    SwipeRefreshLayout mSwipeRefreshEvents;
    ArrayList<EventModel> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        mSwipeRefreshEvents = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshEvents);
        mListViewEvents = (ListView) findViewById(R.id.listview_events);
        mListViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView title = (TextView) view.findViewById(R.id.text_title);
                Toast.makeText(getApplicationContext(), "Events\n" + title.getText(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, EventDetailActivity.class);
                AdapterEvent.ViewHolder viewHolder = (AdapterEvent.ViewHolder) view.getTag();
                Log.d(TAG, "onItemClick: " + viewHolder.idEvent);
                intent.putExtra("idEvent", viewHolder.idEvent);
                startActivity(intent);
            }
        });

        mSwipeRefreshEvents.setColorSchemeResources(R.color.orange, R.color.green_, R.color.blue);
        mSwipeRefreshEvents.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshEvents();
            }
        });
        mAuthTask = new APIEventsTask();
        mAuthTask.execute((Void) null);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }

    private void refreshEvents() {
//        getSupportActionBar().hide();
        mAuthTask = new APIEventsTask();
        mAuthTask.execute((Void) null);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_tickets: {
                break;
            }
            case R.id.nav_events: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_signin: {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_signup: {
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_addevent: {
                Intent intent = new Intent(this, AddEventActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_share:
            case R.id.nav_send:
            default: {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;
            }
        }
        Toast toast = Toast.makeText(getBaseContext(), "" + id, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        toast.show();
        return true;
    }

    void onLoadSuccess(ArrayList<EventModel> events) {
        mList = new ArrayList<EventModel>(events);
        AdapterEvent mAdapter = new AdapterEvent(this, mList);
        mListViewEvents.setAdapter(mAdapter);
        mSwipeRefreshEvents.setRefreshing(false);
    }

    void onLoadFailed(int code, String message) {
        Toast toast = Toast.makeText(getApplicationContext(), "Load events thất bại\n" + message, Toast.LENGTH_LONG);
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
            // TODO: attempt authentication against a network service.
            Log.d(TAG, "doInBackground: start");

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://sticket.herokuapp.com/api/events")
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
            mAuthTask = null;
            if (success) {
                //Parse data ra
                ArrayList<EventModel> events = new ArrayList<EventModel>();
                JSONArray mainObject = null;
                try {
                    mainObject = new JSONArray(mResponse);
                    for (int i = 0; i < mainObject.length(); i++) {
                        JSONObject jsonObject = mainObject.getJSONObject(i);
                        int idEvent = jsonObject.getInt("idEvent");
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
                    Log.d(TAG, events.toString());
                    Log.d(TAG, events.get(0).getInformation());
                } catch (JSONException e) {
                    e.printStackTrace();
                    onLoadFailed(-1, "Rút dữ liệu bị lỗi");
                }
            } else {
                onLoadFailed(-2, "Xảy ra lỗi khi đăng kí");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
