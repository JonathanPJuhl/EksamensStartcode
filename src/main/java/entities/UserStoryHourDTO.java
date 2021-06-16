package entities;

import java.util.ArrayList;
import java.util.List;

public class UserStoryHourDTO {

    private String projectName;
    private List<String> userStories;


    public UserStoryHourDTO( String projectName, List<String> userStories) {

        this.projectName = projectName;
        this.userStories = new ArrayList<>();

    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<String> getUserStories() {
        return userStories;
    }

    public void setUserStories(List<String> userStories) {
        this.userStories = userStories;
    }
    public void addUserStories(String userStories) {
        this.userStories.add(userStories);
    }


}
