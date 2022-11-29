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

/**
 * Supplier Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Supplier Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "supplier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "supplier_code", nullable = false)
    private String supplierCode;

    @NotNull
    @Column(name = "supplier_name", nullable = false)
    private String supplierName;

    @NotNull
    @Column(name = "supplier_credit_limit", precision = 21, scale = 2, nullable = false)
    private BigDecimal supplierCreditLimit;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Lob
    @Column(name = "image")
    @NotAudited
    private byte[] image;

    @Column(name = "image_content_type")
    @NotAudited
    private String imageContentType;

    @OneToOne
    @JoinColumn(unique = true)
    @NotAudited
    private DocumentHistory history;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("suppliers")
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public Supplier supplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
        return this;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public Supplier supplierName(String supplierName) {
        this.supplierName = supplierName;
        return this;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public BigDecimal getSupplierCreditLimit() {
        return supplierCreditLimit;
    }

    public Supplier supplierCreditLimit(BigDecimal supplierCreditLimit) {
        this.supplierCreditLimit = supplierCreditLimit;
        return this;
    }

    public void setSupplierCreditLimit(BigDecimal supplierCreditLimit) {
        this.supplierCreditLimit = supplierCreditLimit;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Supplier isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Double getRating() {
        return rating;
    }

    public Supplier rating(Double rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getPhone() {
        return phone;
    }

    public Supplier phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public Supplier addressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        return this;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public Supplier addressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        return this;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public Supplier city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public Supplier country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public byte[] getImage() {
        return image;
    }

    public Supplier image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Supplier imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public DocumentHistory getHistory() {
        return history;
    }

    public Supplier history(DocumentHistory documentHistory) {
        this.history = documentHistory;
        return this;
    }

    public void setHistory(DocumentHistory documentHistory) {
        this.history = documentHistory;
    }

    public Location getLocation() {
        return location;
    }

    public Supplier location(Location location) {
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
        if (!(o instanceof Supplier)) {
            return false;
        }
        return id != null && id.equals(((Supplier) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Supplier{" +
            "id=" + getId() +
            ", supplierCode='" + getSupplierCode() + "'" +
            ", supplierName='" + getSupplierName() + "'" +
            ", supplierCreditLimit=" + getSupplierCreditLimit() +
            ", isActive='" + isIsActive() + "'" +
            ", rating=" + getRating() +
            ", phone='" + getPhone() + "'" +
            ", addressLine1='" + getAddressLine1() + "'" +
            ", addressLine2='" + getAddressLine2() + "'" +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
