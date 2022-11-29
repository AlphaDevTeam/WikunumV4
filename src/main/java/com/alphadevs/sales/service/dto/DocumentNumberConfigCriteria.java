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
 * Criteria class for the {@link com.alphadevs.sales.domain.DocumentNumberConfig} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.DocumentNumberConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-number-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DocumentNumberConfigCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter documentPrefix;

    private StringFilter documentPostfix;

    private DoubleFilter currentNumber;

    private BooleanFilter isActive;

    private LongFilter documentId;

    private LongFilter locationId;

    private LongFilter transactionTypeId;

    public DocumentNumberConfigCriteria(){
    }

    public DocumentNumberConfigCriteria(DocumentNumberConfigCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.documentPrefix = other.documentPrefix == null ? null : other.documentPrefix.copy();
        this.documentPostfix = other.documentPostfix == null ? null : other.documentPostfix.copy();
        this.currentNumber = other.currentNumber == null ? null : other.currentNumber.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.documentId = other.documentId == null ? null : other.documentId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.transactionTypeId = other.transactionTypeId == null ? null : other.transactionTypeId.copy();
    }

    @Override
    public DocumentNumberConfigCriteria copy() {
        return new DocumentNumberConfigCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDocumentPrefix() {
        return documentPrefix;
    }

    public void setDocumentPrefix(StringFilter documentPrefix) {
        this.documentPrefix = documentPrefix;
    }

    public StringFilter getDocumentPostfix() {
        return documentPostfix;
    }

    public void setDocumentPostfix(StringFilter documentPostfix) {
        this.documentPostfix = documentPostfix;
    }

    public DoubleFilter getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(DoubleFilter currentNumber) {
        this.currentNumber = currentNumber;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getDocumentId() {
        return documentId;
    }

    public void setDocumentId(LongFilter documentId) {
        this.documentId = documentId;
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
        final DocumentNumberConfigCriteria that = (DocumentNumberConfigCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(documentPrefix, that.documentPrefix) &&
            Objects.equals(documentPostfix, that.documentPostfix) &&
            Objects.equals(currentNumber, that.currentNumber) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(transactionTypeId, that.transactionTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        documentPrefix,
        documentPostfix,
        currentNumber,
        isActive,
        documentId,
        locationId,
        transactionTypeId
        );
    }

    @Override
    public String toString() {
        return "DocumentNumberConfigCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (documentPrefix != null ? "documentPrefix=" + documentPrefix + ", " : "") +
                (documentPostfix != null ? "documentPostfix=" + documentPostfix + ", " : "") +
                (currentNumber != null ? "currentNumber=" + currentNumber + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (documentId != null ? "documentId=" + documentId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (transactionTypeId != null ? "transactionTypeId=" + transactionTypeId + ", " : "") +
            "}";
    }

}
