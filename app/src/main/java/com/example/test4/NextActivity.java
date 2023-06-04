package com.example.test4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class NextActivity extends AppCompatActivity {

    private TextView data;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        Intent intent = getIntent();
        String ID = intent.getStringExtra("userID");
        String Time = intent.getStringExtra("busTime");
        String start = intent.getStringExtra("busStart");
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = dateFormat.format(date);
        data = findViewById(R.id.reserve_data);
        data.setText("예약자: "+ID+"\n"+"버스시간: "+Time+"\n"+"정류장: "+start+"\n"+"에약하신 시간"+date);
    }
}
