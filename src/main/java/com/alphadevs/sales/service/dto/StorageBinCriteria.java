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
 * Criteria class for the {@link com.alphadevs.sales.domain.StorageBin} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.StorageBinResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /storage-bins?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StorageBinCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter binNumber;

    private StringFilter binDescription;

    public StorageBinCriteria(){
    }

    public StorageBinCriteria(StorageBinCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.binNumber = other.binNumber == null ? null : other.binNumber.copy();
        this.binDescription = other.binDescription == null ? null : other.binDescription.copy();
    }

    @Override
    public StorageBinCriteria copy() {
        return new StorageBinCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBinNumber() {
        return binNumber;
    }

    public void setBinNumber(StringFilter binNumber) {
        this.binNumber = binNumber;
    }

    public StringFilter getBinDescription() {
        return binDescription;
    }

    public void setBinDescription(StringFilter binDescription) {
        this.binDescription = binDescription;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StorageBinCriteria that = (StorageBinCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(binNumber, that.binNumber) &&
            Objects.equals(binDescription, that.binDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        binNumber,
        binDescription
        );
    }

    @Override
    public String toString() {
        return "StorageBinCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (binNumber != null ? "binNumber=" + binNumber + ", " : "") +
                (binDescription != null ? "binDescription=" + binDescription + ", " : "") +
            "}";
    }

}
