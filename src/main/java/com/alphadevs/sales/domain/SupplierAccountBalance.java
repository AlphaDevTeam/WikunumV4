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
 * SupplierAccount Balance Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "SupplierAccount Balance Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "supplier_account_balance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SupplierAccountBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "balance", precision = 21, scale = 2, nullable = false)
    private BigDecimal balance;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("supplierAccountBalances")
    private Supplier supplier;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("supplierAccountBalances")
    private Location location;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("supplierAccountBalances")
    private TransactionType transactionType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public SupplierAccountBalance balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public SupplierAccountBalance supplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Location getLocation() {
        return location;
    }

    public SupplierAccountBalance location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public SupplierAccountBalance transactionType(TransactionType transactionType) {
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
        if (!(o instanceof SupplierAccountBalance)) {
            return false;
        }
        return id != null && id.equals(((SupplierAccountBalance) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SupplierAccountBalance{" +
            "id=" + getId() +
            ", balance=" + getBalance() +
            "}";
    }
}
