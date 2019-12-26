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
 * Criteria class for the {@link com.alphadevs.sales.domain.JobDetails} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.JobDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /job-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JobDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter jobItemPrice;

    private DoubleFilter jobItemQty;

    private LongFilter itemId;

    private LongFilter jobId;

    public JobDetailsCriteria(){
    }

    public JobDetailsCriteria(JobDetailsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.jobItemPrice = other.jobItemPrice == null ? null : other.jobItemPrice.copy();
        this.jobItemQty = other.jobItemQty == null ? null : other.jobItemQty.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
        this.jobId = other.jobId == null ? null : other.jobId.copy();
    }

    @Override
    public JobDetailsCriteria copy() {
        return new JobDetailsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getJobItemPrice() {
        return jobItemPrice;
    }

    public void setJobItemPrice(BigDecimalFilter jobItemPrice) {
        this.jobItemPrice = jobItemPrice;
    }

    public DoubleFilter getJobItemQty() {
        return jobItemQty;
    }

    public void setJobItemQty(DoubleFilter jobItemQty) {
        this.jobItemQty = jobItemQty;
    }

    public LongFilter getItemId() {
        return itemId;
    }

    public void setItemId(LongFilter itemId) {
        this.itemId = itemId;
    }

    public LongFilter getJobId() {
        return jobId;
    }

    public void setJobId(LongFilter jobId) {
        this.jobId = jobId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final JobDetailsCriteria that = (JobDetailsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(jobItemPrice, that.jobItemPrice) &&
            Objects.equals(jobItemQty, that.jobItemQty) &&
            Objects.equals(itemId, that.itemId) &&
            Objects.equals(jobId, that.jobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        jobItemPrice,
        jobItemQty,
        itemId,
        jobId
        );
    }

    @Override
    public String toString() {
        return "JobDetailsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (jobItemPrice != null ? "jobItemPrice=" + jobItemPrice + ", " : "") +
                (jobItemQty != null ? "jobItemQty=" + jobItemQty + ", " : "") +
                (itemId != null ? "itemId=" + itemId + ", " : "") +
                (jobId != null ? "jobId=" + jobId + ", " : "") +
            "}";
    }

}
