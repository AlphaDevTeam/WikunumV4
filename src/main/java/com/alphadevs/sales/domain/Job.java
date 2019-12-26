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
 * Job Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Job Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "job")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "job_code", nullable = false)
    private String jobCode;

    @NotNull
    @Column(name = "job_description", nullable = false)
    private String jobDescription;

    @NotNull
    @Column(name = "job_start_date", nullable = false)
    private LocalDate jobStartDate;

    @NotNull
    @Column(name = "job_end_date", nullable = false)
    private LocalDate jobEndDate;

    @NotNull
    @Column(name = "job_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal jobAmount;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private JobStatus status;

    @OneToOne
    @JoinColumn(unique = true)
    private DocumentHistory history;

    @OneToMany(mappedBy = "job")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<JobDetails> details = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("jobs")
    private Location location;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("jobs")
    private Customer customer;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "job_assigned_to",
               joinColumns = @JoinColumn(name = "job_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "assigned_to_id", referencedColumnName = "id"))
    private Set<Worker> assignedTos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobCode() {
        return jobCode;
    }

    public Job jobCode(String jobCode) {
        this.jobCode = jobCode;
        return this;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public Job jobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
        return this;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public LocalDate getJobStartDate() {
        return jobStartDate;
    }

    public Job jobStartDate(LocalDate jobStartDate) {
        this.jobStartDate = jobStartDate;
        return this;
    }

    public void setJobStartDate(LocalDate jobStartDate) {
        this.jobStartDate = jobStartDate;
    }

    public LocalDate getJobEndDate() {
        return jobEndDate;
    }

    public Job jobEndDate(LocalDate jobEndDate) {
        this.jobEndDate = jobEndDate;
        return this;
    }

    public void setJobEndDate(LocalDate jobEndDate) {
        this.jobEndDate = jobEndDate;
    }

    public BigDecimal getJobAmount() {
        return jobAmount;
    }

    public Job jobAmount(BigDecimal jobAmount) {
        this.jobAmount = jobAmount;
        return this;
    }

    public void setJobAmount(BigDecimal jobAmount) {
        this.jobAmount = jobAmount;
    }

    public JobStatus getStatus() {
        return status;
    }

    public Job status(JobStatus jobStatus) {
        this.status = jobStatus;
        return this;
    }

    public void setStatus(JobStatus jobStatus) {
        this.status = jobStatus;
    }

    public DocumentHistory getHistory() {
        return history;
    }

    public Job history(DocumentHistory documentHistory) {
        this.history = documentHistory;
        return this;
    }

    public void setHistory(DocumentHistory documentHistory) {
        this.history = documentHistory;
    }

    public Set<JobDetails> getDetails() {
        return details;
    }

    public Job details(Set<JobDetails> jobDetails) {
        this.details = jobDetails;
        return this;
    }

    public Job addDetails(JobDetails jobDetails) {
        this.details.add(jobDetails);
        jobDetails.setJob(this);
        return this;
    }

    public Job removeDetails(JobDetails jobDetails) {
        this.details.remove(jobDetails);
        jobDetails.setJob(null);
        return this;
    }

    public void setDetails(Set<JobDetails> jobDetails) {
        this.details = jobDetails;
    }

    public Location getLocation() {
        return location;
    }

    public Job location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Job customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<Worker> getAssignedTos() {
        return assignedTos;
    }

    public Job assignedTos(Set<Worker> workers) {
        this.assignedTos = workers;
        return this;
    }

    public Job addAssignedTo(Worker worker) {
        this.assignedTos.add(worker);
        worker.getJobs().add(this);
        return this;
    }

    public Job removeAssignedTo(Worker worker) {
        this.assignedTos.remove(worker);
        worker.getJobs().remove(this);
        return this;
    }

    public void setAssignedTos(Set<Worker> workers) {
        this.assignedTos = workers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Job)) {
            return false;
        }
        return id != null && id.equals(((Job) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Job{" +
            "id=" + getId() +
            ", jobCode='" + getJobCode() + "'" +
            ", jobDescription='" + getJobDescription() + "'" +
            ", jobStartDate='" + getJobStartDate() + "'" +
            ", jobEndDate='" + getJobEndDate() + "'" +
            ", jobAmount=" + getJobAmount() +
            "}";
    }
}
