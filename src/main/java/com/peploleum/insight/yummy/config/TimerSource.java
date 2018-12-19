package com.peploleum.insight.yummy.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.yummy.dto.SourceMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.support.GenericMessage;

import java.util.Arrays;

@Configuration
@EnableBinding(Source.class)
public class TimerSource {
    private final Logger log = LoggerFactory.getLogger(TimerSource.class);

    @Value("${format}")
    private String format;

    @Bean
    @InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "${fixed-delay}", maxMessagesPerPoll = "1"))
    public MessageSource<String> timerMessageSource() {
        return () -> {
            final ObjectMapper om = new ObjectMapper();
            final SourceMessage sourceMessage = new SourceMessage();
            sourceMessage.setVersion("1");
            sourceMessage.setTimestamp("2018-12-14T09:21:52.436Z");
            sourceMessage.setLink(Arrays.asList(new String[]{"https://www.francetvinfo.fr/societe/religion/pedophilie-de-l-eglise/pedophilie-le-pape-ecarte-deux-cardinaux-de-son-cercle-de-conseillers_3096979.html#xtor=RSS-3-[societe]", "https://www.francetvinfo.fr/faits-divers/terrorisme/fusillade-a-strasbourg/fusillade-de-strasbourg-la-france-passe-en-alerte-urgence-attentat_3096921.html#xtor=RSS-3-[societe]", "https://www.francetvinfo.fr/faits-divers/terrorisme/fusillade-a-strasbourg/fusillade-a-strasbourg-les-marches-de-noel-cible-privilegiee_3096917.html#xtor=RSS-3-[societe]"}));
            sourceMessage.setDescription(Arrays.asList(new String[]{"<![CDATA[Il s'agit du cardinal George Pell,&nbsp;poursuivi en Australie pour agressions sexuelles contre des enfants, et du cardinal Francisco Errázuriz, soupçonné d'avoir tu au Chili les agissements d'un prêtre pédophile.]]>", "<![CDATA[Le niveau du plan Vigipirate a été relevé à son plus haut mercredi 12 décembre. La France est donc en alerte \"urgence attentat\". Depuis le ministère du l'Intérieur, Sophie Gauthier fait le point.]]>", "<![CDATA[Le marché de Strasbourg accueille chaque année quelque deux millions de visiteurs. La fusillade, mardi 11 décembre, intervient deux ans après l'attentat au camion bélier du marché de Noël de Ber]]"}));
            sourceMessage.setTitle(Arrays.asList(new String[]{"Pédophilie : le pape écarte deux cardinaux de son cercle de conseillers", "Fusillade de Strasbourg : la France passe en alerte", "Fusillade à Strasbourg : les marchés de Noël, cible privilégiée"}));
            sourceMessage.setDateTraiment("2018-12-14T09:21:52.436Z");
            try {
                final String message = om.writeValueAsString(sourceMessage);
                this.log.info("SENDING " + message);
                return new GenericMessage<>(message);

            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
//            final String message = "{\"@version\":\"1\",\"@timestamp\":\"2018-12-14T09:21:52.436Z\",\"link\":[\"https://www.francetvinfo.fr/societe/religion/pedophilie-de-l-eglise/pedophilie-le-pape-ecarte-deux-cardinaux-de-son-cercle-de-conseillers_3096979.html#xtor=RSS-3-[societe]\",\"https://www.francetvinfo.fr/faits-divers/terrorisme/fusillade-a-strasbourg/fusillade-de-strasbourg-la-france-passe-en-alerte-urgence-attentat_3096921.html#xtor=RSS-3-[societe]\",\"https://www.francetvinfo.fr/faits-divers/terrorisme/fusillade-a-strasbourg/fusillade-a-strasbourg-les-marches-de-noel-cible-privilegiee_3096917.html#xtor=RSS-3-[societe]\"],\"soureData\":\"rss\",\"description\":[\"<![CDATA[Il s'agit du cardinal George Pell,&nbsp;poursuivi en Australie pour agressions sexuelles contre des enfants, et du cardinal Francisco Errázuriz, soupçonné d'avoir tu au Chili les agissements d'un prêtre pédophile.]]>\",\"<![CDATA[Le niveau du plan Vigipirate a été relevé à son plus haut mercredi 12 décembre. La France est donc en alerte \\\"urgence attentat\\\". Depuis le ministère du l'Intérieur, Sophie Gauthier fait le point.]]>\",\"<![CDATA[Le marché de Strasbourg accueille chaque année quelque deux millions de visiteurs. La fusillade, mardi 11 décembre, intervient deux ans après l'attentat au camion bélier du marché de Noël de Berlin, qui avait fait 12 morts.]]>\"],\"dateTraiment\":\"2018-12-14T09:21:52.436Z\",\"title\":[\"Pédophilie : le pape écarte deux cardinaux de son cercle de conseillers\",\"Fusillade de Strasbourg : la France passe en alerte\",\"Fusillade à Strasbourg : les marchés de Noël, cible privilégiée\"]}";

            return new GenericMessage<>("");
        };
    }

}
