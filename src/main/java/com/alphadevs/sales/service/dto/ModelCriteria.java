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
 * Criteria class for the {@link com.alphadevs.sales.domain.Model} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.ModelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /models?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ModelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter modelCode;

    private StringFilter modelName;

    private LongFilter historyId;

    private LongFilter relatedProductId;

    private LongFilter locationId;

    public ModelCriteria(){
    }

    public ModelCriteria(ModelCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.modelCode = other.modelCode == null ? null : other.modelCode.copy();
        this.modelName = other.modelName == null ? null : other.modelName.copy();
        this.historyId = other.historyId == null ? null : other.historyId.copy();
        this.relatedProductId = other.relatedProductId == null ? null : other.relatedProductId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public ModelCriteria copy() {
        return new ModelCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getModelCode() {
        return modelCode;
    }

    public void setModelCode(StringFilter modelCode) {
        this.modelCode = modelCode;
    }

    public StringFilter getModelName() {
        return modelName;
    }

    public void setModelName(StringFilter modelName) {
        this.modelName = modelName;
    }

    public LongFilter getHistoryId() {
        return historyId;
    }

    public void setHistoryId(LongFilter historyId) {
        this.historyId = historyId;
    }

    public LongFilter getRelatedProductId() {
        return relatedProductId;
    }

    public void setRelatedProductId(LongFilter relatedProductId) {
        this.relatedProductId = relatedProductId;
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
        final ModelCriteria that = (ModelCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(modelCode, that.modelCode) &&
            Objects.equals(modelName, that.modelName) &&
            Objects.equals(historyId, that.historyId) &&
            Objects.equals(relatedProductId, that.relatedProductId) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        modelCode,
        modelName,
        historyId,
        relatedProductId,
        locationId
        );
    }

    @Override
    public String toString() {
        return "ModelCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (modelCode != null ? "modelCode=" + modelCode + ", " : "") +
                (modelName != null ? "modelName=" + modelName + ", " : "") +
                (historyId != null ? "historyId=" + historyId + ", " : "") +
                (relatedProductId != null ? "relatedProductId=" + relatedProductId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
