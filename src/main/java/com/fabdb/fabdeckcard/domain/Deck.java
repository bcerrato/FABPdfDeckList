package com.fabdb.fabdeckcard.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Deck {
    String slug;
    String name;
    String formatted;
    String notes;
    String visibility;
    Integer cardBack;
    String createdAt;
    Integer totalVotes;
    Integer myVote;
    List<Card> cards;
    List<Card> sideboard;
}

/**
 * {
 *     "slug": "NmzrmMWV",
 *     "name": "Katsudon",
 *     "format": "constructed",
 *     "notes": null,
 *     "visibility": "public",
 *     "cardBack": 1,
 *     "createdAt": "2020-05-31 09:04:13",
 *     "totalVotes": 0,
 *     "myVote": 0,
 *     "cards": [
 *          {
 *             "identifier": "WTR082",
 *             "name": "Ancestral Empowerment",
 *             "rarity": "M",
 *             "stats": {"cost": "0", "defense": "2", "resource": "1"},
 *             "text": "",
 *             "keywords":["ninja", "attack", "reaction"],
 *             "flavour": "",
 *             "comments": "",
 *             "image": "https:\/\/fabdb2.imgix.net\/cards\/wtr\/82.png?w=300&fit=clip&auto=compress",
 *             "total": 3
 *         },
 *         ...
 *     ],
 *     "sideboard": []
 * }
 */