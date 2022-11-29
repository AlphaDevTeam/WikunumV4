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
 * Criteria class for the {@link com.alphadevs.sales.domain.Job} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.JobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JobCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter jobCode;

    private StringFilter jobDescription;

    private LocalDateFilter jobStartDate;

    private LocalDateFilter jobEndDate;

    private BigDecimalFilter jobAmount;

    private LongFilter statusId;

    private LongFilter detailsId;

    private LongFilter locationId;

    private LongFilter customerId;

    private LongFilter assignedToId;

    public JobCriteria(){
    }

    public JobCriteria(JobCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.jobCode = other.jobCode == null ? null : other.jobCode.copy();
        this.jobDescription = other.jobDescription == null ? null : other.jobDescription.copy();
        this.jobStartDate = other.jobStartDate == null ? null : other.jobStartDate.copy();
        this.jobEndDate = other.jobEndDate == null ? null : other.jobEndDate.copy();
        this.jobAmount = other.jobAmount == null ? null : other.jobAmount.copy();
        this.statusId = other.statusId == null ? null : other.statusId.copy();
        this.detailsId = other.detailsId == null ? null : other.detailsId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.assignedToId = other.assignedToId == null ? null : other.assignedToId.copy();
    }

    @Override
    public JobCriteria copy() {
        return new JobCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getJobCode() {
        return jobCode;
    }

    public void setJobCode(StringFilter jobCode) {
        this.jobCode = jobCode;
    }

    public StringFilter getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(StringFilter jobDescription) {
        this.jobDescription = jobDescription;
    }

    public LocalDateFilter getJobStartDate() {
        return jobStartDate;
    }

    public void setJobStartDate(LocalDateFilter jobStartDate) {
        this.jobStartDate = jobStartDate;
    }

    public LocalDateFilter getJobEndDate() {
        return jobEndDate;
    }

    public void setJobEndDate(LocalDateFilter jobEndDate) {
        this.jobEndDate = jobEndDate;
    }

    public BigDecimalFilter getJobAmount() {
        return jobAmount;
    }

    public void setJobAmount(BigDecimalFilter jobAmount) {
        this.jobAmount = jobAmount;
    }

    public LongFilter getStatusId() {
        return statusId;
    }

    public void setStatusId(LongFilter statusId) {
        this.statusId = statusId;
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

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(LongFilter assignedToId) {
        this.assignedToId = assignedToId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final JobCriteria that = (JobCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(jobCode, that.jobCode) &&
            Objects.equals(jobDescription, that.jobDescription) &&
            Objects.equals(jobStartDate, that.jobStartDate) &&
            Objects.equals(jobEndDate, that.jobEndDate) &&
            Objects.equals(jobAmount, that.jobAmount) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(detailsId, that.detailsId) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(assignedToId, that.assignedToId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        jobCode,
        jobDescription,
        jobStartDate,
        jobEndDate,
        jobAmount,
        statusId,
        detailsId,
        locationId,
        customerId,
        assignedToId
        );
    }

    @Override
    public String toString() {
        return "JobCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (jobCode != null ? "jobCode=" + jobCode + ", " : "") +
                (jobDescription != null ? "jobDescription=" + jobDescription + ", " : "") +
                (jobStartDate != null ? "jobStartDate=" + jobStartDate + ", " : "") +
                (jobEndDate != null ? "jobEndDate=" + jobEndDate + ", " : "") +
                (jobAmount != null ? "jobAmount=" + jobAmount + ", " : "") +
                (statusId != null ? "statusId=" + statusId + ", " : "") +
                (detailsId != null ? "detailsId=" + detailsId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
                (assignedToId != null ? "assignedToId=" + assignedToId + ", " : "") +
            "}";
    }

}
