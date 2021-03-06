package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.inject.Default;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "users")
@NamedQuery(name = "users.deleteAllRows", query = "DELETE from User")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_name", length = 60)
    private String username;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "user_pass")
    private String password;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "profile_text")
    private String profileText = "";

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "two_factor_code")
    private String twoFactorCode = "";

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "unlock_key")
    private String keyForUnlocking = "";

    @Basic(optional = false)
    @Default
    @NotNull
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Basic(optional = false)
    @Default
    @NotNull
    @Column(name = "is_verified")
    private boolean isVerified = false;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "verification_key")
    private String verificationKey = "";

    @Basic(optional = false)
    @Default
    @NotNull
    @Column(name = "ttl")
    private Date ttlBeforeDeletion;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "reset_pass_key")
    private String resetPassKey = "";

    @JoinTable(name = "user_roles", joinColumns = {
            @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
            @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
    @ManyToMany
    private List<Role> roleList = new ArrayList<>();

    public List<String> getRolesAsStrings() {
        if (roleList.isEmpty()) {
            return null;
        }
        List<String> rolesAsStrings = new ArrayList<>();
        roleList.forEach((role) -> {
            rolesAsStrings.add(role.getRoleName());
        });
        return rolesAsStrings;
    }

    public User() {
    }

    public boolean verifyPassword(String pw) {
        return (BCrypt.checkpw(pw, password));
    }

    public User(String username, String password) {
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User(String username, boolean isDeleted) {
        this.username = username;
        this.isDeleted = isDeleted;
    }

    public User(String username, String password, String profileText) {
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.profileText = profileText;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String userPass) {
        this.password = BCrypt.hashpw(userPass, BCrypt.gensalt());
    }

    public void addRole(Role userRole) {
        roleList.add(userRole);
    }

    public String getUsername() {
        return username;
    }

    public String getProfileText() {
        return profileText;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setTwoFactorCode(String twoFactorCode) {
        this.twoFactorCode = twoFactorCode;
    }

    public String getTwoFactorCode() {
        return twoFactorCode;
    }

    public String getKeyForUnlocking() {
        return keyForUnlocking;
    }

    public void setKeyForUnlocking(String keyForUnlocking) {
        this.keyForUnlocking = keyForUnlocking;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public Date getTtlBeforeDeletion() {
        return ttlBeforeDeletion;
    }

    public void setTtlBeforeDeletion(Date ttlBeforeDeletion) {
        this.ttlBeforeDeletion = ttlBeforeDeletion;
    }

    public String getVerificationKey() {
        return verificationKey;
    }

    public void setVerificationKey(String verificationKey) {
        this.verificationKey = verificationKey;
    }

    public String getResetPassKey() {
        return resetPassKey;
    }

    public void setResetPassKey(String resetPassKey) {
        this.resetPassKey = resetPassKey;
    }

    @Override
    public String toString() {
        return "Enduser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}