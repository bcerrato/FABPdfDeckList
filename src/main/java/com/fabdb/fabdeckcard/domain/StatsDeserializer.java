package com.fabdb.fabdeckcard.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class StatsDeserializer extends StdDeserializer<Stats> {

    ObjectMapper mapper;

    public StatsDeserializer() {
        super(Stats.class);
        mapper = new ObjectMapper();
    }

    @Override
    public Stats deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode tree = p.getCodec().readTree(p);
        if (tree.isArray()) return null;

        return mapper.treeToValue(tree,Stats.class);
    }
}
