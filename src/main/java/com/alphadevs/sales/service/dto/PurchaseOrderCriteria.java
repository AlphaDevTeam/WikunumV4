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
 * Criteria class for the {@link com.alphadevs.sales.domain.PurchaseOrder} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.PurchaseOrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /purchase-orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PurchaseOrderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter poNumber;

    private LocalDateFilter poDate;

    private BigDecimalFilter poAmount;

    private LongFilter historyId;

    private LongFilter detailsId;

    private LongFilter supplierId;

    private LongFilter locationId;

    private LongFilter transactionTypeId;

    private LongFilter relatedGRNId;

    public PurchaseOrderCriteria(){
    }

    public PurchaseOrderCriteria(PurchaseOrderCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.poNumber = other.poNumber == null ? null : other.poNumber.copy();
        this.poDate = other.poDate == null ? null : other.poDate.copy();
        this.poAmount = other.poAmount == null ? null : other.poAmount.copy();
        this.historyId = other.historyId == null ? null : other.historyId.copy();
        this.detailsId = other.detailsId == null ? null : other.detailsId.copy();
        this.supplierId = other.supplierId == null ? null : other.supplierId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.transactionTypeId = other.transactionTypeId == null ? null : other.transactionTypeId.copy();
        this.relatedGRNId = other.relatedGRNId == null ? null : other.relatedGRNId.copy();
    }

    @Override
    public PurchaseOrderCriteria copy() {
        return new PurchaseOrderCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(StringFilter poNumber) {
        this.poNumber = poNumber;
    }

    public LocalDateFilter getPoDate() {
        return poDate;
    }

    public void setPoDate(LocalDateFilter poDate) {
        this.poDate = poDate;
    }

    public BigDecimalFilter getPoAmount() {
        return poAmount;
    }

    public void setPoAmount(BigDecimalFilter poAmount) {
        this.poAmount = poAmount;
    }

    public LongFilter getHistoryId() {
        return historyId;
    }

    public void setHistoryId(LongFilter historyId) {
        this.historyId = historyId;
    }

    public LongFilter getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(LongFilter detailsId) {
        this.detailsId = detailsId;
    }

    public LongFilter getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(LongFilter supplierId) {
        this.supplierId = supplierId;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public LongFilter getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(LongFilter transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public LongFilter getRelatedGRNId() {
        return relatedGRNId;
    }

    public void setRelatedGRNId(LongFilter relatedGRNId) {
        this.relatedGRNId = relatedGRNId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PurchaseOrderCriteria that = (PurchaseOrderCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(poNumber, that.poNumber) &&
            Objects.equals(poDate, that.poDate) &&
            Objects.equals(poAmount, that.poAmount) &&
            Objects.equals(historyId, that.historyId) &&
            Objects.equals(detailsId, that.detailsId) &&
            Objects.equals(supplierId, that.supplierId) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(transactionTypeId, that.transactionTypeId) &&
            Objects.equals(relatedGRNId, that.relatedGRNId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        poNumber,
        poDate,
        poAmount,
        historyId,
        detailsId,
        supplierId,
        locationId,
        transactionTypeId,
        relatedGRNId
        );
    }

    @Override
    public String toString() {
        return "PurchaseOrderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (poNumber != null ? "poNumber=" + poNumber + ", " : "") +
                (poDate != null ? "poDate=" + poDate + ", " : "") +
                (poAmount != null ? "poAmount=" + poAmount + ", " : "") +
                (historyId != null ? "historyId=" + historyId + ", " : "") +
                (detailsId != null ? "detailsId=" + detailsId + ", " : "") +
                (supplierId != null ? "supplierId=" + supplierId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (transactionTypeId != null ? "transactionTypeId=" + transactionTypeId + ", " : "") +
                (relatedGRNId != null ? "relatedGRNId=" + relatedGRNId + ", " : "") +
            "}";
    }

}
