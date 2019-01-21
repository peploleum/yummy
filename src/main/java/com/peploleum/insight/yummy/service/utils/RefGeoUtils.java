package com.peploleum.insight.yummy.service.utils;

import com.peploleum.insight.yummy.dto.source.elasticSearch.EsResponse;
import com.peploleum.insight.yummy.dto.source.elasticSearch.EsSource;
import com.peploleum.insight.yummy.service.ElasticSearchService;

import java.util.List;

/**
 * Created by cpoullot on 21/01/2019.
 * classe pour interroger le reférentiel géographique via ElasticSearchService
 * et recuperer les coordonnées
 */
public class RefGeoUtils {

 /*   methode pour interroger le reférentiel géographique via ElasticSearchService
  et recuperer les coordonnées de locationName*/
    public static String getRefGeoCoordonates(String locationName, ElasticSearchService elasticSearchService) {
        String coordonates = null;
        try {
            EsResponse reponse = elasticSearchService.submitElasticSearchRequest("locationName");
            List<EsSource> sourceList = reponse.getSourceList();
            coordonates = sourceList.get(0).getLatitude() + "," + sourceList.get(0).getLongitude();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return coordonates;
    }
}
