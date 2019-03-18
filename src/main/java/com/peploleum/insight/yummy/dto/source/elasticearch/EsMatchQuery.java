package com.peploleum.insight.yummy.dto.source.elasticearch;

/**
 * Created by cpoullot on 21/01/2019.
 * requete Pour elastic Search avec un contenu spécifique "match'
 */
public class EsMatchQuery extends EsQuery {

    private String attrName;
    private String attrValue;

    public EsMatchQuery(String attrName, String attrValue) {
        this.attrName = attrName;
        this.attrValue = attrValue;
        updateContent();
    }

    public void updateContent() {
        setContent("{\"query\":{\"match\" : {\"" + attrName + "\":\"" + attrValue + "\"}}}");
    }

    public String getAttrName() {
        return attrName;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
        updateContent();
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
        updateContent();
    }

}

