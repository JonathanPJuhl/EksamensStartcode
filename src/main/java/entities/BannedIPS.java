package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "bans")
@NamedQuery(name = "bans.deleteAllRows", query = "DELETE from BannedIPS")
public class BannedIPS {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ip", length = 25)
    private String iP;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "banned_until")
    private String bannedUntil;

    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "banned_Accounts")
    private String bannedFromAccount;

    public BannedIPS(String iP, String bannedUntil) {
        this.iP = iP;
        this.bannedUntil = bannedUntil;
    }

    public BannedIPS() {
    }

    public String getiP() {
        return iP;
    }

    public void setiP(String iP) {
        this.iP = iP;
    }

    public String getBannedUntil() {
        return bannedUntil;
    }

    public void setBannedUntil(String bannedUntil) {
        this.bannedUntil = bannedUntil;
    }

    public String getBannedFromAccount() {
        return bannedFromAccount;
    }

    public void setBannedFromAccount(String bannedFromAccount) {
        this.bannedFromAccount = bannedFromAccount;
    }
}
