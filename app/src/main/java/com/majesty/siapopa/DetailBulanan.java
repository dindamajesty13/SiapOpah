package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DetailBulanan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getStringExtra("status").equals("harian")){
            setContentView(R.layout.activity_detail_lap_harian);
        }

    }
}