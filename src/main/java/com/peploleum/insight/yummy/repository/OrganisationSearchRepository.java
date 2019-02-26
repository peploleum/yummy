package com.peploleum.insight.yummy.repository;

import com.peploleum.insight.yummy.dto.entities.insight.Organisation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Elastic Repository for Organisation
 */
public interface OrganisationSearchRepository extends ElasticsearchRepository<Organisation, String> {
}
