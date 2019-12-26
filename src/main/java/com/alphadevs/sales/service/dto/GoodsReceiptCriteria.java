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
 * Criteria class for the {@link com.alphadevs.sales.domain.GoodsReceipt} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.GoodsReceiptResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /goods-receipts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GoodsReceiptCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter grnNumber;

    private LocalDateFilter grnDate;

    private StringFilter poNumber;

    private BigDecimalFilter grnAmount;

    private LongFilter historyId;

    private LongFilter detailsId;

    private LongFilter linkedPOsId;

    private LongFilter supplierId;

    private LongFilter locationId;

    private LongFilter transactionTypeId;

    public GoodsReceiptCriteria(){
    }

    public GoodsReceiptCriteria(GoodsReceiptCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.grnNumber = other.grnNumber == null ? null : other.grnNumber.copy();
        this.grnDate = other.grnDate == null ? null : other.grnDate.copy();
        this.poNumber = other.poNumber == null ? null : other.poNumber.copy();
        this.grnAmount = other.grnAmount == null ? null : other.grnAmount.copy();
        this.historyId = other.historyId == null ? null : other.historyId.copy();
        this.detailsId = other.detailsId == null ? null : other.detailsId.copy();
        this.linkedPOsId = other.linkedPOsId == null ? null : other.linkedPOsId.copy();
        this.supplierId = other.supplierId == null ? null : other.supplierId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.transactionTypeId = other.transactionTypeId == null ? null : other.transactionTypeId.copy();
    }

    @Override
    public GoodsReceiptCriteria copy() {
        return new GoodsReceiptCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getGrnNumber() {
        return grnNumber;
    }

    public void setGrnNumber(StringFilter grnNumber) {
        this.grnNumber = grnNumber;
    }

    public LocalDateFilter getGrnDate() {
        return grnDate;
    }

    public void setGrnDate(LocalDateFilter grnDate) {
        this.grnDate = grnDate;
    }

    public StringFilter getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(StringFilter poNumber) {
        this.poNumber = poNumber;
    }

    public BigDecimalFilter getGrnAmount() {
        return grnAmount;
    }

    public void setGrnAmount(BigDecimalFilter grnAmount) {
        this.grnAmount = grnAmount;
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

    public LongFilter getLinkedPOsId() {
        return linkedPOsId;
    }

    public void setLinkedPOsId(LongFilter linkedPOsId) {
        this.linkedPOsId = linkedPOsId;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GoodsReceiptCriteria that = (GoodsReceiptCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(grnNumber, that.grnNumber) &&
            Objects.equals(grnDate, that.grnDate) &&
            Objects.equals(poNumber, that.poNumber) &&
            Objects.equals(grnAmount, that.grnAmount) &&
            Objects.equals(historyId, that.historyId) &&
            Objects.equals(detailsId, that.detailsId) &&
            Objects.equals(linkedPOsId, that.linkedPOsId) &&
            Objects.equals(supplierId, that.supplierId) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(transactionTypeId, that.transactionTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        grnNumber,
        grnDate,
        poNumber,
        grnAmount,
        historyId,
        detailsId,
        linkedPOsId,
        supplierId,
        locationId,
        transactionTypeId
        );
    }

    @Override
    public String toString() {
        return "GoodsReceiptCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (grnNumber != null ? "grnNumber=" + grnNumber + ", " : "") +
                (grnDate != null ? "grnDate=" + grnDate + ", " : "") +
                (poNumber != null ? "poNumber=" + poNumber + ", " : "") +
                (grnAmount != null ? "grnAmount=" + grnAmount + ", " : "") +
                (historyId != null ? "historyId=" + historyId + ", " : "") +
                (detailsId != null ? "detailsId=" + detailsId + ", " : "") +
                (linkedPOsId != null ? "linkedPOsId=" + linkedPOsId + ", " : "") +
                (supplierId != null ? "supplierId=" + supplierId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (transactionTypeId != null ? "transactionTypeId=" + transactionTypeId + ", " : "") +
            "}";
    }

}
