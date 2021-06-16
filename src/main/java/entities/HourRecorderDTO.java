package entities;

public class HourRecorderDTO {
    private String email;
    private String projectName;
    private String userStory;
    private String description;
    private double hoursSpent;


    public HourRecorderDTO(String email, String projectName, double hoursSpent, String userStory) {
        this.email = email;
        this.projectName = projectName;
        this.hoursSpent = hoursSpent;
        this.userStory = userStory;
    }
    public HourRecorderDTO(String email, String projectName, double hoursSpent, String userStory, String description) {
        this.email = email;
        this.projectName = projectName;
        this.hoursSpent = hoursSpent;
        this.userStory = userStory;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
