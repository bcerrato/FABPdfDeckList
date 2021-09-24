package com.fabdb.fabdeckcard.service;

import com.fabdb.fabdeckcard.domain.CardResult;
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

    public Mono<CardResult> getCardResult(String set, int page) {
        if ("all".equals(set))
            return getCardResultAll(page);
        return this.webClient.get().uri("/cards?set={set}&page={page}",set,page)
                .retrieve().bodyToMono(CardResult.class);
    }

    public Mono<CardResult> getCardResultAll(int page) {
        return this.webClient.get().uri("/cards?page={page}",page)
                .retrieve().bodyToMono(CardResult.class);
    }

}
