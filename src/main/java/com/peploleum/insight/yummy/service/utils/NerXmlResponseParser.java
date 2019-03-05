package com.peploleum.insight.yummy.service.utils;

import com.peploleum.insight.yummy.dto.source.ner.Entity;
import com.peploleum.insight.yummy.dto.source.ner.NerEntitiesType;
import com.peploleum.insight.yummy.dto.source.ner.NerJsonObjectResponse;
import com.peploleum.insight.yummy.dto.source.ner.Term;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gFolgoas on 04/03/2019.
 */
public class NerXmlResponseParser {
    public static NerJsonObjectResponse getResponseObjectDTO(final Document xmlDocument) {
        NerJsonObjectResponse nerResponse = new NerJsonObjectResponse();

        Element kafElement = (Element) xmlDocument.getElementsByTagName("KAF").item(0);
        final String language = kafElement.getAttribute("xml:lang");
        nerResponse.setLanguage(language);

        NodeList textNodeLists = ((Element) kafElement.getElementsByTagName("text").item(0)).getElementsByTagName("wf");
        NodeList termsNodeLists = ((Element) kafElement.getElementsByTagName("terms").item(0)).getElementsByTagName("term");
        NodeList entitiesNodeLists = ((Element) kafElement.getElementsByTagName("entities").item(0)).getElementsByTagName("entity");

        // key = wid, value = xmlElement
        Map<String, Element> texts = new HashMap<>();
        for (int i = 0; i < textNodeLists.getLength(); i++) {
            Node wfNode = textNodeLists.item(i);
            if (wfNode.getNodeType() == Node.ELEMENT_NODE) {
                Element wfElement = (Element) wfNode;
                texts.put(wfElement.getAttribute("wid"), wfElement);
            }
        }

        // key = tid, value = wid correspondant
        Map<String, String> termRefs = new HashMap<>();
        Map<String, Term> terms = new HashMap<>();
        for (int i = 0; i < termsNodeLists.getLength(); i++) {
            Node node = termsNodeLists.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element termElement = (Element) node;

                final Term term = new Term();
                term.setType(termElement.getAttribute("type"));
                term.setLemma(termElement.getAttribute("lemma"));
                term.setPos(termElement.getAttribute("pos"));
                term.setMorphofeat(termElement.getAttribute("morphofeat"));

                Element span = (Element) termElement.getElementsByTagName("span").item(0);
                Element target = (Element) span.getElementsByTagName("target").item(0);
                termRefs.put(termElement.getAttribute("tid"), target.getAttribute("id"));

                Element wfElement = texts.get(target.getAttribute("id"));
                term.setOffset(Integer.valueOf(wfElement.getAttribute("offset")));
                term.setLength(Integer.valueOf(wfElement.getAttribute("length")));
                term.setText(wfElement.getTextContent());
                terms.put(termElement.getAttribute("tid"), term);
            }
        }
        nerResponse.setTerms(terms);

        Map<String, Entity> entities = new HashMap<>();
        for (int i = 0; i < entitiesNodeLists.getLength(); i++) {
            Node entityNode = entitiesNodeLists.item(i);
            if (entityNode.getNodeType() == Node.ELEMENT_NODE) {
                Element entityElement = (Element) entityNode;
                final Entity entityDTO = new Entity();
                entityDTO.setEid(entityElement.getAttribute("eid"));
                entityDTO.setType(NerEntitiesType.valueOf(entityElement.getAttribute("type")));

                List<String> targetIds = new ArrayList<>();
                NodeList targets = ((Element) ((Element) entityElement.getElementsByTagName("references").item(0))
                        .getElementsByTagName("span").item(0)).getElementsByTagName("target");
                for (int j = 0; j < targets.getLength(); j++) {
                    Element target = (Element) targets.item(j);
                    targetIds.add(target.getAttribute("id"));
                }
                entityDTO.setTerms(targetIds);
                if (targetIds.size() > 0) {
                    String matchingTextId = termRefs.get(targetIds.get(0));
                    entityDTO.setText(texts.get(matchingTextId).getTextContent());
                }
                entities.put(entityDTO.getEid(), entityDTO);
            }
        }
        nerResponse.setEntities(entities);

        return nerResponse;
    }
}
