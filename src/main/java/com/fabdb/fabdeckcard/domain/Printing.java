package com.fabdb.fabdeckcard.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Printing (
    String unique_id, String set_printing_unique_id,
    String id, String set_id, String edition, String foiling,
    String rarity, String artist, String art_variation,
    String flavor_text, String flavor_text_plain, String image_url,
    String tcgplayer_product_id, String tcgplayer_url)
{}

