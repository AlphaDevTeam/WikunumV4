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
 * GRN Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "GRN Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "goods_receipt")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GoodsReceipt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "grn_number", nullable = false)
    private String grnNumber;

    @NotNull
    @Column(name = "grn_date", nullable = false)
    private LocalDate grnDate;

    @Column(name = "po_number")
    private String poNumber;

    @NotNull
    @Column(name = "grn_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal grnAmount;

    @Column(name = "cash_amount", precision = 21, scale = 2)
    private BigDecimal cashAmount;

    @Column(name = "card_amount", precision = 21, scale = 2)
    private BigDecimal cardAmount;

    @Column(name = "due_amount", precision = 21, scale = 2)
    private BigDecimal dueAmount;

    @OneToMany(mappedBy = "grn")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GoodsReceiptDetails> details = new HashSet<>();

    @OneToMany(mappedBy = "relatedGRN")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PurchaseOrder> linkedPOs = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("goodsReceipts")
    private Supplier supplier;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("goodsReceipts")
    private Location location;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("goodsReceipts")
    private TransactionType transactionType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrnNumber() {
        return grnNumber;
    }

    public GoodsReceipt grnNumber(String grnNumber) {
        this.grnNumber = grnNumber;
        return this;
    }

    public void setGrnNumber(String grnNumber) {
        this.grnNumber = grnNumber;
    }

    public LocalDate getGrnDate() {
        return grnDate;
    }

    public GoodsReceipt grnDate(LocalDate grnDate) {
        this.grnDate = grnDate;
        return this;
    }

    public void setGrnDate(LocalDate grnDate) {
        this.grnDate = grnDate;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public GoodsReceipt poNumber(String poNumber) {
        this.poNumber = poNumber;
        return this;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public BigDecimal getGrnAmount() {
        return grnAmount;
    }

    public GoodsReceipt grnAmount(BigDecimal grnAmount) {
        this.grnAmount = grnAmount;
        return this;
    }

    public void setGrnAmount(BigDecimal grnAmount) {
        this.grnAmount = grnAmount;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public GoodsReceipt cashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
        return this;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    public BigDecimal getCardAmount() {
        return cardAmount;
    }

    public GoodsReceipt cardAmount(BigDecimal cardAmount) {
        this.cardAmount = cardAmount;
        return this;
    }

    public void setCardAmount(BigDecimal cardAmount) {
        this.cardAmount = cardAmount;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public GoodsReceipt dueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
        return this;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }

    public Set<GoodsReceiptDetails> getDetails() {
        return details;
    }

    public GoodsReceipt details(Set<GoodsReceiptDetails> goodsReceiptDetails) {
        this.details = goodsReceiptDetails;
        return this;
    }

    public GoodsReceipt addDetails(GoodsReceiptDetails goodsReceiptDetails) {
        this.details.add(goodsReceiptDetails);
        goodsReceiptDetails.setGrn(this);
        return this;
    }

    public GoodsReceipt removeDetails(GoodsReceiptDetails goodsReceiptDetails) {
        this.details.remove(goodsReceiptDetails);
        goodsReceiptDetails.setGrn(null);
        return this;
    }

    public void setDetails(Set<GoodsReceiptDetails> goodsReceiptDetails) {
        this.details = goodsReceiptDetails;
    }

    public Set<PurchaseOrder> getLinkedPOs() {
        return linkedPOs;
    }

    public GoodsReceipt linkedPOs(Set<PurchaseOrder> purchaseOrders) {
        this.linkedPOs = purchaseOrders;
        return this;
    }

    public GoodsReceipt addLinkedPOs(PurchaseOrder purchaseOrder) {
        this.linkedPOs.add(purchaseOrder);
        purchaseOrder.setRelatedGRN(this);
        return this;
    }

    public GoodsReceipt removeLinkedPOs(PurchaseOrder purchaseOrder) {
        this.linkedPOs.remove(purchaseOrder);
        purchaseOrder.setRelatedGRN(null);
        return this;
    }

    public void setLinkedPOs(Set<PurchaseOrder> purchaseOrders) {
        this.linkedPOs = purchaseOrders;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public GoodsReceipt supplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Location getLocation() {
        return location;
    }

    public GoodsReceipt location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public GoodsReceipt transactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GoodsReceipt)) {
            return false;
        }
        return id != null && id.equals(((GoodsReceipt) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GoodsReceipt{" +
            "id=" + getId() +
            ", grnNumber='" + getGrnNumber() + "'" +
            ", grnDate='" + getGrnDate() + "'" +
            ", poNumber='" + getPoNumber() + "'" +
            ", grnAmount=" + getGrnAmount() +
            ", cashAmount=" + getCashAmount() +
            ", cardAmount=" + getCardAmount() +
            ", dueAmount=" + getDueAmount() +
            "}";
    }
}
