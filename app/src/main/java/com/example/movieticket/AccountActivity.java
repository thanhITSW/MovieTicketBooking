package com.example.movieticket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Locale;

public class AccountActivity extends AppCompatActivity {

    LinearLayout areaCoin, areaHome, areaHistory, areaInformation, areaLoginRegister;
    TextView tvCoin, tvUsername, tvEmail, tvPhone;
    Button btnLogin, btnRegister, btnLogout, btnChangeInformation;
    int coin = 0;
    String id, username, email, phone, password;
    final static int LoginCode = 1001;
    final static int RegisterCode = 1002;
    final static int ChaneInformationCode = 1003;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        areaHome = findViewById(R.id.areaHome);
        areaHistory = findViewById(R.id.areaHistory);
        areaInformation = findViewById(R.id.areaInformation);
        areaLoginRegister = findViewById(R.id.areaLoginRegister);
        tvCoin = findViewById(R.id.tvCoin);
        areaCoin = findViewById(R.id.areaCoin);
        tvUsername = findViewById(R.id.tvUsername);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        btnLogout = findViewById(R.id.btnLogout);
        btnChangeInformation = findViewById(R.id.btnChangeInformation);

        sessionManager = new SessionManager(AccountActivity.this);

        checkLogin();

        areaHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        areaHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                startActivityForResult(intent, LoginCode);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, RegisterActivity.class);
                startActivityForResult(intent, RegisterCode);
            }
        });

        btnChangeInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, ChangeInformationActivity.class);

                intent.putExtra("id", id);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                intent.putExtra("password", password);

                startActivityForResult(intent, ChaneInformationCode);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogLogout();
            }
        });

        areaCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCoin();
            }
        });
    }

    private void checkLogin() {
        if(!sessionManager.isLoggedIn()) {
            tvCoin.setText("0");
            tvUsername.setText("");
            tvEmail.setText("");
            tvPhone.setText("");

            areaLoginRegister.setVisibility(View.VISIBLE);
            areaInformation.setVisibility(View.GONE);
        }

        else {
            id = sessionManager.getUserId();
            coin = sessionManager.getCoin();
            username = sessionManager.getUsername();
            email = sessionManager.getEmail();
            phone = sessionManager.getPhone();
            password = sessionManager.getPassword();

            areaLoginRegister.setVisibility(View.GONE);
            areaInformation.setVisibility(View.VISIBLE);

            tvCoin.setText(coin + "");
            tvUsername.setText(username);
            tvEmail.setText(email);
            tvPhone.setText(phone);
        }
    }

    private void showDialogLogout() throws Resources.NotFoundException {
        new AlertDialog.Builder(this)
                .setTitle(R.string.x_c_nh_n_ng_xu_t)
                .setMessage(R.string.b_n_c_mu_n_ng_xu_t)
                .setIcon(R.drawable.baseline_logout_24)
                .setPositiveButton(R.string.c, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sessionManager.logout();
                        checkLogin();
                    }
                })
                .setNegativeButton(R.string.h_y, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                }).show();
    }

    private void showDialogRegisterSuccess() throws Resources.NotFoundException {
        new AlertDialog.Builder(this)
                .setTitle(R.string.tr_ng_th_i_ng_k)
                .setMessage(R.string.ng_k_t_i_kho_n_th_nh_c_ng)
                .setIcon(R.drawable.baseline_update_success)
                .setPositiveButton(R.string.ng, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void showDialogUpdateSuccess() throws Resources.NotFoundException {
        new AlertDialog.Builder(this)
                .setTitle(R.string.tr_ng_th_i_c_p_nh_t)
                .setMessage(R.string.c_p_nh_t_th_ng_tin_t_i_kho_n_th_nh_c_ng)
                .setIcon(R.drawable.baseline_update_success)
                .setPositiveButton(R.string.ng, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void showDialogCoin() throws Resources.NotFoundException {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(R.string.xu_th_ng);
        alertDialog.setIcon(R.drawable.icons8_coin_75);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView tvCoin = new TextView(this);
        tvCoin.setText(getString(R.string.xu_hi_n_t_i) + coin);
        tvCoin.setTextSize(20);
        tvCoin.setTextColor(Color.parseColor("#2596be"));
        layout.addView(tvCoin);

        TextView tvNotification = new TextView(this);
        tvNotification.setText(R.string.nh_n_10_xu_m_i_100_000_vnd_ti_n_v_thanh_to_n);
        tvNotification.setTextSize(20);
        tvNotification.setTextColor(Color.parseColor("#FFEA0909"));
        layout.addView(tvNotification);

        TextView tvTip = new TextView(this);
        tvTip.setText(R.string.s_d_ng_100_xu_gi_m_50_t_ng_ti_n_thanh_to_n_gi_v);
        tvTip.setTextSize(20);
        tvTip.setTextColor(Color.parseColor("#FFEA0909"));
        layout.addView(tvTip);

        layout.setPadding(30, 0, 30, 0);
        layout.setBackgroundColor(Color.parseColor("#eeeee4"));
        alertDialog.setView(layout);

        alertDialog.setPositiveButton(R.string.ng, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            id = data.getStringExtra("id");
            coin = data.getIntExtra("coin", 0);
            username = data.getStringExtra("username");
            email = data.getStringExtra("email");
            phone = data.getStringExtra("phone");
            password = data.getStringExtra("password");

            sessionManager.login(id, username, email, phone, password, coin);

            checkLogin();

            if(getIntent().getBooleanExtra("requestLogin", false)) {
                setResult(RESULT_OK);
                finish();
            }
            else {
                if(requestCode == RegisterCode) {
                    showDialogRegisterSuccess();
                }
                else if(requestCode == ChaneInformationCode) {
                    showDialogUpdateSuccess();
                }
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_language) {
            openSelectLanguage();
            return true;
        }

        return false;
    }

    private void openSelectLanguage() {
        String[] languageItems = { getString(R.string.ti_ng_vi_t), getString(R.string.ti_ng_anh), getString(R.string.ti_ng_h_n)};

        AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
        builder.setItems(languageItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String language = languageItems[which];
                if(language.equals(languageItems[0])) {
                    changeLanguage("vn");
                }
                else if(language.equals(languageItems[1])) {
                    changeLanguage("en");
                }
                else if(language.equals(languageItems[2])) {
                    changeLanguage("kr");
                }
                AccountActivity.this.recreate();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void changeLanguage(String language) {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = new Locale(language);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}