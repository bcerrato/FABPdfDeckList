package com.fabdb.fabdeckcard.service;

import com.fabdb.fabdeckcard.domain.Card;
import com.fabdb.fabdeckcard.domain.Printing;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistListWriterService {

    public void writeCards(BufferedWriter writer, String set, List<Card> cards)
    {
        List<String> artists = cards.stream().map(c -> c.set_printing(set).artist()).distinct().toList();
        try {
            writer.write(set);
            writer.write("\n");
            for(String artist:artists) {
                writer.write("  "+artist+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
