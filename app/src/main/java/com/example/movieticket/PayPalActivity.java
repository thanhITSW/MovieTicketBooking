package com.example.movieticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PayPalActivity extends AppCompatActivity {

    LinearLayout areaLogin, areaPayment;
    EditText edtPPUsername, edtPPPassword;
    TextView tvPPUsername, tvError, tvPrice;
    CheckedTextView checkedTvLogout;
    Button btnLogin, btnPayment;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_pal);

        areaLogin = findViewById(R.id.areaLogin);
        areaPayment = findViewById(R.id.areaPayment);
        edtPPUsername = findViewById(R.id.edtPPUsername);
        edtPPPassword = findViewById(R.id.edtPPPassword);
        tvPPUsername = findViewById(R.id.tvPPUsername);
        tvError = findViewById(R.id.tvError);
        tvPrice = findViewById(R.id.tvPrice);
        checkedTvLogout = findViewById(R.id.checkedTvLogout);
        btnLogin = findViewById(R.id.btnLogin);
        btnPayment = findViewById(R.id.btnPayment);

        tvError.setVisibility(View.GONE);
        areaPayment.setVisibility(View.GONE);
        tvPrice.setText(getIntent().getStringExtra("price"));

        sessionManager = new SessionManager(this);
        checkLogin();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ppUsername = edtPPUsername.getText().toString();
                String ppPassword = edtPPUsername.getText().toString();

                if(ppUsername.equals("") || ppPassword.equals("")) {
                    tvError.setText(R.string.thi_u_th_ng_tin);
                    tvError.setVisibility(View.VISIBLE);
                }
                else if(ppUsername.length() < 5) {
                    tvError.setText(R.string.t_n_ch_s_h_u_c_t_nh_t_5_k_t);
                    tvError.setVisibility(View.VISIBLE);
                }
                else if(ppPassword.length() < 5) {
                    tvError.setText(R.string.m_t_kh_u_c_t_nh_t_5_k_t);
                    tvError.setVisibility(View.VISIBLE);
                }
                else {
                    tvError.setVisibility(View.GONE);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    sessionManager.loginPayPal(ppUsername, ppPassword);
                    checkLogin();
                }
            }
        });

        checkedTvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutPayPal();
                checkLogin();
            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void checkLogin() {
        if(!sessionManager.isLoggedInPayPal()) {
            areaLogin.setVisibility(View.VISIBLE);
            areaPayment.setVisibility(View.GONE);

            edtPPUsername.setText("");
            edtPPPassword.setText("");
        }
        else {

            areaLogin.setVisibility(View.GONE);
            areaPayment.setVisibility(View.VISIBLE);

            tvPPUsername.setText(sessionManager.getPPUsername());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_back) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }

        return false;
    }
}