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
 * Criteria class for the {@link com.alphadevs.sales.domain.MenuItems} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.MenuItemsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /menu-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MenuItemsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter menuName;

    private StringFilter menuURL;

    private BooleanFilter isActive;

    private LongFilter historyId;

    private LongFilter userPermissionId;

    public MenuItemsCriteria(){
    }

    public MenuItemsCriteria(MenuItemsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.menuName = other.menuName == null ? null : other.menuName.copy();
        this.menuURL = other.menuURL == null ? null : other.menuURL.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.historyId = other.historyId == null ? null : other.historyId.copy();
        this.userPermissionId = other.userPermissionId == null ? null : other.userPermissionId.copy();
    }

    @Override
    public MenuItemsCriteria copy() {
        return new MenuItemsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMenuName() {
        return menuName;
    }

    public void setMenuName(StringFilter menuName) {
        this.menuName = menuName;
    }

    public StringFilter getMenuURL() {
        return menuURL;
    }

    public void setMenuURL(StringFilter menuURL) {
        this.menuURL = menuURL;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getHistoryId() {
        return historyId;
    }

    public void setHistoryId(LongFilter historyId) {
        this.historyId = historyId;
    }

    public LongFilter getUserPermissionId() {
        return userPermissionId;
    }

    public void setUserPermissionId(LongFilter userPermissionId) {
        this.userPermissionId = userPermissionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MenuItemsCriteria that = (MenuItemsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(menuName, that.menuName) &&
            Objects.equals(menuURL, that.menuURL) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(historyId, that.historyId) &&
            Objects.equals(userPermissionId, that.userPermissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        menuName,
        menuURL,
        isActive,
        historyId,
        userPermissionId
        );
    }

    @Override
    public String toString() {
        return "MenuItemsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (menuName != null ? "menuName=" + menuName + ", " : "") +
                (menuURL != null ? "menuURL=" + menuURL + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (historyId != null ? "historyId=" + historyId + ", " : "") +
                (userPermissionId != null ? "userPermissionId=" + userPermissionId + ", " : "") +
            "}";
    }

}
