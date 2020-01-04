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
 * Criteria class for the {@link com.alphadevs.sales.domain.LicenseType} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.LicenseTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /license-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LicenseTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter licenseTypeCode;

    private StringFilter licenseTypeName;

    private DoubleFilter validityDays;

    private BooleanFilter isActive;

    public LicenseTypeCriteria(){
    }

    public LicenseTypeCriteria(LicenseTypeCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.licenseTypeCode = other.licenseTypeCode == null ? null : other.licenseTypeCode.copy();
        this.licenseTypeName = other.licenseTypeName == null ? null : other.licenseTypeName.copy();
        this.validityDays = other.validityDays == null ? null : other.validityDays.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
    }

    @Override
    public LicenseTypeCriteria copy() {
        return new LicenseTypeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLicenseTypeCode() {
        return licenseTypeCode;
    }

    public void setLicenseTypeCode(StringFilter licenseTypeCode) {
        this.licenseTypeCode = licenseTypeCode;
    }

    public StringFilter getLicenseTypeName() {
        return licenseTypeName;
    }

    public void setLicenseTypeName(StringFilter licenseTypeName) {
        this.licenseTypeName = licenseTypeName;
    }

    public DoubleFilter getValidityDays() {
        return validityDays;
    }

    public void setValidityDays(DoubleFilter validityDays) {
        this.validityDays = validityDays;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LicenseTypeCriteria that = (LicenseTypeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(licenseTypeCode, that.licenseTypeCode) &&
            Objects.equals(licenseTypeName, that.licenseTypeName) &&
            Objects.equals(validityDays, that.validityDays) &&
            Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        licenseTypeCode,
        licenseTypeName,
        validityDays,
        isActive
        );
    }

    @Override
    public String toString() {
        return "LicenseTypeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (licenseTypeCode != null ? "licenseTypeCode=" + licenseTypeCode + ", " : "") +
                (licenseTypeName != null ? "licenseTypeName=" + licenseTypeName + ", " : "") +
                (validityDays != null ? "validityDays=" + validityDays + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
            "}";
    }

}
