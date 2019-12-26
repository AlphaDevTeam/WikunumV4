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
 * PurchaseOrder Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "PurchaseOrder Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "purchase_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "po_number", nullable = false)
    private String poNumber;

    @NotNull
    @Column(name = "po_date", nullable = false)
    private LocalDate poDate;

    @Column(name = "po_amount", precision = 21, scale = 2)
    private BigDecimal poAmount;

    @OneToOne
    @JoinColumn(unique = true)
    private DocumentHistory history;

    @OneToMany(mappedBy = "po")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PurchaseOrderDetails> details = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("purchaseOrders")
    private Supplier supplier;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("purchaseOrders")
    private Location location;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("purchaseOrders")
    private TransactionType transactionType;

    @ManyToOne
    @JsonIgnoreProperties("linkedPOs")
    private GoodsReceipt relatedGRN;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public PurchaseOrder poNumber(String poNumber) {
        this.poNumber = poNumber;
        return this;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public LocalDate getPoDate() {
        return poDate;
    }

    public PurchaseOrder poDate(LocalDate poDate) {
        this.poDate = poDate;
        return this;
    }

    public void setPoDate(LocalDate poDate) {
        this.poDate = poDate;
    }

    public BigDecimal getPoAmount() {
        return poAmount;
    }

    public PurchaseOrder poAmount(BigDecimal poAmount) {
        this.poAmount = poAmount;
        return this;
    }

    public void setPoAmount(BigDecimal poAmount) {
        this.poAmount = poAmount;
    }

    public DocumentHistory getHistory() {
        return history;
    }

    public PurchaseOrder history(DocumentHistory documentHistory) {
        this.history = documentHistory;
        return this;
    }

    public void setHistory(DocumentHistory documentHistory) {
        this.history = documentHistory;
    }

    public Set<PurchaseOrderDetails> getDetails() {
        return details;
    }

    public PurchaseOrder details(Set<PurchaseOrderDetails> purchaseOrderDetails) {
        this.details = purchaseOrderDetails;
        return this;
    }

    public PurchaseOrder addDetails(PurchaseOrderDetails purchaseOrderDetails) {
        this.details.add(purchaseOrderDetails);
        purchaseOrderDetails.setPo(this);
        return this;
    }

    public PurchaseOrder removeDetails(PurchaseOrderDetails purchaseOrderDetails) {
        this.details.remove(purchaseOrderDetails);
        purchaseOrderDetails.setPo(null);
        return this;
    }

    public void setDetails(Set<PurchaseOrderDetails> purchaseOrderDetails) {
        this.details = purchaseOrderDetails;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public PurchaseOrder supplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Location getLocation() {
        return location;
    }

    public PurchaseOrder location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public PurchaseOrder transactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public GoodsReceipt getRelatedGRN() {
        return relatedGRN;
    }

    public PurchaseOrder relatedGRN(GoodsReceipt goodsReceipt) {
        this.relatedGRN = goodsReceipt;
        return this;
    }

    public void setRelatedGRN(GoodsReceipt goodsReceipt) {
        this.relatedGRN = goodsReceipt;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseOrder)) {
            return false;
        }
        return id != null && id.equals(((PurchaseOrder) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" +
            "id=" + getId() +
            ", poNumber='" + getPoNumber() + "'" +
            ", poDate='" + getPoDate() + "'" +
            ", poAmount=" + getPoAmount() +
            "}";
    }
}
