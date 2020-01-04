package com.alphadevs.sales.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Items Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Items Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "items")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Items implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "item_code", nullable = false)
    private String itemCode;

    @NotNull
    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "item_price", precision = 21, scale = 2)
    private BigDecimal itemPrice;

    @Column(name = "item_barcode")
    private String itemBarcode;

    @Column(name = "item_supplier_barcode")
    private String itemSupplierBarcode;

    @Column(name = "item_promotional_price", precision = 21, scale = 2)
    private BigDecimal itemPromotionalPrice;

    @NotNull
    @Column(name = "item_cost", precision = 21, scale = 2, nullable = false)
    private BigDecimal itemCost;

    @Column(name = "is_item_on_sale")
    private Boolean isItemOnSale;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("items")
    private Model relatedModel;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("items")
    private Products relatedProduct;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("items")
    private Location location;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("items")
    private UnitOfMeasure unitOfMeasure;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("items")
    private Currency currency;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "items_addons",
               joinColumns = @JoinColumn(name = "items_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "addons_id", referencedColumnName = "id"))
    private Set<ItemAddOns> addons = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public Items itemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public Items itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public Items itemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
        return this;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public Items itemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
        return this;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public Items itemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
        return this;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getItemSupplierBarcode() {
        return itemSupplierBarcode;
    }

    public Items itemSupplierBarcode(String itemSupplierBarcode) {
        this.itemSupplierBarcode = itemSupplierBarcode;
        return this;
    }

    public void setItemSupplierBarcode(String itemSupplierBarcode) {
        this.itemSupplierBarcode = itemSupplierBarcode;
    }

    public BigDecimal getItemPromotionalPrice() {
        return itemPromotionalPrice;
    }

    public Items itemPromotionalPrice(BigDecimal itemPromotionalPrice) {
        this.itemPromotionalPrice = itemPromotionalPrice;
        return this;
    }

    public void setItemPromotionalPrice(BigDecimal itemPromotionalPrice) {
        this.itemPromotionalPrice = itemPromotionalPrice;
    }

    public BigDecimal getItemCost() {
        return itemCost;
    }

    public Items itemCost(BigDecimal itemCost) {
        this.itemCost = itemCost;
        return this;
    }

    public void setItemCost(BigDecimal itemCost) {
        this.itemCost = itemCost;
    }

    public Boolean isIsItemOnSale() {
        return isItemOnSale;
    }

    public Items isItemOnSale(Boolean isItemOnSale) {
        this.isItemOnSale = isItemOnSale;
        return this;
    }

    public void setIsItemOnSale(Boolean isItemOnSale) {
        this.isItemOnSale = isItemOnSale;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public Items expiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public byte[] getImage() {
        return image;
    }

    public Items image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Items imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Model getRelatedModel() {
        return relatedModel;
    }

    public Items relatedModel(Model model) {
        this.relatedModel = model;
        return this;
    }

    public void setRelatedModel(Model model) {
        this.relatedModel = model;
    }

    public Products getRelatedProduct() {
        return relatedProduct;
    }

    public Items relatedProduct(Products products) {
        this.relatedProduct = products;
        return this;
    }

    public void setRelatedProduct(Products products) {
        this.relatedProduct = products;
    }

    public Location getLocation() {
        return location;
    }

    public Items location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public Items unitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
        return this;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Items currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Set<ItemAddOns> getAddons() {
        return addons;
    }

    public Items addons(Set<ItemAddOns> itemAddOns) {
        this.addons = itemAddOns;
        return this;
    }

    public Items addAddons(ItemAddOns itemAddOns) {
        this.addons.add(itemAddOns);
        itemAddOns.getItems().add(this);
        return this;
    }

    public Items removeAddons(ItemAddOns itemAddOns) {
        this.addons.remove(itemAddOns);
        itemAddOns.getItems().remove(this);
        return this;
    }

    public void setAddons(Set<ItemAddOns> itemAddOns) {
        this.addons = itemAddOns;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Items)) {
            return false;
        }
        return id != null && id.equals(((Items) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Items{" +
            "id=" + getId() +
            ", itemCode='" + getItemCode() + "'" +
            ", itemName='" + getItemName() + "'" +
            ", itemDescription='" + getItemDescription() + "'" +
            ", itemPrice=" + getItemPrice() +
            ", itemBarcode='" + getItemBarcode() + "'" +
            ", itemSupplierBarcode='" + getItemSupplierBarcode() + "'" +
            ", itemPromotionalPrice=" + getItemPromotionalPrice() +
            ", itemCost=" + getItemCost() +
            ", isItemOnSale='" + isIsItemOnSale() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
