package com.fabdb.fabdeckcard.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stats {
    String cost;
    String defense;
    String resource;
    String attack;
}

/**
 * {"cost": "0", "defense": "2", "resource": "1"}
 */