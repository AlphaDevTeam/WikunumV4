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
 * Criteria class for the {@link com.alphadevs.sales.domain.GoodsReceiptDetails} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.GoodsReceiptDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /goods-receipt-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GoodsReceiptDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter grnQty;

    private BigDecimalFilter revisedItemCost;

    private LongFilter itemId;

    private LongFilter storageBinId;

    private LongFilter grnId;

    public GoodsReceiptDetailsCriteria(){
    }

    public GoodsReceiptDetailsCriteria(GoodsReceiptDetailsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.grnQty = other.grnQty == null ? null : other.grnQty.copy();
        this.revisedItemCost = other.revisedItemCost == null ? null : other.revisedItemCost.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
        this.storageBinId = other.storageBinId == null ? null : other.storageBinId.copy();
        this.grnId = other.grnId == null ? null : other.grnId.copy();
    }

    @Override
    public GoodsReceiptDetailsCriteria copy() {
        return new GoodsReceiptDetailsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getGrnQty() {
        return grnQty;
    }

    public void setGrnQty(DoubleFilter grnQty) {
        this.grnQty = grnQty;
    }

    public BigDecimalFilter getRevisedItemCost() {
        return revisedItemCost;
    }

    public void setRevisedItemCost(BigDecimalFilter revisedItemCost) {
        this.revisedItemCost = revisedItemCost;
    }

    public LongFilter getItemId() {
        return itemId;
    }

    public void setItemId(LongFilter itemId) {
        this.itemId = itemId;
    }

    public LongFilter getStorageBinId() {
        return storageBinId;
    }

    public void setStorageBinId(LongFilter storageBinId) {
        this.storageBinId = storageBinId;
    }

    public LongFilter getGrnId() {
        return grnId;
    }

    public void setGrnId(LongFilter grnId) {
        this.grnId = grnId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GoodsReceiptDetailsCriteria that = (GoodsReceiptDetailsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(grnQty, that.grnQty) &&
            Objects.equals(revisedItemCost, that.revisedItemCost) &&
            Objects.equals(itemId, that.itemId) &&
            Objects.equals(storageBinId, that.storageBinId) &&
            Objects.equals(grnId, that.grnId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        grnQty,
        revisedItemCost,
        itemId,
        storageBinId,
        grnId
        );
    }

    @Override
    public String toString() {
        return "GoodsReceiptDetailsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (grnQty != null ? "grnQty=" + grnQty + ", " : "") +
                (revisedItemCost != null ? "revisedItemCost=" + revisedItemCost + ", " : "") +
                (itemId != null ? "itemId=" + itemId + ", " : "") +
                (storageBinId != null ? "storageBinId=" + storageBinId + ", " : "") +
                (grnId != null ? "grnId=" + grnId + ", " : "") +
            "}";
    }

}
