package com.alphadevs.sales.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Worker Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Worker Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "worker")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Worker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "worker_code", nullable = false)
    private String workerCode;

    @NotNull
    @Column(name = "worker_name", nullable = false)
    private String workerName;

    @Column(name = "worker_limit", precision = 21, scale = 2)
    private BigDecimal workerLimit;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "rating")
    private Double rating;

    @OneToOne
    @JoinColumn(unique = true)
    private DocumentHistory history;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("workers")
    private Location location;

    @ManyToMany(mappedBy = "assignedTos")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Job> jobs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkerCode() {
        return workerCode;
    }

    public Worker workerCode(String workerCode) {
        this.workerCode = workerCode;
        return this;
    }

    public void setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
    }

    public String getWorkerName() {
        return workerName;
    }

    public Worker workerName(String workerName) {
        this.workerName = workerName;
        return this;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public BigDecimal getWorkerLimit() {
        return workerLimit;
    }

    public Worker workerLimit(BigDecimal workerLimit) {
        this.workerLimit = workerLimit;
        return this;
    }

    public void setWorkerLimit(BigDecimal workerLimit) {
        this.workerLimit = workerLimit;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Worker isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Double getRating() {
        return rating;
    }

    public Worker rating(Double rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public DocumentHistory getHistory() {
        return history;
    }

    public Worker history(DocumentHistory documentHistory) {
        this.history = documentHistory;
        return this;
    }

    public void setHistory(DocumentHistory documentHistory) {
        this.history = documentHistory;
    }

    public Location getLocation() {
        return location;
    }

    public Worker location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public Worker jobs(Set<Job> jobs) {
        this.jobs = jobs;
        return this;
    }

    public Worker addJobs(Job job) {
        this.jobs.add(job);
        job.getAssignedTos().add(this);
        return this;
    }

    public Worker removeJobs(Job job) {
        this.jobs.remove(job);
        job.getAssignedTos().remove(this);
        return this;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Worker)) {
            return false;
        }
        return id != null && id.equals(((Worker) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Worker{" +
            "id=" + getId() +
            ", workerCode='" + getWorkerCode() + "'" +
            ", workerName='" + getWorkerName() + "'" +
            ", workerLimit=" + getWorkerLimit() +
            ", isActive='" + isIsActive() + "'" +
            ", rating=" + getRating() +
            "}";
    }
}
