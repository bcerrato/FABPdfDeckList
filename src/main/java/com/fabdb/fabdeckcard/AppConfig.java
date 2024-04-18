package com.fabdb.fabdeckcard;

import com.fabdb.fabdeckcard.service.FabCubeDataSetService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public FabCubeDataSetService fabCubeDataSetService() {
        return new FabCubeDataSetService("C:\\Users\\Brent Cerrato\\IdeaProjects\\flesh-and-blood-cards\\json\\english\\");
    }
}
