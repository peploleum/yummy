package com.peploleum.insight.yummy.dto.entities.insight;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the RawData entity.
 */
public class RawDataDTO implements Serializable {

    private String id;

    @NotNull
    private String rawDataName;

    private String rawDataType;

    private String rawDataSubType;

    private String rawDataSourceName;

    private String rawDataSourceUri;

    private String rawDataSourceType;

    private String rawDataContent;

    private Instant rawDataCreationDate;

    private Instant rawDataExtractedDate;

    private String rawDataSymbol;

    private byte[] rawDataData;
    private String rawDataDataContentType;

    private String rawDataCoordinates;

    private String rawDataAnnotations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRawDataName() {
        return rawDataName;
    }

    public void setRawDataName(String rawDataName) {
        this.rawDataName = rawDataName;
    }

    public String getRawDataType() {
        return rawDataType;
    }

    public void setRawDataType(String rawDataType) {
        this.rawDataType = rawDataType;
    }

    public String getRawDataSubType() {
        return rawDataSubType;
    }

    public void setRawDataSubType(String rawDataSubType) {
        this.rawDataSubType = rawDataSubType;
    }

    public String getRawDataSourceName() {
        return rawDataSourceName;
    }

    public void setRawDataSourceName(String rawDataSourceName) {
        this.rawDataSourceName = rawDataSourceName;
    }

    public String getRawDataSourceUri() {
        return rawDataSourceUri;
    }

    public void setRawDataSourceUri(String rawDataSourceUri) {
        this.rawDataSourceUri = rawDataSourceUri;
    }

    public String getRawDataSourceType() {
        return rawDataSourceType;
    }

    public void setRawDataSourceType(String rawDataSourceType) {
        this.rawDataSourceType = rawDataSourceType;
    }

    public String getRawDataContent() {
        return rawDataContent;
    }

    public void setRawDataContent(String rawDataContent) {
        this.rawDataContent = rawDataContent;
    }

    public Instant getRawDataCreationDate() {
        return rawDataCreationDate;
    }

    public void setRawDataCreationDate(Instant rawDataCreationDate) {
        this.rawDataCreationDate = rawDataCreationDate;
    }

    public Instant getRawDataExtractedDate() {
        return rawDataExtractedDate;
    }

    public void setRawDataExtractedDate(Instant rawDataExtractedDate) {
        this.rawDataExtractedDate = rawDataExtractedDate;
    }

    public String getRawDataSymbol() {
        return rawDataSymbol;
    }

    public void setRawDataSymbol(String rawDataSymbol) {
        this.rawDataSymbol = rawDataSymbol;
    }

    public byte[] getRawDataData() {
        return rawDataData;
    }

    public void setRawDataData(byte[] rawDataData) {
        this.rawDataData = rawDataData;
    }

    public String getRawDataDataContentType() {
        return rawDataDataContentType;
    }

    public void setRawDataDataContentType(String rawDataDataContentType) {
        this.rawDataDataContentType = rawDataDataContentType;
    }

    public String getRawDataCoordinates() {
        return rawDataCoordinates;
    }

    public void setRawDataCoordinates(String rawDataCoordinates) {
        this.rawDataCoordinates = rawDataCoordinates;
    }

    public String getRawDataAnnotations() {
        return rawDataAnnotations;
    }

    public void setRawDataAnnotations(String rawDataAnnotations) {
        this.rawDataAnnotations = rawDataAnnotations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RawDataDTO rawDataDTO = (RawDataDTO) o;
        if (rawDataDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rawDataDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RawDataDTO{" +
            "id=" + getId() +
            ", rawDataName='" + getRawDataName() + "'" +
            ", rawDataType='" + getRawDataType() + "'" +
            ", rawDataSubType='" + getRawDataSubType() + "'" +
            ", rawDataSourceName='" + getRawDataSourceName() + "'" +
            ", rawDataSourceUri='" + getRawDataSourceUri() + "'" +
            ", rawDataSourceType='" + getRawDataSourceType() + "'" +
            ", rawDataContent='" + getRawDataContent() + "'" +
            ", rawDataCreationDate='" + getRawDataCreationDate() + "'" +
            ", rawDataExtractedDate='" + getRawDataExtractedDate() + "'" +
            ", rawDataSymbol='" + getRawDataSymbol() + "'" +
            ", rawDataData='" + getRawDataData() + "'" +
            ", rawDataCoordinates='" + getRawDataCoordinates() + "'" +
            ", rawDataAnnotations='" + getRawDataAnnotations() + "'" +
            "}";
    }
}
