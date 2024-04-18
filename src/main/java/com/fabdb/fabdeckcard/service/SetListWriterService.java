package com.fabdb.fabdeckcard.service;

import com.fabdb.fabdeckcard.domain.Card;
import com.fabdb.fabdeckcard.domain.Printing;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetListWriterService {

    public void writeCards(BufferedWriter writer, String set, List<Card> cards)
    {
        cards = cards.stream().sorted(Comparator.comparing(c -> c.set_printing(set).id())).collect(Collectors.toList());
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
        if (card.types() != null) {
            if (card.types().contains("Hero") || card.types().contains("Equipment") ||
                    card.types().contains("Weapon") || card.types().contains("Off-Hand")) {
                collect = 1;
            }
            if (card.types().contains("1H")) {
                collect = 2;
            }

        }
        else {
            System.out.println("null stats");
        }
        if (card.card_keywords()!= null && card.card_keywords().contains("Legendary")) collect = 1;
        Printing printing = card.printings().stream().filter(p -> p.set_id().equals(set)).findFirst().get();
        if (printing.rarity().equals("T")) collect = 1;
        for (int i = 0; i < 3; i++) {
            if (i < collect)
                row.append("O");
            else
                row.append(" ");
        }
        row.append(" ");
        String setNumber = printing.id();
        row.append(setNumber).append(" - ");
        row.append(card.name());
        row.append(" (").append(printing.rarity()).append(")");
        if (card.pitch() != null) row.append(" {").append(card.pitch()).append("}");
        row.append(" - ");
        for(String keyword:card.card_keywords()) {
            row.append(keyword).append(";");
        }
        row.append("\n");

        return row.toString();
    }
}
