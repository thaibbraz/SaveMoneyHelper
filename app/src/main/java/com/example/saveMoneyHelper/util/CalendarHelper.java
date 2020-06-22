package com.example.saveMoneyHelper.util;

import com.example.saveMoneyHelper.firebase.models.User;
import com.example.saveMoneyHelper.settings.UserSettings;

import java.util.Calendar;

import static com.example.saveMoneyHelper.settings.UserSettings.PERIOD_MONTHLY;


public class CalendarHelper {




    public static Calendar getStartDate(UserSettings userSettings){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        if (userSettings.getHomeCounterPeriod() == PERIOD_MONTHLY){
            calendar.set(Calendar.DAY_OF_MONTH, 1);

        }else{
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        }
        return calendar;
    }
    public static Calendar getEndDate(UserSettings userSettings){
        Calendar calendar = getStartDate(userSettings);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        if (userSettings.getHomeCounterPeriod() == PERIOD_MONTHLY){
            calendar.add(Calendar.MONTH,1);

        }else{
            calendar.add(Calendar.DAY_OF_WEEK,6);
        }
        return calendar;
    }
}
