package com.alphadevs.sales.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * PaymentTypes Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "PaymentTypes Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "payment_types")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PaymentTypes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "payment_types_code", nullable = false)
    private String paymentTypesCode;

    @NotNull
    @Column(name = "payment_types", nullable = false)
    private String paymentTypes;

    @Column(name = "payment_types_charge_per")
    private Double paymentTypesChargePer;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("paymentTypes")
    private Location location;

    @ManyToOne
    @JsonIgnoreProperties("payTypes")
    private Invoice invicePay;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentTypesCode() {
        return paymentTypesCode;
    }

    public PaymentTypes paymentTypesCode(String paymentTypesCode) {
        this.paymentTypesCode = paymentTypesCode;
        return this;
    }

    public void setPaymentTypesCode(String paymentTypesCode) {
        this.paymentTypesCode = paymentTypesCode;
    }

    public String getPaymentTypes() {
        return paymentTypes;
    }

    public PaymentTypes paymentTypes(String paymentTypes) {
        this.paymentTypes = paymentTypes;
        return this;
    }

    public void setPaymentTypes(String paymentTypes) {
        this.paymentTypes = paymentTypes;
    }

    public Double getPaymentTypesChargePer() {
        return paymentTypesChargePer;
    }

    public PaymentTypes paymentTypesChargePer(Double paymentTypesChargePer) {
        this.paymentTypesChargePer = paymentTypesChargePer;
        return this;
    }

    public void setPaymentTypesChargePer(Double paymentTypesChargePer) {
        this.paymentTypesChargePer = paymentTypesChargePer;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public PaymentTypes isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Location getLocation() {
        return location;
    }

    public PaymentTypes location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Invoice getInvicePay() {
        return invicePay;
    }

    public PaymentTypes invicePay(Invoice invoice) {
        this.invicePay = invoice;
        return this;
    }

    public void setInvicePay(Invoice invoice) {
        this.invicePay = invoice;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentTypes)) {
            return false;
        }
        return id != null && id.equals(((PaymentTypes) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PaymentTypes{" +
            "id=" + getId() +
            ", paymentTypesCode='" + getPaymentTypesCode() + "'" +
            ", paymentTypes='" + getPaymentTypes() + "'" +
            ", paymentTypesChargePer=" + getPaymentTypesChargePer() +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
