package com.example.saveMoneyHelper.auth;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.saveMoneyHelper.auth.helper.Functions;
import com.example.saveMoneyHelper.auth.helper.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.saveMoneyHelper.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class EmailVerify extends AppCompatActivity {
    private static final String TAG = EmailVerify.class.getSimpleName();

    private TextInputLayout textVerifyCode;
    private MaterialButton btnVerify, btnResend;
    private TextView otpCountDown;

    private SessionManager session;
    //private DatabaseHandler db;
    private ProgressDialog pDialog;

    private static final String FORMAT = "%02d:%02d";

    Bundle bundle;

    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        textVerifyCode = findViewById(R.id.verify_code);
        btnVerify = findViewById(R.id.btnVerify);
        btnResend = findViewById(R.id.btnResendCode);
        otpCountDown = findViewById(R.id.otpCountDown);

        bundle = getIntent().getExtras();

       // db = new DatabaseHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
    }

    private void init() {
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide Keyboard
                Functions.hideSoftKeyboard(EmailVerify.this);

                String email = bundle.getString("email");
                String otp = textVerifyCode.getEditText().getText().toString();
                if (!otp.isEmpty()) {
                    verifyCode(email, otp);
                    textVerifyCode.setErrorEnabled(false);
                } else {
                    textVerifyCode.setError("Please enter verification code");
                }
            }
        });

        btnResend.setEnabled(false);
        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = bundle.getString("email");
                resendCode(email);
            }
        });

        countDown();
    }

    private void countDown() {
        new CountDownTimer(70000, 1000) { // adjust the milli seconds here

            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            public void onTick(long millisUntilFinished) {
                otpCountDown.setVisibility(View.VISIBLE);
                otpCountDown.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)) ));
            }

            public void onFinish() {
                otpCountDown.setVisibility(View.GONE);
                btnResend.setEnabled(true);
            }
        }.start();
    }

    private void verifyCode(final String email, final String otp) {
        // Tag used to cancel the request
        String tag_string_req = "req_verify_code";

        pDialog.setMessage("Checking in ...");
        showDialog();

    }

    private void resendCode(final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_resend_code";

        pDialog.setMessage("Resending code ...");
        showDialog();


    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onResume(){
        super.onResume();
        countDown();
    }
}
