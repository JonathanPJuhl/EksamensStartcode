package entities;

public class HourRecorderDTO {
    private String email;
    private String projectName;
    private String userStory;
    private double hoursSpent;


    public HourRecorderDTO(String email, String projectName, double hoursSpent, String userStory) {
        this.email = email;
        this.projectName = projectName;
        this.hoursSpent = hoursSpent;
        this.userStory = userStory;
    }

    @Override
    public String toString() {
        return "HourRecorderDTO{" +
                "email='" + email + '\'' +
                ", projectName='" + projectName + '\'' +
                ", userStory='" + userStory + '\'' +
                ", hoursSpent=" + hoursSpent +
                '}';
    }

    public String getUserStory() {
        return userStory;
    }

    public void setUserStory(String userStory) {
        this.userStory = userStory;
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
