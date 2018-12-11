package com.peploleum.insight.yummy.dto;

import java.util.Date;

public class DummyPayload {
    private String message;
    private Date date;

    public DummyPayload() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
