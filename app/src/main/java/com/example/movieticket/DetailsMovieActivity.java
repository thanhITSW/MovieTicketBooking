package com.example.movieticket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class DetailsMovieActivity extends AppCompatActivity {

    ImageView imgViewDetail;
    TextView tvNameDetail, tvCategory, tvActor, tvDateDetail, tvPrice, tvDescription;
    WebView webViewTrailer;
    ScrollView scrollView;

    Button btnOrder, btnPlay;
    final int requestLoginCode = 1001;
    final int orderCode = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_movie);

        imgViewDetail = findViewById(R.id.imgViewDetail);
        tvNameDetail = findViewById(R.id.tvNameDetail);
        tvCategory = findViewById(R.id.tvCategory);
        tvActor = findViewById(R.id.tvActor);
        tvDateDetail = findViewById(R.id.tvDateDetail);
        tvPrice = findViewById(R.id.tvPriceDetail);
        tvDescription = findViewById(R.id.tvDescription);
        webViewTrailer = findViewById(R.id.webViewTrailer);
        scrollView = findViewById(R.id.scrollView);
        btnOrder = findViewById(R.id.btnOrder);
        btnPlay = findViewById(R.id.btnPlay);

        Intent intent = getIntent();
        tvNameDetail.setText(intent.getStringExtra("name"));
        tvCategory.setText(intent.getStringExtra("category"));
        tvActor.setText(intent.getStringExtra("actor"));
        tvDateDetail.setText(intent.getStringExtra("date"));
        tvPrice.setText(covertPrice(intent.getDoubleExtra("price", 0)));
        tvDescription.setText(intent.getStringExtra("description"));
        String url_avatar = intent.getStringExtra("url_avatar");

        Picasso.get().load(url_avatar).into(imgViewDetail);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                String url_trailer = intent.getStringExtra("url_trailer");
                PlayTrailer(url_trailer);
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = new SessionManager(DetailsMovieActivity.this);
                if(!sessionManager.isLoggedIn()) {
                    showDialogRequestLogin();
                }
                else {
                    Intent intentOrder = new Intent(DetailsMovieActivity.this, OrderActivity.class);
                    intentOrder.putExtra("id", intent.getStringExtra("id"));
                    intentOrder.putExtra("name", intent.getStringExtra("name"));
                    intentOrder.putExtra("date", intent.getStringExtra("date"));
                    intentOrder.putExtra("url_avatar", intent.getStringExtra("url_avatar"));
                    intentOrder.putExtra("price", intent.getDoubleExtra("price", 0));

                    startActivityForResult(intentOrder, orderCode);
                }
            }
        });
    }

    private String covertPrice(double price) {
        DecimalFormat formatter = new DecimalFormat("#,### VND");
        return formatter.format(price);
    }

    private void PlayTrailer(String url_trailer) {
        webViewTrailer.loadData(url_trailer, "text/html", "utf-8");
        webViewTrailer.getSettings().setJavaScriptEnabled(true);
        webViewTrailer.setWebChromeClient(new WebChromeClient());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_back) {
            finish();
            return true;
        }

        return false;
    }

    private void showDialogRequestLogin() throws Resources.NotFoundException {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        TextView tvNotification = new TextView(this);
        tvNotification.setText(R.string.vui_l_ng_ng_nh_p);
        tvNotification.setTextSize(20);
        tvNotification.setTextColor(Color.parseColor("#FFEA0909"));
        layout.addView(tvNotification);
        layout.setPadding(30, 0, 30, 0);
        alertDialog.setView(layout);

        alertDialog.setPositiveButton(R.string.ng_nh_p, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(DetailsMovieActivity.this, AccountActivity.class);
                intent.putExtra("requestLogin", true);
                startActivityForResult(intent, requestLoginCode);
            }
        });
        alertDialog.setNegativeButton(R.string.h_y, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        alertDialog.show();
    }

    private void showDialogOrderSuccess() throws Resources.NotFoundException {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        TextView tvNotification = new TextView(this);
        tvNotification.setText(R.string.thanh_to_n_th_nh_c_ng_ki_m_tra_trong_l_ch_s_t_v);
        tvNotification.setTextSize(20);
        tvNotification.setTextColor(Color.parseColor("#49be25"));
        layout.addView(tvNotification);
        layout.setPadding(30, 0, 30, 0);
        alertDialog.setView(layout);

        alertDialog.setPositiveButton(R.string.ng, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });

        alertDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == orderCode) {
            showDialogOrderSuccess();
        }
        else if(resultCode == RESULT_OK && requestCode == requestLoginCode) {
            Toast.makeText(this, R.string.ng_nh_p_th_nh_c_ng, Toast.LENGTH_SHORT).show();
        }
    }
}