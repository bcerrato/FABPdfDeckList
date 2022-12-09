package com.fabdb.fabdeckcard;

import com.fabdb.fabdeckcard.domain.Card;
import com.fabdb.fabdeckcard.domain.CardResult;
import com.fabdb.fabdeckcard.domain.Deck;
import com.fabdb.fabdeckcard.service.DeckListWriter;
import com.fabdb.fabdeckcard.service.DeckSheetWriterService;
import com.fabdb.fabdeckcard.service.FabDBService;
import com.fabdb.fabdeckcard.service.SetListWriterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FabDeckCardApplication {

    public FabDeckCardApplication(FabDBService fabDBService, DeckListWriter deckListWriter, DeckSheetWriterService deckSheetWriterService, SetListWriterService setListWriterService) {
        this.fabDBService = fabDBService;
        this.deckListWriter = deckListWriter;
        this.deckSheetWriterService = deckSheetWriterService;
        this.setListWriterService = setListWriterService;
        this.mapper = new ObjectMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(FabDeckCardApplication.class, args);
    }

    final
    FabDBService fabDBService;
    DeckListWriter deckListWriter;
    DeckSheetWriterService deckSheetWriterService;
    SetListWriterService setListWriterService;
    ObjectMapper mapper;

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx)
    {
        return args -> {
            System.out.println("Creating output for deck");
            if ("csv".equals(args[0])) {
                Mono<Deck> deckResult = fabDBService.getFabDeck(args[1]);
                Deck deck = deckResult.block();
                deckSheetWriterService.writeDeckSheet(deck);
            }
            else if ("set".equals(args[0])) {
                List<String> sets = Arrays.asList("ARC","CRU","DYN","ELE","EVR","MON","UPR","WTR","1HP");
                for (String set:sets) {
                    BufferedWriter writer = null;
                    try {
                        writer = new BufferedWriter(new FileWriter(set + ".txt"));
                        int page = 1;
                        Mono<CardResult> cardResult = fabDBService.getCardResult(set, page++);
                        CardResult cardData = cardResult.block();
                        while (cardData.getLinks().getNext() != null) {
                            setListWriterService.writeCards(writer, set, cardData.getData());
                            cardResult = fabDBService.getCardResult(set, page++);
                            cardData = cardResult.block();
                        }
                        setListWriterService.writeCards(writer, set, cardData.getData());
                        writer.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
            else if ("blank".equals(args[0])) {
                    int page = 1;
                    Mono<CardResult> cardResult = fabDBService.getCardResult("all", page++);
                    CardResult cardData = cardResult.block();
                    while (cardData.getLinks().getNext() != null) {
                        List<Card> cardList = cardData.getData();
                        for (Card card: cardList) {
                            if (card.getText() == null || card.getText().length() == 0)
                            {
                                System.out.println(card.getIdentifier() + " " + card.getName());
                            }
                        }
                        cardResult = fabDBService.getCardResult(args[1], page++);
                        cardData = cardResult.block();
                    }
            }
            else {
                Mono<Deck> deckResult = fabDBService.getFabDeck(args[1]);
                Deck deck = deckResult.block();
                deckListWriter.writeDeckList(deck);
            }
            System.out.println("Deck Written");
            System.exit(0);
        };
    }

}
