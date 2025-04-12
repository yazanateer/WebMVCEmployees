package il.ac.afeka.cloud.model;

public class BirthdateBoundary {
    private String day;
    private String month;
    private String year;

    public BirthdateBoundary() {}

    public BirthdateBoundary(String day, String month, String year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public BirthdateBoundary(Birthdate birthdate) {
       if(birthdate != null) {
           this.day = birthdate.getDay();
           this.month = birthdate.getMonth();
           this.year = birthdate.getYear();
       }
    }

    public Birthdate toEntity() {
        return new Birthdate(day, month, year);
    }

    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getMonth() {
        return month;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }


}
