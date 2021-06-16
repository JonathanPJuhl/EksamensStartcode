package entities;

public class HourRecorderDTO {
    private String email;
    private String projectName;
    private double hoursSpent;

    public HourRecorderDTO(String email, String projectName, double hoursSpent) {
        this.email = email;
        this.projectName = projectName;
        this.hoursSpent = hoursSpent;
    }

    @Override
    public String toString() {
        return "HourRecorderDTO{" +
                "email='" + email + '\'' +
                ", projectName='" + projectName + '\'' +
                ", hoursSpent=" + hoursSpent +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public double getHoursSpent() {
        return hoursSpent;
    }

    public void setHoursSpent(double hoursSpent) {
        this.hoursSpent = hoursSpent;
    }
}
