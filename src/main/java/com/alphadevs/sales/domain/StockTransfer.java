package com.alphadevs.sales.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * StockTransfer Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "StockTransfer Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "stock_transfer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockTransfer implements Serializable {

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
    @Column(name = "transaction_qty", nullable = false)
    private Double transactionQty;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("stockTransfers")
    private Items item;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("stockTransfers")
    private Location locationFrom;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("stockTransfers")
    private Location locationTo;

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

    public StockTransfer transactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
        return this;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public StockTransfer transactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public StockTransfer transactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
        return this;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public Double getTransactionQty() {
        return transactionQty;
    }

    public StockTransfer transactionQty(Double transactionQty) {
        this.transactionQty = transactionQty;
        return this;
    }

    public void setTransactionQty(Double transactionQty) {
        this.transactionQty = transactionQty;
    }

    public Items getItem() {
        return item;
    }

    public StockTransfer item(Items items) {
        this.item = items;
        return this;
    }

    public void setItem(Items items) {
        this.item = items;
    }

    public Location getLocationFrom() {
        return locationFrom;
    }

    public StockTransfer locationFrom(Location location) {
        this.locationFrom = location;
        return this;
    }

    public void setLocationFrom(Location location) {
        this.locationFrom = location;
    }

    public Location getLocationTo() {
        return locationTo;
    }

    public StockTransfer locationTo(Location location) {
        this.locationTo = location;
        return this;
    }

    public void setLocationTo(Location location) {
        this.locationTo = location;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockTransfer)) {
            return false;
        }
        return id != null && id.equals(((StockTransfer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StockTransfer{" +
            "id=" + getId() +
            ", transactionNumber='" + getTransactionNumber() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", transactionDescription='" + getTransactionDescription() + "'" +
            ", transactionQty=" + getTransactionQty() +
            "}";
    }
}
