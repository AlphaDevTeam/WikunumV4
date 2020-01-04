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
 * Criteria class for the {@link com.alphadevs.sales.domain.UnitOfMeasure} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.UnitOfMeasureResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /unit-of-measures?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UnitOfMeasureCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter unitOfMeasureCode;

    private StringFilter unitOfMeasureDescription;

    private BooleanFilter isActive;

    public UnitOfMeasureCriteria(){
    }

    public UnitOfMeasureCriteria(UnitOfMeasureCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.unitOfMeasureCode = other.unitOfMeasureCode == null ? null : other.unitOfMeasureCode.copy();
        this.unitOfMeasureDescription = other.unitOfMeasureDescription == null ? null : other.unitOfMeasureDescription.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
    }

    @Override
    public UnitOfMeasureCriteria copy() {
        return new UnitOfMeasureCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUnitOfMeasureCode() {
        return unitOfMeasureCode;
    }

    public void setUnitOfMeasureCode(StringFilter unitOfMeasureCode) {
        this.unitOfMeasureCode = unitOfMeasureCode;
    }

    public StringFilter getUnitOfMeasureDescription() {
        return unitOfMeasureDescription;
    }

    public void setUnitOfMeasureDescription(StringFilter unitOfMeasureDescription) {
        this.unitOfMeasureDescription = unitOfMeasureDescription;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UnitOfMeasureCriteria that = (UnitOfMeasureCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(unitOfMeasureCode, that.unitOfMeasureCode) &&
            Objects.equals(unitOfMeasureDescription, that.unitOfMeasureDescription) &&
            Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        unitOfMeasureCode,
        unitOfMeasureDescription,
        isActive
        );
    }

    @Override
    public String toString() {
        return "UnitOfMeasureCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (unitOfMeasureCode != null ? "unitOfMeasureCode=" + unitOfMeasureCode + ", " : "") +
                (unitOfMeasureDescription != null ? "unitOfMeasureDescription=" + unitOfMeasureDescription + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
            "}";
    }

}
