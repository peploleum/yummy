package com.peploleum.insight.yummy.service.utils;

import com.peploleum.insight.yummy.dto.Entity;
import com.peploleum.insight.yummy.dto.NerJsonObjectResponse;
import com.peploleum.insight.yummy.dto.entities.insight.BiographicsDTO;
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
        RawDataDTO dto = new RawDataDTO();
        dto.setRawDataName(this.simpleRawData.getSourceName());
        dto.setRawDataCreationDate(Instant.now());
        dto.setRawDataType(this.simpleRawData.getSourceType());
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

    public static List<Object> extractGraphyEntites(final NerJsonObjectResponse response) {
        return response.getEntities().values().stream()
                .map(dto -> mapToGraphytEntityDto(dto)).filter(dto -> dto != null)
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
                final BiographicsDTO biographicsDTO = new BiographicsDTO();
                biographicsDTO.setBiographicsName(entity.getText());
                biographicsDTO.setBiographicsFirstname(" ");
                return biographicsDTO;
            default:
                return null;
        }
    }

    private static Object mapToGraphytEntityDto(Entity entity) {
        switch (entity.getType()) {
            case LOCATION:
            case LOC:
                final com.peploleum.insight.yummy.dto.entities.graphy.LocationDTO locationDTO = new com.peploleum.insight.yummy.dto.entities.graphy.LocationDTO();
                locationDTO.setName(entity.getText());
                return locationDTO;
            case ORGANIZATION:
            case ORG:
                final com.peploleum.insight.yummy.dto.entities.graphy.OrganisationDTO organisationDTO = new com.peploleum.insight.yummy.dto.entities.graphy.OrganisationDTO();
                organisationDTO.setName(entity.getText());
                return organisationDTO;
            case PERSON:
            case PER:
                final com.peploleum.insight.yummy.dto.entities.graphy.BiographicsDTO biographicsDTO = new com.peploleum.insight.yummy.dto.entities.graphy.BiographicsDTO();
                biographicsDTO.setName(entity.getText());
                return biographicsDTO;
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
