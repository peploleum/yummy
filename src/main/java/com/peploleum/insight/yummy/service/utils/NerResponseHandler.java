package com.peploleum.insight.yummy.service.utils;

import com.peploleum.insight.yummy.dto.entities.insight.Biographics;
import com.peploleum.insight.yummy.dto.source.ner.Entity;
import com.peploleum.insight.yummy.dto.source.ner.NerJsonObjectResponse;
import com.peploleum.insight.yummy.dto.entities.insight.LocationDTO;
import com.peploleum.insight.yummy.dto.entities.insight.OrganisationDTO;
import com.peploleum.insight.yummy.dto.entities.insight.RawDataDTO;
import com.peploleum.insight.yummy.dto.source.SimpleRawData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NerResponseHandler {

    private final NerJsonObjectResponse nerResponse;
    private final SimpleRawData simpleRawData;

    private List<Object> insightEntities;
    private RawDataDTO rawDataDto;

    public NerResponseHandler(final NerJsonObjectResponse nerResponse, final SimpleRawData simple) {
        this.nerResponse = nerResponse;
        this.simpleRawData = simple;
        this.rawDataDto = buildRawDataDto();
        this.insightEntities = buildResponseEntities();
    }

    private RawDataDTO buildRawDataDto() {
        final RawDataDTO dto = new RawDataDTO();
        dto.setRawDataName(this.simpleRawData.getSourceName());
        dto.setRawDataCreationDate(Instant.now());
        dto.setRawDataType("OSINT");
        dto.setRawDataSubType(this.simpleRawData.getSourceType());
        dto.setRawDataSourceType(this.simpleRawData.getSourceType());
        dto.setRawDataSourceUri(this.simpleRawData.getSourceUrl());
        dto.setRawDataContent(this.simpleRawData.getText());
        if (this.nerResponse != null) {
            dto.setRawDataAnnotations(this.nerResponse.getContent());
            dto.setRawDataDataContentType(this.nerResponse.getLanguage());
        }
        return dto;
    }

    private List<Object> buildResponseEntities() {
        if (this.nerResponse == null || this.nerResponse.getEntities() == null)
            return new ArrayList<>();
        final List<Object> insightEntities = extractInsightEntites(this.nerResponse);
        return insightEntities;
    }

    public static List<Object> extractInsightEntites(final NerJsonObjectResponse response) {
        return response.getEntities().values().stream()
                .map(dto -> mapToInsightEntityDto(dto)).filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    private static Object mapToInsightEntityDto(Entity entity) {
        switch (entity.getType()) {
            case LOCATION:
            case LOC:
                final LocationDTO locationDTO = new LocationDTO();
                locationDTO.setLocationName(entity.getText());
                return locationDTO;
            case ORGANIZATION:
            case ORG:
                final OrganisationDTO organisationDTO = new OrganisationDTO();
                organisationDTO.setOrganisationName(entity.getText());
                return organisationDTO;
            case PERSON:
            case PER:
                final Biographics biographics = new Biographics();
                biographics.setBiographicsName(entity.getText());
                biographics.setBiographicsFirstname(" ");
                return biographics;
            default:
                return null;
        }
    }

    public List<Object> getInsightEntities() {
        return insightEntities;
    }

    public RawDataDTO getRawDataDto() {
        return rawDataDto;
    }
}
