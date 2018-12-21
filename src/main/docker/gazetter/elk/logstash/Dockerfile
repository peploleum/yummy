ARG ELK_VERSION

# https://github.com/elastic/logstash-docker
FROM docker.elastic.co/logstash/logstash-oss:${ELK_VERSION}


# Add your logstash plugins setup here
# Example: RUN logstash-plugin install logstash-filter-json
RUN logstash-plugin install logstash-input-file
RUN logstash-plugin install logstash-input-kafka
RUN logstash-plugin install logstash-filter-grok
RUN logstash-plugin install logstash-filter-csv
RUN logstash-plugin install logstash-filter-geoip
RUN logstash-plugin install logstash-output-http
