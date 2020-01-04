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
 * Criteria class for the {@link com.alphadevs.sales.domain.Quotation} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.QuotationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /quotations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuotationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter quotationNumber;

    private LocalDateFilter quotationDate;

    private LocalDateFilter quotationexpireDate;

    private BigDecimalFilter quotationTotalAmount;

    private StringFilter quotationTo;

    private StringFilter quotationFrom;

    private StringFilter projectNumber;

    private StringFilter quotationNote;

    private LongFilter detailsId;

    private LongFilter locationId;

    public QuotationCriteria(){
    }

    public QuotationCriteria(QuotationCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.quotationNumber = other.quotationNumber == null ? null : other.quotationNumber.copy();
        this.quotationDate = other.quotationDate == null ? null : other.quotationDate.copy();
        this.quotationexpireDate = other.quotationexpireDate == null ? null : other.quotationexpireDate.copy();
        this.quotationTotalAmount = other.quotationTotalAmount == null ? null : other.quotationTotalAmount.copy();
        this.quotationTo = other.quotationTo == null ? null : other.quotationTo.copy();
        this.quotationFrom = other.quotationFrom == null ? null : other.quotationFrom.copy();
        this.projectNumber = other.projectNumber == null ? null : other.projectNumber.copy();
        this.quotationNote = other.quotationNote == null ? null : other.quotationNote.copy();
        this.detailsId = other.detailsId == null ? null : other.detailsId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public QuotationCriteria copy() {
        return new QuotationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getQuotationNumber() {
        return quotationNumber;
    }

    public void setQuotationNumber(StringFilter quotationNumber) {
        this.quotationNumber = quotationNumber;
    }

    public LocalDateFilter getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(LocalDateFilter quotationDate) {
        this.quotationDate = quotationDate;
    }

    public LocalDateFilter getQuotationexpireDate() {
        return quotationexpireDate;
    }

    public void setQuotationexpireDate(LocalDateFilter quotationexpireDate) {
        this.quotationexpireDate = quotationexpireDate;
    }

    public BigDecimalFilter getQuotationTotalAmount() {
        return quotationTotalAmount;
    }

    public void setQuotationTotalAmount(BigDecimalFilter quotationTotalAmount) {
        this.quotationTotalAmount = quotationTotalAmount;
    }

    public StringFilter getQuotationTo() {
        return quotationTo;
    }

    public void setQuotationTo(StringFilter quotationTo) {
        this.quotationTo = quotationTo;
    }

    public StringFilter getQuotationFrom() {
        return quotationFrom;
    }

    public void setQuotationFrom(StringFilter quotationFrom) {
        this.quotationFrom = quotationFrom;
    }

    public StringFilter getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(StringFilter projectNumber) {
        this.projectNumber = projectNumber;
    }

    public StringFilter getQuotationNote() {
        return quotationNote;
    }

    public void setQuotationNote(StringFilter quotationNote) {
        this.quotationNote = quotationNote;
    }

    public LongFilter getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(LongFilter detailsId) {
        this.detailsId = detailsId;
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
        final QuotationCriteria that = (QuotationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(quotationNumber, that.quotationNumber) &&
            Objects.equals(quotationDate, that.quotationDate) &&
            Objects.equals(quotationexpireDate, that.quotationexpireDate) &&
            Objects.equals(quotationTotalAmount, that.quotationTotalAmount) &&
            Objects.equals(quotationTo, that.quotationTo) &&
            Objects.equals(quotationFrom, that.quotationFrom) &&
            Objects.equals(projectNumber, that.projectNumber) &&
            Objects.equals(quotationNote, that.quotationNote) &&
            Objects.equals(detailsId, that.detailsId) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        quotationNumber,
        quotationDate,
        quotationexpireDate,
        quotationTotalAmount,
        quotationTo,
        quotationFrom,
        projectNumber,
        quotationNote,
        detailsId,
        locationId
        );
    }

    @Override
    public String toString() {
        return "QuotationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (quotationNumber != null ? "quotationNumber=" + quotationNumber + ", " : "") +
                (quotationDate != null ? "quotationDate=" + quotationDate + ", " : "") +
                (quotationexpireDate != null ? "quotationexpireDate=" + quotationexpireDate + ", " : "") +
                (quotationTotalAmount != null ? "quotationTotalAmount=" + quotationTotalAmount + ", " : "") +
                (quotationTo != null ? "quotationTo=" + quotationTo + ", " : "") +
                (quotationFrom != null ? "quotationFrom=" + quotationFrom + ", " : "") +
                (projectNumber != null ? "projectNumber=" + projectNumber + ", " : "") +
                (quotationNote != null ? "quotationNote=" + quotationNote + ", " : "") +
                (detailsId != null ? "detailsId=" + detailsId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
