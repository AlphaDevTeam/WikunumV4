package com.alphadevs.sales.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * ItemAddOns Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "ItemAddOns Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "item_add_ons")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ItemAddOns implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "addon_code", nullable = false)
    private String addonCode;

    @NotNull
    @Column(name = "addon_name", nullable = false)
    private String addonName;

    @Column(name = "addon_description")
    private String addonDescription;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "allow_substract")
    private Boolean allowSubstract;

    @Column(name = "addon_price", precision = 21, scale = 2)
    private BigDecimal addonPrice;

    @Column(name = "substract_price", precision = 21, scale = 2)
    private BigDecimal substractPrice;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("itemAddOns")
    private Location location;

    @ManyToMany(mappedBy = "addons")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Items> items = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddonCode() {
        return addonCode;
    }

    public ItemAddOns addonCode(String addonCode) {
        this.addonCode = addonCode;
        return this;
    }

    public void setAddonCode(String addonCode) {
        this.addonCode = addonCode;
    }

    public String getAddonName() {
        return addonName;
    }

    public ItemAddOns addonName(String addonName) {
        this.addonName = addonName;
        return this;
    }

    public void setAddonName(String addonName) {
        this.addonName = addonName;
    }

    public String getAddonDescription() {
        return addonDescription;
    }

    public ItemAddOns addonDescription(String addonDescription) {
        this.addonDescription = addonDescription;
        return this;
    }

    public void setAddonDescription(String addonDescription) {
        this.addonDescription = addonDescription;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public ItemAddOns isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isAllowSubstract() {
        return allowSubstract;
    }

    public ItemAddOns allowSubstract(Boolean allowSubstract) {
        this.allowSubstract = allowSubstract;
        return this;
    }

    public void setAllowSubstract(Boolean allowSubstract) {
        this.allowSubstract = allowSubstract;
    }

    public BigDecimal getAddonPrice() {
        return addonPrice;
    }

    public ItemAddOns addonPrice(BigDecimal addonPrice) {
        this.addonPrice = addonPrice;
        return this;
    }

    public void setAddonPrice(BigDecimal addonPrice) {
        this.addonPrice = addonPrice;
    }

    public BigDecimal getSubstractPrice() {
        return substractPrice;
    }

    public ItemAddOns substractPrice(BigDecimal substractPrice) {
        this.substractPrice = substractPrice;
        return this;
    }

    public void setSubstractPrice(BigDecimal substractPrice) {
        this.substractPrice = substractPrice;
    }

    public byte[] getImage() {
        return image;
    }

    public ItemAddOns image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public ItemAddOns imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Location getLocation() {
        return location;
    }

    public ItemAddOns location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Items> getItems() {
        return items;
    }

    public ItemAddOns items(Set<Items> items) {
        this.items = items;
        return this;
    }

    public ItemAddOns addItems(Items items) {
        this.items.add(items);
        items.getAddons().add(this);
        return this;
    }

    public ItemAddOns removeItems(Items items) {
        this.items.remove(items);
        items.getAddons().remove(this);
        return this;
    }

    public void setItems(Set<Items> items) {
        this.items = items;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemAddOns)) {
            return false;
        }
        return id != null && id.equals(((ItemAddOns) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ItemAddOns{" +
            "id=" + getId() +
            ", addonCode='" + getAddonCode() + "'" +
            ", addonName='" + getAddonName() + "'" +
            ", addonDescription='" + getAddonDescription() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", allowSubstract='" + isAllowSubstract() + "'" +
            ", addonPrice=" + getAddonPrice() +
            ", substractPrice=" + getSubstractPrice() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
