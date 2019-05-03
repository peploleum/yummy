package com.peploleum.insight.yummy.dto.source.elasticearch;

/**
 * Created by cpoullot on 03/05/2019.
 */
public class EsTermExactQuery extends EsQuery {

    private String attrName;
    private String attrValue;

    public EsTermExactQuery(String attrName,String attrValue )
    {
        this.attrName = attrName;
        this.attrValue = attrValue;
        updateContent();
    }

    public void updateContent() {
        setContent("{\"query\":{\"constant_score\" : {\"filter\" : {\"bool\" : {\"must\" :[{\"term\" : {\"" + attrName + ".exact\":\"" + attrValue + "\"}}]}}}}}");
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