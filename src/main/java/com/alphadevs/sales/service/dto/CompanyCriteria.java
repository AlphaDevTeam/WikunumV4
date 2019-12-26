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
 * Criteria class for the {@link com.alphadevs.sales.domain.Company} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.CompanyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /companies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CompanyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter companyCode;

    private StringFilter companyName;

    private StringFilter companyAddress;

    private StringFilter companyRegNumber;

    private DoubleFilter rating;

    private StringFilter phone;

    private StringFilter addressLine1;

    private StringFilter addressLine2;

    private StringFilter city;

    private StringFilter country;

    private LongFilter historyId;

    public CompanyCriteria(){
    }

    public CompanyCriteria(CompanyCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.companyCode = other.companyCode == null ? null : other.companyCode.copy();
        this.companyName = other.companyName == null ? null : other.companyName.copy();
        this.companyAddress = other.companyAddress == null ? null : other.companyAddress.copy();
        this.companyRegNumber = other.companyRegNumber == null ? null : other.companyRegNumber.copy();
        this.rating = other.rating == null ? null : other.rating.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.addressLine1 = other.addressLine1 == null ? null : other.addressLine1.copy();
        this.addressLine2 = other.addressLine2 == null ? null : other.addressLine2.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.historyId = other.historyId == null ? null : other.historyId.copy();
    }

    @Override
    public CompanyCriteria copy() {
        return new CompanyCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(StringFilter companyCode) {
        this.companyCode = companyCode;
    }

    public StringFilter getCompanyName() {
        return companyName;
    }

    public void setCompanyName(StringFilter companyName) {
        this.companyName = companyName;
    }

    public StringFilter getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(StringFilter companyAddress) {
        this.companyAddress = companyAddress;
    }

    public StringFilter getCompanyRegNumber() {
        return companyRegNumber;
    }

    public void setCompanyRegNumber(StringFilter companyRegNumber) {
        this.companyRegNumber = companyRegNumber;
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

    public LongFilter getHistoryId() {
        return historyId;
    }

    public void setHistoryId(LongFilter historyId) {
        this.historyId = historyId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CompanyCriteria that = (CompanyCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(companyCode, that.companyCode) &&
            Objects.equals(companyName, that.companyName) &&
            Objects.equals(companyAddress, that.companyAddress) &&
            Objects.equals(companyRegNumber, that.companyRegNumber) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(addressLine1, that.addressLine1) &&
            Objects.equals(addressLine2, that.addressLine2) &&
            Objects.equals(city, that.city) &&
            Objects.equals(country, that.country) &&
            Objects.equals(historyId, that.historyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        companyCode,
        companyName,
        companyAddress,
        companyRegNumber,
        rating,
        phone,
        addressLine1,
        addressLine2,
        city,
        country,
        historyId
        );
    }

    @Override
    public String toString() {
        return "CompanyCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (companyCode != null ? "companyCode=" + companyCode + ", " : "") +
                (companyName != null ? "companyName=" + companyName + ", " : "") +
                (companyAddress != null ? "companyAddress=" + companyAddress + ", " : "") +
                (companyRegNumber != null ? "companyRegNumber=" + companyRegNumber + ", " : "") +
                (rating != null ? "rating=" + rating + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (addressLine1 != null ? "addressLine1=" + addressLine1 + ", " : "") +
                (addressLine2 != null ? "addressLine2=" + addressLine2 + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (historyId != null ? "historyId=" + historyId + ", " : "") +
            "}";
    }

}
