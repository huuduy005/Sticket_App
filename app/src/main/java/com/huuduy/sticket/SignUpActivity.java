package com.huuduy.sticket;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
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

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private static final String API = "https://sticket.herokuapp.com/api/sign-up";

    private UserSignUpTask mAuthTask = null;

    private EditText name;
    private EditText email;
    private EditText phoneNumber;
    private EditText password;
    private EditText rePassword;
    private Button btnSignUp;
    private TextView linkLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        // remove title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        name = (EditText) findViewById(R.id.input_name);
        email = (EditText) findViewById(R.id.input_email);
        phoneNumber = (EditText) findViewById(R.id.input_mobile);
        password = (EditText) findViewById(R.id.input_password);
        rePassword = (EditText) findViewById(R.id.input_reEnterPassword);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        linkLogin = (TextView) findViewById(R.id.link_login);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private JSONObject collectData() {
        JSONObject data = new JSONObject();
        String name = this.name.getText().toString();
        String email = this.email.getText().toString();
        String mobile = phoneNumber.getText().toString();
        String password = this.password.getText().toString();

        try {
            data.put("idUser", mobile);
            data.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void signup() {
        Log.d(TAG, "SignUp");
        /*Kiểm tra các trường thông tin*/
        if (!validate()) {
            onSignUpFailed(-1, "Thông tin điền sai.");
            return;
        }
        btnSignUp.setEnabled(false);

        progressDialog = new ProgressDialog(SignUpActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Đang đăng kí...");
        progressDialog.show();

        JSONObject data = collectData();
        // TODO: Implement your own signup logic here.
        mAuthTask = new UserSignUpTask(data);
        mAuthTask.execute((Void) null);
    }


    public void onSignUpSuccess(String message) {
        btnSignUp.setEnabled(true);
        Intent data = new Intent();
        data.putExtra("id", phoneNumber.getText().toString());
        data.putExtra("password", password.getText().toString());
        setResult(RESULT_OK, data);
        progressDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this, R.style.AppTheme_Dark_Dialog);
        builder.setTitle("Đăng kí thành công")
                .setMessage("Bạn đã đăng kí thành công\n" + message)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishActivity(0);
                        finish();
                    }
                });
        builder.show();
    }

    public void onSignUpFailed(int code, String message) {
        if (code > 0)
            progressDialog.dismiss();

        Toast toast = Toast.makeText(getApplicationContext(), "Đăng kí thất bại\n" + message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

        switch (code) {
            case 1: {
                password.setError("Mật khẩu sai");
                password.requestFocus();
                break;
            }
            default: {
                break;
            }
        }
        btnSignUp.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = this.name.getText().toString();
        String email = this.email.getText().toString();
        String mobile = phoneNumber.getText().toString();
        String pw = password.getText().toString();
        String reEnterPassword = rePassword.getText().toString();


        if (name.isEmpty() || name.length() < 3) {
            this.name.setError("Tên có ít nhất phải có 3 kí tự.");
            valid = false;
        } else {
            this.name.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email.setError("Địa chỉ email không hợp lệ!");
            valid = false;
        } else {
            this.email.setError(null);
        }

        if (mobile.isEmpty()) { //|| mobile.length() != 10) {
            phoneNumber.setError("Số điện thoại không hợp lệ!");
            valid = false;
        } else {
            phoneNumber.setError(null);
        }

        if (pw.isEmpty() || pw.length() < 6 || pw.length() > 10) {
            this.password.setError("Mật khẩu có độ dài từ 6 đến 10 kí tự.");
            valid = false;
        } else {
            this.password.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 6 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(pw))) {
            rePassword.setError("Mật khẩu không khớp!");
            valid = false;
        } else {
            rePassword.setError(null);
        }

        return valid;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserSignUpTask extends AsyncTask<Void, Void, Boolean> {

        private final JSONObject mData;
        private String mResponse;

        UserSignUpTask(JSONObject data) {
            mData = data;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Log.d(TAG, "doInBackground: start");

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            Log.d(TAG, "doInBackground: \n\t" + mData.toString());
            RequestBody body = null;
            try {
                body = RequestBody.create(mediaType, "idUser=" + mData.getString("idUser") + "&password=" + mData.getString("password"));
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            Request request = new Request.Builder()
                    .url(API)
                    .post(body)
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
                int ErroCode = 1;
                JSONObject mainObject = null;
                String status = "FAIL";
                String message = "Lỗi không xác dịnh";
                try {
                    mainObject = new JSONObject(mResponse);
                    status = mainObject.getString("status");
                    message = mainObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                    onSignUpFailed(-1, message);
                }
                if (status.contains("OK")) {
                    onSignUpSuccess(message);
                } else {
                    onSignUpFailed(1, message);
                }
            } else {
                onSignUpFailed(-2, "Xảy ra lỗi khi đăng kí");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
