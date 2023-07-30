package main.java.com.gomu.gomustock;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyDate {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    public MyDate() {

    }

    public int getTodayDayofWeek() {
        LocalDate  date = LocalDate.now();
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getValue();
    }

    public String getCurrentTime(String index) {
        String result="";
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        switch(index){
            case "YEAR":
                sdf = new SimpleDateFormat("yyyy");
                result = sdf.format(date);
                break;
            case "MONTH" :
                sdf = new SimpleDateFormat("MM");
                result = sdf.format(date);
                break;
            case "DAY" :
                sdf = new SimpleDateFormat("dd");
                result = sdf.format(date);
                break;
        }
        return result;
    }


    public String getBeforeTime(String index) {
        String result="";
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        int before;

        switch(index){
            case "YEAR":
                sdf = new SimpleDateFormat("yyyy");
                result = sdf.format(date);
                before = Integer.parseInt(result)-1;
                result = Integer.toString(before);
                break;
            case "MONTH" :
                sdf = new SimpleDateFormat("MM");
                result = sdf.format(date);
                before = Integer.parseInt(result)-1;
                if(before < 1) before = 12;
                result = Integer.toString(before);
                break;
            case "DAY" :
                sdf = new SimpleDateFormat("dd");
                result = sdf.format(date);
                before = Integer.parseInt(result)-1;
                if(before < 1) before = 30;
                result = Integer.toString(before);
                break;
        }
        return result;
    }
    public String  getToday() {
        Date date = new Date();
        String result;
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMdd");
        result = mFormat.format(date);
        return result;
    }
    public String  getTodayFullTime() {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());

        String result;
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        result = mFormat.format(date);
        return result;
    }

    //어제 날짜를 가져온다 : 리턴형식 yyyyMMdd
    public String  getYesterday() {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMdd");
        long mNow;
        int before;

        // 어제 날짜를 가져온다
        mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);
        calendar.setTime(mDate);
        int dayweek = calendar.get(Calendar.DAY_OF_WEEK);
        // 어제 주가를 불러와야 하니까 일요일, 월요일 예외처리해줌
        // 국경일이 끼어 있으면 처리할 수 없음(어떻게 되지?)
        if(dayweek == 1) before = -2; // 일요일이면 금요일 날짜로 설정
        else if (dayweek == 2) before = -3; // 월요일이어도 금요일 날짜로 설정
        else before = -1; // 그외 모든요일은 전날로 설정

        calendar.add(Calendar.DATE, before);
        return mFormat.format(calendar.getTime());
    }

    public long epochday_today() {
        Date date = new Date();
        String result="";

        // 년, 월, 일을 뽑아낸다
        sdf = new SimpleDateFormat("yyyy");
        result = sdf.format(date);
        int year = Integer.parseInt(result);

        sdf = new SimpleDateFormat("MM");
        result = sdf.format(date);
        int month = Integer.parseInt(result);

        sdf = new SimpleDateFormat("dd");
        result = sdf.format(date);
        int day = Integer.parseInt(result);

        sdf = new SimpleDateFormat("hh");
        result = sdf.format(date);
        int hour = Integer.parseInt(result);

        // epoch day를 계산한
        LocalDate date1 = LocalDate.of(year, month, day);
        long today = date1.toEpochDay()*86400;

        return today;
    }

    public long yf_epoch_today() {
        return System.currentTimeMillis()/(1000); // 초단위 epoch day
    }
    public long yf_epoch_1yearago() {
        long today = System.currentTimeMillis()/(1000); // 초단위 epoch day
        return today - (365*24*60*60);
    }

    // epoch 값 = (특정일-25569)*86400
    public long epochday_before3month() {
        Date date = new Date();
        String result="";

        // 3달전의 날짜를 가져온다
        SimpleDateFormat sdformat = new SimpleDateFormat("MM/dd/yyyy");
        Date before3Mon = getMonthfromThisDate(date,-3);

        // 년, 월, 일을 뽑아낸다
        sdf = new SimpleDateFormat("yyyy");
        result = sdf.format(before3Mon);
        int year = Integer.parseInt(result);

        sdf = new SimpleDateFormat("MM");
        result = sdf.format(before3Mon);
        int month = Integer.parseInt(result);

        sdf = new SimpleDateFormat("dd");
        result = sdf.format(before3Mon);
        int day = Integer.parseInt(result);

        // epoch day를 계산한
        LocalDate date1 = LocalDate.of(year, month, day);
        long before3month = date1.toEpochDay()*86400;

        return before3month;
    }

    public static Date getMonthfromThisDate(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    public static Date addDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    public String getYesterday2() {
        String str="";
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        // 하루 전의 date를 넘겨준다
        date = addDay(date,-1);
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
        str = sdformat.format(date);
        return str;
    }

    public String getBeforeday(int beforeday) {
        String str="";
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        // 하루 전의 date를 넘겨준다
        date = addDay(date,-beforeday);
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
        str = sdformat.format(date);
        return str;
    }

    public String today2string() {
        Date date = new Date();
        String str="";
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
        str = sdformat.format(date);
        return str;
    }
    public String MonthAgo2String(int months) {
        Date date = new Date();
        String str="";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        date = cal.getTime();
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
        str = sdformat.format(date);
        return str;
    }

    public String MonthAgo15(int months) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");;
        cal.set(Calendar.DATE, 28);
        cal.add(Calendar.MONTH, months-1);
        String date = format.format(cal.getTime());
        return date;
    }
    public String MonthNow15() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DATE, 28);
        String date = format.format(cal.getTime());
        //cal.add(Calendar.MONTH, -months);
        //String startdate = format.format(cal.getTime());
        return date;
    }

    public String YearAgo2String(int years) {
        Date date = new Date();
        String str="";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, years);
        date = cal.getTime();
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
        str = sdformat.format(date);
        return str;
    }

}
