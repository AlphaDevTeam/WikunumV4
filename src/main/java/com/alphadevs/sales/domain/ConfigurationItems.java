package com.alphadevs.sales.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * ConfigurationItems Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "ConfigurationItems Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "configuration_items")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
public class ConfigurationItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "config_code", nullable = false)
    private String configCode;

    @NotNull
    @Column(name = "config_description", nullable = false)
    private String configDescription;

    @Column(name = "config_enabled")
    private Boolean configEnabled;

    @Column(name = "config_paramter")
    private Double configParamter;

    @ManyToMany(mappedBy = "configitems")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Location> locations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigCode() {
        return configCode;
    }

    public ConfigurationItems configCode(String configCode) {
        this.configCode = configCode;
        return this;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public String getConfigDescription() {
        return configDescription;
    }

    public ConfigurationItems configDescription(String configDescription) {
        this.configDescription = configDescription;
        return this;
    }

    public void setConfigDescription(String configDescription) {
        this.configDescription = configDescription;
    }

    public Boolean isConfigEnabled() {
        return configEnabled;
    }

    public ConfigurationItems configEnabled(Boolean configEnabled) {
        this.configEnabled = configEnabled;
        return this;
    }

    public void setConfigEnabled(Boolean configEnabled) {
        this.configEnabled = configEnabled;
    }

    public Double getConfigParamter() {
        return configParamter;
    }

    public ConfigurationItems configParamter(Double configParamter) {
        this.configParamter = configParamter;
        return this;
    }

    public void setConfigParamter(Double configParamter) {
        this.configParamter = configParamter;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public ConfigurationItems locations(Set<Location> locations) {
        this.locations = locations;
        return this;
    }

    public ConfigurationItems addLocation(Location location) {
        this.locations.add(location);
        location.getConfigitems().add(this);
        return this;
    }

    public ConfigurationItems removeLocation(Location location) {
        this.locations.remove(location);
        location.getConfigitems().remove(this);
        return this;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigurationItems)) {
            return false;
        }
        return id != null && id.equals(((ConfigurationItems) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigurationItems{" +
            "id=" + getId() +
            ", configCode='" + getConfigCode() + "'" +
            ", configDescription='" + getConfigDescription() + "'" +
            ", configEnabled='" + isConfigEnabled() + "'" +
            ", configParamter=" + getConfigParamter() +
            "}";
    }
}
