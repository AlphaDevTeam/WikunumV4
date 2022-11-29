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
 * Criteria class for the {@link com.alphadevs.sales.domain.PaymentTypes} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.PaymentTypesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payment-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PaymentTypesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter paymentTypesCode;

    private StringFilter paymentTypes;

    private DoubleFilter paymentTypesChargePer;

    private BooleanFilter isActive;

    private LongFilter locationId;

    public PaymentTypesCriteria(){
    }

    public PaymentTypesCriteria(PaymentTypesCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.paymentTypesCode = other.paymentTypesCode == null ? null : other.paymentTypesCode.copy();
        this.paymentTypes = other.paymentTypes == null ? null : other.paymentTypes.copy();
        this.paymentTypesChargePer = other.paymentTypesChargePer == null ? null : other.paymentTypesChargePer.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public PaymentTypesCriteria copy() {
        return new PaymentTypesCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPaymentTypesCode() {
        return paymentTypesCode;
    }

    public void setPaymentTypesCode(StringFilter paymentTypesCode) {
        this.paymentTypesCode = paymentTypesCode;
    }

    public StringFilter getPaymentTypes() {
        return paymentTypes;
    }

    public void setPaymentTypes(StringFilter paymentTypes) {
        this.paymentTypes = paymentTypes;
    }

    public DoubleFilter getPaymentTypesChargePer() {
        return paymentTypesChargePer;
    }

    public void setPaymentTypesChargePer(DoubleFilter paymentTypesChargePer) {
        this.paymentTypesChargePer = paymentTypesChargePer;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PaymentTypesCriteria that = (PaymentTypesCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(paymentTypesCode, that.paymentTypesCode) &&
            Objects.equals(paymentTypes, that.paymentTypes) &&
            Objects.equals(paymentTypesChargePer, that.paymentTypesChargePer) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        paymentTypesCode,
        paymentTypes,
        paymentTypesChargePer,
        isActive,
        locationId
        );
    }

    @Override
    public String toString() {
        return "PaymentTypesCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (paymentTypesCode != null ? "paymentTypesCode=" + paymentTypesCode + ", " : "") +
                (paymentTypes != null ? "paymentTypes=" + paymentTypes + ", " : "") +
                (paymentTypesChargePer != null ? "paymentTypesChargePer=" + paymentTypesChargePer + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
