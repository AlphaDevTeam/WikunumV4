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
 * Criteria class for the {@link com.alphadevs.sales.domain.QuotationDetails} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.QuotationDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /quotation-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuotationDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter rate;

    private StringFilter description;

    private LongFilter itemId;

    private LongFilter quoteId;

    public QuotationDetailsCriteria(){
    }

    public QuotationDetailsCriteria(QuotationDetailsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.rate = other.rate == null ? null : other.rate.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
        this.quoteId = other.quoteId == null ? null : other.quoteId.copy();
    }

    @Override
    public QuotationDetailsCriteria copy() {
        return new QuotationDetailsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getRate() {
        return rate;
    }

    public void setRate(DoubleFilter rate) {
        this.rate = rate;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getItemId() {
        return itemId;
    }

    public void setItemId(LongFilter itemId) {
        this.itemId = itemId;
    }

    public LongFilter getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(LongFilter quoteId) {
        this.quoteId = quoteId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final QuotationDetailsCriteria that = (QuotationDetailsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(rate, that.rate) &&
            Objects.equals(description, that.description) &&
            Objects.equals(itemId, that.itemId) &&
            Objects.equals(quoteId, that.quoteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        rate,
        description,
        itemId,
        quoteId
        );
    }

    @Override
    public String toString() {
        return "QuotationDetailsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (rate != null ? "rate=" + rate + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (itemId != null ? "itemId=" + itemId + ", " : "") +
                (quoteId != null ? "quoteId=" + quoteId + ", " : "") +
            "}";
    }

}
