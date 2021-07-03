package com.fabdb.fabdeckcard;

import com.fabdb.fabdeckcard.domain.Deck;
import com.fabdb.fabdeckcard.service.DeckListWriter;
import com.fabdb.fabdeckcard.service.FabDBService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class FabDeckCardApplication {

    public FabDeckCardApplication(FabDBService fabDBService, DeckListWriter deckListWriter) {
        this.fabDBService = fabDBService;
        this.deckListWriter = deckListWriter;
        this.mapper = new ObjectMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(FabDeckCardApplication.class, args);
    }

    final
    FabDBService fabDBService;
    DeckListWriter deckListWriter;
    ObjectMapper mapper;

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx)
    {
        return args -> {
            System.out.println("Creating output for deck");
            Mono<Deck> deckResult = fabDBService.getFabDeck(args[0]);
            Deck deck = deckResult.block();
            deckListWriter.writeDeckList(deck);
            System.out.println("Deck Written");
            System.exit(0);
        };
    }

}
