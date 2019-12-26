package com.alphadevs.sales.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * DocumentHistory Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "DocumentHistory Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "document_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DocumentHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "history_description", nullable = false)
    private String historyDescription;

    @NotNull
    @Column(name = "history_date", nullable = false)
    private ZonedDateTime historyDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("documentHistories")
    private DocumentType type;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("documentHistories")
    private ExUser lastModifiedUser;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("documentHistories")
    private ExUser createdUser;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "document_history_change_log",
               joinColumns = @JoinColumn(name = "document_history_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "change_log_id", referencedColumnName = "id"))
    private Set<ChangeLog> changeLogs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHistoryDescription() {
        return historyDescription;
    }

    public DocumentHistory historyDescription(String historyDescription) {
        this.historyDescription = historyDescription;
        return this;
    }

    public void setHistoryDescription(String historyDescription) {
        this.historyDescription = historyDescription;
    }

    public ZonedDateTime getHistoryDate() {
        return historyDate;
    }

    public DocumentHistory historyDate(ZonedDateTime historyDate) {
        this.historyDate = historyDate;
        return this;
    }

    public void setHistoryDate(ZonedDateTime historyDate) {
        this.historyDate = historyDate;
    }

    public DocumentType getType() {
        return type;
    }

    public DocumentHistory type(DocumentType documentType) {
        this.type = documentType;
        return this;
    }

    public void setType(DocumentType documentType) {
        this.type = documentType;
    }

    public ExUser getLastModifiedUser() {
        return lastModifiedUser;
    }

    public DocumentHistory lastModifiedUser(ExUser exUser) {
        this.lastModifiedUser = exUser;
        return this;
    }

    public void setLastModifiedUser(ExUser exUser) {
        this.lastModifiedUser = exUser;
    }

    public ExUser getCreatedUser() {
        return createdUser;
    }

    public DocumentHistory createdUser(ExUser exUser) {
        this.createdUser = exUser;
        return this;
    }

    public void setCreatedUser(ExUser exUser) {
        this.createdUser = exUser;
    }

    public Set<ChangeLog> getChangeLogs() {
        return changeLogs;
    }

    public DocumentHistory changeLogs(Set<ChangeLog> changeLogs) {
        this.changeLogs = changeLogs;
        return this;
    }

    public DocumentHistory addChangeLog(ChangeLog changeLog) {
        this.changeLogs.add(changeLog);
        changeLog.getDocumentHistories().add(this);
        return this;
    }

    public DocumentHistory removeChangeLog(ChangeLog changeLog) {
        this.changeLogs.remove(changeLog);
        changeLog.getDocumentHistories().remove(this);
        return this;
    }

    public void setChangeLogs(Set<ChangeLog> changeLogs) {
        this.changeLogs = changeLogs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentHistory)) {
            return false;
        }
        return id != null && id.equals(((DocumentHistory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DocumentHistory{" +
            "id=" + getId() +
            ", historyDescription='" + getHistoryDescription() + "'" +
            ", historyDate='" + getHistoryDate() + "'" +
            "}";
    }
}
