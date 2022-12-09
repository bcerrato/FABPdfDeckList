package com.fabdb.fabdeckcard.service;

import com.fabdb.fabdeckcard.domain.Card;
import com.fabdb.fabdeckcard.domain.Deck;
import com.fabdb.fabdeckcard.domain.Printing;
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
                writer.write(getCardRow(card, set));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCardRow(Card card, String set) {
        StringBuffer row = new StringBuffer();
        int collect = 3;
        if (card.getKeywords() != null) {
            if (card.getKeywords().contains("hero") || card.getKeywords().contains("equipment") ||
                    card.getKeywords().contains("weapon") || card.getKeywords().contains("off-hand")) {
                collect = 1;
            }
            if (card.getKeywords().contains("1h")) {
                collect = 2;
            }

        }
        else {
            System.out.println("null stats");
        }
        if (card.getText()!= null && card.getText().contains("**Legendary**")) collect = 1;
        if (card.getRarity().equals("T")) collect = 1;
        for (int i = 0; i < 3; i++) {
            if (i < collect)
                row.append("O");
            else
                row.append(" ");
        }
        row.append(" ");
        row.append(set.toUpperCase());
        String setNumber = getSetNumber(card.getPrintings(),set);
        row.append(setNumber).append(" - ");
        row.append(card.getName());
        row.append(" (").append(card.getRarity()).append(")");
        if (card.getStats() != null && card.getStats().getResource() != null) row.append(" {").append(card.getStats().getResource()).append("}");
        row.append(" - ");
        for(String keyword:card.getKeywords()) {
            row.append(keyword).append(";");
        }
        row.append("\n");

        return row.toString();
    }

    private String getSetNumber(List<Printing> printings, String set) {
        for(Printing printing:printings) {
            if (printing.getSku().getSku().startsWith(set.toUpperCase()))
                return printing.getSku().getNumber();
        }
        return printings.get(0).getSku().getNumber();
    }

}
