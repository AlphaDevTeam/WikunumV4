package com.alphadevs.sales.domain;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * StorageBin Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "StorageBin Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "storage_bin")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StorageBin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "bin_number", nullable = false)
    private String binNumber;

    @NotNull
    @Column(name = "bin_description", nullable = false)
    private String binDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBinNumber() {
        return binNumber;
    }

    public StorageBin binNumber(String binNumber) {
        this.binNumber = binNumber;
        return this;
    }

    public void setBinNumber(String binNumber) {
        this.binNumber = binNumber;
    }

    public String getBinDescription() {
        return binDescription;
    }

    public StorageBin binDescription(String binDescription) {
        this.binDescription = binDescription;
        return this;
    }

    public void setBinDescription(String binDescription) {
        this.binDescription = binDescription;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StorageBin)) {
            return false;
        }
        return id != null && id.equals(((StorageBin) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StorageBin{" +
            "id=" + getId() +
            ", binNumber='" + getBinNumber() + "'" +
            ", binDescription='" + getBinDescription() + "'" +
            "}";
    }
}
