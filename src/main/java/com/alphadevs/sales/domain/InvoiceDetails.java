package com.alphadevs.sales.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Invoice Details Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Invoice Details Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "invoice_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
public class InvoiceDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "inv_qty", nullable = false)
    private Double invQty;

    @Column(name = "revised_item_sales_price", precision = 21, scale = 2)
    private BigDecimal revisedItemSalesPrice;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("invoiceDetails")
    private Items item;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("details")
    private Invoice inv;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getInvQty() {
        return invQty;
    }

    public InvoiceDetails invQty(Double invQty) {
        this.invQty = invQty;
        return this;
    }

    public void setInvQty(Double invQty) {
        this.invQty = invQty;
    }

    public BigDecimal getRevisedItemSalesPrice() {
        return revisedItemSalesPrice;
    }

    public InvoiceDetails revisedItemSalesPrice(BigDecimal revisedItemSalesPrice) {
        this.revisedItemSalesPrice = revisedItemSalesPrice;
        return this;
    }

    public void setRevisedItemSalesPrice(BigDecimal revisedItemSalesPrice) {
        this.revisedItemSalesPrice = revisedItemSalesPrice;
    }

    public Items getItem() {
        return item;
    }

    public InvoiceDetails item(Items items) {
        this.item = items;
        return this;
    }

    public void setItem(Items items) {
        this.item = items;
    }

    public Invoice getInv() {
        return inv;
    }

    public InvoiceDetails inv(Invoice invoice) {
        this.inv = invoice;
        return this;
    }

    public void setInv(Invoice invoice) {
        this.inv = invoice;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceDetails)) {
            return false;
        }
        return id != null && id.equals(((InvoiceDetails) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "InvoiceDetails{" +
            "id=" + getId() +
            ", invQty=" + getInvQty() +
            ", revisedItemSalesPrice=" + getRevisedItemSalesPrice() +
            "}";
    }
}
