package com.peploleum.insight.yummy.dto.entities.insight;

public class RelationDTO {

    private String idJanusSource;

    private String idJanusCible;

    private String name;

    private String typeSource;

    private String typeCible;

    public RelationDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdJanusSource() {
        return idJanusSource;
    }

    public void setIdJanusSource(String idJanusSource) {
        this.idJanusSource = idJanusSource;
    }

    public String getIdJanusCible() {
        return idJanusCible;
    }

    public void setIdJanusCible(String idJanusCible) {
        this.idJanusCible = idJanusCible;
    }

    public String getTypeSource() {
        return typeSource;
    }

    public void setTypeSource(String typeSource) {
        this.typeSource = typeSource;
    }

    public String getTypeCible() {
        return typeCible;
    }

    public void setTypeCible(String typeCible) {
        this.typeCible = typeCible;
    }

    @Override
    public String toString() {
        return "RawDataDTO{" +
                "idJanusSource=" + getIdJanusSource() +
                "idJanusCible=" + getIdJanusCible() +
                "typeSource=" + getTypeSource() +
                "typeCible=" + getTypeCible() +
                ", name='" + getName() + "'" +
                "}";
    }
}

