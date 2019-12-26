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
 * Criteria class for the {@link com.alphadevs.sales.domain.Currency} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.CurrencyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /currencies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CurrencyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter currencyCode;

    private StringFilter currencyName;

    private LongFilter historyId;

    public CurrencyCriteria(){
    }

    public CurrencyCriteria(CurrencyCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.currencyCode = other.currencyCode == null ? null : other.currencyCode.copy();
        this.currencyName = other.currencyName == null ? null : other.currencyName.copy();
        this.historyId = other.historyId == null ? null : other.historyId.copy();
    }

    @Override
    public CurrencyCriteria copy() {
        return new CurrencyCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(StringFilter currencyCode) {
        this.currencyCode = currencyCode;
    }

    public StringFilter getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(StringFilter currencyName) {
        this.currencyName = currencyName;
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
        final CurrencyCriteria that = (CurrencyCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(currencyCode, that.currencyCode) &&
            Objects.equals(currencyName, that.currencyName) &&
            Objects.equals(historyId, that.historyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        currencyCode,
        currencyName,
        historyId
        );
    }

    @Override
    public String toString() {
        return "CurrencyCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (currencyCode != null ? "currencyCode=" + currencyCode + ", " : "") +
                (currencyName != null ? "currencyName=" + currencyName + ", " : "") +
                (historyId != null ? "historyId=" + historyId + ", " : "") +
            "}";
    }

}
