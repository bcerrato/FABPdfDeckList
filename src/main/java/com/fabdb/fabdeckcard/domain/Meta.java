package com.fabdb.fabdeckcard.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Meta {
    @JsonProperty(value = "current_page")
    Integer currentPage;
    Integer from;
    @JsonProperty(value = "last_page")
    Integer lastPage;
    List<Link> links;
}
