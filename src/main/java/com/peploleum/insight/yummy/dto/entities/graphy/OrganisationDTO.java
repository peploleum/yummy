package com.peploleum.insight.yummy.dto.entities.graphy;

public class OrganisationDTO {

    private String idMongo;
    private String name;
    private String description;

    public OrganisationDTO() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "OrganisationDTO{" +
                "idMongo=" + getIdMongo() +
                ", name='" + getName() + "'" +
                "}";
    }
}
