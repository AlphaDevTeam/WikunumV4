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
 * Criteria class for the {@link com.alphadevs.sales.domain.Worker} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.WorkerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /workers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WorkerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter workerCode;

    private StringFilter workerName;

    private BigDecimalFilter workerLimit;

    private BooleanFilter isActive;

    private DoubleFilter rating;

    private LongFilter historyId;

    private LongFilter locationId;

    private LongFilter jobsId;

    public WorkerCriteria(){
    }

    public WorkerCriteria(WorkerCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.workerCode = other.workerCode == null ? null : other.workerCode.copy();
        this.workerName = other.workerName == null ? null : other.workerName.copy();
        this.workerLimit = other.workerLimit == null ? null : other.workerLimit.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.rating = other.rating == null ? null : other.rating.copy();
        this.historyId = other.historyId == null ? null : other.historyId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.jobsId = other.jobsId == null ? null : other.jobsId.copy();
    }

    @Override
    public WorkerCriteria copy() {
        return new WorkerCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getWorkerCode() {
        return workerCode;
    }

    public void setWorkerCode(StringFilter workerCode) {
        this.workerCode = workerCode;
    }

    public StringFilter getWorkerName() {
        return workerName;
    }

    public void setWorkerName(StringFilter workerName) {
        this.workerName = workerName;
    }

    public BigDecimalFilter getWorkerLimit() {
        return workerLimit;
    }

    public void setWorkerLimit(BigDecimalFilter workerLimit) {
        this.workerLimit = workerLimit;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public DoubleFilter getRating() {
        return rating;
    }

    public void setRating(DoubleFilter rating) {
        this.rating = rating;
    }

    public LongFilter getHistoryId() {
        return historyId;
    }

    public void setHistoryId(LongFilter historyId) {
        this.historyId = historyId;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public LongFilter getJobsId() {
        return jobsId;
    }

    public void setJobsId(LongFilter jobsId) {
        this.jobsId = jobsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WorkerCriteria that = (WorkerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(workerCode, that.workerCode) &&
            Objects.equals(workerName, that.workerName) &&
            Objects.equals(workerLimit, that.workerLimit) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(historyId, that.historyId) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(jobsId, that.jobsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        workerCode,
        workerName,
        workerLimit,
        isActive,
        rating,
        historyId,
        locationId,
        jobsId
        );
    }

    @Override
    public String toString() {
        return "WorkerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (workerCode != null ? "workerCode=" + workerCode + ", " : "") +
                (workerName != null ? "workerName=" + workerName + ", " : "") +
                (workerLimit != null ? "workerLimit=" + workerLimit + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (rating != null ? "rating=" + rating + ", " : "") +
                (historyId != null ? "historyId=" + historyId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (jobsId != null ? "jobsId=" + jobsId + ", " : "") +
            "}";
    }

}
