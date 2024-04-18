package com.fabdb.fabdeckcard.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Card (
    String unique_id, String name, String pitch, String cost,
    String power, String defense, String health, String intelligence,
    List<String> types, List<String> card_keywords,
    List<String> abilities_and_effects, List<String> ability_and_effect_keywords,
    List<String> granted_keywords, List<String> removed_keywords,
    List<String> interacts_with_keywords, String functional_text,
    String functional_text_plain, String type_text, Boolean played_horizontally,
    Boolean blitz_legal, Boolean cc_legal, Boolean commoner_legal,
    Boolean blitz_living_legend, Boolean cc_living_legend, Boolean blitz_banned,
    Boolean cc_banned, Boolean commoner_banned, Boolean upf_banned,
    Boolean blitz_suspended, Boolean cc_suspended, Boolean commoner_suspended,
    Boolean ll_restricted, List<Printing> printings)
{
    public Printing set_printing(String set) {
        return printings().stream().filter(p -> p.set_id().equals(set)).findFirst().get();
    }
}
