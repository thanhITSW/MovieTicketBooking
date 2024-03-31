package com.example.movieticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.DecimalFormat;

public class ElectronicTicketActivity extends AppCompatActivity {

    TextView tvId, tvMovieName, tvUsername, tvDateOrder, tvDate, tvShift, tvCinema, tvQuantity, tvSelected, tvMethod, tvTotalPrice;
    ImageView imgQRCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.electronic_ticket);

        tvId = findViewById(R.id.tvId);
        tvMovieName = findViewById(R.id.tvMovieName);
        tvUsername = findViewById(R.id.tvUsername);
        tvDateOrder = findViewById(R.id.tvDateOrder);
        tvDate = findViewById(R.id.tvDate);
        tvShift = findViewById(R.id.tvShift);
        tvCinema = findViewById(R.id.tvCinema);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvSelected = findViewById(R.id.tvSelected);
        tvMethod = findViewById(R.id.tvMethod);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        imgQRCode = findViewById(R.id.imgQRCode);

        Intent intent = getIntent();
        Order order = (Order) intent.getSerializableExtra("order");

        tvId.setText(order.id);
        tvMovieName.setText(order.movieName);
        tvUsername.setText(order.username);
        tvDateOrder.setText(order.creation_date);
        tvDate.setText(order.date);
        tvShift.setText(order.shift);
        tvQuantity.setText(order.quantity + "");
        tvSelected.setText(order.selected);
        tvMethod.setText(order.method);
        DecimalFormat formatter = new DecimalFormat("#,### VND");
        tvTotalPrice.setText(formatter.format(order.totalPrice));

        String data = order.displayDataQRCode();
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap bitmap = barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, 400, 400);
            imgQRCode.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}