package com.example.test4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.app.DatePickerDialog;
import android.widget.RelativeLayout;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReservationActivity extends AppCompatActivity {
    private ListView ReservationListView; //findListview
    private ReservationAdapter reservationAdapter;
    private List<ReservationItem> reservationList; //findlist
    String userID;
    private Button select_route;
    private Button select_date;
    private Calendar calendar;
    private Button view_btn;
    private LinearLayout turn_select;
    private RelativeLayout second_turn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        Intent intent = getIntent();
        if(intent != null){
            userID = intent.getStringExtra("userID");
        }
        select_route = findViewById(R.id.route_select);
        select_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                builder.setTitle("노선 선택");

                final String[] routes = {"세종","도안","계룡","가오동"};
                builder.setItems(routes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String selectedroute = routes[i];
                        select_route.setText(selectedroute);
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        select_date = findViewById(R.id.date_select);
        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        view_btn = findViewById(R.id.route_check);
        view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turn_select = findViewById(R.id.turn_select);
                second_turn = findViewById(R.id.second_turn);
                if(select_route.equals("세종")){
                    turn_select.setVisibility(View.VISIBLE);
                    second_turn.setVisibility(View.GONE);
                }else{
                    turn_select.setVisibility(View.VISIBLE);
                }

            }
        });
    }
    private void showDatePicker(){
        final Calendar currentDate = Calendar.getInstance();
        final Calendar dateLimit =  Calendar.getInstance();
        dateLimit.add(Calendar.DAY_OF_MONTH,3);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);
                updateSelectedDateButton();
            }
        },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(dateLimit.getTimeInMillis());
        datePickerDialog.show();
    }

    private void updateSelectedDateButton(){
        if(calendar != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
            String selectedDateStr = dateFormat.format(calendar.getTime());
            select_date.setText(selectedDateStr);
        }
    }


}