package com.alphadevs.sales.domain;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Company LicenseType.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Company LicenseType.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "license_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LicenseType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "license_type_code", nullable = false)
    private String licenseTypeCode;

    @NotNull
    @Column(name = "license_type_name", nullable = false)
    private String licenseTypeName;

    @Column(name = "validity_days")
    private Double validityDays;

    @Column(name = "is_active")
    private Boolean isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicenseTypeCode() {
        return licenseTypeCode;
    }

    public LicenseType licenseTypeCode(String licenseTypeCode) {
        this.licenseTypeCode = licenseTypeCode;
        return this;
    }

    public void setLicenseTypeCode(String licenseTypeCode) {
        this.licenseTypeCode = licenseTypeCode;
    }

    public String getLicenseTypeName() {
        return licenseTypeName;
    }

    public LicenseType licenseTypeName(String licenseTypeName) {
        this.licenseTypeName = licenseTypeName;
        return this;
    }

    public void setLicenseTypeName(String licenseTypeName) {
        this.licenseTypeName = licenseTypeName;
    }

    public Double getValidityDays() {
        return validityDays;
    }

    public LicenseType validityDays(Double validityDays) {
        this.validityDays = validityDays;
        return this;
    }

    public void setValidityDays(Double validityDays) {
        this.validityDays = validityDays;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public LicenseType isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LicenseType)) {
            return false;
        }
        return id != null && id.equals(((LicenseType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "LicenseType{" +
            "id=" + getId() +
            ", licenseTypeCode='" + getLicenseTypeCode() + "'" +
            ", licenseTypeName='" + getLicenseTypeName() + "'" +
            ", validityDays=" + getValidityDays() +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
