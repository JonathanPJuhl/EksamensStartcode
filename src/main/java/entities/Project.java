package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Project")
@NamedQuery(name = "Project.deleteAllRows", query = "DELETE from Project")
public class Project  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "project_name")
    private String name;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "desc")
    private String description;

/*
    @OneToMany
    @JoinColumn(name="name")
    private List<ProjectHours> projectHours = new ArrayList<>();

    @ManyToMany(mappedBy = "projectsList")
    private List<Developer> developerList = new ArrayList<>();
*/

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Project() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

  /*  public List<ProjectHours> getProjectHours() {
        return projectHours;
    }

    public void setProjectHours(List<ProjectHours> projectHours) {
        this.projectHours = projectHours;
    }
    public void addProjectHours(ProjectHours hours){
        projectHours.add(hours);
    }*/

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
