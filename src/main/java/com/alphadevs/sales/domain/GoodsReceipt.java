package com.alphadevs.sales.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

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
@Audited
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

    @OneToOne
    @JoinColumn(unique = true)
    @NotAudited
    private DocumentHistory history;

    @OneToMany(mappedBy = "grn", fetch = FetchType.EAGER)
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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("goodsReceipts")
    private PaymentTypes payType;

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

    public DocumentHistory getHistory() {
        return history;
    }

    public GoodsReceipt history(DocumentHistory documentHistory) {
        this.history = documentHistory;
        return this;
    }

    public void setHistory(DocumentHistory documentHistory) {
        this.history = documentHistory;
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

    public PaymentTypes getPayType() {
        return payType;
    }

    public GoodsReceipt payType(PaymentTypes paymentTypes) {
        this.payType = paymentTypes;
        return this;
    }

    public void setPayType(PaymentTypes paymentTypes) {
        this.payType = paymentTypes;
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
            "}";
    }
}
