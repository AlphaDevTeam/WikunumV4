package com.alphadevs.sales.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * DocumentNumberConfig Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "DocumentNumberConfig Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "document_number_config")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DocumentNumberConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "document_prefix", nullable = false)
    private String documentPrefix;

    @Column(name = "document_postfix")
    private String documentPostfix;

    @NotNull
    @Column(name = "current_number", nullable = false)
    private Double currentNumber;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("documentNumberConfigs")
    private DocumentType document;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("documentNumberConfigs")
    private Location location;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("documentNumberConfigs")
    private TransactionType transactionType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentPrefix() {
        return documentPrefix;
    }

    public DocumentNumberConfig documentPrefix(String documentPrefix) {
        this.documentPrefix = documentPrefix;
        return this;
    }

    public void setDocumentPrefix(String documentPrefix) {
        this.documentPrefix = documentPrefix;
    }

    public String getDocumentPostfix() {
        return documentPostfix;
    }

    public DocumentNumberConfig documentPostfix(String documentPostfix) {
        this.documentPostfix = documentPostfix;
        return this;
    }

    public void setDocumentPostfix(String documentPostfix) {
        this.documentPostfix = documentPostfix;
    }

    public Double getCurrentNumber() {
        return currentNumber;
    }

    public DocumentNumberConfig currentNumber(Double currentNumber) {
        this.currentNumber = currentNumber;
        return this;
    }

    public void setCurrentNumber(Double currentNumber) {
        this.currentNumber = currentNumber;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public DocumentNumberConfig isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public DocumentType getDocument() {
        return document;
    }

    public DocumentNumberConfig document(DocumentType documentType) {
        this.document = documentType;
        return this;
    }

    public void setDocument(DocumentType documentType) {
        this.document = documentType;
    }

    public Location getLocation() {
        return location;
    }

    public DocumentNumberConfig location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public DocumentNumberConfig transactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentNumberConfig)) {
            return false;
        }
        return id != null && id.equals(((DocumentNumberConfig) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DocumentNumberConfig{" +
            "id=" + getId() +
            ", documentPrefix='" + getDocumentPrefix() + "'" +
            ", documentPostfix='" + getDocumentPostfix() + "'" +
            ", currentNumber=" + getCurrentNumber() +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
