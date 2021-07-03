package com.fabdb.fabdeckcard.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sku {
    String sku;
    String finish;
    String number;
}

/**
 *           "sku": {
 *             "sku": "MON063",
 *             "finish": "regular",
 *             "set": {
 *               "id": "mon",
 *               "name": "Monarch",
 *               "released": "2021-05-07",
 *               "browseable": true,
 *               "draftable": true
 *             },
 *             "number": "MON063"
 *           },
*/
