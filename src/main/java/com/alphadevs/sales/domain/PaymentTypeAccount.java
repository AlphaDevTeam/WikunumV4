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
 * PaymentTypes Account Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "PaymentTypes Account Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "payment_type_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PaymentTypeAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @NotNull
    @Column(name = "transaction_description", nullable = false)
    private String transactionDescription;

    @NotNull
    @Column(name = "transaction_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal transactionAmount;

    @NotNull
    @Column(name = "transaction_balance", precision = 21, scale = 2, nullable = false)
    private BigDecimal transactionBalance;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("paymentTypeAccounts")
    private Location location;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("paymentTypeAccounts")
    private TransactionType transactionType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("paymentTypeAccounts")
    private PaymentTypes payType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public PaymentTypeAccount transactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public PaymentTypeAccount transactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
        return this;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public PaymentTypeAccount transactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
        return this;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public BigDecimal getTransactionBalance() {
        return transactionBalance;
    }

    public PaymentTypeAccount transactionBalance(BigDecimal transactionBalance) {
        this.transactionBalance = transactionBalance;
        return this;
    }

    public void setTransactionBalance(BigDecimal transactionBalance) {
        this.transactionBalance = transactionBalance;
    }

    public Location getLocation() {
        return location;
    }

    public PaymentTypeAccount location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public PaymentTypeAccount transactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public PaymentTypes getPayType() {
        return payType;
    }

    public PaymentTypeAccount payType(PaymentTypes paymentTypes) {
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
        if (!(o instanceof PaymentTypeAccount)) {
            return false;
        }
        return id != null && id.equals(((PaymentTypeAccount) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PaymentTypeAccount{" +
            "id=" + getId() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", transactionDescription='" + getTransactionDescription() + "'" +
            ", transactionAmount=" + getTransactionAmount() +
            ", transactionBalance=" + getTransactionBalance() +
            "}";
    }
}
