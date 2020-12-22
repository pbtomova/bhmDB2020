//import java.time.LocalDate;
//import java.time.LocalTime;
import java.sql.Date;

/** Patient is a class which contains all the data for one patient */

public class Patient {
    // The following fields list patient's profile information
    private String name;
    private int hospID;
    private Date DOB; // String for now, will figure out how to set it as a date
    private String timeOfBirth;
    private double weight;
    private String gender;
    private String motherName;
    private String fatherName;
    private String contactNum;
    private String condition;   // stores additional details on pre-existing conditions/diseases
    private double healthIndex;
    //private MonitoredParams param;

    // constructors
    public Patient(String name) {
        this.name = name;
//        param = new MonitoredParams();
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public Date getDOB() {
        return DOB;
    }
    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getHospID() {
        return hospID;
    }
    public void setHospID(int hospID) {
        this.hospID = hospID;
    }

    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getMotherName() {
        return motherName;
    }
    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherName() {
        return fatherName;
    }
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getContactNum() {
        return contactNum;
    }
    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getTimeOfBirth() {
        return timeOfBirth;
    }
    public void setTimeOfBirth(String timeOfBirth) {
        this.timeOfBirth = timeOfBirth;
    }

    public void setHealthIndex(double perc) {
        // code to calculate and store health index
        healthIndex = perc;
    }
    public double getHealthIndex() {
        return healthIndex;
    }

    public String getCondition() {
        return condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }

    // Main functions
    // set monitoredParams from data in text files
    // public void graphPlotter()
}
