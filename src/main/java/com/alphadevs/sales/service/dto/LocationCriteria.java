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

    private StringFilter email;

    private DoubleFilter rating;

    private StringFilter phone;

    private StringFilter addressLine1;

    private StringFilter addressLine2;

    private StringFilter city;

    private StringFilter country;

    private StringFilter vatNumber;

    private DoubleFilter vatPerc;

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
        this.email = other.email == null ? null : other.email.copy();
        this.rating = other.rating == null ? null : other.rating.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.addressLine1 = other.addressLine1 == null ? null : other.addressLine1.copy();
        this.addressLine2 = other.addressLine2 == null ? null : other.addressLine2.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.vatNumber = other.vatNumber == null ? null : other.vatNumber.copy();
        this.vatPerc = other.vatPerc == null ? null : other.vatPerc.copy();
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

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public DoubleFilter getRating() {
        return rating;
    }

    public void setRating(DoubleFilter rating) {
        this.rating = rating;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(StringFilter addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public StringFilter getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(StringFilter addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getCountry() {
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(StringFilter vatNumber) {
        this.vatNumber = vatNumber;
    }

    public DoubleFilter getVatPerc() {
        return vatPerc;
    }

    public void setVatPerc(DoubleFilter vatPerc) {
        this.vatPerc = vatPerc;
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
            Objects.equals(email, that.email) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(addressLine1, that.addressLine1) &&
            Objects.equals(addressLine2, that.addressLine2) &&
            Objects.equals(city, that.city) &&
            Objects.equals(country, that.country) &&
            Objects.equals(vatNumber, that.vatNumber) &&
            Objects.equals(vatPerc, that.vatPerc) &&
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
        email,
        rating,
        phone,
        addressLine1,
        addressLine2,
        city,
        country,
        vatNumber,
        vatPerc,
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
                (email != null ? "email=" + email + ", " : "") +
                (rating != null ? "rating=" + rating + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (addressLine1 != null ? "addressLine1=" + addressLine1 + ", " : "") +
                (addressLine2 != null ? "addressLine2=" + addressLine2 + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (vatNumber != null ? "vatNumber=" + vatNumber + ", " : "") +
                (vatPerc != null ? "vatPerc=" + vatPerc + ", " : "") +
                (companyId != null ? "companyId=" + companyId + ", " : "") +
                (configitemsId != null ? "configitemsId=" + configitemsId + ", " : "") +
                (usersId != null ? "usersId=" + usersId + ", " : "") +
            "}";
    }

}
