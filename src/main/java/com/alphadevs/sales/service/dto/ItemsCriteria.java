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
 * Criteria class for the {@link com.alphadevs.sales.domain.Items} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.ItemsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ItemsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter itemCode;

    private StringFilter itemName;

    private StringFilter itemDescription;

    private BigDecimalFilter itemPrice;

    private StringFilter itemBarcode;

    private StringFilter itemSupplierBarcode;

    private BigDecimalFilter itemPromotionalPrice;

    private BigDecimalFilter itemCost;

    private BooleanFilter isItemOnSale;

    private LocalDateFilter expiryDate;

    private LongFilter relatedModelId;

    private LongFilter relatedProductId;

    private LongFilter locationId;

    private LongFilter unitOfMeasureId;

    private LongFilter currencyId;

    private LongFilter addonsId;

    public ItemsCriteria(){
    }

    public ItemsCriteria(ItemsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.itemCode = other.itemCode == null ? null : other.itemCode.copy();
        this.itemName = other.itemName == null ? null : other.itemName.copy();
        this.itemDescription = other.itemDescription == null ? null : other.itemDescription.copy();
        this.itemPrice = other.itemPrice == null ? null : other.itemPrice.copy();
        this.itemBarcode = other.itemBarcode == null ? null : other.itemBarcode.copy();
        this.itemSupplierBarcode = other.itemSupplierBarcode == null ? null : other.itemSupplierBarcode.copy();
        this.itemPromotionalPrice = other.itemPromotionalPrice == null ? null : other.itemPromotionalPrice.copy();
        this.itemCost = other.itemCost == null ? null : other.itemCost.copy();
        this.isItemOnSale = other.isItemOnSale == null ? null : other.isItemOnSale.copy();
        this.expiryDate = other.expiryDate == null ? null : other.expiryDate.copy();
        this.relatedModelId = other.relatedModelId == null ? null : other.relatedModelId.copy();
        this.relatedProductId = other.relatedProductId == null ? null : other.relatedProductId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.unitOfMeasureId = other.unitOfMeasureId == null ? null : other.unitOfMeasureId.copy();
        this.currencyId = other.currencyId == null ? null : other.currencyId.copy();
        this.addonsId = other.addonsId == null ? null : other.addonsId.copy();
    }

    @Override
    public ItemsCriteria copy() {
        return new ItemsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getItemCode() {
        return itemCode;
    }

    public void setItemCode(StringFilter itemCode) {
        this.itemCode = itemCode;
    }

    public StringFilter getItemName() {
        return itemName;
    }

    public void setItemName(StringFilter itemName) {
        this.itemName = itemName;
    }

    public StringFilter getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(StringFilter itemDescription) {
        this.itemDescription = itemDescription;
    }

    public BigDecimalFilter getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimalFilter itemPrice) {
        this.itemPrice = itemPrice;
    }

    public StringFilter getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(StringFilter itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public StringFilter getItemSupplierBarcode() {
        return itemSupplierBarcode;
    }

    public void setItemSupplierBarcode(StringFilter itemSupplierBarcode) {
        this.itemSupplierBarcode = itemSupplierBarcode;
    }

    public BigDecimalFilter getItemPromotionalPrice() {
        return itemPromotionalPrice;
    }

    public void setItemPromotionalPrice(BigDecimalFilter itemPromotionalPrice) {
        this.itemPromotionalPrice = itemPromotionalPrice;
    }

    public BigDecimalFilter getItemCost() {
        return itemCost;
    }

    public void setItemCost(BigDecimalFilter itemCost) {
        this.itemCost = itemCost;
    }

    public BooleanFilter getIsItemOnSale() {
        return isItemOnSale;
    }

    public void setIsItemOnSale(BooleanFilter isItemOnSale) {
        this.isItemOnSale = isItemOnSale;
    }

    public LocalDateFilter getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateFilter expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LongFilter getRelatedModelId() {
        return relatedModelId;
    }

    public void setRelatedModelId(LongFilter relatedModelId) {
        this.relatedModelId = relatedModelId;
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

    public LongFilter getUnitOfMeasureId() {
        return unitOfMeasureId;
    }

    public void setUnitOfMeasureId(LongFilter unitOfMeasureId) {
        this.unitOfMeasureId = unitOfMeasureId;
    }

    public LongFilter getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(LongFilter currencyId) {
        this.currencyId = currencyId;
    }

    public LongFilter getAddonsId() {
        return addonsId;
    }

    public void setAddonsId(LongFilter addonsId) {
        this.addonsId = addonsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ItemsCriteria that = (ItemsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(itemCode, that.itemCode) &&
            Objects.equals(itemName, that.itemName) &&
            Objects.equals(itemDescription, that.itemDescription) &&
            Objects.equals(itemPrice, that.itemPrice) &&
            Objects.equals(itemBarcode, that.itemBarcode) &&
            Objects.equals(itemSupplierBarcode, that.itemSupplierBarcode) &&
            Objects.equals(itemPromotionalPrice, that.itemPromotionalPrice) &&
            Objects.equals(itemCost, that.itemCost) &&
            Objects.equals(isItemOnSale, that.isItemOnSale) &&
            Objects.equals(expiryDate, that.expiryDate) &&
            Objects.equals(relatedModelId, that.relatedModelId) &&
            Objects.equals(relatedProductId, that.relatedProductId) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(unitOfMeasureId, that.unitOfMeasureId) &&
            Objects.equals(currencyId, that.currencyId) &&
            Objects.equals(addonsId, that.addonsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        itemCode,
        itemName,
        itemDescription,
        itemPrice,
        itemBarcode,
        itemSupplierBarcode,
        itemPromotionalPrice,
        itemCost,
        isItemOnSale,
        expiryDate,
        relatedModelId,
        relatedProductId,
        locationId,
        unitOfMeasureId,
        currencyId,
        addonsId
        );
    }

    @Override
    public String toString() {
        return "ItemsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (itemCode != null ? "itemCode=" + itemCode + ", " : "") +
                (itemName != null ? "itemName=" + itemName + ", " : "") +
                (itemDescription != null ? "itemDescription=" + itemDescription + ", " : "") +
                (itemPrice != null ? "itemPrice=" + itemPrice + ", " : "") +
                (itemBarcode != null ? "itemBarcode=" + itemBarcode + ", " : "") +
                (itemSupplierBarcode != null ? "itemSupplierBarcode=" + itemSupplierBarcode + ", " : "") +
                (itemPromotionalPrice != null ? "itemPromotionalPrice=" + itemPromotionalPrice + ", " : "") +
                (itemCost != null ? "itemCost=" + itemCost + ", " : "") +
                (isItemOnSale != null ? "isItemOnSale=" + isItemOnSale + ", " : "") +
                (expiryDate != null ? "expiryDate=" + expiryDate + ", " : "") +
                (relatedModelId != null ? "relatedModelId=" + relatedModelId + ", " : "") +
                (relatedProductId != null ? "relatedProductId=" + relatedProductId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (unitOfMeasureId != null ? "unitOfMeasureId=" + unitOfMeasureId + ", " : "") +
                (currencyId != null ? "currencyId=" + currencyId + ", " : "") +
                (addonsId != null ? "addonsId=" + addonsId + ", " : "") +
            "}";
    }

}
