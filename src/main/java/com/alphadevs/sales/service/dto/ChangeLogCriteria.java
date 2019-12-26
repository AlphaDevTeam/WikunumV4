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
 * Criteria class for the {@link com.alphadevs.sales.domain.ChangeLog} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.ChangeLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /change-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChangeLogCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter changeKey;

    private StringFilter changeFrom;

    private StringFilter changeTo;

    private LongFilter documentHistoryId;

    public ChangeLogCriteria(){
    }

    public ChangeLogCriteria(ChangeLogCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.changeKey = other.changeKey == null ? null : other.changeKey.copy();
        this.changeFrom = other.changeFrom == null ? null : other.changeFrom.copy();
        this.changeTo = other.changeTo == null ? null : other.changeTo.copy();
        this.documentHistoryId = other.documentHistoryId == null ? null : other.documentHistoryId.copy();
    }

    @Override
    public ChangeLogCriteria copy() {
        return new ChangeLogCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getChangeKey() {
        return changeKey;
    }

    public void setChangeKey(StringFilter changeKey) {
        this.changeKey = changeKey;
    }

    public StringFilter getChangeFrom() {
        return changeFrom;
    }

    public void setChangeFrom(StringFilter changeFrom) {
        this.changeFrom = changeFrom;
    }

    public StringFilter getChangeTo() {
        return changeTo;
    }

    public void setChangeTo(StringFilter changeTo) {
        this.changeTo = changeTo;
    }

    public LongFilter getDocumentHistoryId() {
        return documentHistoryId;
    }

    public void setDocumentHistoryId(LongFilter documentHistoryId) {
        this.documentHistoryId = documentHistoryId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ChangeLogCriteria that = (ChangeLogCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(changeKey, that.changeKey) &&
            Objects.equals(changeFrom, that.changeFrom) &&
            Objects.equals(changeTo, that.changeTo) &&
            Objects.equals(documentHistoryId, that.documentHistoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        changeKey,
        changeFrom,
        changeTo,
        documentHistoryId
        );
    }

    @Override
    public String toString() {
        return "ChangeLogCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (changeKey != null ? "changeKey=" + changeKey + ", " : "") +
                (changeFrom != null ? "changeFrom=" + changeFrom + ", " : "") +
                (changeTo != null ? "changeTo=" + changeTo + ", " : "") +
                (documentHistoryId != null ? "documentHistoryId=" + documentHistoryId + ", " : "") +
            "}";
    }

}
