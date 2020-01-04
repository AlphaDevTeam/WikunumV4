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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.alphadevs.sales.domain.StockTransfer} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.StockTransferResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /stock-transfers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StockTransferCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter transactionNumber;

    private LocalDateFilter transactionDate;

    private StringFilter transactionDescription;

    private DoubleFilter transactionQty;

    private LongFilter itemId;

    private LongFilter locationFromId;

    private LongFilter locationToId;

    public StockTransferCriteria(){
    }

    public StockTransferCriteria(StockTransferCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.transactionNumber = other.transactionNumber == null ? null : other.transactionNumber.copy();
        this.transactionDate = other.transactionDate == null ? null : other.transactionDate.copy();
        this.transactionDescription = other.transactionDescription == null ? null : other.transactionDescription.copy();
        this.transactionQty = other.transactionQty == null ? null : other.transactionQty.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
        this.locationFromId = other.locationFromId == null ? null : other.locationFromId.copy();
        this.locationToId = other.locationToId == null ? null : other.locationToId.copy();
    }

    @Override
    public StockTransferCriteria copy() {
        return new StockTransferCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(StringFilter transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public LocalDateFilter getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateFilter transactionDate) {
        this.transactionDate = transactionDate;
    }

    public StringFilter getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(StringFilter transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public DoubleFilter getTransactionQty() {
        return transactionQty;
    }

    public void setTransactionQty(DoubleFilter transactionQty) {
        this.transactionQty = transactionQty;
    }

    public LongFilter getItemId() {
        return itemId;
    }

    public void setItemId(LongFilter itemId) {
        this.itemId = itemId;
    }

    public LongFilter getLocationFromId() {
        return locationFromId;
    }

    public void setLocationFromId(LongFilter locationFromId) {
        this.locationFromId = locationFromId;
    }

    public LongFilter getLocationToId() {
        return locationToId;
    }

    public void setLocationToId(LongFilter locationToId) {
        this.locationToId = locationToId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StockTransferCriteria that = (StockTransferCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(transactionNumber, that.transactionNumber) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(transactionDescription, that.transactionDescription) &&
            Objects.equals(transactionQty, that.transactionQty) &&
            Objects.equals(itemId, that.itemId) &&
            Objects.equals(locationFromId, that.locationFromId) &&
            Objects.equals(locationToId, that.locationToId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        transactionNumber,
        transactionDate,
        transactionDescription,
        transactionQty,
        itemId,
        locationFromId,
        locationToId
        );
    }

    @Override
    public String toString() {
        return "StockTransferCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (transactionNumber != null ? "transactionNumber=" + transactionNumber + ", " : "") +
                (transactionDate != null ? "transactionDate=" + transactionDate + ", " : "") +
                (transactionDescription != null ? "transactionDescription=" + transactionDescription + ", " : "") +
                (transactionQty != null ? "transactionQty=" + transactionQty + ", " : "") +
                (itemId != null ? "itemId=" + itemId + ", " : "") +
                (locationFromId != null ? "locationFromId=" + locationFromId + ", " : "") +
                (locationToId != null ? "locationToId=" + locationToId + ", " : "") +
            "}";
    }

}
