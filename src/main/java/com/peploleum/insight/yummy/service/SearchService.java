package com.peploleum.insight.yummy.service;

import com.peploleum.insight.yummy.dto.entities.insight.Biographics;
import com.peploleum.insight.yummy.repository.BiographicsSearchRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Main search services with all searchRepository
 */
@Service
public class SearchService {
    BiographicsSearchRepository biographicsSearchRepository;

    public SearchService(BiographicsSearchRepository biographicsSearchRepository) {
        this.biographicsSearchRepository = biographicsSearchRepository;
    }

    public Object searchObjectByName(Object o) {
        ArrayList<Object> searchResult = new ArrayList<>();
        if (o instanceof Biographics) {
            Iterable<Biographics> iterable = biographicsSearchRepository.search(queryStringQuery("biographicsName : " + ((Biographics) o).getBiographicsName()));
            iterable.forEach(x -> {
                if(x != null)
                    searchResult.add(x);
            });
        }

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
}
