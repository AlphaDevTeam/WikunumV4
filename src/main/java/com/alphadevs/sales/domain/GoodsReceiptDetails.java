package com.alphadevs.sales.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * GRN Details Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "GRN Details Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "goods_receipt_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GoodsReceiptDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "grn_qty", nullable = false)
    private Double grnQty;

    @Column(name = "revised_item_cost", precision = 21, scale = 2)
    private BigDecimal revisedItemCost;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("goodsReceiptDetails")
    private Items item;

    @ManyToOne
    @JsonIgnoreProperties("goodsReceiptDetails")
    private StorageBin storageBin;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("details")
    private GoodsReceipt grn;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getGrnQty() {
        return grnQty;
    }

    public GoodsReceiptDetails grnQty(Double grnQty) {
        this.grnQty = grnQty;
        return this;
    }

    public void setGrnQty(Double grnQty) {
        this.grnQty = grnQty;
    }

    public BigDecimal getRevisedItemCost() {
        return revisedItemCost;
    }

    public GoodsReceiptDetails revisedItemCost(BigDecimal revisedItemCost) {
        this.revisedItemCost = revisedItemCost;
        return this;
    }

    public void setRevisedItemCost(BigDecimal revisedItemCost) {
        this.revisedItemCost = revisedItemCost;
    }

    public Items getItem() {
        return item;
    }

    public GoodsReceiptDetails item(Items items) {
        this.item = items;
        return this;
    }

    public void setItem(Items items) {
        this.item = items;
    }

    public StorageBin getStorageBin() {
        return storageBin;
    }

    public GoodsReceiptDetails storageBin(StorageBin storageBin) {
        this.storageBin = storageBin;
        return this;
    }

    public void setStorageBin(StorageBin storageBin) {
        this.storageBin = storageBin;
    }

    public GoodsReceipt getGrn() {
        return grn;
    }

    public GoodsReceiptDetails grn(GoodsReceipt goodsReceipt) {
        this.grn = goodsReceipt;
        return this;
    }

    public void setGrn(GoodsReceipt goodsReceipt) {
        this.grn = goodsReceipt;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GoodsReceiptDetails)) {
            return false;
        }
        return id != null && id.equals(((GoodsReceiptDetails) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GoodsReceiptDetails{" +
            "id=" + getId() +
            ", grnQty=" + getGrnQty() +
            ", revisedItemCost=" + getRevisedItemCost() +
            "}";
    }
}
