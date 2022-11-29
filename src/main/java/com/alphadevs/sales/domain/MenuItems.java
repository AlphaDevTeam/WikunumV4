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
 * MenuItems Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "MenuItems Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "menu_items")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
public class MenuItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "menu_name", nullable = false)
    private String menuName;

    @NotNull
    @Column(name = "menu_url", nullable = false)
    private String menuURL;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToOne
    @JoinColumn(unique = true)
    @NotAudited
    private DocumentHistory history;

    @ManyToMany(mappedBy = "menuItems")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<UserPermissions> userPermissions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public MenuItems menuName(String menuName) {
        this.menuName = menuName;
        return this;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuURL() {
        return menuURL;
    }

    public MenuItems menuURL(String menuURL) {
        this.menuURL = menuURL;
        return this;
    }

    public void setMenuURL(String menuURL) {
        this.menuURL = menuURL;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public MenuItems isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public DocumentHistory getHistory() {
        return history;
    }

    public MenuItems history(DocumentHistory documentHistory) {
        this.history = documentHistory;
        return this;
    }

    public void setHistory(DocumentHistory documentHistory) {
        this.history = documentHistory;
    }

    public Set<UserPermissions> getUserPermissions() {
        return userPermissions;
    }

    public MenuItems userPermissions(Set<UserPermissions> userPermissions) {
        this.userPermissions = userPermissions;
        return this;
    }

    public MenuItems addUserPermission(UserPermissions userPermissions) {
        this.userPermissions.add(userPermissions);
        userPermissions.getMenuItems().add(this);
        return this;
    }

    public MenuItems removeUserPermission(UserPermissions userPermissions) {
        this.userPermissions.remove(userPermissions);
        userPermissions.getMenuItems().remove(this);
        return this;
    }

    public void setUserPermissions(Set<UserPermissions> userPermissions) {
        this.userPermissions = userPermissions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuItems)) {
            return false;
        }
        return id != null && id.equals(((MenuItems) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MenuItems{" +
            "id=" + getId() +
            ", menuName='" + getMenuName() + "'" +
            ", menuURL='" + getMenuURL() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
