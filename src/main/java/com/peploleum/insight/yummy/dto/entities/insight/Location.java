package com.peploleum.insight.yummy.dto.entities.insight;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Location entity.
 */
public class Location extends InsightEntity implements Serializable {

    private String id;

    @NotNull
    private String locationName;

    private LocationType locationType;

    private String locationCoordinates;

    private byte[] locationImage;
    private String locationImageContentType;

    private String locationSymbol;

    private String externalId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public String getLocationCoordinates() {
        return locationCoordinates;
    }

    public void setLocationCoordinates(String locationCoordinates) {
        this.locationCoordinates = locationCoordinates;
    }

    public byte[] getLocationImage() {
        return locationImage;
    }

    public void setLocationImage(byte[] locationImage) {
        this.locationImage = locationImage;
    }

    public String getLocationImageContentType() {
        return locationImageContentType;
    }

    public void setLocationImageContentType(String locationImageContentType) {
        this.locationImageContentType = locationImageContentType;
    }

    public String getLocationSymbol() {
        return locationSymbol;
    }

    public void setLocationSymbol(String locationSymbol) {
        this.locationSymbol = locationSymbol;
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

        Location location = (Location) o;
        if (location.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), location.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + getId() +
                ", locationName='" + getLocationName() + "'" +
                ", locationType='" + getLocationType() + "'" +
                ", locationCoordinates='" + getLocationCoordinates() + "'" +
                ", locationImage='" + getLocationImage() + "'" +
                ", locationSymbol='" + getLocationSymbol() + "'" +
                ", externalId='" + getExternalId() + "'" +
                "}";
    }
}
