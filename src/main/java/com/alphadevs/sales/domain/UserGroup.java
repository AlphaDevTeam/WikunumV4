package com.alphadevs.sales.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * UserGroup Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "UserGroup Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "user_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
public class UserGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "group_name", nullable = false)
    private String groupName;

    @OneToOne
    @JoinColumn(unique = true)
    @NotAudited
    private DocumentHistory history;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "user_group_user_permissions",
               joinColumns = @JoinColumn(name = "user_group_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "user_permissions_id", referencedColumnName = "id"))
    private Set<UserPermissions> userPermissions = new HashSet<>();

    @ManyToMany(mappedBy = "userGroups")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<ExUser> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public UserGroup groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public DocumentHistory getHistory() {
        return history;
    }

    public UserGroup history(DocumentHistory documentHistory) {
        this.history = documentHistory;
        return this;
    }

    public void setHistory(DocumentHistory documentHistory) {
        this.history = documentHistory;
    }

    public Set<UserPermissions> getUserPermissions() {
        return userPermissions;
    }

    public UserGroup userPermissions(Set<UserPermissions> userPermissions) {
        this.userPermissions = userPermissions;
        return this;
    }

    public UserGroup addUserPermissions(UserPermissions userPermissions) {
        this.userPermissions.add(userPermissions);
        userPermissions.getUserGroups().add(this);
        return this;
    }

    public UserGroup removeUserPermissions(UserPermissions userPermissions) {
        this.userPermissions.remove(userPermissions);
        userPermissions.getUserGroups().remove(this);
        return this;
    }

    public void setUserPermissions(Set<UserPermissions> userPermissions) {
        this.userPermissions = userPermissions;
    }

    public Set<ExUser> getUsers() {
        return users;
    }

    public UserGroup users(Set<ExUser> exUsers) {
        this.users = exUsers;
        return this;
    }

    public UserGroup addUsers(ExUser exUser) {
        this.users.add(exUser);
        exUser.getUserGroups().add(this);
        return this;
    }

    public UserGroup removeUsers(ExUser exUser) {
        this.users.remove(exUser);
        exUser.getUserGroups().remove(this);
        return this;
    }

    public void setUsers(Set<ExUser> exUsers) {
        this.users = exUsers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGroup)) {
            return false;
        }
        return id != null && id.equals(((UserGroup) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UserGroup{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            "}";
    }
}
