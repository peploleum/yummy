package com.peploleum.insight.yummy;

import com.peploleum.insight.yummy.dto.source.elasticSearch.EsResponse;
import com.peploleum.insight.yummy.dto.source.elasticSearch.EsSource;
import com.peploleum.insight.yummy.service.ElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class YummyApplication {


    public static void main(String[] args) {

       SpringApplication.run(YummyApplication.class, args);

    }
}
