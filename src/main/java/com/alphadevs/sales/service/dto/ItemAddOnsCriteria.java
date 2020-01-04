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
 * Criteria class for the {@link com.alphadevs.sales.domain.ItemAddOns} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.ItemAddOnsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /item-add-ons?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ItemAddOnsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter addonCode;

    private StringFilter addonName;

    private StringFilter addonDescription;

    private BooleanFilter isActive;

    private BooleanFilter allowSubstract;

    private BigDecimalFilter addonPrice;

    private BigDecimalFilter substractPrice;

    private LongFilter locationId;

    private LongFilter itemsId;

    public ItemAddOnsCriteria(){
    }

    public ItemAddOnsCriteria(ItemAddOnsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.addonCode = other.addonCode == null ? null : other.addonCode.copy();
        this.addonName = other.addonName == null ? null : other.addonName.copy();
        this.addonDescription = other.addonDescription == null ? null : other.addonDescription.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.allowSubstract = other.allowSubstract == null ? null : other.allowSubstract.copy();
        this.addonPrice = other.addonPrice == null ? null : other.addonPrice.copy();
        this.substractPrice = other.substractPrice == null ? null : other.substractPrice.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.itemsId = other.itemsId == null ? null : other.itemsId.copy();
    }

    @Override
    public ItemAddOnsCriteria copy() {
        return new ItemAddOnsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAddonCode() {
        return addonCode;
    }

    public void setAddonCode(StringFilter addonCode) {
        this.addonCode = addonCode;
    }

    public StringFilter getAddonName() {
        return addonName;
    }

    public void setAddonName(StringFilter addonName) {
        this.addonName = addonName;
    }

    public StringFilter getAddonDescription() {
        return addonDescription;
    }

    public void setAddonDescription(StringFilter addonDescription) {
        this.addonDescription = addonDescription;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public BooleanFilter getAllowSubstract() {
        return allowSubstract;
    }

    public void setAllowSubstract(BooleanFilter allowSubstract) {
        this.allowSubstract = allowSubstract;
    }

    public BigDecimalFilter getAddonPrice() {
        return addonPrice;
    }

    public void setAddonPrice(BigDecimalFilter addonPrice) {
        this.addonPrice = addonPrice;
    }

    public BigDecimalFilter getSubstractPrice() {
        return substractPrice;
    }

    public void setSubstractPrice(BigDecimalFilter substractPrice) {
        this.substractPrice = substractPrice;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public LongFilter getItemsId() {
        return itemsId;
    }

    public void setItemsId(LongFilter itemsId) {
        this.itemsId = itemsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ItemAddOnsCriteria that = (ItemAddOnsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(addonCode, that.addonCode) &&
            Objects.equals(addonName, that.addonName) &&
            Objects.equals(addonDescription, that.addonDescription) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(allowSubstract, that.allowSubstract) &&
            Objects.equals(addonPrice, that.addonPrice) &&
            Objects.equals(substractPrice, that.substractPrice) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(itemsId, that.itemsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        addonCode,
        addonName,
        addonDescription,
        isActive,
        allowSubstract,
        addonPrice,
        substractPrice,
        locationId,
        itemsId
        );
    }

    @Override
    public String toString() {
        return "ItemAddOnsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (addonCode != null ? "addonCode=" + addonCode + ", " : "") +
                (addonName != null ? "addonName=" + addonName + ", " : "") +
                (addonDescription != null ? "addonDescription=" + addonDescription + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (allowSubstract != null ? "allowSubstract=" + allowSubstract + ", " : "") +
                (addonPrice != null ? "addonPrice=" + addonPrice + ", " : "") +
                (substractPrice != null ? "substractPrice=" + substractPrice + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (itemsId != null ? "itemsId=" + itemsId + ", " : "") +
            "}";
    }

}
