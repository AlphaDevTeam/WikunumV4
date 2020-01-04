package com.alphadevs.sales.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Quotation Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Quotation Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "quotation_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class QuotationDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "rate", nullable = false)
    private Double rate;

    @Column(name = "description")
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("quotationDetails")
    private Items item;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("details")
    private Quotation quote;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRate() {
        return rate;
    }

    public QuotationDetails rate(Double rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public QuotationDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Items getItem() {
        return item;
    }

    public QuotationDetails item(Items items) {
        this.item = items;
        return this;
    }

    public void setItem(Items items) {
        this.item = items;
    }

    public Quotation getQuote() {
        return quote;
    }

    public QuotationDetails quote(Quotation quotation) {
        this.quote = quotation;
        return this;
    }

    public void setQuote(Quotation quotation) {
        this.quote = quotation;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuotationDetails)) {
            return false;
        }
        return id != null && id.equals(((QuotationDetails) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "QuotationDetails{" +
            "id=" + getId() +
            ", rate=" + getRate() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
