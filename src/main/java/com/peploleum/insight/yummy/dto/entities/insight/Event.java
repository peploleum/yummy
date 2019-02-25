package com.peploleum.insight.yummy.dto.entities.insight;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the Event entity.
 */
public class Event implements Serializable {

    private String id;

    @NotNull
    private String eventName;

    private String eventDescription;

    private EventType eventType;

    private Instant eventDate;

    private String eventCoordinates;

    private byte[] eventImage;
    private String eventImageContentType;

    private String eventSymbol;

    private String externalId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Instant getEventDate() {
        return eventDate;
    }

    public void setEventDate(Instant eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventCoordinates() {
        return eventCoordinates;
    }

    public void setEventCoordinates(String eventCoordinates) {
        this.eventCoordinates = eventCoordinates;
    }

    public byte[] getEventImage() {
        return eventImage;
    }

    public void setEventImage(byte[] eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventImageContentType() {
        return eventImageContentType;
    }

    public void setEventImageContentType(String eventImageContentType) {
        this.eventImageContentType = eventImageContentType;
    }

    public String getEventSymbol() {
        return eventSymbol;
    }

    public void setEventSymbol(String eventSymbol) {
        this.eventSymbol = eventSymbol;
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

        Event event = (Event) o;
        if (event.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", eventName='" + getEventName() + "'" +
            ", eventDescription='" + getEventDescription() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", eventDate='" + getEventDate() + "'" +
            ", eventCoordinates='" + getEventCoordinates() + "'" +
            ", eventImage='" + getEventImage() + "'" +
            ", eventSymbol='" + getEventSymbol() + "'" +
            ", externalId='" + getExternalId() + "'" +
            "}";
    }
}
