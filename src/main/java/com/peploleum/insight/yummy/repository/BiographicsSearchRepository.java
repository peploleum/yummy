package com.peploleum.insight.yummy.repository;

import com.peploleum.insight.yummy.dto.entities.insight.Biographics;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Elastic Repository for Biographic
 */
public interface BiographicsSearchRepository extends ElasticsearchRepository<Biographics, String> {
}
