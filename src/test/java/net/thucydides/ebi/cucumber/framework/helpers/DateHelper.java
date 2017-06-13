package net.thucydides.ebi.cucumber.framework.helpers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
    public static void main(String[] args) throws Exception {
        String yy = getFutureDate("10-1-2017",0, "DD-MM-YYYY");
        String xx = getTimeStamp();
        System.out.println(xx);
    }

    public static String getDateToDay(String dtformat) throws Exception {
        try{
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(dtformat);
            String newDate = DATE_FORMAT.format(new Date());
            return newDate;
        }catch (Exception e){
            throw new Exception("ERROR: While getting the Date in the required format");
        }
    }

    public static String getDateToDay(String dtformat, String caseInfo) throws Exception {
        try{
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(dtformat);
            String newDate = DATE_FORMAT.format(new Date());
            if(caseInfo.contains("Upper")){
                newDate = newDate.toUpperCase();
            }
            return newDate;
        }catch (Exception e){
            throw new Exception("ERROR: While getting the Date in the required format");
        }
    }

    public static String getFutureDate(String date,int change, String format) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(date));
            c.add(Calendar.DATE, change);  // number of days to add
            date = sdf.format(c.getTime());
            return date;
        }catch (Exception e){
            throw new Exception("ERROR: While getting the Future Date in the required format");
        }
    }

    public static String getAnyDateWithRequiredFormat(int days, String format, String caseInfo) throws Exception {
        String dtValue = null;
        try{
            Calendar cal  = Calendar.getInstance();
            cal.add(Calendar.DATE, days);
            SimpleDateFormat s = new SimpleDateFormat(format);
            dtValue = s.format(new Date(cal.getTimeInMillis()));
            if(caseInfo.contains("Upper")){
                dtValue = dtValue.toUpperCase();
            }
        }catch (Exception e){
            throw new Exception("");
        }
        return dtValue;
    }

    public static String getTimeStamp() throws Exception {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            return sdf.format(timestamp);
        }catch (Exception e){
            throw new Exception("ERROR: While getting the time stamp");
        }
    }
}
