package com.peploleum.insight.yummy.dto.entities.graphy;

public class RawDataDTO {
    private String idMongo;
    private String name;
    private String type;

    public RawDataDTO() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RawDataDTO{" +
                "idMongo=" + getIdMongo() +
                ", name='" + getName() + "'" +
                "}";
    }
}
