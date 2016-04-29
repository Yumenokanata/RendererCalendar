package com.fenrir.app.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import indi.yume.tools.renderercalendar.CalendarView;
import indi.yume.tools.renderercalendar.OnClickListener;
import indi.yume.tools.renderercalendar.listener.OnDayClickListener;
import indi.yume.tools.renderercalendar.model.DayDate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendar_view);

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(DayDate date, boolean isInThisMonth) {
                System.out.println("CalendarView: Click date= " + date.toString() + " isInThisMonth= " + isInThisMonth);
            }
        });
        calendarView.setOnDayDoubleClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(DayDate date, boolean isInThisMonth) {
                System.out.println("CalendarView: DoubleClick date= " + date.toString() + " isInThisMonth= " + isInThisMonth);
            }
        });
    }
}
