package com.peploleum.insight.yummy.dto.source.elasticearch;

/**
 * Created by cpoullot on 18/01/2019.
 * contenu (Body) Json de la requete a envoyer a elasticearch
 */


public class EsQuery {
    private String content;

    public EsQuery() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
