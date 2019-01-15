package com.peploleum.insight.yummy.dto.entities.graphy;

public class BiographicsDTO {
    private String name;

    private String idMongo;

    public BiographicsDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdMongo() {
        return idMongo;
    }

    public void setIdMongo(String idMongo) {
        this.idMongo = idMongo;
    }

    @Override
    public String toString() {
        return "BiographicsDTO{" +
                "idMongo=" + getIdMongo() +
                ", name='" + getName() + "'" +
                "}";
    }
}
