package apkt.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatService {

    public static String brDateFormat(Date date) {

        DateFormat dayFormat = new SimpleDateFormat("dd");
        DateFormat monthFormat = new SimpleDateFormat("MM");
        DateFormat yearFormat = new SimpleDateFormat("yyyy");

        String dayString = dayFormat.format(date);
        String monthString = monthFormat.format(date);
        String yearString = yearFormat.format(date);

        String dateString = dayString.concat("/").concat(monthString)
                        .concat("/").concat(yearString);

        return dateString;
    }

    public static String time24hourFormatString(Date date) {

        DateFormat time24HourFormat = new SimpleDateFormat("HH:mm");

        String time24HourString = time24HourFormat.format(date);

        // strips '0' when time starts with '0' (ex.: '05:54')
        if (time24HourString.charAt(0) == '0'){
                time24HourString = time24HourString.substring(1);
        }

        return time24HourString;
    }

    public static boolean compareWithTodayDate(Date date) {

        Date today = new Date();

        DateFormat dayFormat = new SimpleDateFormat("dd");
        DateFormat monthFormat = new SimpleDateFormat("MM");
        DateFormat yearFormat = new SimpleDateFormat("yyyy");

        String dayStringToday = dayFormat.format(today);
        String monthStringToday = monthFormat.format(today);
        String yearStringToday = yearFormat.format(today);

        String dayString = dayFormat.format(date);
        String monthString = monthFormat.format(date);
        String yearString = yearFormat.format(date);

        String dateStringToday = dayStringToday.concat("/").concat(monthStringToday)
                        .concat("/").concat(yearStringToday);

        String dateString = dayString.concat("/").concat(monthString)
                        .concat("/").concat(yearString);

        if (dateStringToday.equals(dateString)){
                return true;
        }

        return false;
    }

}
