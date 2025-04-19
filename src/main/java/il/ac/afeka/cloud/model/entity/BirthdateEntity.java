package il.ac.afeka.cloud.model.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Birthdate")
public class BirthdateEntity {

    @Id
    private String id;

    private String day;
    private String month;
    private String year;

    public BirthdateEntity() {
    }

    public BirthdateEntity(String day, String month, String year) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.id = day + "-" + month + "-" + year;
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

    public void setId(String birthdateId) {
        this.id = birthdateId;
    }
}