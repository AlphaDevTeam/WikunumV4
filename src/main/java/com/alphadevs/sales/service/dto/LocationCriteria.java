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
 * Criteria class for the {@link com.alphadevs.sales.domain.Location} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.LocationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /locations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LocationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter locationCode;

    private StringFilter locationName;

    private BooleanFilter isActive;

    private LongFilter historyId;

    private LongFilter companyId;

    private LongFilter configitemsId;

    private LongFilter usersId;

    public LocationCriteria(){
    }

    public LocationCriteria(LocationCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.locationCode = other.locationCode == null ? null : other.locationCode.copy();
        this.locationName = other.locationName == null ? null : other.locationName.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.historyId = other.historyId == null ? null : other.historyId.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.configitemsId = other.configitemsId == null ? null : other.configitemsId.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
    }

    @Override
    public LocationCriteria copy() {
        return new LocationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(StringFilter locationCode) {
        this.locationCode = locationCode;
    }

    public StringFilter getLocationName() {
        return locationName;
    }

    public void setLocationName(StringFilter locationName) {
        this.locationName = locationName;
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

    public LongFilter getCompanyId() {
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
    }

    public LongFilter getConfigitemsId() {
        return configitemsId;
    }

    public void setConfigitemsId(LongFilter configitemsId) {
        this.configitemsId = configitemsId;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LocationCriteria that = (LocationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(locationCode, that.locationCode) &&
            Objects.equals(locationName, that.locationName) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(historyId, that.historyId) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(configitemsId, that.configitemsId) &&
            Objects.equals(usersId, that.usersId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        locationCode,
        locationName,
        isActive,
        historyId,
        companyId,
        configitemsId,
        usersId
        );
    }

    @Override
    public String toString() {
        return "LocationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (locationCode != null ? "locationCode=" + locationCode + ", " : "") +
                (locationName != null ? "locationName=" + locationName + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (historyId != null ? "historyId=" + historyId + ", " : "") +
                (companyId != null ? "companyId=" + companyId + ", " : "") +
                (configitemsId != null ? "configitemsId=" + configitemsId + ", " : "") +
                (usersId != null ? "usersId=" + usersId + ", " : "") +
            "}";
    }

}
