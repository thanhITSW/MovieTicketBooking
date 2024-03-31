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

public class CreditCardActivity extends AppCompatActivity {

    LinearLayout areaLogin, areaPayment;
    Spinner spinnerBank;
    EditText edtCardUsername, edtCardPassword, edtCardNumber, edtCardDate;
    TextView tvBank, tvCardUsername, tvCardNumber, tvError, tvPrice;
    CheckedTextView checkedTvLogout;
    Button btnLogin, btnPayment;
    SessionManager sessionManager;
    String[] listBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_card);

        areaLogin = findViewById(R.id.areaLogin);
        areaPayment = findViewById(R.id.areaPayment);
        spinnerBank = findViewById(R.id.spinnerBank);
        edtCardUsername = findViewById(R.id.edtCardUsername);
        edtCardPassword = findViewById(R.id.edtCardPassword);
        edtCardNumber = findViewById(R.id.edtCardNumber);
        edtCardDate = findViewById(R.id.edtCardDate);
        tvBank = findViewById(R.id.tvBank);
        tvCardUsername = findViewById(R.id.tvCardUsername);
        tvCardNumber = findViewById(R.id.tvCardNumber);
        tvError = findViewById(R.id.tvError);
        tvPrice = findViewById(R.id.tvPrice);
        checkedTvLogout = findViewById(R.id.checkedTvLogout);
        btnLogin = findViewById(R.id.btnLogin);
        btnPayment = findViewById(R.id.btnPayment);

        tvError.setVisibility(View.GONE);
        areaPayment.setVisibility(View.GONE);
        tvPrice.setText(getIntent().getStringExtra("price"));

        listBank = new String[]{"Vietcombank", "VietinBank", "BIDV", "Agribank", "Sacombank", "Techcombank", "MB Bank", "ACB", "VPBank", "Eximbank"};

        sessionManager = new SessionManager(this);
        checkLogin();

        edtCardDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        ArrayAdapter<String> adapterBank = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, listBank) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextSize(20);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextSize(20);
                return view;
            }
        };

        adapterBank.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBank.setAdapter(adapterBank);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardBank = (String) spinnerBank.getSelectedItem();
                String cardUsername = edtCardUsername.getText().toString();
                String cardPassword = edtCardPassword.getText().toString();
                String cardNumber = edtCardNumber.getText().toString();
                String cardDate = edtCardDate.getText().toString();

                if(cardBank.equals("") || cardUsername.equals("") || cardPassword.equals("") || cardNumber.equals("") || cardDate.equals("")) {
                    tvError.setText(R.string.thi_u_th_ng_tin);
                    tvError.setVisibility(View.VISIBLE);
                }
                else if(cardUsername.length() < 5) {
                    tvError.setText(R.string.t_n_ch_s_h_u_c_t_nh_t_5_k_t);
                    tvError.setVisibility(View.VISIBLE);
                }
                else if(cardPassword.length() < 5) {
                    tvError.setText(R.string.m_t_kh_u_c_t_nh_t_5_k_t);
                    tvError.setVisibility(View.VISIBLE);
                }
                else if(cardNumber.length() < 5) {
                    tvError.setText(R.string.s_th_c_t_nh_t_5_k_t);
                    tvError.setVisibility(View.VISIBLE);
                }
                else {
                    tvError.setVisibility(View.GONE);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    sessionManager.loginCard(cardBank, cardUsername, cardPassword, cardNumber, cardDate);
                    checkLogin();
                }
            }
        });

        checkedTvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutCard();
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

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDay);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                        String formattedDate = dateFormat.format(selectedDate.getTime());

                        edtCardDate.setText(formattedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void checkLogin() {
        if(!sessionManager.isLoggedInCard()) {
            areaLogin.setVisibility(View.VISIBLE);
            areaPayment.setVisibility(View.GONE);

            edtCardUsername.setText("");
            edtCardPassword.setText("");
            edtCardNumber.setText("");
            edtCardDate.setText("");
        }
        else {

            areaLogin.setVisibility(View.GONE);
            areaPayment.setVisibility(View.VISIBLE);

            tvBank.setText(sessionManager.getCardBank());
            tvCardUsername.setText(sessionManager.getCardUsername());
            tvCardNumber.setText(sessionManager.getCardNumber());
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