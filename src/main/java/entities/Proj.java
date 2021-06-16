package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "Proj")
@NamedQuery(name = "Proj.deleteAllRows", query = "DELETE from Proj")
public class Proj implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Basic(optional = false)
  @Column(name = "project_name")
  private String name;

  @Basic(optional = false)
  @Size(min = 1, max = 255)
  @Column(name = "description")
  private String description;


    @OneToMany
    private List<ProjectHours> projectHours = new ArrayList<>();

   @JoinTable(name = "dev_projects", joinColumns = {
           @JoinColumn(name = "project_name", referencedColumnName = "project_name")}, inverseJoinColumns = {
           @JoinColumn(name = "email", referencedColumnName = "email")})
   @ManyToMany
   private List<Developer> devList = new ArrayList<>();


  public Proj(String name, String description) {
    this.name = name;
    this.description = description;
    this.devList = new ArrayList<>();
  }

  public Proj() {}

  public void addProjectHours(ProjectHours hours){
    projectHours.add(hours);
  }

  public String getName() {
    return name;
  }
  public void addDev(Developer dev){
    devList.add(dev);
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

  public List<Developer> getDevList() {
    return devList;
  }

  public void setDevList(List<Developer> devList) {
    this.devList = devList;
  }

/*
  public void addProject(Project proj){
      projectsList.add(proj);
    }*/





}