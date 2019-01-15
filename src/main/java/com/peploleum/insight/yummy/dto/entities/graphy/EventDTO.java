package com.peploleum.insight.yummy.dto.entities.graphy;

public class EventDTO {

    private String idMongo;
    private String name;
    private String descrption;

    public EventDTO() {
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

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "idMongo=" + getIdMongo() +
                ", name='" + getName() + "'" +
                "}";
    }
}
