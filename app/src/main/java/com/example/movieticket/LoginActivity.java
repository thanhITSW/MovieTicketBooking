package com.example.movieticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin;
    TextView tvError;
    String id, username, password, email, phone, error;
    int coin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        edtUsername = findViewById(R.id.edtUsernameLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvError = findViewById(R.id.tvError);

        tvError.setVisibility(View.GONE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtUsername.getText().toString().length() < 5) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(R.string.t_n_t_i_kho_n_ph_i_c_t_nh_t_5_k_t);
                }
                else if(edtPassword.getText().toString().length() < 5) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(R.string.m_t_kh_u_ph_i_c_t_nh_t_5_k_t);
                }
                else {
                    login();
                }
            }
        });
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

    private void checkLogin() {
        if(!error.equals("")) {
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(error);
        }
        else {
            Intent replyIntent = new Intent();
            replyIntent.putExtra("id", id);
            replyIntent.putExtra("coin", coin);
            replyIntent.putExtra("username", username);
            replyIntent.putExtra("password", password);
            replyIntent.putExtra("email", email);
            replyIntent.putExtra("phone", phone);
            setResult(RESULT_OK, replyIntent);
            finish();
        }
    }

    private void login() {
        OkHttpClient client = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("username", edtUsername.getText().toString());
        builder.add("password", edtPassword.getText().toString());
        RequestBody formBody = builder.build();

        Request request = new Request.Builder().url("https://api-movie-ticket.onrender.com/accounts/login")
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
                        JSONObject account = json.getJSONObject("data");
                        id = account.getString("_id");
                        coin = account.getInt("coin");
                        username = account.getString("username");
                        email = account.getString("email");
                        phone = account.getString("phone");
                        password = account.getString("password");
                        error = "";
                    }
                    else if(code == 2) {
                        error = json.getString("message");
                    }

                    Log.d("success", "success");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            checkLogin();
                        }
                    });
                } catch (JSONException e) {
                    Log.d("onResponse", e.getMessage());
                }
            }
        });
    }
}