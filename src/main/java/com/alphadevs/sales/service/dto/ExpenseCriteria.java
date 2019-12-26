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

/**
 * Criteria class for the {@link com.alphadevs.sales.domain.Expense} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.ExpenseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /expenses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExpenseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter expenseCode;

    private StringFilter expenseName;

    private BigDecimalFilter expenseLimit;

    private BooleanFilter isActive;

    private LongFilter locationId;

    public ExpenseCriteria(){
    }

    public ExpenseCriteria(ExpenseCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.expenseCode = other.expenseCode == null ? null : other.expenseCode.copy();
        this.expenseName = other.expenseName == null ? null : other.expenseName.copy();
        this.expenseLimit = other.expenseLimit == null ? null : other.expenseLimit.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public ExpenseCriteria copy() {
        return new ExpenseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getExpenseCode() {
        return expenseCode;
    }

    public void setExpenseCode(StringFilter expenseCode) {
        this.expenseCode = expenseCode;
    }

    public StringFilter getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(StringFilter expenseName) {
        this.expenseName = expenseName;
    }

    public BigDecimalFilter getExpenseLimit() {
        return expenseLimit;
    }

    public void setExpenseLimit(BigDecimalFilter expenseLimit) {
        this.expenseLimit = expenseLimit;
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
        final ExpenseCriteria that = (ExpenseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(expenseCode, that.expenseCode) &&
            Objects.equals(expenseName, that.expenseName) &&
            Objects.equals(expenseLimit, that.expenseLimit) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        expenseCode,
        expenseName,
        expenseLimit,
        isActive,
        locationId
        );
    }

    @Override
    public String toString() {
        return "ExpenseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (expenseCode != null ? "expenseCode=" + expenseCode + ", " : "") +
                (expenseName != null ? "expenseName=" + expenseName + ", " : "") +
                (expenseLimit != null ? "expenseLimit=" + expenseLimit + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
