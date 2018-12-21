package com.peploleum.insight.yummy.service;

import com.peploleum.insight.yummy.dto.Entity;
import com.peploleum.insight.yummy.dto.NerJsonObjectResponse;
import com.peploleum.insight.yummy.dto.RawDataDTO;
import com.peploleum.insight.yummy.dto.entities.BiographicsDTO;
import com.peploleum.insight.yummy.dto.entities.LocationDTO;
import com.peploleum.insight.yummy.dto.entities.OrganisationDTO;
import com.peploleum.insight.yummy.dto.source.RssSourceMessage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gFolgoas on 21/12/2018.
 */
public class NerResponseHandler {

    private final NerJsonObjectResponse nerResponse;
    private final RssSourceMessage rssMessage;
    private final String rssTextContent;

    private List<Object> insightEntities;
    private RawDataDTO rawDataDto;

    public NerResponseHandler(final NerJsonObjectResponse nerResponse, final RssSourceMessage rssMessage,
                              final String rssTextContent) {
        this.nerResponse = nerResponse;
        this.rssMessage = rssMessage;
        this.rssTextContent = rssTextContent;
        this.rawDataDto = buildRawDataDto();
        this.insightEntities = buildResponseEntities();
    }

    private RawDataDTO buildRawDataDto() {
        RawDataDTO dto = new RawDataDTO();
        dto.setRawDataName(this.rssMessage.getChannel().getTitle());
        dto.setRawDataCreationDate(LocalDate.now());
        dto.setRawDataType("RSS");
        try {
            if (this.rssMessage.getChannel().getLink() instanceof ArrayList) {
                dto.setRawDataSourceUri(((ArrayList) this.rssMessage.getChannel().getLink()).get(0).toString());
            }
            if (this.rssMessage.getChannel().getLink() instanceof String) {
                dto.setRawDataSourceUri((String) this.rssMessage.getChannel().getLink());
            }
        } catch (Exception e) {
            // nothing
        }
        dto.setRawDataContent(this.rssTextContent);
        if (this.nerResponse != null) {
            dto.setRawDataAnnotations(this.nerResponse.getContent());
            dto.setRawDataDataContentType(this.nerResponse.getLanguage());
        }
        return dto;
    }

    private List<Object> buildResponseEntities() {
        if (this.nerResponse == null || this.nerResponse.getEntities() == null)
            return new ArrayList<>();
        List<Object> insightEntities = this.nerResponse.getEntities().values().stream()
                .map(dto -> mapToEntityDto(dto)).filter(dto -> dto != null)
                .collect(Collectors.toList());
        return insightEntities;
    }

    private Object mapToEntityDto(Entity entity) {
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
