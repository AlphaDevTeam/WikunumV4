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
 * Criteria class for the {@link com.alphadevs.sales.domain.TransactionType} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.TransactionTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transaction-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TransactionTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter transactionTypeCode;

    private StringFilter transactionType;

    private BooleanFilter isActive;

    private LongFilter historyId;

    public TransactionTypeCriteria(){
    }

    public TransactionTypeCriteria(TransactionTypeCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.transactionTypeCode = other.transactionTypeCode == null ? null : other.transactionTypeCode.copy();
        this.transactionType = other.transactionType == null ? null : other.transactionType.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.historyId = other.historyId == null ? null : other.historyId.copy();
    }

    @Override
    public TransactionTypeCriteria copy() {
        return new TransactionTypeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTransactionTypeCode() {
        return transactionTypeCode;
    }

    public void setTransactionTypeCode(StringFilter transactionTypeCode) {
        this.transactionTypeCode = transactionTypeCode;
    }

    public StringFilter getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(StringFilter transactionType) {
        this.transactionType = transactionType;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TransactionTypeCriteria that = (TransactionTypeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(transactionTypeCode, that.transactionTypeCode) &&
            Objects.equals(transactionType, that.transactionType) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(historyId, that.historyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        transactionTypeCode,
        transactionType,
        isActive,
        historyId
        );
    }

    @Override
    public String toString() {
        return "TransactionTypeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (transactionTypeCode != null ? "transactionTypeCode=" + transactionTypeCode + ", " : "") +
                (transactionType != null ? "transactionType=" + transactionType + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (historyId != null ? "historyId=" + historyId + ", " : "") +
            "}";
    }

}
