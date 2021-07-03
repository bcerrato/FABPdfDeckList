package com.fabdb.fabdeckcard.service;

import com.fabdb.fabdeckcard.domain.Deck;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FabDBService {
    private final WebClient webClient;

    public FabDBService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.fabdb.net").build();
    }

    public Mono<Deck> getFabDeck(String deckSlug) {
        return this.webClient.get().uri("/decks/{slug}", deckSlug)
                .retrieve().bodyToMono(Deck.class);
    }

}
