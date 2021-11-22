package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "login_attempts")
@NamedQuery(name = "login-attempts.deleteAllRows", query = "DELETE from LoginAttempts")
public class LoginAttempts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ip")
    private String ip;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "target_account")
    private String targetAccount;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "intent")
    private String intent;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "timestamp")
    private String timestamp;

    public LoginAttempts() {
    }

    public LoginAttempts(String ip, String targetAccount, String intent) {
        this.ip = ip;
        this.targetAccount = targetAccount;
        this.intent = intent;
    }

    public String getIp() {
        return ip;
    }

    public String getTargetAccount() {
        return targetAccount;
    }

    public String getIntent() {
        return intent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}