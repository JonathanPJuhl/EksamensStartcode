//package entities;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//import javax.persistence.*;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
//import org.mindrot.jbcrypt.BCrypt;
//
//@Entity
//@Table(name = "Developer")
//@NamedQuery(name = "Developer.deleteAllRows", query = "DELETE from Developer")
//public class Developer implements Serializable {
//
//  private static final long serialVersionUID = 1L;
//
//  @Id
//  @Basic(optional = false)
//  @NotNull
//  @Column(name = "email", length = 25)
//  private String email;
//
//  @Basic(optional = false)
//  @NotNull
//  @Column(name = "billing_pr_hour")
//  private double billingPrHour;
//
//  @Basic(optional = false)
//  @NotNull
//  @Size(min = 1, max = 255)
//  @Column(name = "phone_nr")
//  private String phoneNr;
//
//  @Basic(optional = false)
//  @NotNull
//  @Size(min = 1, max = 255)
//  @Column(name = "dev_name")
//  private String devName;
//
//  @Basic(optional = false)
//  @NotNull
//  @Size(min = 1, max = 255)
//  @Column(name = "user_pass")
//  private String password;
//
//
//
//  @JoinTable(name = "user_roles", joinColumns = {
//    @JoinColumn(name = "email", referencedColumnName = "user_name")}, inverseJoinColumns = {
//    @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
//  @ManyToMany
//  private List<Role> roleList = new ArrayList<>();
//
////  @ManyToMany(mappedBy = "devList")
////  private List<Proj> projectsList = new ArrayList<>();
//
//
//  public List<String> getRolesAsStrings() {
//    if (roleList.isEmpty()) {
//      return null;
//    }
//    List<String> rolesAsStrings = new ArrayList<>();
//    roleList.forEach((role) -> {
//        rolesAsStrings.add(role.getRoleName());
//      });
//    return rolesAsStrings;
//  }
//
//  public Developer() {}
//
//  //TODO Change when password is hashed
//   public boolean verifyPassword(String pw){
//        return( BCrypt.checkpw(pw, password));
//    }
//
//
//  public Developer(String username, String password) {
//    this.email = username;
//    this.password = BCrypt.hashpw(password, BCrypt.gensalt());
//
//  }
//
//  public Developer(String email, double billingPrHour, String phoneNr, String devName, String password) {
//    this.email = email;
//    this.billingPrHour = billingPrHour;
//    this.phoneNr = phoneNr;
//    this.devName = devName;
//    this.password = BCrypt.hashpw(password, BCrypt.gensalt());
//  }
///*
//  public void addProject(Project proj){
//      projectsList.add(proj);
//    }*/
//
//  public double getBillingPrHour() {
//    return billingPrHour;
//  }
//
//  public void setBillingPrHour(double billingPrHour) {
//    this.billingPrHour = billingPrHour;
//  }
//
//  public String getPhoneNr() {
//    return phoneNr;
//  }
//
//  public void setPhoneNr(String phoneNr) {
//    this.phoneNr = phoneNr;
//  }
//
//  public String getDevName() {
//    return devName;
//  }
//
//  public void setDevName(String devName) {
//    this.devName = devName;
//  }
//
//  public String getEmail() {
//    return email;
//  }
//
//  public void setEmail(String userName) {
//    this.email = userName;
//  }
//
//  public String getPassword() {
//    return this.password;
//  }
//
//  public void setPassword(String userPass) {
//    this.password = BCrypt.hashpw(userPass, BCrypt.gensalt());
//  }
//
//  public List<Role> getRoleList() {
//    return roleList;
//  }
//
//  public void setRoleList(List<Role> roleList) {
//    this.roleList = roleList;
//  }
//
//  public void addRole(Role userRole) {
//    roleList.add(userRole);
//  }
//
//
//
//
//  @Override
//  public String toString() {
//    return "Developer{" +
//            "username='" + email + '\'' +
//            ", password='" + password + '\'' +
//            '}';
//  }
//}