package com.alphadevs.sales.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Job Details Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Job Details Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "job_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
public class JobDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "job_item_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal jobItemPrice;

    @NotNull
    @Column(name = "job_item_qty", nullable = false)
    private Double jobItemQty;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("jobDetails")
    private Items item;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("details")
    private Job job;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getJobItemPrice() {
        return jobItemPrice;
    }

    public JobDetails jobItemPrice(BigDecimal jobItemPrice) {
        this.jobItemPrice = jobItemPrice;
        return this;
    }

    public void setJobItemPrice(BigDecimal jobItemPrice) {
        this.jobItemPrice = jobItemPrice;
    }

    public Double getJobItemQty() {
        return jobItemQty;
    }

    public JobDetails jobItemQty(Double jobItemQty) {
        this.jobItemQty = jobItemQty;
        return this;
    }

    public void setJobItemQty(Double jobItemQty) {
        this.jobItemQty = jobItemQty;
    }

    public Items getItem() {
        return item;
    }

    public JobDetails item(Items items) {
        this.item = items;
        return this;
    }

    public void setItem(Items items) {
        this.item = items;
    }

    public Job getJob() {
        return job;
    }

    public JobDetails job(Job job) {
        this.job = job;
        return this;
    }

    public void setJob(Job job) {
        this.job = job;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobDetails)) {
            return false;
        }
        return id != null && id.equals(((JobDetails) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "JobDetails{" +
            "id=" + getId() +
            ", jobItemPrice=" + getJobItemPrice() +
            ", jobItemQty=" + getJobItemQty() +
            "}";
    }
}
