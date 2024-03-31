package com.example.movieticket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HistoryActivity extends AppCompatActivity {

    LinearLayout areaNotification, areaLogin, areaHome, areaAccount;
    ListView listViewOrder;
    TextView tvEmpty;
    ArrayAdapter<Order> adapter;
    ArrayList<Order> dataOrders;
    DecimalFormat formatter;
    SessionManager sessionManager;
    final int requestLoginCode = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        areaHome = findViewById(R.id.areaHome);
        areaAccount = findViewById(R.id.areaAccount);
        areaNotification = findViewById(R.id.areaNotification);
        areaLogin = findViewById(R.id.areaLogin);
        listViewOrder = findViewById(R.id.listViewOrder);
        tvEmpty = findViewById(R.id.tvEmpty);

        areaHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        areaAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        areaNotification.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
        sessionManager = new SessionManager(this);

        dataOrders = new ArrayList<>();
        formatter = new DecimalFormat("#,### VND");

        adapter = new ArrayAdapter<Order>(
                this,
                R.layout.order_item,
                R.id.tvName,
                dataOrders) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View itemView = super.getView(position, convertView, parent);

                TextView tvName = itemView.findViewById(R.id.tvName);
                TextView tvDateOrder = itemView.findViewById(R.id.tvDateOrder);
                TextView tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
                ImageView imgView = itemView.findViewById(R.id.imgView2);
                Button btnETicket = itemView.findViewById(R.id.btnETicket);
                Button btnReview = itemView.findViewById(R.id.btnReview);

                Order order = dataOrders.get(position);
                tvName.setText(order.movieName);
                tvDateOrder.setText(order.creation_date);
                tvTotalPrice.setText(formatter.format(order.totalPrice));
                Picasso.get().load(order.url_avatar).into(imgView);

                btnETicket.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HistoryActivity.this, ElectronicTicketActivity.class);
                        intent.putExtra("order", order);
                        startActivity(intent);
                    }
                });

                btnReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HistoryActivity.this, ReviewActivity.class);
                        intent.putExtra("order", order);
                        startActivity(intent);
                    }
                });

                return itemView;
            }
        };

        listViewOrder.setAdapter(adapter);

        areaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, AccountActivity.class);
                intent.putExtra("requestLogin", true);
                startActivityForResult(intent, requestLoginCode);
            }
        });

        checkLogin();
    }

    private void checkLogin() {
        if(!sessionManager.isLoggedIn()) {
            areaNotification.setVisibility(View.VISIBLE);
            listViewOrder.setVisibility(View.GONE);
        }
        else {
            areaNotification.setVisibility(View.GONE);
            listViewOrder.setVisibility(View.VISIBLE);

            getDataOrders();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == requestLoginCode) {
            checkLogin();
        }
    }

    private void displayOrders() {
        if(dataOrders.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
        }
        else {
            adapter.notifyDataSetChanged();
        }
    }

    private void getDataOrders() {
        OkHttpClient client = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("username", sessionManager.getUsername());
        RequestBody formBody = builder.build();

        Request request = new Request.Builder().url("https://api-movie-ticket.onrender.com/orders")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("onFailure", e.getMessage());
            }
            @Override
            public void onResponse(Call call, final Response response)
                    throws IOException {
                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    int code = json.getInt("code");

                    if(code == 0) {
                        JSONArray dataArray = json.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject orderObject = dataArray.getJSONObject(i);

                            String id = orderObject.getString("_id");
                            String creation_date = orderObject.getString("creation_date");
                            String username = orderObject.getString("username");
                            String movieId = orderObject.getString("movieId");
                            String movieName = orderObject.getString("movieName");
                            String date = orderObject.getString("date");
                            String url_avatar = orderObject.getString("url_avatar");
                            String shift = orderObject.getString("shift");
                            String cinema = orderObject.getString("cinema");
                            int quantity = orderObject.getInt("quantity");
                            String selected = orderObject.getString("selected");
                            String method = orderObject.getString("method");
                            Double totalPrice = orderObject.getDouble("totalPrice");

                            Order order = new Order(id, creation_date, username, movieId, movieName, date, url_avatar, shift, cinema, quantity, selected, method, totalPrice);
                            dataOrders.add(order);
                        }
                    }

                    Log.d("success", "success");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayOrders();
                        }
                    });
                } catch (JSONException e) {
                    Log.d("onResponse", e.getMessage());
                }
            }
        });
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

        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
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
                HistoryActivity.this.recreate();
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