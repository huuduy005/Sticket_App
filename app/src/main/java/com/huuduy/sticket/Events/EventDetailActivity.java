package com.huuduy.sticket.Events;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.huuduy.sticket.LoginActivity;
import com.huuduy.sticket.R;
import com.huuduy.sticket.SignUpActivity;

public class EventDetailActivity extends AppCompatActivity {

    Button mBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeBlue);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        android.support.v4.app.Fragment fragment = new EventsList();

        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();

        mBooking = (Button) findViewById(R.id.booking);
        mBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
                String token = pre.getString("token", "");
                if (token == "") {
                    AlertDialog dialog = createDialog();
                    dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Đang thực hiện", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private AlertDialog createDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_Dark_Dialog);
        builder.setTitle("Thao tác không thực hiện được");
        builder.setCancelable(false);
        builder.setMessage("Không thể thực hiện thao tác vì bạn chưa đăng nhập")
                .setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplication(), LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNeutralButton("Đăng kí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplication(), SignUpActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Hủy", null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

}
