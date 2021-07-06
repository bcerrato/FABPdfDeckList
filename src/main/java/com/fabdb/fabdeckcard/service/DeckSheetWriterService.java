package com.fabdb.fabdeckcard.service;

import com.fabdb.fabdeckcard.domain.Card;
import com.fabdb.fabdeckcard.domain.Deck;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class DeckSheetWriterService {

    public void writeDeckSheet(Deck deck)
    {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(deck.getSlug()+".csv"));

            String headers = "Card Name,Card Number,Rarity,Pitch,Cost,Attack,Defense,Keywords\n";
            writer.write(headers);
            for(Card card:deck.getCards()) {
                if (card.getKeywords().contains("equipment") ||
                        card.getKeywords().contains("weapon")) {
                    writer.write(getCardRow(card));
                }
            }
            for (Card card: deck.getCards()) {
                if (!card.getKeywords().contains("equipment") &&
                        !card.getKeywords().contains("hero") &&
                        !card.getKeywords().contains("weapon")) {
                    writer.write(getCardRow(card));
                }
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCardRow(Card card) {
        StringBuffer row = new StringBuffer();
        row.append("\"").append(card.getName()).append("\",");
        row.append(card.getPrintings().get(0).getSku().getNumber()).append(",");
        row.append(card.getRarity()).append(",");
        if (card.getStats() != null) {
            if (card.getStats().getResource() != null)
                row.append(card.getStats().getResource()).append(",");
            else
                row.append(",");
            if (card.getStats().getCost() != null)
                row.append(card.getStats().getCost()).append(",");
            else
                row.append(",");
            if (card.getStats().getAttack() != null)
                row.append(card.getStats().getAttack()).append(",");
            else
                row.append(",");
            if (card.getStats().getDefense() != null)
                row.append(card.getStats().getDefense()).append(",");
            else
                row.append(",");
        }
        else
        {
            row.append(",,,,");
        }
        row.append("\"");
        boolean first = true;
        for (String keyword:card.getKeywords()) {
            if (!first) {
                row.append(",");
            }
            else
            {
                first = false;
            }
            row.append(keyword);
        }
        row.append("\"\n");

        StringBuffer repeats = new StringBuffer();
        for (int cardCount = 0; cardCount < card.getTotal(); cardCount++)
        {
            repeats.append(row);
        }
        return repeats.toString();
    }

}
