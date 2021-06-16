package entities;

public class InvoiceDTO {
    private String userStory;
    private String description;
    private double hours;
    private double hourlyRate;
    private String devName;

    public InvoiceDTO(String userStory, String description, double hours, double hourlyRate, String devName) {
        this.userStory = userStory;
        this.description = description;
        this.hours = hours;
        this.hourlyRate = hourlyRate;
        this.devName = devName;
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
                "userStory='" + userStory + '\'' +
                ", description='" + description + '\'' +
                ", hours=" + hours +
                ", hourlyRate=" + hourlyRate +
                '}';
    }

    public String getUserStory() {
        return userStory;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public void setUserStory(String userStory) {
        this.userStory = userStory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}
