package com.peploleum.insight.yummy.service.utils;

import com.peploleum.insight.yummy.dto.source.elasticSearch.EsResponse;
import com.peploleum.insight.yummy.dto.source.elasticSearch.EsSource;
import com.peploleum.insight.yummy.service.ElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by cpoullot on 21/01/2019.
 * classe pour interroger le reférentiel géographique via ElasticSearchService
 * et recuperer les coordonnées
 */
public class RefGeoUtils {
    private final static Logger log = LoggerFactory.getLogger(RefGeoUtils.class);

    /*   methode pour interroger le reférentiel géographique via ElasticSearchService
     et recuperer les coordonnées de locationName*/
    public static String getRefGeoCoordinates(String locationName, ElasticSearchService elasticSearchService) throws IOException {
        log.info("Getting coordinates for " + locationName);
        String coordinates = null;
        try {
            EsResponse reponse = elasticSearchService.submitElasticSearchGazetteerRequest(locationName);
            List<EsSource> sourceList = reponse.getSourceList();
            if (sourceList == null || sourceList.isEmpty()) {
                return null;
            }
            log.info("Found source list " + sourceList.isEmpty() + " " + sourceList.size());
            coordinates = sourceList.get(0).getLatitude() + "," + sourceList.get(0).getLongitude();

        } catch (Exception e) {
            log.error("Failed to get location for locationName " + locationName);
            log.error(e.getMessage(), e);
            throw e;
        }
        return coordinates;
    }
}
