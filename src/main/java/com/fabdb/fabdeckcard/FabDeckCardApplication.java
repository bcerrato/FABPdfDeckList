package com.fabdb.fabdeckcard;

import com.fabdb.fabdeckcard.domain.Card;
import com.fabdb.fabdeckcard.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FabDeckCardApplication {

    public FabDeckCardApplication(FabCubeDataSetService fabCubeDataSetService, SetListWriterService setListWriterService, ArtistListWriterService artistListWriterService) {
        this.fabCubeDataSetService = fabCubeDataSetService;
        this.setListWriterService = setListWriterService;
        this.artistListWriterService = artistListWriterService;
        this.mapper = new ObjectMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(FabDeckCardApplication.class, args);
    }

    final SetListWriterService setListWriterService;
    final ArtistListWriterService artistListWriterService;
    final FabCubeDataSetService fabCubeDataSetService;
    ObjectMapper mapper;

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx)
    {
        return args -> {
            List<String> sets = Arrays.asList("ARC","CRU","DYN","ELE","EVR","MON","UPR","WTR","1HP","UPR","DTD","HVY");
            for (String set:sets) {
                BufferedWriter writer = null;
                BufferedWriter artistWriter = null;
                try {
                    writer = new BufferedWriter(new FileWriter(set + ".txt"));
                    artistWriter = new BufferedWriter(new FileWriter(set+"-artists.txt"));
                    int page = 1;
                    List<Card> cardResult = fabCubeDataSetService.getCardsInSet(set);
                    setListWriterService.writeCards(writer, set, cardResult);
                    artistListWriterService.writeCards(artistWriter,set, cardResult);
                    writer.close();
                    artistWriter.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            System.out.println("Sets Written");
            System.exit(0);
        };
    }

}
