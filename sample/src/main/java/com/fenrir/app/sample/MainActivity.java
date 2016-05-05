package com.fenrir.app.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import indi.yume.tools.renderercalendar.CalendarView;
import indi.yume.tools.renderercalendar.listener.GestureListener;
import indi.yume.tools.renderercalendar.listener.OnDayClickListener;
import indi.yume.tools.renderercalendar.model.DayDate;
import indi.yume.tools.renderercalendar.util.ScrollMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CalendarView calendarView = (CalendarView) findViewById(R.id.calendar_view);
        Button nextBtn = (Button) findViewById(R.id.move_to_next_btn);
        Button backBtn = (Button) findViewById(R.id.move_to_back_btn);

        if(calendarView == null)
            return;

        calendarView.setScrollMode(ScrollMode.SINGLE_PAGE);
        calendarView.setOnDayClickListener(new GestureListener() {
            @Override
            public boolean onClick(int currentYear, int currentMonth, DayDate date, boolean isInThisMonth) {
                System.out.println("onClick: date= " + date.toString() + " isInThisMonth= " + isInThisMonth);
                if(!isInThisMonth)
                    if(date.getYear() > currentYear || (currentYear == date.getYear() && date.getMonth() > currentMonth))
                        calendarView.moveToNextMonth();
                    else
                        calendarView.moveToBackMonth();
                return isInThisMonth;
            }

            @Override
            public boolean onDoubleClick(int currentYear, int currentMonth, DayDate date, boolean isInThisMonth) {
                System.out.println("onDoubleClick: date= " + date.toString() + " isInThisMonth= " + isInThisMonth);
                return false;
            }

            @Override
            public void onSelect(int currentYear, int currentMonth, DayDate date, boolean isInThisMonth) {
                System.out.println("onSelect: date= " + date.toString() + " isInThisMonth= " + isInThisMonth);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.moveToNextMonth();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.moveToBackMonth();
            }
        });
    }
}
