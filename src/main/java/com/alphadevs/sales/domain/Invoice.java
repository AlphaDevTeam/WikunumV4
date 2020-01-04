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
 * Invoice Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Invoice Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "inv_number", nullable = false)
    private String invNumber;

    @NotNull
    @Column(name = "inv_date", nullable = false)
    private LocalDate invDate;

    @NotNull
    @Column(name = "inv_total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal invTotalAmount;

    @Column(name = "cash_amount", precision = 21, scale = 2)
    private BigDecimal cashAmount;

    @Column(name = "card_amount", precision = 21, scale = 2)
    private BigDecimal cardAmount;

    @Column(name = "due_amount", precision = 21, scale = 2)
    private BigDecimal dueAmount;

    @OneToMany(mappedBy = "inv")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InvoiceDetails> details = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("invoices")
    private Customer customer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("invoices")
    private TransactionType transactionType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("invoices")
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvNumber() {
        return invNumber;
    }

    public Invoice invNumber(String invNumber) {
        this.invNumber = invNumber;
        return this;
    }

    public void setInvNumber(String invNumber) {
        this.invNumber = invNumber;
    }

    public LocalDate getInvDate() {
        return invDate;
    }

    public Invoice invDate(LocalDate invDate) {
        this.invDate = invDate;
        return this;
    }

    public void setInvDate(LocalDate invDate) {
        this.invDate = invDate;
    }

    public BigDecimal getInvTotalAmount() {
        return invTotalAmount;
    }

    public Invoice invTotalAmount(BigDecimal invTotalAmount) {
        this.invTotalAmount = invTotalAmount;
        return this;
    }

    public void setInvTotalAmount(BigDecimal invTotalAmount) {
        this.invTotalAmount = invTotalAmount;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public Invoice cashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
        return this;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    public BigDecimal getCardAmount() {
        return cardAmount;
    }

    public Invoice cardAmount(BigDecimal cardAmount) {
        this.cardAmount = cardAmount;
        return this;
    }

    public void setCardAmount(BigDecimal cardAmount) {
        this.cardAmount = cardAmount;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public Invoice dueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
        return this;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }

    public Set<InvoiceDetails> getDetails() {
        return details;
    }

    public Invoice details(Set<InvoiceDetails> invoiceDetails) {
        this.details = invoiceDetails;
        return this;
    }

    public Invoice addDetails(InvoiceDetails invoiceDetails) {
        this.details.add(invoiceDetails);
        invoiceDetails.setInv(this);
        return this;
    }

    public Invoice removeDetails(InvoiceDetails invoiceDetails) {
        this.details.remove(invoiceDetails);
        invoiceDetails.setInv(null);
        return this;
    }

    public void setDetails(Set<InvoiceDetails> invoiceDetails) {
        this.details = invoiceDetails;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Invoice customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Invoice transactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Location getLocation() {
        return location;
    }

    public Invoice location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", invNumber='" + getInvNumber() + "'" +
            ", invDate='" + getInvDate() + "'" +
            ", invTotalAmount=" + getInvTotalAmount() +
            ", cashAmount=" + getCashAmount() +
            ", cardAmount=" + getCardAmount() +
            ", dueAmount=" + getDueAmount() +
            "}";
    }
}
