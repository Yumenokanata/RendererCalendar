package com.fenrir.app.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import indi.yume.tools.renderercalendar.CalendarView;
import indi.yume.tools.renderercalendar.listener.GestureListener;
import indi.yume.tools.renderercalendar.model.DayDate;
import indi.yume.tools.renderercalendar.util.LogUtil;
import indi.yume.tools.renderercalendar.util.ScrollMode;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.calendar_view)
    CalendarView calendarView;
    @Bind(R.id.move_to_back_btn)
    Button moveToBackBtn;
    @Bind(R.id.move_to_next_btn)
    Button moveToNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LogUtil.setDebug(true);
        calendarView.setScrollMode(ScrollMode.SINGLE_PAGE);
        calendarView.setOnDayClickListener(new GestureListener() {
            @Override
            public boolean onClick(int currentYear, int currentMonth, DayDate date, boolean isInThisMonth) {
                System.out.println("onClick: date= " + date.toString() + " isInThisMonth= " + isInThisMonth);
                if (!isInThisMonth)
                    if (date.getYear() > currentYear || (currentYear == date.getYear() && date.getMonth() > currentMonth))
                        calendarView.moveToNextMonth();
                    else
                        calendarView.moveToBackMonth();
                return isInThisMonth;
            }

            @Override
            public boolean onDoubleClick(int currentYear, int currentMonth, DayDate date, boolean isInThisMonth) {
                System.out.println("onDoubleClick: date= " + date.toString() + " isInThisMonth= " + isInThisMonth);
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
                return true;
            }

            @Override
            public void onSelect(int currentYear, int currentMonth, DayDate date, boolean isInThisMonth) {
                System.out.println("onSelect: date= " + date.toString() + " isInThisMonth= " + isInThisMonth);
            }
        });

        moveToNextBtn.setOnClickListener(v -> calendarView.moveToNextMonth());

        moveToBackBtn.setOnClickListener(v -> calendarView.moveToBackMonth());
    }
}

//public class MainActivity extends BaseFragmentManagerActivity {
//
//    @Override
//    public int fragmentViewId() {
//        return R.id.fragment_collect;
//    }
//
//    @Override
//    public Map<String, Class<? extends BaseManagerFragment>> BaseFragmentWithTag() {
//        Map<String, Class<? extends BaseManagerFragment>> map = new HashMap<>();
//        map.put("tag1", BlankFragment.class);
//        return map;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_main);
//        super.onCreate(savedInstanceState);
//    }
//}