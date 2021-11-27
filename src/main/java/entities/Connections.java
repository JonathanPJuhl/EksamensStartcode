package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "connections")
@NamedQuery(name = "connections.deleteAllRows", query = "DELETE from Connections")
public class Connections implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Relation relation;

    @Basic(optional = false)
    @NotNull
    @Column(name = "are_friends", length = 60)
    private boolean are_friends;

    @Basic(optional = false)
    @NotNull
    @Column(name = "requested", length = 60)
    private boolean requested;

    @Basic(optional = false)
    @NotNull
    @Column(name = "is_blocked", length = 60)
    private boolean is_blocked = false;

    public Connections() {

    }

    public Connections(Relation relation, boolean are_friends, boolean requested) {
        this.relation = relation;
        this.are_friends = are_friends;
        this.requested = requested;
    }

    public boolean isRequested() {
        return requested;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    public boolean are_friends() {
        return are_friends;
    }

    public void setAre_friends(boolean are_friends) {
        this.are_friends = are_friends;
    }

    public Relation getRelation() {
        return relation;
    }

    public boolean isIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(boolean is_blocked) {
        this.is_blocked = is_blocked;
    }
}