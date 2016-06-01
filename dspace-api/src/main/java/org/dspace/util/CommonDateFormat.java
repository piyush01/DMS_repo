package org.dspace.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CommonDateFormat {

  public static final String SCREEEN_DATE_FORMAT = "dd-MMM-yyyy";
  public static final String SCREEEN_MERISHOP = "MM/dd/yyyy";
  private static final SimpleDateFormat dateformat = new SimpleDateFormat(SCREEEN_MERISHOP);
  private static final SimpleDateFormat dateformatddmmmyyyy = new SimpleDateFormat("dd/MMM/yyyy");
  private static final SimpleDateFormat sdf = new SimpleDateFormat(SCREEEN_DATE_FORMAT);
  private static final String SCREEN_DMS="yyyy-MM-dd";
  private static final SimpleDateFormat postgreydateformat = new SimpleDateFormat(SCREEN_DMS);
  private static final String SCREEN_DMS_1="dd/MM/yyyy";
  
  /**
   * Returns the common {@link DateFormat} implementation being used across this application.
   * 
   * @return {@link DateFormat} instance
   */
  public static DateFormat getDateFormat() {
    return sdf;
  }

  public static String displayFormat(long timestamp) {
    return sdf.format(new Date(timestamp));
  }

  public static String currentDate() {
    return CommonDateFormat.displayFormat(System.currentTimeMillis());
  }

  public static String currentDateForPresenation() {
    return sdf.format(new Date());
  }

  public static Date getDateAsString(String displayDate) {
    Date formatDate = null;
    try {
      formatDate = new Date(sdf.parse(displayDate).getTime());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return formatDate;

  }
  
  public static Date getDateyyyymmddAsString(String displayDate) throws ParseException {	
	  Date date = dateformat.parse(displayDate);
	  String convertedDate = postgreydateformat.format(date);  
      Date requireddate=postgreydateformat.parse(convertedDate);
      
  return requireddate;
  }
  
  public static Date getDateyyyymmdd(String displayDate) throws ParseException {	
	  Date date = dateformatddmmmyyyy.parse(displayDate);
	  String convertedDate = postgreydateformat.format(date);  
      Date requireddate=postgreydateformat.parse(convertedDate);
      
  return requireddate;
  }
  
  public static String getTodayDateString() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, 0);
    return dateFormat.format(cal.getTime());
  }

  public static String getTodayDatemmddyyyyString() {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, 0);
    return dateFormat.format(cal.getTime());
  }
  
  public static String getTodayDateddmmyyyyString(Date date)
  {
	     SimpleDateFormat mdyFormat = new SimpleDateFormat("MM/dd/yyyy");
	    // Format the date to Strings
	    String mdy = mdyFormat.format(date);
	   	    
	    return mdy;
	  }


  @SuppressWarnings("deprecation")
  public static Date getDateAsStringyyyymmdd(String displayDate) {
    Date formatDate = null;
    try {
      formatDate = new Date(dateformat.parse(displayDate).getTime());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return formatDate;

  }

  public static String getDateForPresenation(String date) {
    String s = null;
    try {
      // than what is expected in other parts of the application??
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      s = displayFormat(dateFormat.parse(date).getTime());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return s;
  }

  public static Timestamp getDateAsTimestamp(String displayDate) {
    Timestamp formatDate = null;
    try {
      formatDate = new Timestamp(sdf.parse(displayDate).getTime());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return formatDate;

  }

  /**
   * What the heck is this method doing?? Does this even belong to this util class??
   * 
   * @param arrayLong
   * @return
   */
  public static Object[] convertLongToStringArray(Object[] arrayLong) {
    if (arrayLong == null || arrayLong.length == 0)
      return null;

    String[] str = new String[arrayLong.length];

    for (int i = 0; i < arrayLong.length; i++) {
      str[i] = arrayLong[i].toString();

    }
    arrayLong = str;

    return arrayLong;
  }

  /**
   * This function gives start date of invoice report according to start date of work order date and
   * user giving date. user giving date will be YYYY-MM format.
   * 
   * @param workOrderDate
   * @param searchDate
   * @return
   */
  public static String getStartDate(Date workOrderDate, String searchDate) {

    String date = null;
    int wodate = workOrderDate.getDate();
    int womonth = workOrderDate.getMonth();
    int woYear = workOrderDate.getYear() + 1900;
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
    String userYear = searchDate.substring(0, 4);
    String userMonth = searchDate.substring(searchDate.indexOf("-") + 1);

    if (woYear < Integer.parseInt(userYear)) {
      date = "01" + "-" + userMonth + "-" + userYear;
    } else if (woYear == Integer.parseInt(userYear)) {
      if (womonth == Integer.parseInt(userMonth) - 1) {
        date = wodate + "-" + userMonth + "-" + userYear;
      } else if (womonth < Integer.parseInt(userMonth) - 1) {
        date = "01" + "-" + userMonth + "-" + userYear;
      } else if (date == null) {
        date = dateFormat2.format(workOrderDate);
      }
    }
    return date;
  }

  /**
   * This function gives end date of invoice report according to enddate of work order date and user
   * giving date. user giving date will be YYYY-MM format.
   * 
   * @param workOrderDate
   * @param searchDate
   * @return String
   * @author manoj.shakya
   */
  public static String getEndDate(Date workOrderDate, String searchDate) {
    String date = null;
    int woLastdate = workOrderDate.getDate();
    int woLastmonth = workOrderDate.getMonth();
    int woLastYear = workOrderDate.getYear() + 1900;
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    String userYear = searchDate.substring(0, 4);
    String userMonth = searchDate.substring(searchDate.indexOf("-") + 1);

    if (woLastYear > Integer.parseInt(userYear)) {
      date = getNoOfDays(Integer.parseInt(userMonth), Integer.parseInt(userYear)) + "-" + userMonth
          + "-" + userYear;
    } else if (woLastYear == Integer.parseInt(userYear)) {
      if (woLastmonth > Integer.parseInt(userMonth) - 1) {
        date = getNoOfDays(Integer.parseInt(userMonth), Integer.parseInt(userYear)) + "-"
            + userMonth + "-" + userYear;
      } else if (woLastmonth == Integer.parseInt(userMonth)) {
        date = woLastdate + "-" + userMonth + "-" + userYear;
      } else if (date == null) {
        date = dateFormat2.format(workOrderDate);
      }
    }
    return date;
  }

  /**
   * This function gives the no of days in a month.
   * 
   * @param month
   * @return int
   * @author manoj.shakya
   */
  public static int getNoOfDays(int month, int year) {
    Calendar calendar = Calendar.getInstance();
    int date = 1;
    calendar.set(year, month - 1, date);
    int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    return days;
  }

  // Example DD-MM-YYYY convert to YYYY-MM-DD
  public static String getDateFormat(String date, String format, String convert) {
    String formatedDate = null;
    Date tempDate = new Date();
    SimpleDateFormat currentFormat = new SimpleDateFormat(format);
    SimpleDateFormat convertFormat = new SimpleDateFormat(convert);
    try {
      tempDate = currentFormat.parse(date);
      formatedDate = convertFormat.format(tempDate);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return formatedDate;
  }

  public static String getOneDayIncrementDate(String yearMonth, int day) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    SimpleDateFormat dateFormatd = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    c.setTime(dateFormat.parse(yearMonth));
    c.add(Calendar.DATE, day);
    return dateFormatd.format(c.getTime());

  }

  public static String now(String dateFormat) {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    return sdf.format(cal.getTime());
  }

  public static int getMonthDays(int year, String month) {
    int mon = 0;
    try {
      GregorianCalendar cal = new GregorianCalendar();
      if (month.equals("01") || month.equalsIgnoreCase("Jan")) {
        mon = 31;
      }
      if (cal.isLeapYear(year) && (month.equalsIgnoreCase("02") || month.equalsIgnoreCase("Feb"))) {
        mon = 29;
      } else if (!cal.isLeapYear(year)
          && (month.equalsIgnoreCase("02") || month.equalsIgnoreCase("Feb"))) {
        mon = 28;
      }
      if (month.equalsIgnoreCase("03") || month.equalsIgnoreCase("Mar")) {
        mon = 31;
      }
      if (month.equalsIgnoreCase("04") || month.equalsIgnoreCase("Apr")) {
        mon = 30;
      }
      if (month.equalsIgnoreCase("05") || month.equalsIgnoreCase("May")) {
        mon = 31;
      }
      if (month.equalsIgnoreCase("06") || month.equalsIgnoreCase("Jun")) {
        mon = 30;
      }
      if (month.equalsIgnoreCase("07") || month.equalsIgnoreCase("Jul")) {
        mon = 31;
      }
      if (month.equalsIgnoreCase("08") || month.equalsIgnoreCase("Aug")) {
        mon = 31;
      }
      if (month.equalsIgnoreCase("09") || month.equalsIgnoreCase("Sep")) {
        mon = 30;
      }
      if (month.equalsIgnoreCase("10") || month.equalsIgnoreCase("Oct")) {
        mon = 31;
      }
      if (month.equalsIgnoreCase("11") || month.equalsIgnoreCase("Nov")) {
        mon = 30;
      }
      if (month.equalsIgnoreCase("12") || month.equalsIgnoreCase("Dec")) {
        mon = 31;
      }

    } catch (Exception ne) {
      ne.printStackTrace();
    }
    return mon;
  }

  public static String getMonthName(String month) {
    String mon = "";
    try {

      if (month.equalsIgnoreCase("01")) {
        mon = "Jan";
      }
      if (month.equalsIgnoreCase("02")) {
        mon = "Feb";
      }
      if (month.equalsIgnoreCase("03")) {
        mon = "Mar";
      }
      if (month.equalsIgnoreCase("04")) {
        mon = "Apr";
      }
      if (month.equalsIgnoreCase("05")) {
        mon = "May";
      }
      if (month.equalsIgnoreCase("06")) {
        mon = "Jun";
      }
      if (month.equalsIgnoreCase("07")) {
        mon = "Jul";
      }
      if (month.equalsIgnoreCase("08")) {
        mon = "Aug";
      }
      if (month.equalsIgnoreCase("09")) {
        mon = "Sep";
      }
      if (month.equalsIgnoreCase("10")) {
        mon = "Oct";
      }
      if (month.equalsIgnoreCase("11")) {
        mon = "Nov";
      }
      if (month.equalsIgnoreCase("12")) {
        mon = "Dec";
      }

    } catch (Exception ne) {
      ne.printStackTrace();
    }
    return mon;
  }

  public static float dateDifference(Date startdate, Date enddate) {
    float days = 0.0f;
    try {
      long startmillisecond = startdate.getTime();
      long endmillisecond = enddate.getTime();
      long total = endmillisecond - startmillisecond;
      days = total / (1000 * 60 * 60 * 24);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return days + 1;
  }

  public static Integer finddatedifference(String dateStart, String dateStop) {
    Integer days = 0;
    // HH converts hour in 24 hours format (0-23), day calculation
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    Date d1 = null;
    Date d2 = null;
    try {
      d1 = format.parse(dateStart);
      d2 = format.parse(dateStop);
      // in milliseconds
      long diff = d2.getTime() - d1.getTime();
      long diffSeconds = diff / 1000 % 60;
      long diffMinutes = diff / (60 * 1000) % 60;
      long diffHours = diff / (60 * 60 * 1000) % 24;
      long diffDays = diff / (24 * 60 * 60 * 1000);
      days = (int) diffDays;

    } catch (Exception e) {
      e.printStackTrace();
    }
    return days;
  }

  public static String getDayIncrementDate(String date, int day) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
	   // SimpleDateFormat dateFormatd = new SimpleDateFormat("dd/MMM/yyyy");
	    Calendar c = Calendar.getInstance();
	    try {
			c.setTime(dateFormat.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    c.add(Calendar.DATE, day);
	    return dateFormat.format(c.getTime());

	  }
  
  public static void main(String[] args) throws ParseException  {
	
    //int days = finddatedifference("10/14/2012 09:29:58", "10/23/2012 09:29:58");
   // Date days=getDateyyyymmddAsString("08/23/2016");
   String days= getTodayDateddmmyyyyString(new Date());
    System.out.println("" + days);
  }

}
