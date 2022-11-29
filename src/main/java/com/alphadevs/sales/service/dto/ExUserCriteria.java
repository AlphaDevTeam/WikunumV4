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
import io.github.jhipster.service.filter.BigDecimalFilter;

/**
 * Criteria class for the {@link com.alphadevs.sales.domain.ExUser} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.ExUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ex-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter userKey;

    private StringFilter login;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private BooleanFilter isActive;

    private StringFilter phone;

    private StringFilter addressLine1;

    private StringFilter addressLine2;

    private StringFilter city;

    private StringFilter country;

    private BigDecimalFilter userLimit;

    private DoubleFilter creditScore;

    private LongFilter relatedUserId;

    private LongFilter companyId;

    private LongFilter locationsId;

    private LongFilter userGroupsId;

    private LongFilter userPermissionsId;

    public ExUserCriteria(){
    }

    public ExUserCriteria(ExUserCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.userKey = other.userKey == null ? null : other.userKey.copy();
        this.login = other.login == null ? null : other.login.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.addressLine1 = other.addressLine1 == null ? null : other.addressLine1.copy();
        this.addressLine2 = other.addressLine2 == null ? null : other.addressLine2.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.userLimit = other.userLimit == null ? null : other.userLimit.copy();
        this.creditScore = other.creditScore == null ? null : other.creditScore.copy();
        this.relatedUserId = other.relatedUserId == null ? null : other.relatedUserId.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.locationsId = other.locationsId == null ? null : other.locationsId.copy();
        this.userGroupsId = other.userGroupsId == null ? null : other.userGroupsId.copy();
        this.userPermissionsId = other.userPermissionsId == null ? null : other.userPermissionsId.copy();
    }

    @Override
    public ExUserCriteria copy() {
        return new ExUserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUserKey() {
        return userKey;
    }

    public void setUserKey(StringFilter userKey) {
        this.userKey = userKey;
    }

    public StringFilter getLogin() {
        return login;
    }

    public void setLogin(StringFilter login) {
        this.login = login;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
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

    public BigDecimalFilter getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(BigDecimalFilter userLimit) {
        this.userLimit = userLimit;
    }

    public DoubleFilter getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(DoubleFilter creditScore) {
        this.creditScore = creditScore;
    }

    public LongFilter getRelatedUserId() {
        return relatedUserId;
    }

    public void setRelatedUserId(LongFilter relatedUserId) {
        this.relatedUserId = relatedUserId;
    }

    public LongFilter getCompanyId() {
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
    }

    public LongFilter getLocationsId() {
        return locationsId;
    }

    public void setLocationsId(LongFilter locationsId) {
        this.locationsId = locationsId;
    }

    public LongFilter getUserGroupsId() {
        return userGroupsId;
    }

    public void setUserGroupsId(LongFilter userGroupsId) {
        this.userGroupsId = userGroupsId;
    }

    public LongFilter getUserPermissionsId() {
        return userPermissionsId;
    }

    public void setUserPermissionsId(LongFilter userPermissionsId) {
        this.userPermissionsId = userPermissionsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExUserCriteria that = (ExUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(userKey, that.userKey) &&
            Objects.equals(login, that.login) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(addressLine1, that.addressLine1) &&
            Objects.equals(addressLine2, that.addressLine2) &&
            Objects.equals(city, that.city) &&
            Objects.equals(country, that.country) &&
            Objects.equals(userLimit, that.userLimit) &&
            Objects.equals(creditScore, that.creditScore) &&
            Objects.equals(relatedUserId, that.relatedUserId) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(locationsId, that.locationsId) &&
            Objects.equals(userGroupsId, that.userGroupsId) &&
            Objects.equals(userPermissionsId, that.userPermissionsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        userKey,
        login,
        firstName,
        lastName,
        email,
        isActive,
        phone,
        addressLine1,
        addressLine2,
        city,
        country,
        userLimit,
        creditScore,
        relatedUserId,
        companyId,
        locationsId,
        userGroupsId,
        userPermissionsId
        );
    }

    @Override
    public String toString() {
        return "ExUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (userKey != null ? "userKey=" + userKey + ", " : "") +
                (login != null ? "login=" + login + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (addressLine1 != null ? "addressLine1=" + addressLine1 + ", " : "") +
                (addressLine2 != null ? "addressLine2=" + addressLine2 + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (userLimit != null ? "userLimit=" + userLimit + ", " : "") +
                (creditScore != null ? "creditScore=" + creditScore + ", " : "") +
                (relatedUserId != null ? "relatedUserId=" + relatedUserId + ", " : "") +
                (companyId != null ? "companyId=" + companyId + ", " : "") +
                (locationsId != null ? "locationsId=" + locationsId + ", " : "") +
                (userGroupsId != null ? "userGroupsId=" + userGroupsId + ", " : "") +
                (userPermissionsId != null ? "userPermissionsId=" + userPermissionsId + ", " : "") +
            "}";
    }

}
