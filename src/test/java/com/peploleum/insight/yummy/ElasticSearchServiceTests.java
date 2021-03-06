package com.peploleum.insight.yummy;

import com.peploleum.insight.yummy.dto.source.elasticearch.EsHit;
import com.peploleum.insight.yummy.dto.source.elasticearch.EsResponse;
import com.peploleum.insight.yummy.service.ElasticSearchService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticSearchServiceTests {

    private final Logger log = LoggerFactory.getLogger(ElasticSearchServiceTests.class);

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Test
    public void getBiographicsByName() {
        this.log.info("searching by name criteria");
        final EsResponse result = this.elasticSearchService.getByNameCriteria("biographicsName", "Donald Trump", "biographics");
        final EsHit esHit = result.getHits().getHits().stream().findFirst().get();
        Assert.assertNotNull(esHit);
        Assert.assertNotNull(esHit.getSource().getAdditionalProperties().get("externalId"));
        Assert.assertEquals("biographics", esHit.getType());
    }

    @Test
    public void getLocation()
    {
        this.log.info("search location");
        try {
            final EsResponse result = this.elasticSearchService.submitElasticSearchGazetteerRequest("Paris");
            this.log.info(result.toString());
        }catch (Exception e)
        {
            this.log.info(e.getMessage());
            e.printStackTrace();
        }

    }

    @Test
    public void instantTest() {
        this.log.info(Instant.now().toString());
    }
}
