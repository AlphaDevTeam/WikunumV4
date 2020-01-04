package com.alphadevs.sales.domain;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Worker EmployeeType.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Worker EmployeeType.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "employee_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EmployeeType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "employee_type_code", nullable = false)
    private String employeeTypeCode;

    @NotNull
    @Column(name = "employee_type_name", nullable = false)
    private String employeeTypeName;

    @Column(name = "is_active")
    private Boolean isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeTypeCode() {
        return employeeTypeCode;
    }

    public EmployeeType employeeTypeCode(String employeeTypeCode) {
        this.employeeTypeCode = employeeTypeCode;
        return this;
    }

    public void setEmployeeTypeCode(String employeeTypeCode) {
        this.employeeTypeCode = employeeTypeCode;
    }

    public String getEmployeeTypeName() {
        return employeeTypeName;
    }

    public EmployeeType employeeTypeName(String employeeTypeName) {
        this.employeeTypeName = employeeTypeName;
        return this;
    }

    public void setEmployeeTypeName(String employeeTypeName) {
        this.employeeTypeName = employeeTypeName;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public EmployeeType isActive(Boolean isActive) {
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
        if (!(o instanceof EmployeeType)) {
            return false;
        }
        return id != null && id.equals(((EmployeeType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EmployeeType{" +
            "id=" + getId() +
            ", employeeTypeCode='" + getEmployeeTypeCode() + "'" +
            ", employeeTypeName='" + getEmployeeTypeName() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
