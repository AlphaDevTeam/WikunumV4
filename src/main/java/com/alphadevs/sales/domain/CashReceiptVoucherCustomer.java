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

/**
 * CashReceiptVoucherCustomer Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "CashReceiptVoucherCustomer Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "cash_receipt_voucher_customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CashReceiptVoucherCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "transaction_number", nullable = false)
    private String transactionNumber;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @NotNull
    @Column(name = "transaction_description", nullable = false)
    private String transactionDescription;

    @NotNull
    @Column(name = "transaction_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal transactionAmount;

    @OneToOne
    @JoinColumn(unique = true)
    private DocumentHistory history;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("cashReceiptVoucherCustomers")
    private Location location;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("cashReceiptVoucherCustomers")
    private TransactionType transactionType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("cashReceiptVoucherCustomers")
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public CashReceiptVoucherCustomer transactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
        return this;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public CashReceiptVoucherCustomer transactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public CashReceiptVoucherCustomer transactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
        return this;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public CashReceiptVoucherCustomer transactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
        return this;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public DocumentHistory getHistory() {
        return history;
    }

    public CashReceiptVoucherCustomer history(DocumentHistory documentHistory) {
        this.history = documentHistory;
        return this;
    }

    public void setHistory(DocumentHistory documentHistory) {
        this.history = documentHistory;
    }

    public Location getLocation() {
        return location;
    }

    public CashReceiptVoucherCustomer location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public CashReceiptVoucherCustomer transactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public CashReceiptVoucherCustomer customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CashReceiptVoucherCustomer)) {
            return false;
        }
        return id != null && id.equals(((CashReceiptVoucherCustomer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CashReceiptVoucherCustomer{" +
            "id=" + getId() +
            ", transactionNumber='" + getTransactionNumber() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", transactionDescription='" + getTransactionDescription() + "'" +
            ", transactionAmount=" + getTransactionAmount() +
            "}";
    }
}