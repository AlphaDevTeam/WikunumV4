package com.alphadevs.sales.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.alphadevs.sales.domain.ConfigurationItems} entity. This class is used
 * in {@link com.alphadevs.sales.web.rest.ConfigurationItemsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /configuration-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ConfigurationItemsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter configCode;

    private StringFilter configDescription;

    private BooleanFilter configEnabled;

    private DoubleFilter configParamter;

    private LongFilter locationId;

    public ConfigurationItemsCriteria(){
    }

    public ConfigurationItemsCriteria(ConfigurationItemsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.configCode = other.configCode == null ? null : other.configCode.copy();
        this.configDescription = other.configDescription == null ? null : other.configDescription.copy();
        this.configEnabled = other.configEnabled == null ? null : other.configEnabled.copy();
        this.configParamter = other.configParamter == null ? null : other.configParamter.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public ConfigurationItemsCriteria copy() {
        return new ConfigurationItemsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getConfigCode() {
        return configCode;
    }

    public void setConfigCode(StringFilter configCode) {
        this.configCode = configCode;
    }

    public StringFilter getConfigDescription() {
        return configDescription;
    }

    public void setConfigDescription(StringFilter configDescription) {
        this.configDescription = configDescription;
    }

    public BooleanFilter getConfigEnabled() {
        return configEnabled;
    }

    public void setConfigEnabled(BooleanFilter configEnabled) {
        this.configEnabled = configEnabled;
    }

    public DoubleFilter getConfigParamter() {
        return configParamter;
    }

    public void setConfigParamter(DoubleFilter configParamter) {
        this.configParamter = configParamter;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ConfigurationItemsCriteria that = (ConfigurationItemsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(configCode, that.configCode) &&
            Objects.equals(configDescription, that.configDescription) &&
            Objects.equals(configEnabled, that.configEnabled) &&
            Objects.equals(configParamter, that.configParamter) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        configCode,
        configDescription,
        configEnabled,
        configParamter,
        locationId
        );
    }

    @Override
    public String toString() {
        return "ConfigurationItemsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (configCode != null ? "configCode=" + configCode + ", " : "") +
                (configDescription != null ? "configDescription=" + configDescription + ", " : "") +
                (configEnabled != null ? "configEnabled=" + configEnabled + ", " : "") +
                (configParamter != null ? "configParamter=" + configParamter + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
