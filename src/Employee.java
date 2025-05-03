package src;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.math.BigDecimal;

public class Employee {

    private int id;
    private String name;
    private String birthday;
    private String emailAddress;
    private BigDecimal salary;
    private String startDate;
    private int managerId;
    private String role;
    private String department;

    public Employee(int id, String name, String birthday, String emailAddress,
                    BigDecimal salary, String startDate, int managerId,
                    String role, String department) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.emailAddress = emailAddress;
        this.salary = salary;
        this.startDate = startDate;
        this.managerId = managerId;
        this.role = role;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBirthdayString() {
        return birthday;
    }

    public LocalDate getBirthday() {
        if (this.birthday == null || this.birthday.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(this.birthday);
        } catch (DateTimeParseException e) {
            System.err.println("Fehler beim Parsen des Geburtsdatums für Mitarbeiter " + id + ": " + this.birthday);
            return null;
        }
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public String getStartDateString() {
        return startDate;
    }

    public LocalDate getStartDate() {
        if (this.startDate == null || this.startDate.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(this.startDate);
        } catch (DateTimeParseException e) {
            System.err.println("Fehler beim Parsen des Startdatums für Mitarbeiter " + id + ": " + this.startDate);
            return null;
        }
    }


    public int getManagerId() {
        return managerId;
    }

    public String getRole() {
        return role;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", salary=" + salary +
                ", startDate='" + startDate + '\'' +
                ", managerId=" + managerId +
                ", role='" + role + '\'' +
                ", department='" + department + '\'' +
                '}';
    }

}