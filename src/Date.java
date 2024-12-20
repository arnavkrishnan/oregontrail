public class Date {

    private static final int[] COMMON_YEAR_DAYS = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final int[] LEAP_YEAR_DAYS = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public static String calculate(int days, int month, String type) {
        int year = 1848;
        int[] monthDays = isLeapYear(year) ? LEAP_YEAR_DAYS : COMMON_YEAR_DAYS;

        while (days >= 0) {
            int daysInMonth = monthDays[month];
            if (days < daysInMonth) {
                if (type.equals("date")){
                    return (month + 1) + "/" + (days + 1) + "/" + year;
                } else if (type.equals("month")){
                    return String.valueOf(month+1);
                } else if (type.equals("day")){
                    return String.valueOf(days+1);
                }
            } else {
                days -= daysInMonth;
                month++;
                if (month == 12) {
                    month = 0;
                    year++;
                    monthDays = isLeapYear(year) ? LEAP_YEAR_DAYS : COMMON_YEAR_DAYS;
                }
            }
        }
        return "";
    }

    private static boolean isLeapYear(int year) {

        // fun fact: every 100 years (like 1900) the leap year is NOT a leap year
        // but then, every 400 years the NOT-leap year becomes a leap year (like 2000!)
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0){
                    return true;
                }
                return false;
            }
            return true;
        }
        return false;
    }
}