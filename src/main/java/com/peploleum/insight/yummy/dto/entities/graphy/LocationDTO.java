package com.peploleum.insight.yummy.dto.entities.graphy;

public class LocationDTO {

    private String idMongo;
    private String name;

    public LocationDTO() {
    }

    public String getIdMongo() {
        return idMongo;
    }

    public void setIdMongo(String idMongo) {
        this.idMongo = idMongo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LocationDTO{" +
                "idMongo=" + getIdMongo() +
                ", name='" + getName() + "'" +
                "}";
    }
}
