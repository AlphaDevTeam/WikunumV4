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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.alphadevs.sales.domain.Invoice} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.InvoiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /invoices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InvoiceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter invNumber;

    private LocalDateFilter invDate;

    private BigDecimalFilter invTotalAmount;

    private BigDecimalFilter cashAmount;

    private BigDecimalFilter cardAmount;

    private BigDecimalFilter dueAmount;

    private LongFilter detailsId;

    private LongFilter customerId;

    private LongFilter transactionTypeId;

    private LongFilter locationId;

    public InvoiceCriteria(){
    }

    public InvoiceCriteria(InvoiceCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.invNumber = other.invNumber == null ? null : other.invNumber.copy();
        this.invDate = other.invDate == null ? null : other.invDate.copy();
        this.invTotalAmount = other.invTotalAmount == null ? null : other.invTotalAmount.copy();
        this.cashAmount = other.cashAmount == null ? null : other.cashAmount.copy();
        this.cardAmount = other.cardAmount == null ? null : other.cardAmount.copy();
        this.dueAmount = other.dueAmount == null ? null : other.dueAmount.copy();
        this.detailsId = other.detailsId == null ? null : other.detailsId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.transactionTypeId = other.transactionTypeId == null ? null : other.transactionTypeId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public InvoiceCriteria copy() {
        return new InvoiceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getInvNumber() {
        return invNumber;
    }

    public void setInvNumber(StringFilter invNumber) {
        this.invNumber = invNumber;
    }

    public LocalDateFilter getInvDate() {
        return invDate;
    }

    public void setInvDate(LocalDateFilter invDate) {
        this.invDate = invDate;
    }

    public BigDecimalFilter getInvTotalAmount() {
        return invTotalAmount;
    }

    public void setInvTotalAmount(BigDecimalFilter invTotalAmount) {
        this.invTotalAmount = invTotalAmount;
    }

    public BigDecimalFilter getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(BigDecimalFilter cashAmount) {
        this.cashAmount = cashAmount;
    }

    public BigDecimalFilter getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(BigDecimalFilter cardAmount) {
        this.cardAmount = cardAmount;
    }

    public BigDecimalFilter getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimalFilter dueAmount) {
        this.dueAmount = dueAmount;
    }

    public LongFilter getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(LongFilter detailsId) {
        this.detailsId = detailsId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(LongFilter transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
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
        final InvoiceCriteria that = (InvoiceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(invNumber, that.invNumber) &&
            Objects.equals(invDate, that.invDate) &&
            Objects.equals(invTotalAmount, that.invTotalAmount) &&
            Objects.equals(cashAmount, that.cashAmount) &&
            Objects.equals(cardAmount, that.cardAmount) &&
            Objects.equals(dueAmount, that.dueAmount) &&
            Objects.equals(detailsId, that.detailsId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(transactionTypeId, that.transactionTypeId) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        invNumber,
        invDate,
        invTotalAmount,
        cashAmount,
        cardAmount,
        dueAmount,
        detailsId,
        customerId,
        transactionTypeId,
        locationId
        );
    }

    @Override
    public String toString() {
        return "InvoiceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (invNumber != null ? "invNumber=" + invNumber + ", " : "") +
                (invDate != null ? "invDate=" + invDate + ", " : "") +
                (invTotalAmount != null ? "invTotalAmount=" + invTotalAmount + ", " : "") +
                (cashAmount != null ? "cashAmount=" + cashAmount + ", " : "") +
                (cardAmount != null ? "cardAmount=" + cardAmount + ", " : "") +
                (dueAmount != null ? "dueAmount=" + dueAmount + ", " : "") +
                (detailsId != null ? "detailsId=" + detailsId + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
                (transactionTypeId != null ? "transactionTypeId=" + transactionTypeId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
