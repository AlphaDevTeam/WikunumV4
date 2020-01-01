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
 * Expense Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Expense Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "expense")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
public class Expense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "expense_code", nullable = false)
    private String expenseCode;

    @NotNull
    @Column(name = "expense_name", nullable = false)
    private String expenseName;

    @NotNull
    @Column(name = "expense_limit", precision = 21, scale = 2, nullable = false)
    private BigDecimal expenseLimit;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("expenses")
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExpenseCode() {
        return expenseCode;
    }

    public Expense expenseCode(String expenseCode) {
        this.expenseCode = expenseCode;
        return this;
    }

    public void setExpenseCode(String expenseCode) {
        this.expenseCode = expenseCode;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public Expense expenseName(String expenseName) {
        this.expenseName = expenseName;
        return this;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public BigDecimal getExpenseLimit() {
        return expenseLimit;
    }

    public Expense expenseLimit(BigDecimal expenseLimit) {
        this.expenseLimit = expenseLimit;
        return this;
    }

    public void setExpenseLimit(BigDecimal expenseLimit) {
        this.expenseLimit = expenseLimit;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Expense isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Location getLocation() {
        return location;
    }

    public Expense location(Location location) {
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
        if (!(o instanceof Expense)) {
            return false;
        }
        return id != null && id.equals(((Expense) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Expense{" +
            "id=" + getId() +
            ", expenseCode='" + getExpenseCode() + "'" +
            ", expenseName='" + getExpenseName() + "'" +
            ", expenseLimit=" + getExpenseLimit() +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
