package com.fabdb.fabdeckcard.service;

import com.fabdb.fabdeckcard.domain.Card;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FabCubeDataSetService {
    private String jsonRoot;
    private ObjectMapper mapper = new ObjectMapper();

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    private List<Card> cards;
    public FabCubeDataSetService(String jsonRoot) {
        this.jsonRoot = jsonRoot;

        try {
            cards = mapper.readValue(new File(this.jsonRoot + "card.json"), new TypeReference<List<Card>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Card> getCardsInSet(String set) {
        List<Card> setCards = new ArrayList<>();
        for (Card card:this.cards) {
            if (card.printings().stream().anyMatch(p -> p.set_id().equals(set))) {
                setCards.add(card);
            }
        }
        return setCards;
    }

}
