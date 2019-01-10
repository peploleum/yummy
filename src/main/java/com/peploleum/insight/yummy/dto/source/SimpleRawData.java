package com.peploleum.insight.yummy.dto.source;

import com.peploleum.insight.yummy.dto.source.rss.RssSourceMessage;
import com.peploleum.insight.yummy.dto.source.twitter.TwitterSourceMessage;

import java.util.ArrayList;

public class SimpleRawData {
    private String sourceUrl;
    private String sourceName;
    private String sourceType;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public static SimpleRawData fromTwitterSourceMessage(final TwitterSourceMessage twitterSourceMessage) {
        final SimpleRawData simpleRawData = new SimpleRawData();
        simpleRawData.setSourceName(twitterSourceMessage.getUser().getScreenName());
        simpleRawData.setSourceType("TWITTER");
        simpleRawData.setSourceUrl(twitterSourceMessage.getSource());
        simpleRawData.setText(twitterSourceMessage.getText());
        return simpleRawData;
    }

    public static SimpleRawData fromRssSourceMessage(final RssSourceMessage rssSourceMessage) {
        final SimpleRawData simpleRawData = new SimpleRawData();
        simpleRawData.setSourceName(rssSourceMessage.getChannel().getTitle());
        simpleRawData.setSourceType("RSS");
        try {
            if (rssSourceMessage.getChannel().getLink() instanceof ArrayList) {
                simpleRawData.setSourceUrl(((ArrayList) rssSourceMessage.getChannel().getLink()).get(0).toString());
            }
            if (rssSourceMessage.getChannel().getLink() instanceof String) {
                simpleRawData.setSourceUrl((String) rssSourceMessage.getChannel().getLink());
            }
        } catch (Exception e) {
            // nothing
        }
        return simpleRawData;
    }

}
