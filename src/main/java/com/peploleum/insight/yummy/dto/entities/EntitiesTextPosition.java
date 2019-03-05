package com.peploleum.insight.yummy.dto.entities;

import java.util.List;

/**
 * Created by gFolgoas on 04/03/2019.
 */
public class EntitiesTextPosition {
    private Integer entitiesPosition;
    private String entityRefTerm;

    public Integer getEntitiesPosition() {
        return entitiesPosition;
    }

    public void setEntitiesPosition(Integer entitiesPosition) {
        this.entitiesPosition = entitiesPosition;
    }

    public String getEntityRefTerm() {
        return entityRefTerm;
    }

    public void setEntityRefTerm(String entityRefTerm) {
        this.entityRefTerm = entityRefTerm;
    }
}
