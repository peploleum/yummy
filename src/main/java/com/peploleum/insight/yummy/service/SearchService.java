package com.peploleum.insight.yummy.service;

import com.peploleum.insight.yummy.dto.entities.insight.Biographics;
import com.peploleum.insight.yummy.dto.entities.insight.Organisation;
import com.peploleum.insight.yummy.repository.BiographicsSearchRepository;
import com.peploleum.insight.yummy.repository.OrganisationSearchRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Main search services with all searchRepository
 */
@Service
public class SearchService {
    private BiographicsSearchRepository biographicsSearchRepository;
    private OrganisationSearchRepository organisationSearchRepository;

    public SearchService(BiographicsSearchRepository biographicsSearchRepository, OrganisationSearchRepository organisationSearchRepository) {
        this.biographicsSearchRepository = biographicsSearchRepository;
        this.organisationSearchRepository = organisationSearchRepository;
    }

    public Object searchObjectByName(Object o) throws IllegalAccessException {
        ArrayList<Object> searchResult = new ArrayList<>();

        ElasticsearchRepository searchRepo = getRepositoryFromClass(o);

        String fieldName = getFieldName(o);
        searchRepo.search(queryStringQuery(fieldName + " : " + getFieldValue(o, fieldName))).forEach(x -> {
            if (x != null)
                searchResult.add(x);
        });

        if (searchResult.isEmpty())
            return o;
        else
            return searchResult.get(0);
    }

    public void save(Object o) {
        if (o instanceof Biographics)
            this.biographicsSearchRepository.save((Biographics) o);
    }

    public void delete(Object o) {
        if (o instanceof Biographics)
            this.biographicsSearchRepository.delete((Biographics) o);
    }

    private ElasticsearchRepository getRepositoryFromClass(Object o) {
        if (Biographics.class.equals(o.getClass()))
            return this.biographicsSearchRepository;
        else if (Organisation.class.equals(o.getClass()))
            return this.organisationSearchRepository;

        return null;
    }

    private static String getFieldName(Object dto) {
        String nameField = dto.getClass().getSimpleName() + "Name";
        return Introspector.decapitalize(nameField);
    }

    private static String getFieldValue(Object dto, String fieldName) throws IllegalAccessException {
        Field field = org.springframework.util.ReflectionUtils.findField(dto.getClass(), fieldName);
        org.springframework.util.ReflectionUtils.makeAccessible(field);
        return field.get(dto).toString();
    }
}
