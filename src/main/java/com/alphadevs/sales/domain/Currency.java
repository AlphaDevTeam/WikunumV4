package com.alphadevs.sales.domain;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Currency Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Currency Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "currency")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
public class Currency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "currency_code", nullable = false)
    private String currencyCode;

    @NotNull
    @Column(name = "currency_name", nullable = false)
    private String currencyName;

    @OneToOne
    @JoinColumn(unique = true)
    @NotAudited
    private DocumentHistory history;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public Currency currencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public Currency currencyName(String currencyName) {
        this.currencyName = currencyName;
        return this;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public DocumentHistory getHistory() {
        return history;
    }

    public Currency history(DocumentHistory documentHistory) {
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
        if (!(o instanceof Currency)) {
            return false;
        }
        return id != null && id.equals(((Currency) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Currency{" +
            "id=" + getId() +
            ", currencyCode='" + getCurrencyCode() + "'" +
            ", currencyName='" + getCurrencyName() + "'" +
            "}";
    }
}
