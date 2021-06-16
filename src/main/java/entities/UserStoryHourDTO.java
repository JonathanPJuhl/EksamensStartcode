package entities;

import java.util.ArrayList;
import java.util.List;

public class UserStoryHourDTO {


    private List<String> userStories;


    public UserStoryHourDTO(  List<String> userStories) {


        this.userStories = new ArrayList<>();

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
