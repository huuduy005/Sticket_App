package com.huuduy.sticket;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final String API = "https://sticket.herokuapp.com/api/sign-in";

    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int REQUEST_SIGNUP = 0;
    private UserLoginTask mAuthTask = null;

    EditText ID;
    EditText password;
    Button btnLogin;
    TextView linkSignUp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        ID = (EditText) findViewById(R.id.input_id);
        password = (EditText) findViewById(R.id.input_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        linkSignUp = (TextView) findViewById(R.id.link_signup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        mayRequestContacts();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void login() {
        if (!validate()) {
            onLoginFailed(-1, "Thông tin cung cấp sai.");
            return;
        }
        btnLogin.setEnabled(false);
        String id = ID.getText().toString();
        String pw = password.getText().toString();
        View v = getCurrentFocus();
        if (v != null) {
            v.clearFocus();
        }
        // TODO: Implement your own authentication logic here.
        progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Đang đăng nhập...");
        progressDialog.show();

        if (mAuthTask != null) {
            return;
        }
        mAuthTask = new UserLoginTask(id, pw);
        mAuthTask.execute((Void) null);
    }

    public void onLoginSuccess(String token) {
        password.setError(null);
        ID.setError(null);
        btnLogin.setEnabled(true);
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();
        finish();
    }

    public void onLoginFailed(int code, String message) {
        if (code != -1)
            progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Đăng nhập thất bại\n" + message, Toast.LENGTH_LONG).show();
        switch (code) {
            case 1: {
                password.setError("Mật khẩu sai");
                password.requestFocus();
                break;
            }
            case 2: {
                ID.setError("Tài khoản sai");
                ID.requestFocus();
                break;
            }
            default: {
                break;
            }
        }
        btnLogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String id = ID.getText().toString();
        String pw = password.getText().toString();

        if (id.isEmpty() || !Patterns.PHONE.matcher(id).matches()) {
            ID.setError("Số điện thoại không đúng định dạng");
            valid = false;
        } else {
            ID.setError(null);
        }

        if (pw.isEmpty() || pw.length() < 6 || pw.length() > 10) {
            password.setError("Mật khẩu có độ dài từ 6 đến 10 kí tự");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Đăng kí thành công", Toast.LENGTH_LONG).show();
                String id = data.getStringExtra("id");
                String pw = data.getStringExtra("password");
                ID.setText(id);
                password.setText(pw);
                btnLogin.requestFocus();
            }
        }
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(ID, "djkhfsd", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Đã được cấp quyền", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //TODO Dùng AsyncTask để thực hiện gọi API

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mID;
        private final String mPassword;
        private String data;

        UserLoginTask(String id, String password) {
            mID = id;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Log.d(TAG, "doInBackground: start");

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "idUser=" + mID + "&password=" + mPassword);
            Request request = new Request.Builder()
                    .url(API)
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("device", "Android")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                data = response.body().string();
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
                JSONObject mainObject = null;
                String status = "FAIL";
                String message = "Lỗi không xác dịnh";
                String token = "notoken";
                try {
                    mainObject = new JSONObject(data);
                    status = mainObject.getString("status");
                    message = mainObject.getString("message");
                    token = mainObject.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                    onLoginFailed(-1, message);
                }
                if (status.contains("OK")) {
                    onLoginSuccess(token);
                } else {
                    int ErroCode = 1;
                    onLoginFailed(ErroCode, message);
                }
            } else {
                onLoginFailed(-1, "Quá trình đăng nhập lỗi");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}

