package com.peploleum.insight.yummy.dto.entities.insight;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Organisation entity.
 */
@org.springframework.data.elasticsearch.annotations.Document(indexName = "organisation", createIndex = false, useServerConfiguration = true)
public class Organisation implements Serializable {

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @NotNull
    private String organisationName;

    private String organisationDescrption;

    private Size organisationSize;

    private String organisationCoordinates;

    private byte[] organisationImage;
    private String organisationImageContentType;

    private String organisationSymbol;

    private String externalId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getOrganisationDescrption() {
        return organisationDescrption;
    }

    public void setOrganisationDescrption(String organisationDescrption) {
        this.organisationDescrption = organisationDescrption;
    }

    public Size getOrganisationSize() {
        return organisationSize;
    }

    public void setOrganisationSize(Size organisationSize) {
        this.organisationSize = organisationSize;
    }

    public String getOrganisationCoordinates() {
        return organisationCoordinates;
    }

    public void setOrganisationCoordinates(String organisationCoordinates) {
        this.organisationCoordinates = organisationCoordinates;
    }

    public byte[] getOrganisationImage() {
        return organisationImage;
    }

    public void setOrganisationImage(byte[] organisationImage) {
        this.organisationImage = organisationImage;
    }

    public String getOrganisationImageContentType() {
        return organisationImageContentType;
    }

    public void setOrganisationImageContentType(String organisationImageContentType) {
        this.organisationImageContentType = organisationImageContentType;
    }

    public String getOrganisationSymbol() {
        return organisationSymbol;
    }

    public void setOrganisationSymbol(String organisationSymbol) {
        this.organisationSymbol = organisationSymbol;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Organisation organisation = (Organisation) o;
        if (organisation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organisation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Organisation{" +
                "id=" + getId() +
                ", organisationName='" + getOrganisationName() + "'" +
                ", organisationDescrption='" + getOrganisationDescrption() + "'" +
                ", organisationSize='" + getOrganisationSize() + "'" +
                ", organisationCoordinates='" + getOrganisationCoordinates() + "'" +
                ", organisationImage='" + getOrganisationImage() + "'" +
                ", organisationSymbol='" + getOrganisationSymbol() + "'" +
                ", externalId='" + getExternalId() + "'" +
                "}";
    }
}
