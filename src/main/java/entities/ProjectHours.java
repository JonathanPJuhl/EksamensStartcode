package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projecthours")
@NamedQuery(name = "projecthours.deleteAllRows", query = "DELETE from ProjectHours")
public class ProjectHours implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Column(name = "user_story")
    private String userStory;


    @NotNull
    @Column(name = "desc")
    private String description;


    @Column(name = "hours_spent")
    private double hoursSpent;

   /* @ManyToOne(fetch = FetchType.LAZY)
    private Project project;*/

    public ProjectHours(String userStory, String description, double hoursSpent) {
        this.userStory = userStory;
        this.description = description;
        this.hoursSpent = hoursSpent;
    }

    public ProjectHours() {
    }

    public String getUserStory() {
        return userStory;
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

    public double getHoursSpent() {
        return hoursSpent;
    }

    public void setHoursSpent(double hoursSpent) {
        this.hoursSpent = hoursSpent;
    }

   /* public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }*/
}
