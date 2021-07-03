package com.fabdb.fabdeckcard.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {
    String identifier;
    String name;
    String rarity;
    @JsonDeserialize(using = StatsDeserializer.class)
    Stats stats;
    String text;
    List<String> keywords;
    String flavour;
    String comments;
    String image;
    Integer total;
}

/**
 * {
 *  *             "identifier": "WTR082",
 *  *             "name": "Ancestral Empowerment",
 *  *             "rarity": "M",
 *  *             "stats": {"cost": "0", "defense": "2", "resource": "1"},
 *  *             "text": "",
 *  *             "keywords":["ninja", "attack", "reaction"],
 *  *             "flavour": "",
 *  *             "comments": "",
 *  *             "image": "https:\/\/fabdb2.imgix.net\/cards\/wtr\/82.png?w=300&fit=clip&auto=compress",
 *  *             "total": 3
 *  *         },
 */