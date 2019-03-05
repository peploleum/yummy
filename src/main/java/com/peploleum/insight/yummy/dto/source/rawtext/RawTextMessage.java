package com.peploleum.insight.yummy.dto.source.rawtext;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by gFolgoas on 04/03/2019.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RawTextMessage {
    private String rawText;
    private String title;
    private String documentType;

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
}
