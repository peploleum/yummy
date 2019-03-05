package com.peploleum.insight.yummy.service.utils;

import com.peploleum.insight.yummy.dto.entities.insight.*;
import com.peploleum.insight.yummy.dto.source.SimpleRawData;
import com.peploleum.insight.yummy.dto.source.ner.Entity;
import com.peploleum.insight.yummy.dto.source.ner.NerJsonObjectResponse;
import com.peploleum.insight.yummy.dto.source.ner.Term;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NerResponseHandler {

    private final NerJsonObjectResponse nerResponse;
    private final SimpleRawData simpleRawData;

    private List<InsightEntity> insightEntities;
    private RawData rawData;

    public NerResponseHandler(final NerJsonObjectResponse nerResponse, final SimpleRawData simple) {
        this.nerResponse = nerResponse;
        this.simpleRawData = simple;
        this.rawData = buildRawDataDto();
        this.insightEntities = buildResponseEntities();
    }

    private RawData buildRawDataDto() {
        final RawData dto = new RawData();
        dto.setRawDataName(this.simpleRawData.getSourceName());
        dto.setRawDataCreationDate(Instant.now());
        dto.setRawDataType("OSINT");
        dto.setRawDataSubType(this.simpleRawData.getSourceType());
        dto.setRawDataSourceType(this.simpleRawData.getSourceType());
        dto.setRawDataSourceUri(this.simpleRawData.getSourceUrl());
        dto.setRawDataContent(this.simpleRawData.getText());
        if (this.nerResponse != null) {
            // dto.setRawDataAnnotations(this.nerResponse.getContent());
            dto.setRawDataDataContentType(this.nerResponse.getLanguage());
        }
        return dto;
    }

    private List<InsightEntity> buildResponseEntities() {
        if (this.nerResponse == null || this.nerResponse.getEntities() == null)
            return new ArrayList<>();
        return extractInsightEntites(this.nerResponse);
    }

    public static List<InsightEntity> extractInsightEntites(final NerJsonObjectResponse response) {
        return response.getEntities().values().stream()
                .map(dto -> mapToInsightEntityDto(dto, response)).filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    private static InsightEntity mapToInsightEntityDto(Entity entity, final NerJsonObjectResponse nerResponse) {
        switch (entity.getType()) {
            case LOCATION:
            case LOC:
                final Location location = new Location();
                location.setLocationName(entity.getText());

                setEntitiesPositions(location, entity, nerResponse);

                return location;
            case ORGANIZATION:
            case ORG:
                final Organisation organisation = new Organisation();
                organisation.setOrganisationName(entity.getText());

                setEntitiesPositions(organisation, entity, nerResponse);

                return organisation;
            case PERSON:
            case PER:
                final Biographics biographics = new Biographics();
                biographics.setBiographicsName(entity.getText());
                biographics.setBiographicsFirstname(" ");

                setEntitiesPositions(biographics, entity, nerResponse);

                return biographics;
            default:
                return null;
        }
    }

    private static void setEntitiesPositions(final InsightEntity insEntity, final Entity entity, final NerJsonObjectResponse nerResponse) {
        List<Integer> positions = new ArrayList<>();
        List<String> terms = entity.getTerms();
        for (String id : terms) {
            Term term = nerResponse.getTerms().get(id);
            positions.add(term.getOffset());
        }
        insEntity.setTextPositionInfo(positions);
    }

    public List<InsightEntity> getInsightEntities() {
        return insightEntities;
    }

    public RawData getRawData() {
        return rawData;
    }
}
