package com.example.movieticket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReviewActivity extends AppCompatActivity {

    ImageView imgView;
    TextView tvMovieName;
    RatingBar ratingBarAvg, ratingBarMe;
    EditText edtComment;
    Button btnComment, btnHidden, btnSend;
    ListView listViewReview;
    LinearLayout areaMyReview;
    ArrayAdapter<Review> adapter;
    ArrayList<Review> dataReviews;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);

        imgView = findViewById(R.id.imgView);
        tvMovieName = findViewById(R.id.tvMovieName);
        ratingBarAvg = findViewById(R.id.ratingBarAvg);
        ratingBarMe = findViewById(R.id.ratingBarMe);
        edtComment = findViewById(R.id.edtComment);
        btnComment = findViewById(R.id.btnComment);
        btnHidden = findViewById(R.id.btnHidden);
        btnSend = findViewById(R.id.btnSend);
        listViewReview = findViewById(R.id.listViewReview);
        areaMyReview = findViewById(R.id.areaMyReview);

        ratingBarAvg.setNumStars(5);
        ratingBarAvg.setRating(0);
        ratingBarMe.setNumStars(5);
        ratingBarMe.setRating(0);
        areaMyReview.setVisibility(View.GONE);

        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");

        tvMovieName.setText(order.movieName);
        Picasso.get().load(order.url_avatar).into(imgView);

        dataReviews = new ArrayList<>();

        adapter = new ArrayAdapter<Review>(
                this,
                R.layout.line_review_item,
                R.id.tvComment,
                dataReviews) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View itemView = super.getView(position, convertView, parent);

                TextView tvUsername = itemView.findViewById(R.id.tvUsername);
                TextView tvComment = itemView.findViewById(R.id.tvComment);
                TextView tvDate = itemView.findViewById(R.id.tvDate);
                RatingBar ratingBar = itemView.findViewById(R.id.ratingBar);

                Review review = dataReviews.get(position);
                tvUsername.setText(review.username);
                tvComment.setText(review.comment);
                tvDate.setText(review.date);
                ratingBar.setRating(review.rating);

                return itemView;
            }
        };

        listViewReview.setAdapter(adapter);

        getDataReviews();

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaMyReview.setVisibility(View.VISIBLE);
            }
        });

        btnHidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaMyReview.setVisibility(View.GONE);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float ratingMe = ratingBarMe.getRating();
                String comment = edtComment.getText().toString();
                if(ratingMe == 0) {
                    Toast.makeText(ReviewActivity.this, R.string.ch_n_x_p_h_ng, Toast.LENGTH_SHORT).show();
                }
                else if(comment.equals("")) {
                    Toast.makeText(ReviewActivity.this, R.string.vi_t_b_nh_lu_n, Toast.LENGTH_SHORT).show();
                }
                else {
                    addReview(comment, ratingMe);
                }
            }
        });
    }

    private void displayReviews() {

        if(dataReviews.size() > 0) {
            float ratingAvg = 0;
            for(int i=0;i<dataReviews.size();i++) {
                ratingAvg = ratingAvg + dataReviews.get(i).rating;
            }
            ratingAvg = ratingAvg / dataReviews.size();
            ratingBarAvg.setRating(ratingAvg);
        }

        adapter.notifyDataSetChanged();
    }

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

    private void getDataReviews() {
        dataReviews.clear();

        OkHttpClient client = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("movieName", order.movieName);
        builder.add("username", new SessionManager(this).getUsername());

        RequestBody formBody = builder.build();

        Request request = new Request.Builder().url("https://api-movie-ticket.onrender.com/movies/reviews")
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
                            JSONObject reviewObject = dataArray.getJSONObject(i);

                            String id = reviewObject.getString("_id");
                            String movieName = reviewObject.getString("movieName");
                            String username = reviewObject.getString("username");
                            String date = reviewObject.getString("date");
                            String comment = reviewObject.getString("comment");
                            Float rating = Float.parseFloat(reviewObject.getString("rating"));

                            Review review = new Review(id, movieName,username, date, comment, rating);
                            dataReviews.add(review);
                        }
                    }

                    Log.d("success", "success");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayReviews();
                        }
                    });
                } catch (JSONException e) {
                    Log.d("onResponse", e.getMessage());
                }
            }
        });
    }

    private void addReview(String comment, Float rating) {
        OkHttpClient client = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("movieName", order.movieName);
        builder.add("username", new SessionManager(this).getUsername());
        builder.add("comment", comment);
        builder.add("rating", rating + "");

        RequestBody formBody = builder.build();

        Request request = new Request.Builder().url("https://api-movie-ticket.onrender.com/movies/reviews/add")
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

                    Log.d("success", "success");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(code == 0) {
                                areaMyReview.setVisibility(View.GONE);
                                getDataReviews();
                            }
                        }
                    });
                } catch (JSONException e) {
                    Log.d("onResponse", e.getMessage());
                }
            }
        });
    }
}