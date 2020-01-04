package com.alphadevs.sales.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.alphadevs.sales.domain.UserPermissions} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.UserPermissionsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-permissions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserPermissionsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter userPermKey;

    private StringFilter userPermDescription;

    private BooleanFilter isActive;

    private LongFilter menuItemsId;

    private LongFilter usersId;

    private LongFilter userGroupId;

    public UserPermissionsCriteria(){
    }

    public UserPermissionsCriteria(UserPermissionsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.userPermKey = other.userPermKey == null ? null : other.userPermKey.copy();
        this.userPermDescription = other.userPermDescription == null ? null : other.userPermDescription.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.menuItemsId = other.menuItemsId == null ? null : other.menuItemsId.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
        this.userGroupId = other.userGroupId == null ? null : other.userGroupId.copy();
    }

    @Override
    public UserPermissionsCriteria copy() {
        return new UserPermissionsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUserPermKey() {
        return userPermKey;
    }

    public void setUserPermKey(StringFilter userPermKey) {
        this.userPermKey = userPermKey;
    }

    public StringFilter getUserPermDescription() {
        return userPermDescription;
    }

    public void setUserPermDescription(StringFilter userPermDescription) {
        this.userPermDescription = userPermDescription;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getMenuItemsId() {
        return menuItemsId;
    }

    public void setMenuItemsId(LongFilter menuItemsId) {
        this.menuItemsId = menuItemsId;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }

    public LongFilter getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(LongFilter userGroupId) {
        this.userGroupId = userGroupId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserPermissionsCriteria that = (UserPermissionsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(userPermKey, that.userPermKey) &&
            Objects.equals(userPermDescription, that.userPermDescription) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(menuItemsId, that.menuItemsId) &&
            Objects.equals(usersId, that.usersId) &&
            Objects.equals(userGroupId, that.userGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        userPermKey,
        userPermDescription,
        isActive,
        menuItemsId,
        usersId,
        userGroupId
        );
    }

    @Override
    public String toString() {
        return "UserPermissionsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (userPermKey != null ? "userPermKey=" + userPermKey + ", " : "") +
                (userPermDescription != null ? "userPermDescription=" + userPermDescription + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (menuItemsId != null ? "menuItemsId=" + menuItemsId + ", " : "") +
                (usersId != null ? "usersId=" + usersId + ", " : "") +
                (userGroupId != null ? "userGroupId=" + userGroupId + ", " : "") +
            "}";
    }

}
