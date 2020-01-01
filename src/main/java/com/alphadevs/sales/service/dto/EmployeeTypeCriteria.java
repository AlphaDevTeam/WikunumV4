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
 * Criteria class for the {@link com.alphadevs.sales.domain.EmployeeType} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.EmployeeTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employee-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmployeeTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter employeeTypeCode;

    private StringFilter employeeTypeName;

    private BooleanFilter isActive;

    public EmployeeTypeCriteria(){
    }

    public EmployeeTypeCriteria(EmployeeTypeCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.employeeTypeCode = other.employeeTypeCode == null ? null : other.employeeTypeCode.copy();
        this.employeeTypeName = other.employeeTypeName == null ? null : other.employeeTypeName.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
    }

    @Override
    public EmployeeTypeCriteria copy() {
        return new EmployeeTypeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEmployeeTypeCode() {
        return employeeTypeCode;
    }

    public void setEmployeeTypeCode(StringFilter employeeTypeCode) {
        this.employeeTypeCode = employeeTypeCode;
    }

    public StringFilter getEmployeeTypeName() {
        return employeeTypeName;
    }

    public void setEmployeeTypeName(StringFilter employeeTypeName) {
        this.employeeTypeName = employeeTypeName;
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
        final EmployeeTypeCriteria that = (EmployeeTypeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(employeeTypeCode, that.employeeTypeCode) &&
            Objects.equals(employeeTypeName, that.employeeTypeName) &&
            Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        employeeTypeCode,
        employeeTypeName,
        isActive
        );
    }

    @Override
    public String toString() {
        return "EmployeeTypeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (employeeTypeCode != null ? "employeeTypeCode=" + employeeTypeCode + ", " : "") +
                (employeeTypeName != null ? "employeeTypeName=" + employeeTypeName + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
            "}";
    }

}
