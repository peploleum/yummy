ARG ELK_VERSION

# https://github.com/elastic/elasticsearch-docker
FROM docker.elastic.co/elasticsearch/elasticsearch-oss:${ELK_VERSION}

RUN bin/elasticsearch-plugin install analysis-icu

#copie des fichiers a lancer pour attendre démarrage d'elastic et cree index
COPY docker-entrypoint.sh /docker-entrypoint.sh
COPY config/elasticsearch.yml config/elasticsearch.yml
COPY config/setup.sh config/setup.sh
COPY config/indexconfig.json config/indexconfig.json
RUN mkdir utils
COPY utils/wait-for-it.sh utils/wait-for-it.sh

USER root
RUN chmod +x /docker-entrypoint.sh utils/wait-for-it.sh config/setup.sh
RUN chown -R elasticsearch:elasticsearch /docker-entrypoint.sh utils/wait-for-it.sh config/setup.sh

USER elasticsearch
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/share/elasticsearch/bin/elasticsearch"]



