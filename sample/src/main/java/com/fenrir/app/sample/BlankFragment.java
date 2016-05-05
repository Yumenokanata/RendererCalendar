package com.fenrir.app.sample;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import indi.yume.tools.renderercalendar.CalendarView;
import indi.yume.tools.renderercalendar.listener.GestureListener;
import indi.yume.tools.renderercalendar.model.DayDate;
import indi.yume.tools.renderercalendar.util.ScrollMode;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    @Bind(R.id.calendar_view)
    CalendarView calendarView;
    @Bind(R.id.move_to_back_btn)
    Button moveToBackBtn;
    @Bind(R.id.move_to_next_btn)
    Button moveToNextBtn;
    @Bind(R.id.jump_button)
    Button jumpButton;

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        ButterKnife.bind(this, view);
//
//        calendarView.setScrollMode(ScrollMode.SINGLE_PAGE);
//        calendarView.setOnDayClickListener(new GestureListener() {
//            @Override
//            public boolean onClick(int currentYear, int currentMonth, DayDate date, boolean isInThisMonth) {
//                System.out.println("onClick: date= " + date.toString() + " isInThisMonth= " + isInThisMonth);
//                if (!isInThisMonth)
//                    if (date.getYear() > currentYear || (currentYear == date.getYear() && date.getMonth() > currentMonth))
//                        calendarView.moveToNextMonth();
//                    else
//                        calendarView.moveToBackMonth();
//                return isInThisMonth;
//            }
//
//            @Override
//            public boolean onDoubleClick(int currentYear, int currentMonth, DayDate date, boolean isInThisMonth) {
//                System.out.println("onDoubleClick: date= " + date.toString() + " isInThisMonth= " + isInThisMonth);
//                return false;
//            }
//
//            @Override
//            public void onSelect(int currentYear, int currentMonth, DayDate date, boolean isInThisMonth) {
//                System.out.println("onSelect: date= " + date.toString() + " isInThisMonth= " + isInThisMonth);
//            }
//        });
//
//        moveToNextBtn.setOnClickListener(v -> calendarView.moveToNextMonth());
//
//        moveToBackBtn.setOnClickListener(v -> calendarView.moveToBackMonth());
//
//        jumpButton.setOnClickListener(v -> startFragment(new Intent(getContext(), BlankFragment2.class)));
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.unbind(this);
//    }
}
