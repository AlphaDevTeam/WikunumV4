package com.alphadevs.sales.domain;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * UnitOfMeasure Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "UnitOfMeasure Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "unit_of_measure")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UnitOfMeasure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "unit_of_measure_code", nullable = false)
    private String unitOfMeasureCode;

    @NotNull
    @Column(name = "unit_of_measure_description", nullable = false)
    private String unitOfMeasureDescription;

    @OneToOne
    @JoinColumn(unique = true)
    private DocumentHistory history;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitOfMeasureCode() {
        return unitOfMeasureCode;
    }

    public UnitOfMeasure unitOfMeasureCode(String unitOfMeasureCode) {
        this.unitOfMeasureCode = unitOfMeasureCode;
        return this;
    }

    public void setUnitOfMeasureCode(String unitOfMeasureCode) {
        this.unitOfMeasureCode = unitOfMeasureCode;
    }

    public String getUnitOfMeasureDescription() {
        return unitOfMeasureDescription;
    }

    public UnitOfMeasure unitOfMeasureDescription(String unitOfMeasureDescription) {
        this.unitOfMeasureDescription = unitOfMeasureDescription;
        return this;
    }

    public void setUnitOfMeasureDescription(String unitOfMeasureDescription) {
        this.unitOfMeasureDescription = unitOfMeasureDescription;
    }

    public DocumentHistory getHistory() {
        return history;
    }

    public UnitOfMeasure history(DocumentHistory documentHistory) {
        this.history = documentHistory;
        return this;
    }

    public void setHistory(DocumentHistory documentHistory) {
        this.history = documentHistory;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitOfMeasure)) {
            return false;
        }
        return id != null && id.equals(((UnitOfMeasure) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UnitOfMeasure{" +
            "id=" + getId() +
            ", unitOfMeasureCode='" + getUnitOfMeasureCode() + "'" +
            ", unitOfMeasureDescription='" + getUnitOfMeasureDescription() + "'" +
            "}";
    }
}
