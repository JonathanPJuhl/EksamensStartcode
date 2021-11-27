package entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
public class Relation implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "this_user", length = 60)
    private String this_user;

    @Basic(optional = false)
    @NotNull
    @Column(name = "other_user", length = 60)
    private String other_user;

    public Relation() {
    }

    public Relation(String this_user, String other_user) {
        this.this_user = this_user;
        this.other_user = other_user;
    }

    public String getThis_user() {
        return this_user;
    }

    public void setThis_user(String this_user) {
        this.this_user = this_user;
    }

    public String getOther_user() {
        return other_user;
    }

    public void setOther_user(String other_user) {
        this.other_user = other_user;
    }
}
