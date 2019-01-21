package com.peploleum.insight.yummy.dto.source.elasticSearch;

/**
 * Created by cpoullot on 18/01/2019.
 * contenu (Body) Json de la requete a envoyer a elasticSearch
 */


public class EsQuery {


    private String content;

    public EsQuery(){

    }
    public EsQuery(String content){
        this.content=content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
