package com.fabdb.fabdeckcard.service;

import com.fabdb.fabdeckcard.domain.Card;
import com.fabdb.fabdeckcard.domain.Deck;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class SetListWriterService {

    public void writeCards(BufferedWriter writer, String set, List<Card> cards)
    {
        try {
            for(Card card:cards) {
                writer.write(getCardRow(card));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCardRow(Card card) {
        StringBuffer row = new StringBuffer();
        row.append("OOO ");
        row.append(card.getPrintings().get(0).getSku().getNumber()).append(" - ");
        row.append(card.getName());
        row.append(" (").append(card.getRarity()).append(")");
        row.append("\n");

        return row.toString();
    }

}
