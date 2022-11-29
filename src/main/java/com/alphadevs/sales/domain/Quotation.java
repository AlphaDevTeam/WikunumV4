package com.alphadevs.sales.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Quotation Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Quotation Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "quotation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Quotation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "quotation_number", nullable = false)
    private String quotationNumber;

    @NotNull
    @Column(name = "quotation_date", nullable = false)
    private LocalDate quotationDate;

    @NotNull
    @Column(name = "quotationexpire_date", nullable = false)
    private LocalDate quotationexpireDate;

    @Column(name = "quotation_total_amount", precision = 21, scale = 2)
    private BigDecimal quotationTotalAmount;

    @NotNull
    @Column(name = "quotation_to", nullable = false)
    private String quotationTo;

    @NotNull
    @Column(name = "quotation_from", nullable = false)
    private String quotationFrom;

    @NotNull
    @Column(name = "project_number", nullable = false)
    private String projectNumber;

    @Column(name = "quotation_note")
    private String quotationNote;

    @OneToMany(mappedBy = "quote")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<QuotationDetails> details = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("quotations")
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuotationNumber() {
        return quotationNumber;
    }

    public Quotation quotationNumber(String quotationNumber) {
        this.quotationNumber = quotationNumber;
        return this;
    }

    public void setQuotationNumber(String quotationNumber) {
        this.quotationNumber = quotationNumber;
    }

    public LocalDate getQuotationDate() {
        return quotationDate;
    }

    public Quotation quotationDate(LocalDate quotationDate) {
        this.quotationDate = quotationDate;
        return this;
    }

    public void setQuotationDate(LocalDate quotationDate) {
        this.quotationDate = quotationDate;
    }

    public LocalDate getQuotationexpireDate() {
        return quotationexpireDate;
    }

    public Quotation quotationexpireDate(LocalDate quotationexpireDate) {
        this.quotationexpireDate = quotationexpireDate;
        return this;
    }

    public void setQuotationexpireDate(LocalDate quotationexpireDate) {
        this.quotationexpireDate = quotationexpireDate;
    }

    public BigDecimal getQuotationTotalAmount() {
        return quotationTotalAmount;
    }

    public Quotation quotationTotalAmount(BigDecimal quotationTotalAmount) {
        this.quotationTotalAmount = quotationTotalAmount;
        return this;
    }

    public void setQuotationTotalAmount(BigDecimal quotationTotalAmount) {
        this.quotationTotalAmount = quotationTotalAmount;
    }

    public String getQuotationTo() {
        return quotationTo;
    }

    public Quotation quotationTo(String quotationTo) {
        this.quotationTo = quotationTo;
        return this;
    }

    public void setQuotationTo(String quotationTo) {
        this.quotationTo = quotationTo;
    }

    public String getQuotationFrom() {
        return quotationFrom;
    }

    public Quotation quotationFrom(String quotationFrom) {
        this.quotationFrom = quotationFrom;
        return this;
    }

    public void setQuotationFrom(String quotationFrom) {
        this.quotationFrom = quotationFrom;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public Quotation projectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
        return this;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getQuotationNote() {
        return quotationNote;
    }

    public Quotation quotationNote(String quotationNote) {
        this.quotationNote = quotationNote;
        return this;
    }

    public void setQuotationNote(String quotationNote) {
        this.quotationNote = quotationNote;
    }

    public Set<QuotationDetails> getDetails() {
        return details;
    }

    public Quotation details(Set<QuotationDetails> quotationDetails) {
        this.details = quotationDetails;
        return this;
    }

    public Quotation addDetails(QuotationDetails quotationDetails) {
        this.details.add(quotationDetails);
        quotationDetails.setQuote(this);
        return this;
    }

    public Quotation removeDetails(QuotationDetails quotationDetails) {
        this.details.remove(quotationDetails);
        quotationDetails.setQuote(null);
        return this;
    }

    public void setDetails(Set<QuotationDetails> quotationDetails) {
        this.details = quotationDetails;
    }

    public Location getLocation() {
        return location;
    }

    public Quotation location(Location location) {
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
        if (!(o instanceof Quotation)) {
            return false;
        }
        return id != null && id.equals(((Quotation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Quotation{" +
            "id=" + getId() +
            ", quotationNumber='" + getQuotationNumber() + "'" +
            ", quotationDate='" + getQuotationDate() + "'" +
            ", quotationexpireDate='" + getQuotationexpireDate() + "'" +
            ", quotationTotalAmount=" + getQuotationTotalAmount() +
            ", quotationTo='" + getQuotationTo() + "'" +
            ", quotationFrom='" + getQuotationFrom() + "'" +
            ", projectNumber='" + getProjectNumber() + "'" +
            ", quotationNote='" + getQuotationNote() + "'" +
            "}";
    }
}
