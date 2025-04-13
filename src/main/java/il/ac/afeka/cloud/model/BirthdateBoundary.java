package il.ac.afeka.cloud.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class BirthdateBoundary {

    @NotBlank(message = "Day is required")
    @Pattern(regexp = "^(0?[1-9]|[12][0-9]|3[01])$", message = "Invalid day format")
    private String day;

    @NotBlank(message = "Month is required")
    @Pattern(regexp = "^(0?[1-9]|1[0-2])$", message = "Invalid month format")
    private String month;

    @NotBlank(message = "Month is required")
    @Pattern(regexp = "^(0?[1-9]|1[0-2])$", message = "Invalid month format")
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

    @Override
    public String toString() {
        return "BirthdateBoundary{" +
                "day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                '}';
    }


}
